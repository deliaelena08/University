package factory;

import model.Task;

import java.util.ArrayList;


public class StackContainer implements AbstractContainer{
    @Override
    public Task remove() {
          return list.remove(list.size()-1);
    }
}