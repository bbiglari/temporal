package cicd;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CICDWorkflow {
    @WorkflowMethod
    void runCIWorkflow(String gitRepoPath);
}