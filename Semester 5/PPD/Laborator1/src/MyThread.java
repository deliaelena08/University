public class MyThread  extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("Hello World");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
