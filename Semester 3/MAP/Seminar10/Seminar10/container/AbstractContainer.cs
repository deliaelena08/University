﻿using Task = Seminar10.domain.Task;

namespace Seminar10.container;

public abstract class AbstractContainer: Container
{
    protected List<Task> tasks;
    
    public AbstractContainer ()
    {
        tasks = new List<Task>();
    }

    public virtual Task remove()
    {
        throw new NotImplementedException();
    }
    
    public void add(Task task){
        tasks.Add(task);
    }

    public bool isEmpty()
    {
        return tasks.Count == 0;
    }

    public int size()
    {
        return tasks.Count;
    }
}