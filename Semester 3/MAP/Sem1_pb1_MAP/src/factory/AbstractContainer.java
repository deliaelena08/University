package factory;

import model.Task;

import java.util.ArrayList;

public interface AbstractContainer extends Container {
    ArrayList<Task> list=new ArrayList<>();

    @Override
    public abstract Task remove() ;

    @Override
    public default void add(Task task) {
        list.add(task);
    }

    @Override
    public default int size() {
        return list.size();
    }

    @Override
    public default boolean isEmpty() {
        return list.isEmpty();
    }
}
