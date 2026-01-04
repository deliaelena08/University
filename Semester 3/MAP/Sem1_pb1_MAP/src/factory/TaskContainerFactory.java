package factory;

public class TaskContainerFactory implements Factory{
    private static TaskContainerFactory instance ;
    private TaskContainerFactory(){}

    public static TaskContainerFactory getInstance(){
        if(instance == null){
            instance = new TaskContainerFactory();
        }
        return instance;
    }

    @Override
    public Container createContainer(Strategy strategy) {
        if(strategy.equals(Strategy.LIFO)){
            return new StackContainer();
        }
        else {
            return new QueueContainer(); //return new Q
        }
    }
}
