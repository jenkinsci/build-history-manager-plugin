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

    private String include;
    private String exclude;

    @DataBoundConstructor
    public DeleteArtifactsWithPatternAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getInclude() {
        return include;
    }

    @DataBoundSetter
    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    @DataBoundSetter
    public void setExclude(String exclude) {
        this.exclude = exclude;
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
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        VirtualFile vRoot = run.getArtifactManager().root();
        Collection<String> files = vRoot.list(include, exclude, false);
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
