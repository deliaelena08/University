import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Exercitiul 1");
        System.out.println();
        ThreadClass tc = new ThreadClass();
        Thread t1 = new Thread(tc);
        t1.start();

        System.out.println("Exercitiul 2");
        System.out.println();
        PrintNumberThread tcr = new PrintNumberThread();
        Thread t11 = new Thread(tcr);
        Thread t2 = new Thread(tcr);
        Thread t3 = new Thread(tcr);
        t11.start();
        t2.start();
        t3.start();
        t11.join();
        t2.join();
        t3.join();

        System.out.println("Exercitiul 3");
        System.out.println();
        Counter c = new Counter();
        Thread t4=new Thread(){
            public void run(){
                for(int i=0;i<1000;i++){
                   c.increment();
                }
            }
        };
        Thread t5=new Thread(){
            public void run(){
                for(int i=0;i<1000;i++){
                    c.increment();
                }
            }
        };
        t4.start();
        System.out.println(c.getCounter());
        t5.start();
        System.out.println(c.getCounter());

        System.out.println("Exercitiul 4");
        System.out.println();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            threadPool.submit(task);
        }
        threadPool.shutdown();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) threadPool;
        System.out.println("thread-uri în pool: " + executor.getMaximumPoolSize());
        System.out.println("task-uri trimise: " + executor.getTaskCount());

    }
}