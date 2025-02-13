import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class EmailSender{
    private String message;
    public EmailSender(String message){
        this.message = message;
    }
    public String run(){
        System.out.println(message+" ");
        return "done";
    }
}
public class Main {
   public static void main(String[] args) throws InterruptedException{
       ExecutorService executorService= java.util.concurrent.Executors.newFixedThreadPool(3);
       List<Callable<String>> l=new ArrayList<>();
         for(int i=0;i<3;i++){
            Callable<String> callable=new EmailSender("meeting"+i)::run;
            l.add(callable);
         }
         List<Future<String>> results=executorService.invokeAll(l);
         executorService.shutdown();
   }
}