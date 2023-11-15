package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.Util;
import hudson.model.Run;
import hudson.remoting.VirtualChannel;
import jenkins.util.VirtualFile;

/**
 * Deletes the artifacts matching patterns.
 */
public class DeleteArtifactsMatchingPatternsAction extends Action {
    private static final Logger LOG = Logger.getLogger(DeleteArtifactsMatchingPatternsAction.class.getName());

    private String includePatterns;
    private String excludePatterns;

    @DataBoundConstructor
    public DeleteArtifactsMatchingPatternsAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getIncludePatterns() {
        return includePatterns;
    }

    @DataBoundSetter
    public void setIncludePatterns(String includePatterns) {
        this.includePatterns = includePatterns;
    }

    public String getExcludePatterns() {
        return excludePatterns;
    }

    @DataBoundSetter
    public void setExcludePatterns(String excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    // if 'file' is on a different node, this FileCallable will be transferred to that node and executed there.
    public static final class DeleteFileCallable implements FileCallable<Void> {
        private static final long serialVersionUID = 2469669731898204125L;
        private final File archiveRootDirectory;

        public DeleteFileCallable(File archiveRootDirectory) {
            this.archiveRootDirectory = archiveRootDirectory;
        }

        @Override 
        public Void invoke(File file, VirtualChannel channel) throws IOException {
            deleteFileOrLogError(file);
            return null;
        }

        void deleteEmptyDirectoryAndParent(File directory) throws IOException {
            if (isDirectoryEmpty(directory.toPath()) && hasValidParent(directory.getParentFile())) {
                Util.deleteFile(directory);
                deleteParentDirectories(directory.getParentFile());
            }
        }

        boolean isDirectoryEmpty(Path path) throws IOException {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                return !directoryStream.iterator().hasNext();
            }
        }

        boolean hasValidParent(File parent) {
            return this.archiveRootDirectory.equals(parent);
        }

        void deleteParentDirectories(File directory) {
            File parent = directory;

            while (parent != null && !parent.equals(this.archiveRootDirectory)) {
                deleteDirectory(parent);
                parent = parent.getParentFile();
            }
        }

        void deleteDirectory(File directory) {
            boolean deleteSuccess = directory.delete();
            if(!deleteSuccess) {
                LOG.log(Level.FINE, "Deletion of directory failed: " + directory.getAbsolutePath());
            }
        }

        void deleteFileOrLogError(File file) throws IOException {
            if (file.isFile()) {
                LOG.log(Level.FINE, "Deleting " + file.getName());
                Util.deleteFile(file);
                deleteEmptyDirectoryAndParent(file.getParentFile());

                // Return early when the condition is met
                return;
            }
            LOG.log(Level.FINE, file + " is neither a directory nor a regular file.");
        }

        /**
         * Overrides the 'checkRoles' method to fulfill the requirement of implementing RoleSensitive.
         *
         * @param checker The RoleChecker used for access control and security purposes.
         * @throws SecurityException If there are security-related issues.
         */
        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            //Nothing is being done here even though a call to RoleChecker#check(…​) is expected.
        }
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        VirtualFile virtualRoot = run.getArtifactManager().root();
        Collection<String> files = virtualRoot.list(includePatterns, excludePatterns, false);
        LOG.log(Level.FINE, "Include Pattern Files: " + files);

        for (String path : files) {
            deleteFileAtPath(virtualRoot, path);
        }
    }

    public void deleteFileAtPath(VirtualFile virtualRoot, String path) throws IOException, InterruptedException {
        VirtualFile virtualFile = virtualRoot.child(path);
        File file = new File(virtualFile.toURI().getPath());
        FilePath filePath = new FilePath(file);
        DeleteFileCallable deleteFileCallableInstance = new DeleteFileCallable(new File(virtualRoot.toURI()));
        filePath.act(deleteFileCallableInstance);
    }
}
