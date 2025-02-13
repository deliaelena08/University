package decorator;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends AbstractClassRunner{
    public PrinterTaskRunner(TaskRunner t) {
        super(t);
    }

    @Override
    public void executeOneTask(){
        super.executeOneTask();
        System.out.println(LocalDateTime.now());
    }

    @Override
    public boolean hasTask() {
        return false;
    }
}
