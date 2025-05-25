package decorator;

import model.Task;

public interface TaskRunner {
    void executeOneTask();
    void executeAllTasks();
    void addTask(Task task);
    boolean hasTask(Task task);

    boolean hasTask();
}
