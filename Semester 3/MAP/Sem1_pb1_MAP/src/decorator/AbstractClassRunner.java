package decorator;

import model.Task;

public abstract class AbstractClassRunner implements TaskRunner{
    TaskRunner taskRunner;
    public AbstractClassRunner(TaskRunner t) {
        taskRunner = t;
    }

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
    }

    @Override
    public void executeAllTasks() {
        taskRunner.executeAllTasks();
    }

    @Override
    public void addTask(Task task) {
        taskRunner.addTask(task);
    }

    @Override
    public boolean hasTask(Task task) {
        return taskRunner.hasTask(task);
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }
}
