this is a simple project to build and develop CI/CD pipeline for any size company.

In this sample project, I created a workflow definition with some activities to automatically clone, build, test, and clean a sample java project automativally. 

to see how this project works:
1. clone this project
2. unzip the git-project
3. run the ``` gradle clean build ```
4. get the path to the git-project
   1. cd to git-project
   2. run ``` pwd ```
   3. save the path for step 8
6. run temporal server ```temporal server start-dev```
7. run worker ``` gradle run --args="-w" ``` or ``` gradle run --args="--worker" ```
8. run client ``` gradle run --args="-c --path /path/to/temporal/git-project" ```, or ``` gradle run --args="--client --path /path/to/temporal/git-project" ```
