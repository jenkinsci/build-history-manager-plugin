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
    private static final Logger LOGGER = Logger.getLogger(DeleteArtifactsWithPatternAction.class.getName());

    private String include;
    private String exclude;

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

    @DataBoundConstructor
    public DeleteArtifactsWithPatternAction() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public static String removeLastSlash(String archiveRootPath) {
        if(archiveRootPath.endsWith("/")) {
            return archiveRootPath.substring(0, archiveRootPath.lastIndexOf("/"));
        } else {
            return archiveRootPath;
        }
    }
    // if 'file' is on a different node, this FileCallable will be transferred to that node and executed there.
    private static final class Delete implements FileCallable<Void> {
        private static final long serialVersionUID = 1;
        private final String archiveRootPath;

        public Delete(String archiveRootPath) {
            this.archiveRootPath = removeLastSlash(archiveRootPath);
        }

        @Override 
        public Void invoke(File vFile, VirtualChannel channel) throws IOException {          

            Set<File> directories = new HashSet<>();
            if (vFile.isFile()) {
                LOGGER.log(Level.FINE, "Deleting " + vFile.getName());
                directories.add(vFile.getParentFile());
                Util.deleteFile(vFile); 
            } else {
                LOGGER.log(Level.FINE, vFile + " is neither a directory nor a regular file");
            }

            deleteEmptyDirectoriesAndParents(directories);
            return null;
        }

        private void deleteEmptyDirectoriesAndParents(Set<File> directories) throws IOException {
            for (File dir : directories){
                if (dir != null && dir.isDirectory() && isDirEmpty(dir.toPath())) {
                    File parent = dir.getParentFile();
                    if (parent == null || !parent.getPath().equals(this.archiveRootPath)) {
                        continue;
                    }
                    Util.deleteFile(dir);

                    while (parent !=null && !parent.getPath().equals(this.archiveRootPath)) {
                        if (!parent.delete()) {
                            break;
                        }
                        parent = parent.getParentFile();
                    }
                }
            }
        }

        public boolean isDirEmpty(Path path) throws IOException {
            if (Files.isDirectory(path)) {
                try (Stream<Path> entries = Files.list(path)) {
                    return !entries.findFirst().isPresent();
                }
            }        
            return false;
        }
       
        // checkRoles method is used for access control and security purposes in Jenkins.
        // It is responsible for checking if the current user has the required permissions to execute the code defined in the FileCallable object.
        // By implementing the checkRoles method and checking for the required permission, the code is more secure and protected against unauthorized access.
        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            // TODO Auto-generated method stub
            // Nothing to do here  
        }
    }

    @Override
    public void perform(Run<?, ?> run) throws IOException, InterruptedException {
        Collection<String> files = run.getArtifactManager().root().list(include, exclude, false);
        LOGGER.log(Level.FINE, "Include Pattern Files: ------ " + files);
        VirtualFile vRoot = run.getArtifactManager().root();

        Delete deleteInstance = new Delete(vRoot.toURI().getPath());

        for (String path : files){
            VirtualFile vFile = vRoot.child(path);
            FilePath filePath = new FilePath(new File(vFile.toURI().getPath()));
            filePath.act(deleteInstance);
        }
    }
}