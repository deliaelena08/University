import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
public class Client2 {

    public static void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.out.println("Utilizare: java Client <adresa IP> <port>");
            return;
        }

        // citim ip-ul si port-ul
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);
        Socket clientSocket = new Socket(ipAddress, port);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
        DataInputStream inFromServer = new DataInputStream(bufferedInputStream);

        System.out.println("Introduceti un sir de caractere: ");
        String input = reader.readLine();
	int size=input.length();
       // verificam inputul
        if(size<1){
	  System.out.println("Input invalid");
	  return;
	}
        System.out.println("Trimitem " + size + " caractere.");
        outToServer.writeInt(size);
        outToServer.flush();

        outToServer.write(input.getBytes());
        outToServer.flush();
        System.out.println("Datele au fost trimise cu success");

        int nrspaces = inFromServer.readInt();  // Citim numarul de la server
        System.out.println("Numarul de spatii este "+nrspaces);
        reader.close();
        clientSocket.close();

  }
}
