import java.util.concurrent.ThreadLocalRandom;

public class Task implements Runnable {

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread-ul curent este: " + threadName);
        int sleepTime = ThreadLocalRandom.current().nextInt(1, 4);
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            System.out.println(threadName + " a fost Intrerupt.");
        }
    }
}
