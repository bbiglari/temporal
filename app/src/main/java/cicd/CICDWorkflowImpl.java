package cicd;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CICDWorkflowImpl implements CICDWorkflow {

    private final BuildAndTestActivities activities = Workflow.newActivityStub(
            BuildAndTestActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(Config.START_TO_CLOSE_TIMEOUT))
                    .build());

    @Override
    public void runCIWorkflow(String gitRepoPath) {
        String tempDirPath = activities.createTempDirectory();
        activities.clone(gitRepoPath, tempDirPath);
        activities.build(tempDirPath);
        activities.test(tempDirPath);
        activities.clean(tempDirPath);
    }
}