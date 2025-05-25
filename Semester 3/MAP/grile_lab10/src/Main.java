//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class StaticExample{
    static int count=0;
    StaticExample(){
        count++;
    }
    static int getCount(){
        return count;
    }
}
public class Main {

    public static void main(String[] args) {
        new StaticExample();
        new StaticExample();
        System.out.println(StaticExample.getCount());
    }
}