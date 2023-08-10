
package pl.damianszczepanik.jenkins.buildhistorymanager.model.actions;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class DeleteArtifactsWithPatternActionTest {
    private DeleteArtifactsWithPatternAction action;
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
        action = new DeleteArtifactsWithPatternAction();
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
        action.setInclude("");
        action.setExclude("");

        action.perform(mockRun);

        assertTempFolderContains(6);
        assertFileExists("testFolder/test1.xml");
    }

    @Test // test 2, inc="", exc="**", nothing deleted
    public void excludesOnlyNothingDeleted() throws IOException, InterruptedException {
        action.setInclude("");
        action.setExclude("**");

        action.perform(mockRun);

        assertTempFolderContains(6);
    }

    @Test // test 3, inc="**", exc="**", nothing deleted
    public void bothProvidedNothingDeleted() throws IOException, InterruptedException {
        action.setInclude("**");
        action.setExclude("**");

        action.perform(mockRun);

        assertTempFolderContains(6);
    }

    @Test // test 4, inc="**", exc="", everything deleted
    public void includesOnlyEverythingDeleted() throws IOException, InterruptedException {
        action.setInclude("**");
        action.setExclude("");

        action.perform(mockRun);

        File folder = new File(tempFolder.getRoot().getPath());
        String assertMessage = String.format("All files with folders should have been deleted.");
        Assert.assertTrue(assertMessage, folder.exists());
    }

    @Test // test 5 inc="**", exc="**/*.log" everything except logs are deleted
    public void everythingDeletedButExcludePatterns() throws IOException, InterruptedException {
        action.setInclude("**");
        action.setExclude("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(2);
        assertFileExists("testLog.log");
        assertFileExists("testFolder/testLog1.log", "Log files should have been excluded.");
    }

    @Test // test 5.1 inc="**", exc="**/*.log, **/*.txt" everything except logs and txt
          // files are deleted
    public void everythingDeletedButMultipleExcludePatterns() throws IOException, InterruptedException {
        action.setInclude("**");
        action.setExclude("**/*.log, **/*.txt");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileExists("testLog.log");
        assertFileNotExists("test.xml", "XML files should have been deleted.");
        assertFileNotExists("testFolder/test1.xml");
    }

    @Test // test 6 inc="*.txt", exc="**/*.log" only text files in the root are deleted
    public void selectiveFileDeletionRootOnly() throws IOException, InterruptedException {
        action.setInclude("*.txt");
        action.setExclude("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(5);
        assertFileNotExists("testTxt.txt");
    }

    @Test // test 6.1 inc="*.txt, *.xml", exc="**/*.log" only txt files and xml files in
          // the root are deleted
    public void multipleIncludePatternFilesDeletionRootOnly() throws IOException, InterruptedException {
        action.setInclude("*.txt, *.xml");
        action.setExclude("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileNotExists("testTxt.txt");
        assertFileNotExists("test.xml");
    }

    @Test // test 6.2 inc="*.txt, **/*.xml", exc="**/*.log" only txt files and xml files
          // are deleted
    public void multipleIncludePatternFilesDeletion() throws IOException, InterruptedException {
        action.setInclude("*.txt, **/*.xml");
        action.setExclude("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(3);
        assertFileExists("testLog.log");
        assertFileNotExists("test.xml");
    }

    @Test // test 7 inc="**/*.txt", exc="**/*.log", all text files are deleted
    public void deleteIncludeMatchingFilesButExcludePatterns() throws IOException, InterruptedException {
        action.setInclude("**/*.txt");
        action.setExclude("**/*.log");

        action.perform(mockRun);

        assertTempFolderContains(4);
        assertFileExists("testLog.log");
        assertFileNotExists("testTxt.txt");
    }

    @Test // test 8 inc="**/*.txt", exc="*.txt", all text files but those in the root are
          // deleted
    public void selectiveFileDeletionExcludeRoot() throws IOException, InterruptedException {
        action.setInclude("**/*.txt");
        action.setExclude("*.txt");

        action.perform(mockRun);

        assertTempFolderContains(5);
        assertFileExists("testTxt.txt");
        assertFileNotExists("testFolder/testTxt1.txt");
    }

    @Test // test 9, inc="../**", exc=""
    public void nothingDeletedWhenPointingAboveStartingPoint() throws IOException, InterruptedException {
        action = new DeleteArtifactsWithPatternAction();
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

        action.setInclude("../**");
        action.setExclude("");

        action.perform(mockRun);

        File folder = new File(tempFolder.getRoot().getPath());
        Assert.assertTrue(folder.exists());
        Assert.assertTrue(archiveFolder.exists());
        Assert.assertTrue(buildXML.exists());
        assertFileExists("archive/test1.xml");
    }

    @Test // testing archiveRootPath for code coverage
    public void testRemoveLastSlash_NoSlash() throws IOException, InterruptedException {
        String inputPath = "path/without/slash";

        String result = DeleteArtifactsWithPatternAction.removeLastSlash(inputPath);

        Assert.assertEquals(inputPath, result);
    }

    @Test // testing getInclude method for code coverage
    public void testGetInclude() throws IOException, InterruptedException {
        String expectedInclude = "**";
        DeleteArtifactsWithPatternAction action = new DeleteArtifactsWithPatternAction();
        action.setInclude(expectedInclude);

        String actualInclude = action.getInclude();

        Assert.assertEquals(expectedInclude, actualInclude);
    }

    @Test // testing getInclude method for code coverage
    public void testGetExclude() throws IOException, InterruptedException {
        String expectedExclude = "**/*.log";
        DeleteArtifactsWithPatternAction action = new DeleteArtifactsWithPatternAction();
        action.setExclude(expectedExclude);

        String actualExclude = action.getExclude();

        Assert.assertEquals(expectedExclude, actualExclude);
    }

    @Test // testing isDirEmpty method returns 'False' for code coverage
    public void testIsDirEmptyFalse() throws IOException, InterruptedException {
        Path nonEmptyDir = Files.createTempDirectory("nonEmptyDir");
        Files.createFile(nonEmptyDir.resolve("file.txt"));

        Assert.assertFalse(DeleteArtifactsWithPatternAction.isDirEmpty(nonEmptyDir));
        Assert.assertTrue(Files.exists(nonEmptyDir));
    }

    @Test // testing isDirEmpty method returns 'True' for code coverage
    public void testIsDirEmptyTrue() throws IOException, InterruptedException {
        Path emptyDir = Files.createTempDirectory("emptyDir");

        Assert.assertTrue(DeleteArtifactsWithPatternAction.isDirEmpty(emptyDir));
    }

    @Test // testing isDirEmpty method for code coverage
    public void testIsDirEmptyNonExistentDirectory() throws IOException {
        // Given
        Path nonExistentDirectory = tempFolder.newFolder("non_existent").toPath();
        Files.delete(nonExistentDirectory);

        boolean result = DeleteArtifactsWithPatternAction.isDirEmpty(nonExistentDirectory);

        assertFalse(result);
    }

    @Test // testing checkRoles method for code coverage
    public void testCheckRolesNoException() throws IOException, InterruptedException {
        DeleteArtifactsWithPatternAction.Delete actionDelete = new DeleteArtifactsWithPatternAction.Delete(
                "archiveRootPath/");
        RoleChecker mockRoleChecker = Mockito.mock(RoleChecker.class);

        actionDelete.checkRoles(mockRoleChecker);
    }

    @Test // testing deleteParentDirectories method for code coverage
    public void testDeleteParentDirectories() throws IOException, InterruptedException {
        // Create a temporary directory for testing
        Path tempDir = Files.createTempDirectory("testDir");
        File archiveRootDir = tempDir.resolve("archive").toFile();
        File grandparentDir = new File(archiveRootDir, "grandparent");
        File parentDir = new File(grandparentDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

        // Set up Delete instance with the archiveRootPath
        String archiveRootPath = archiveRootDir.getAbsolutePath();
        DeleteArtifactsWithPatternAction.Delete deleteInstance = new DeleteArtifactsWithPatternAction.Delete(
                archiveRootPath);

        // Call the method
        deleteInstance.deleteParentDirectories(childDir);

        // Verify that child, parent and grandparent directories are deleted, but the
        // archive root directory remains
        Assert.assertFalse(childDir.exists());
        Assert.assertFalse(parentDir.exists());
        Assert.assertFalse(grandparentDir.exists());
        Assert.assertTrue(archiveRootDir.exists());

        // testing shouldDelete method
        File nullDirectory = null;
        // Call the method
        boolean result = deleteInstance.shouldDelete(nullDirectory);

        // Assert that the result is false since directory is null
        Assert.assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteDirectoryFailure() throws IOException, InterruptedException {
        // Create a temporary directory for testing
        Path tempDir = Files.createTempDirectory("testDir");
        File archiveRootDir = tempDir.resolve("archive").toFile();
        File parentDir = new File(archiveRootDir, "parent");
        File childDir = new File(parentDir, "child");
        Assert.assertTrue(childDir.mkdirs());

        // Set up Delete instance with the archiveRootPath
        String archiveRootPath = archiveRootDir.getAbsolutePath();
        DeleteArtifactsWithPatternAction.Delete deleteInstance = new DeleteArtifactsWithPatternAction.Delete(
                archiveRootPath);

       deleteInstance.deleteDirectory(parentDir);
    }

    @Test // testing deleteFileOrLogError method for code coverage
    public void testDeleteRegularFile() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("testDir");
        File archiveRootDir = tempDir.resolve("archive").toFile();
        File parentDir = new File(archiveRootDir, "parent");
        File childDir = new File(parentDir, "child");
        childDir.mkdirs();

        // Create a regular text file named "testFile.txt" under the child directory
        File testFile = new File(childDir, "testFile.txt");
        boolean result = testFile.createNewFile();
        Set<File> directories = new HashSet<>();
        // Set up Delete instance with the archiveRootPath
        String archiveRootPath = archiveRootDir.getAbsolutePath();
        DeleteArtifactsWithPatternAction.Delete deleteInstance = new DeleteArtifactsWithPatternAction.Delete(
                archiveRootPath);
        deleteInstance.deleteFileOrLogError(testFile, directories);

        Assert.assertTrue(directories.contains(childDir));
        Assert.assertFalse(testFile.exists());
    }

    @Test // testing Log non regular file message for code coverage
    public void testLogNonRegularFile() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("testDir");
        File archiveRootDir = tempDir.resolve("archive").toFile();

        File nonRegularFile = Mockito.mock(File.class);
        Set<File> directories = new HashSet<>();

        Mockito.when(nonRegularFile.isFile()).thenReturn(false);
        // Set up Delete instance with the archiveRootPath
        String archiveRootPath = archiveRootDir.getAbsolutePath();
        DeleteArtifactsWithPatternAction.Delete deleteInstance = new DeleteArtifactsWithPatternAction.Delete(
                archiveRootPath);
        deleteInstance.deleteFileOrLogError(nonRegularFile, directories);
    }

    @Test // testing shouldDeleteDirectory method for code coverage
    public void testShouldDeleteDirectory() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("testDir");
        File archiveRootDir = tempDir.resolve("archive").toFile();
        File parentDir = new File(archiveRootDir, "parent");
        File parentDir2 = new File(archiveRootDir, "parent2");
        parentDir.mkdirs();
        parentDir2.mkdirs();
        File newFile = new File(parentDir2,"testFile.txt");
        boolean result = newFile.createNewFile();

        // Set up Delete instance with the archiveRootPath
        String archiveRootPath = archiveRootDir.getAbsolutePath();
        DeleteArtifactsWithPatternAction.Delete deleteInstance = new DeleteArtifactsWithPatternAction.Delete(
                archiveRootPath);

        File nullDirectory = null;
        Assert.assertFalse(deleteInstance.shouldDeleteDirectory(nullDirectory));
        Assert.assertFalse(deleteInstance.shouldDeleteDirectory(newFile));
        Assert.assertFalse(deleteInstance.shouldDeleteDirectory(parentDir2));
        Assert.assertFalse(deleteInstance.shouldDeleteDirectory(archiveRootDir));
        Assert.assertTrue(parentDir != null);
        Assert.assertTrue(deleteInstance.shouldDeleteDirectory(parentDir));
        Assert.assertTrue(parentDir.getParentFile().getPath().equals(archiveRootPath));
    }
}