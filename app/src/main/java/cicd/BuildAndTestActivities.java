package cicd;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface BuildAndTestActivities {

    String createTempDirectory(); // temp directory path

    void clone(String gitRepoPath, String tempDirPath);

    void build(String tempDirPath);

    void test(String tempDirPath);

    void clean(String tempDirPath);
}