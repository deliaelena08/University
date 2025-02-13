import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
public class Client1 {

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

        System.out.println("Introduceti un sir de numere separate prin spatiu: ");
        String input = reader.readLine();
        String[] tokens = input.split(" ");
        List<Integer> numbers = new ArrayList<>();

        // verificam inputul
        for (String token : tokens) {
            if (isNumber(token)) {
                numbers.add(Integer.parseInt(token));
            } else {
                System.out.println("Input invalid: " + token + ". Se asteaptă doar numere întregi.");
                clientSocket.close();
                return;
            }
        }

        int numberOfElements = numbers.size();
        System.out.println("Trimitem " + numberOfElements + " numere.");
        outToServer.writeInt(numberOfElements);
        outToServer.flush();

	    //cream un buffer pentru numerele primite
        ByteBuffer buffer = ByteBuffer.allocate(numberOfElements * 4);
        for (int num : numbers) {
            //outToServer.writeInt(num);
            buffer.putInt(num);
        }
        outToServer.write(buffer.array());
        outToServer.flush();
        System.out.println("Datele au fost trimise cu success");

        try {
            //if (inFromServer.available() > 0) {  // Verificăm dacă sunt date disponibile
                System.out.println("Asteptam suma de la server...");
                int sum = inFromServer.readInt();  // Citim suma de la server
                System.out.println("Suma numerelor este: " + sum);
            //} else {
                //System.out.println("Nu sunt date disponibile de la server.");
            //}
        } catch (EOFException e) {
            System.out.println("Eroare la citire");
        }
        reader.close();
        clientSocket.close();
    }

    // verificam daca e numar
    private static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
