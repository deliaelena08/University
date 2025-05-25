package factory;

import model.AbstractSorter;
import model.Task;

import java.util.ArrayList;

public class QueueContainer implements AbstractContainer {

    @Override
    public Task remove() {
        return list.remove(0);
    }
}
