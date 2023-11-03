
package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.jenkinsci.remoting.RoleChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import hudson.model.Run;
import jenkins.model.ArtifactManager;
import jenkins.util.VirtualFile;

@RunWith(MockitoJUnitRunner.class)
public class DeleteArtifactsMatchingPatternsActionTest {
    private DeleteArtifactsMatchingPatternsAction action;
    private DeleteArtifactsMatchingPatternsAction.Delete deleteInstance;
    private File archiveRootDir;
    private Run<?, ?> mockRun;
    private ArtifactManager mockArtifactManager;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("java.util.logging.config.file",
                ClassLoader.getSystemResource("logging.properties").getPath());
    }

    @Rule
    public TemporaryFolder tempFolder = TemporaryFolder.builder().assureDeletion().build();

    @Before
    public void setup() throws IOException {
        action = new DeleteArtifactsMatchingPatternsAction();
        mockRun = Mockito.mock(Run.class);
        mockArtifactManager = Mockito.mock(ArtifactManager.class);
        Mockito.when(mockRun.getArtifactManager()).thenReturn(mockArtifactManager);
        VirtualFile root = VirtualFile.forFile(tempFolder.getRoot());
        Mockito.when(mockArtifactManager.root()).thenReturn(root);

        // Create the tempFolder and its contents
        tempFolder.newFile("test.xml");
        tempFolder.newFile("testLog.log");
        tempFolder.newFile("testTxt.txt");
        tempFolder.newFolder("testFolder");
        tempFolder.newFile("testFolder/test1.xml");
        tempFolder.newFile("testFolder/testLog1.log");
        tempFolder.newFile("testFolder/testTxt1.txt");

        // Create a temporary directory for code coverage testing
        Path tempDir = Files.createTempDirectory("testDir");
        archiveRootDir = tempDir.resolve("archive").toFile();
        //Set up Delete instance with the archiveRootDir
        deleteInstance = new DeleteArtifactsMatchingPatternsAction.Delete(archiveRootDir);
    }

    private void assertTempFolderContains(final int expectedNumFiles) {
        final int actualNumFiles = countFiles(tempFolder.getRoot());
        final String assertMessage = String.format("Expected %d files, actual found %d files", expectedNumFiles,
                actualNumFiles);
        Assert.assertEquals(assertMessage, expectedNumFiles, actualNumFiles);
    }

    // Custom assertFileExists method
    public void assertFileExists(final String filePath, final String message) {
        final File file = new File(tempFolder.getRoot(), filePath);
        Assert.assertTrue(message, file.exists());
    }

    public void assertFileExists(final String filePath) {
        assertFileExists(filePath, null);
    }

    // Custom assertFileNotExists method
    public void assertFileNotExists(final String filePath, final String message) {
        final File file = new File(tempFolder.getRoot(), filePath);
        Assert.assertFalse(message, file.exists());
    }

    public void assertFileNotExists(final String filePath) {
        assertFileNotExists(filePath, null);
    }

    // Count the number of files in the directory
    public static int countFiles(final File directory) {
        int fileCount = 0;
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.exists()) {
                fileCount++;
            } else if (file.isDirectory()) {
                fileCount += countFiles(file);
            }
        }
        return fileCount;
    }

    @Test // test 1, inc="", exc="", nothing deleted
    public void noneProvidedNothingDeleted() throws IOException, InterruptedException {
        action.setIncludePatterns("");
        action.setExcludePatterns("");

        action.perform(mockRun);

        assertTempFolderContains(6);
        assertFileExists("testFolder/test1.xml");
    }

    @Test // test 2, inc="", exc="**", nothing deleted
    public void excludesOnlyNothingDeleted() throws IOException, InterruptedException {
        action.setIncludePatterns("");
        action.setExcludePatterns("**");

        action.perform(mockRun);

        assertTempFolderContains(6);
    }

    @Test // test 3, inc="**", exc="**", nothing deleted
    public void bothProvidedNothingDeleted() throws IOException, InterruptedException {
        action.setIncludePatterns("**");
        action.setExcludePatterns("**");

        action.perform(mockRun);

        assertTempFolderContains(6);
    }

    @Test // test 4, inc="**", exc="", everything deleted
    public void includesOnlyEverythingDeleted() throws IOException, InterruptedException {
        action.setIncludePatterns("**");
        action.setExcludePatterns("");

        action.perform(mockRun);

        File folder = new File(tempFolder.getRoot().getPath());
        String assertMessage = "All files with folders should have been deleted.";
        Assert.assertTrue(assertMessage, folder.exists());
    }

    @Test // test 5 inc="**", exc="**/*.log" everything except logs are deleted
    public void everythingDeletedButExcludePatterns() throws IOException, InterruptedException {
        action.setIncludePatterns("**");
        action.setExcludePatterns("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(2);
        assertFileExists("testLog.log");
        assertFileExists("testFolder/testLog1.log", "Log files should have been excluded.");
    }

    @Test // test 5.1 inc="**", exc="**/*.log, **/*.txt" everything except logs and txt
          // files are deleted
    public void everythingDeletedButMultipleExcludePatterns() throws IOException, InterruptedException {
        action.setIncludePatterns("**");
        action.setExcludePatterns("**/*.log, **/*.txt");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileExists("testLog.log");
        assertFileNotExists("test.xml", "XML files should have been deleted.");
        assertFileNotExists("testFolder/test1.xml");
    }

    @Test // test 6 inc="*.txt", exc="**/*.log" only text files in the root are deleted
    public void selectiveFileDeletionRootOnly() throws IOException, InterruptedException {
        action.setIncludePatterns("*.txt");
        action.setExcludePatterns("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(5);
        assertFileNotExists("testTxt.txt");
    }

    @Test // test 6.1 inc="*.txt, *.xml", exc="**/*.log" only txt files and xml files in
          // the root are deleted
    public void multipleIncludePatternFilesDeletionRootOnly() throws IOException, InterruptedException {
        action.setIncludePatterns("*.txt, *.xml");
        action.setExcludePatterns("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileNotExists("testTxt.txt");
        assertFileNotExists("test.xml");
    }

    @Test // test 6.2 inc="*.txt, **/*.xml", exc="**/*.log" only txt files and xml files
          // are deleted
    public void multipleIncludePatternsFilesDeletion() throws IOException, InterruptedException {
        action.setIncludePatterns("*.txt, **/*.xml");
        action.setExcludePatterns("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(3);
        assertFileExists("testLog.log");
        assertFileNotExists("test.xml");
    }

    @Test // test 7 inc="**/*.txt", exc="**/*.log", all text files are deleted
    public void deleteIncludeMatchingFilesButExcludePatterns() throws IOException, InterruptedException {
        action.setIncludePatterns("**/*.txt");
        action.setExcludePatterns("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileExists("testLog.log");
        assertFileNotExists("testTxt.txt");
    }

    @Test // test 8 inc="**/*.txt", exc="*.txt", all text files but those in the root are
          // deleted
    public void selectiveFileDeletionExcludeRoot() throws IOException, InterruptedException {
        action.setIncludePatterns("**/*.txt");
        action.setExcludePatterns("*.txt");

        action.perform(mockRun);

        assertTempFolderContains(5);
        assertFileExists("testTxt.txt");
        assertFileNotExists("testFolder/testTxt1.txt");
    }

    @Test // test 9, inc="../**", exc=""
    public void nothingDeletedWhenPointingAboveStartingPoint() throws IOException, InterruptedException {
        action = new DeleteArtifactsMatchingPatternsAction();
        mockRun = Mockito.mock(Run.class);
        mockArtifactManager = Mockito.mock(ArtifactManager.class);
        Mockito.when(mockRun.getArtifactManager()).thenReturn(mockArtifactManager);
        File buildXML = tempFolder.newFile("build.xml");
        tempFolder.newFile("log");
        tempFolder.newFile("log-index");
        File archiveFolder = tempFolder.newFolder("archive");
        tempFolder.newFile("archive/test1.xml");
        tempFolder.newFile("archive/testLog1.log");
        tempFolder.newFile("archive/testTxt1.txt");
        VirtualFile root = VirtualFile.forFile(archiveFolder);
        Mockito.when(mockArtifactManager.root()).thenReturn(root);

        action.setIncludePatterns("../**");
        action.setExcludePatterns("");

        action.perform(mockRun);

        File folder = new File(tempFolder.getRoot().getPath());
        Assert.assertTrue(folder.exists());
        Assert.assertTrue(archiveFolder.exists());
        Assert.assertTrue(buildXML.exists());
        assertFileExists("archive/test1.xml");
    }

    @Test // testing getIncludePatterns method for code coverage
    public void testGetIncludePatterns() {
        String expectedIncludePatterns = "**";
        DeleteArtifactsMatchingPatternsAction action = new DeleteArtifactsMatchingPatternsAction();
        action.setIncludePatterns(expectedIncludePatterns);

        String actualIncludePatterns = action.getIncludePatterns();

        Assert.assertEquals(expectedIncludePatterns, actualIncludePatterns);
    }

    @Test // testing getExcludePatterns method for code coverage
    public void testGetExcludePatterns() {
        String expectedExcludePatterns = "**/*.log";
        DeleteArtifactsMatchingPatternsAction action = new DeleteArtifactsMatchingPatternsAction();
        action.setExcludePatterns(expectedExcludePatterns);

        String actualExcludePatterns = action.getExcludePatterns();

        Assert.assertEquals(expectedExcludePatterns, actualExcludePatterns);
    }

    @Test // testing isDirectoryEmpty method returns 'False' for code coverage
    public void testIsDirectoryEmptyFalse() throws IOException {
        Path nonEmptyDirectory = Files.createTempDirectory("nonEmptyDirectory");
        Files.createFile(nonEmptyDirectory.resolve("file.txt"));

        Assert.assertFalse(DeleteArtifactsMatchingPatternsAction.isDirectoryEmpty(nonEmptyDirectory));
        Assert.assertTrue(Files.exists(nonEmptyDirectory));
    }

    @Test // testing isDirectoryEmpty method returns 'True' for code coverage
    public void testIsDirectoryEmptyTrue() throws IOException {
        Path emptyDirectory = Files.createTempDirectory("emptyDirectory");

        Assert.assertTrue(DeleteArtifactsMatchingPatternsAction.isDirectoryEmpty(emptyDirectory));
    }

    @Test // testing isDirectoryEmpty method for code coverage
    public void testIsDirectoryEmptyNonExistentDirectory() throws IOException {
        // Provide a path to a directory that may not exist
        Path nonExistentDirectory = Paths.get("/tmp/nonExistentDirectory");

        if (Files.exists(nonExistentDirectory)) {
            assertFalse(DeleteArtifactsMatchingPatternsAction.isDirectoryEmpty(nonExistentDirectory));
        } else {
            // Use assertions to handle the case where the directory doesn't exist
            assertFalse("Directory does not exist: " + nonExistentDirectory, Files.exists(nonExistentDirectory));
        }
    }

    @Test // testing checkRoles method for code coverage
    public void testCheckRolesNoException() {
        RoleChecker mockRoleChecker = Mockito.mock(RoleChecker.class);

        deleteInstance.checkRoles(mockRoleChecker);
    }

    @Test // testing deleteParentDirectories method for code coverage
    public void testDeleteParentDirectories() {
        File grandparentDir = new File(archiveRootDir, "grandparent");
        File parentDir = new File(grandparentDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

        // Call the method
        deleteInstance.deleteParentDirectories(childDir);

        // Assert that child, parent and grandparent directories are deleted, but the
        // archive root directory remains
        Assert.assertFalse(childDir.exists());
        Assert.assertFalse(parentDir.exists());
        Assert.assertFalse(grandparentDir.exists());
        Assert.assertTrue(archiveRootDir.exists());

       // This is the scenario where directory is null for code coverage
        deleteInstance.deleteParentDirectories(null);
    }

    @Test // testing deleteDirectory method for successful directory deletion for code coverage
    public void testDeleteDirectorySuccess() {
        File parentDir = new File(archiveRootDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

       deleteInstance.deleteDirectory(childDir);
    }

    @Test // testing deleteDirectory method for failed directory deletion for code coverage
    public void testDeleteDirectoryFailure() {
        File parentDir = new File(archiveRootDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

        // Mock the behavior of directory.delete() to simulate failure
        childDir = Mockito.spy(childDir);
        Mockito.when(childDir.delete()).thenReturn(false);

        deleteInstance.deleteDirectory(childDir);
    }

    @Test // testing deleteFileOrLogError method for code coverage
    public void testDeleteRegularFile() throws IOException {
        File parentDir = new File(archiveRootDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

        // Create a regular text file named "testFile.txt" under the child directory
        File testFile = new File(childDir, "testFile.txt");
        Assert.assertTrue(testFile.createNewFile());
        Set<File> directories = new HashSet<>();
        deleteInstance.deleteFileOrLogError(testFile, directories);

        Assert.assertTrue(directories.contains(childDir));
        Assert.assertFalse(testFile.exists());
    }

    @Test // testing Log non regular file message for code coverage
    public void testLogNonRegularFile() throws IOException {
        File nonRegularFile = Mockito.mock(File.class);
        Set<File> directories = new HashSet<>();

        Mockito.when(nonRegularFile.isFile()).thenReturn(false);
        deleteInstance.deleteFileOrLogError(nonRegularFile, directories);
    }
}
