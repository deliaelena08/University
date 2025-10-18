import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final int N=1000000,P=8;
    private static int[] a = new int[N];
    private static int[] b = new int[N];
    private static int[] c = new int[N];
    private static int[] cSecv = new int[N];
    private static final Random rand = new Random();

    private static void init(int[] vec){
        for(int i=0;i<N;i++){
            vec[i] = rand.nextInt(237);
        }
    }

    private static void printVec(int[] vec, int to_print){
        for(int i=0; i < vec.length && i < to_print; i++){
            System.out.printf("%4d",vec[i]);
        }
        System.out.println();
    }

    private static void sumSecv(int[] a, int[] b, int[] c){
        assert(a.length == b.length);
        assert(c.length == a.length);
        for(int i = 0 ; i < c.length ; i++){
            c[i] = a[i] + b[i];
        }
    }

    public static void main(String[] args) {
        init(a);
        init(b);
        long startTime = System.nanoTime();
        sumSecv(a,b,cSecv);
        long endTime = System.nanoTime();

        SumThread[] threads = new SumThread[P];
        int dim = N/P;
        int rest = N%P;
        int start = 0;
        int end = dim;

        long startTimeParalel = System.nanoTime();
        for(int i = 0 ; i < P ; i++){
            if ( rest > 0 ){
                end += 1;
                rest -= 1;
            }
            threads[i] = new SumThread(a,b,c,start,end);
            threads[i].start();

            start = end;
            end += dim;
        }

        for ( SumThread thread : threads ){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTimeParalel = System.nanoTime();

        if(Arrays.equals(c,cSecv)){
            System.out.println("All good");
        } else {
            System.out.println("Not all good");
        }

        printVec(a,5);
        printVec(b,5);
        printVec(c,5);
        printVec(cSecv,5);
        double timeParalel =  (endTimeParalel - startTimeParalel)/1e6;
        double timeSecvential = (endTime - startTime)/1e6;
        System.out.println("\n");
        System.out.println("Time paralel: " + timeParalel + " ms");
        System.out.println("Time secv: " + timeSecvential + " ms");
    }
}