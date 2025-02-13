package decorator;

import factory.Container;
import factory.Strategy;
import factory.TaskContainerFactory;
import model.Task;

public class StrategyTaskRunner implements TaskRunner{
    private Container container;

    public StrategyTaskRunner(Strategy st) {
        TaskContainerFactory factory = TaskContainerFactory.getInstance();
        container = factory.createContainer(st);

    }

    @Override
    public void executeOneTask() {
        Task model  = container.remove();
        model.execute();
    }

    @Override
    public void executeAllTasks() {
        while (!container.isEmpty()) {
            container.remove().execute();
        }

    }

    @Override
    public void addTask(Task task) {
        container.add(task);

    }

    @Override
    public boolean hasTask(Task task) {
        return false;
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
