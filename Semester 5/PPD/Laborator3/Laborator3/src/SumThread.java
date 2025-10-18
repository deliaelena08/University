public class SumThread extends Thread{
    private int[] a;
    private int[] b;
    private int[] c;
    private int start;
    private int end;

    public SumThread(int[] a, int[] b, int[] c, int start, int end) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        for(int i = start; i < end; i++){
            c[i] = a[i] + b[i];
        }

    }
}
