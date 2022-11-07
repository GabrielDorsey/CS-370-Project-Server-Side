import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class serverSide {


    public static void main(String[ ] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(12345);
        Socket connection = serverSocket.accept();

        PrintStream toClient = new PrintStream(connection.getOutputStream());
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));


        while(true) {
             String clientMessage, serverMessage;

             while((clientMessage = buffReader.readLine()) != null){
                 System.out.println("Client says: "+  clientMessage);
                 serverMessage = keyInput.readLine();
                 toClient.println(serverMessage);

             }
             toClient.close();
             buffReader.close();
             keyInput.close();
             serverSocket.close();
             connection.close();
             System.exit(0);





        }

    }
}