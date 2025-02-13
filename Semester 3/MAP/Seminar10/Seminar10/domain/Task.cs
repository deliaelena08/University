﻿namespace Seminar10.domain;

public class Task
{
    private string taskID;

    private string description;
    
    public string TaskID
    {
        get
        {
            return taskID;
        }
    }
    
    public string Description
    {
        get
        {
            return description;
        }
    }
    
    public Task(string taskID, string description)
    {
        this.taskID = taskID;
        this.description = description;
    }
    
    public override string ToString()
    {
        return "Task: " +  taskID + " " + description;
    }
}