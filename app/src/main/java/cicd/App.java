package cicd;

import org.apache.commons.cli.*;

public class App {

    public static void main(String[] args) {
        Options options = new Options();

        Option clientOption = new Option("c", "client", false, "Run the client");
        clientOption.setRequired(false);
        options.addOption(clientOption);

        Option workerOption = new Option("w", "worker", false, "Run the worker");
        workerOption.setRequired(false);
        options.addOption(workerOption);

        Option pathOption = new Option("p", "path", true, "Git repository path");
        pathOption.setRequired(false);
        options.addOption(pathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("cicd.App", options);
            System.exit(1);
            return;
        }

        boolean runClient = cmd.hasOption("client");
        boolean runWorker = cmd.hasOption("worker");
        String gitRepoPath = cmd.getOptionValue("path");

        if (runClient && runWorker) {
            System.out.println("Error: Client and worker options cannot be used together.");
            formatter.printHelp("cicd.App", options);
            System.exit(1);
            return;
        }

        if (runClient) {
            if (gitRepoPath == null) {
                System.out.println("Error: Git repository path is required for client mode.");
                formatter.printHelp("cicd.App", options);
                System.exit(1);
                return;
            }
            new CICDClient(gitRepoPath).run();
        }

        if (runWorker) {
            new CICDWorker().run();
        }

        if (!runClient && !runWorker) {
            formatter.printHelp("cicd.App", options);
        }
    }
}