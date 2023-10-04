package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
 * Deletes the artifacts with patterns.
 */
public class DeleteArtifactsWithPatternAction extends Action {
    private static final Logger LOG = Logger.getLogger(DeleteArtifactsWithPatternAction.class.getName());

    private String includePatterns;
    private String excludePatterns;

    @DataBoundConstructor
    public DeleteArtifactsWithPatternAction() {
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

    static boolean isDirectoryEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }        
        return false;
    }

    // if 'file' is on a different node, this FileCallable will be transferred to that node and executed there.
    public static final class Delete implements FileCallable<Void> {
        private static final long serialVersionUID = 1L;
        private final File archiveRootFile;

        public Delete(File archiveRootFile) {
            this.archiveRootFile = archiveRootFile;
        }

        @Override 
        public Void invoke(File file, VirtualChannel channel) throws IOException {
            Set<File> directories = new HashSet<>();
            deleteFileOrLogError(file, directories);
            deleteEmptyDirectoriesAndParents(directories);
            return null;
        }

        void deleteEmptyDirectoriesAndParents(Set<File> directories) throws IOException {
            for (File dir : directories) {
                if (shouldDeleteDirectory(dir)) {
                    Util.deleteFile(dir);
                    deleteParentDirectories(dir.getParentFile());
                }
            }
        }

        boolean shouldDeleteDirectory(File dir) throws IOException {
            if (!isValidDirectory(dir)) {
                return false;
            }

            File parent = dir.getParentFile();
            return hasValidParent(parent);
        }

        boolean hasValidParent(File parent) {
            return parent != null && parent.equals(this.archiveRootFile);
        }

        boolean isValidDirectory(File dir) throws IOException {
            return dir != null && dir.isDirectory() && isDirectoryEmpty(dir.toPath());
        }

        void deleteParentDirectories(File directory) {
            File parent = directory;

            while (shouldDelete(parent)) {
                deleteDirectory(parent);
                parent = parent.getParentFile();
            }
        }

        boolean shouldDelete(File directory) {
            return directory != null && !directory.equals(this.archiveRootFile);
        }

        void deleteDirectory(File directory) {
            boolean deleteSuccess = directory.delete();
            if(!deleteSuccess) {
                throw new RuntimeException("Deletion of directory failed: " + directory.getAbsolutePath());
            }
        }

        void deleteFileOrLogError(File file, Set<File> directories) throws IOException {
            if (file.isFile()) {
                LOG.log(Level.FINE, "Deleting " + file.getName());
                directories.add(file.getParentFile());
                Util.deleteFile(file);
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
        VirtualFile vRoot = run.getArtifactManager().root();
        Collection<String> files = vRoot.list(includePatterns, excludePatterns, false);
        LOG.log(Level.FINE, "Include Pattern Files: " + files);

        for (String path : files) {
            deleteFileAtPath(vRoot, path);
        }
    }

    public void deleteFileAtPath(VirtualFile vRoot, String path) throws IOException, InterruptedException {
        VirtualFile virtualFile = vRoot.child(path);
        File file = new File(virtualFile.toURI().getPath());
        FilePath filePath = new FilePath(file);
        File archiveRootFile = new File(vRoot.toURI());
        Delete deleteInstance = new Delete(archiveRootFile);
        filePath.act(deleteInstance);
    }
}
