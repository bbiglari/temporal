package cicd;

import java.io.File;
import java.io.IOException;

public class BuildAndTestActivitiesImpl implements BuildAndTestActivities {

    @Override
    public String createTempDirectory() {
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "git-repo");
            tempDir.mkdirs();
            return tempDir.getAbsolutePath();
        } catch (Exception e) {
            System.err.println("Error creating temp directory: " + e.getMessage());
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    @Override
    public void clone(String gitRepoPath, String tempDirPath) {
        try {
            System.err.println("Cloning repository from: " + gitRepoPath + " to " + tempDirPath);

            ProcessBuilder cloneBuilder = new ProcessBuilder("git", "clone", gitRepoPath, tempDirPath);
            Process cloneProcess = cloneBuilder.inheritIO().start();
            cloneProcess.waitFor();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error during clone: " + e.getMessage());
            throw new RuntimeException("Clone failed", e);
        }
    }

    @Override
    public void build(String tempDirPath) {
        try {
            System.err.println("Building project in: " + tempDirPath);

            ProcessBuilder buildBuilder = new ProcessBuilder("./gradlew", "clean", "build");
            buildBuilder.directory(new File(tempDirPath));
            Process buildProcess = buildBuilder.inheritIO().start();
            int buildExitCode = buildProcess.waitFor();
            if (buildExitCode != 0) {
                throw new RuntimeException("Gradle build failed with exit code: " + buildExitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during build: " + e.getMessage());
            throw new RuntimeException("Build failed", e);
        }
    }

    @Override
    public void test(String tempDirPath) {
        try {
            System.out.println("Running tests in: " + tempDirPath);

            ProcessBuilder testBuilder = new ProcessBuilder("./gradlew", "test");
            testBuilder.directory(new File(tempDirPath));
            Process testProcess = testBuilder.inheritIO().start();
            int testExitCode = testProcess.waitFor();
            if (testExitCode != 0) {
                throw new RuntimeException("Gradle test failed with exit code: " + testExitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during test: " + e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }

    @Override
    public void clean(String tempDirPath) {
        try {
            System.out.println("Cleaning up temporary directory: " + tempDirPath);
            File tempDir = new File(tempDirPath);
            deleteDirectory(tempDir);
            System.out.println("Temporary directory cleaned.");
        } catch (Exception e) {
            System.err.println("Error cleaning up temporary directory: " + e.getMessage());
            throw new RuntimeException("Cleanup failed", e);
        }
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}