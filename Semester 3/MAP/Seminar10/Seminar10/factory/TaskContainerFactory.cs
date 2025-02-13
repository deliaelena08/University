using Seminar10.container;
using Seminar10.factory;
using Seminar10.strategy;

namespace Seminar10.factory;

public class TaskContainerFactory : Factory
{
    private TaskContainerFactory()
    {
        
    }

    private static TaskContainerFactory taskContainerFactory = new TaskContainerFactory();
    
    public static TaskContainerFactory getInstance()
    {
        return taskContainerFactory;
    }
    
    public Container createContainer(ContainerStrategy strategy)
    {
        switch (strategy)
        {
            case ContainerStrategy.LIFO:
                return new StackContainer();
            case ContainerStrategy.FIFO:
                return new QueueContainer();
            default:
                return null;
        }
    }
}