package decorator;

public class DelayTaskRunner extends AbstractClassRunner{
    public DelayTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }
    @Override
    public void executeOneTask(){
        try {
            // Introducerea întârzierii de 3 secunde
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.executeOneTask();
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }

}
