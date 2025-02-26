package cicd;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

public class CICDClient {
    private final WorkflowServiceStubs service;
    private final String gitRepoPath;

    public CICDClient(String gitRepoPath) {
        this.gitRepoPath = gitRepoPath;
        service = WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(Config.SERVER_URL)
                        .build());
    }

    public void run() {

        WorkflowClient workflowClient = WorkflowClient.newInstance(service);

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(Config.TASK_QUEUE)
                .build();
        CICDWorkflow workflow = workflowClient.newWorkflowStub(CICDWorkflow.class, workflowOptions);

        workflow.runCIWorkflow(gitRepoPath);
        System.out.println("CI/CD workflow has started...");
    }
}