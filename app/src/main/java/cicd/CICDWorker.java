package cicd;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class CICDWorker {

    private final WorkflowServiceStubs service;

    public CICDWorker() {
        this.service = WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(Config.SERVER_URL)
                        .build());
    }

    public CICDWorker(WorkflowServiceStubs service) {
        this.service = service;
    }

    public void run() {

        try {
            WorkflowClient client = WorkflowClient.newInstance(service);
            WorkerFactory factory = WorkerFactory.newInstance(client);
            Worker worker = factory.newWorker(Config.TASK_QUEUE);
            worker.registerWorkflowImplementationTypes(CICDWorkflowImpl.class);
            worker.registerActivitiesImplementations(new BuildAndTestActivitiesImpl());
            factory.start();
            System.out.println("Worker started...");
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}