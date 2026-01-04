import java.net.*;
import java.io.*;

public class Main {

    public static void main(String args[]) throws Exception {
        ServerSocket s = new Socket(1234,5,InetAddress.getAllByName());
        System.out.println(s.getInetAddress());
        byte b[] = new byte[100];

        while(true) {
            Socket c = s.accept();
            System.out.println("Client connected!");
            c.getInputStream().read(b);
            System.out.println(new String(b));

            c.close();
        }
    }

}
