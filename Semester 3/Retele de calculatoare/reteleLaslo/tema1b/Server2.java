import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Server2 {
    public static void main(String[] args) {
        int[] buffer =new int[1024];

        try {
            ServerSocket s = new ServerSocket(5000);
            while (true) {
                Socket c = s.accept();
                System.out.println("S-a conectat un client.");

                DataInputStream in = new DataInputStream(c.getInputStream());
                DataOutputStream out = new DataOutputStream(c.getOutputStream());

		byte[] countBytes = new byte[4];
            	in.read(countBytes);
            	int count = ByteBuffer.wrap(countBytes).getInt();
            	count = Integer.reverseBytes(count);
		System.out.println("Numarul de numere este "+count);
                
		byte[] numbersBytes = new byte[count * 4];
            	in.read(numbersBytes);
		for (int i = 0; i < count; i++) {
                  int number = ByteBuffer.wrap(numbersBytes, i * 4, 4).getInt();
                  number = Integer.reverseBytes(number);  // Conversia din rețea în ordinea gazdei
                //  System.out.println("Numărul " + (i + 1) + ": " + number);
            	  buffer[i]=number;
		}

                int suma = 0;
                for (int i = 0; i < count; i++) {
                    suma += buffer[i];
                }

                if (count != 0) {
                    System.out.println("Serverul a calculat suma: " + suma);
                } else {
                    System.out.println("Nu am primit un input valid.");
                }
                out.writeInt(Integer.reverseBytes(suma));
		c.close();

                // Resetam bufferul
                for (int i = 0; i < count; i++) {
                    buffer[i] = 0;
                }
            }

        } catch (IOException e) {
            System.out.println("Eroare la crearea socketului server: " + e.getMessage());
        }
    }
}

