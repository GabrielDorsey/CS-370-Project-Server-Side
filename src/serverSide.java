import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSide extends JFrame{


    private JTextField textBox;
    private JButton sendButton;
    private JButton sendFileButton;
    private JTextArea chatArea;
    private JPanel mainPanel;
    private  static Socket socket;
    private static InputStreamReader inStream;
    private  static BufferedWriter buffWriter;
    private static  BufferedReader buffReader;
    private static ServerSocket serverSocket;
    public serverSide(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textBox.getText();
                if(!message.isEmpty()){
                    chatArea.setText(chatArea.getText().trim()+ "\n Server:\t" + message );
                    sendMessage(message);

                }
                else {
                    System.out.println("Could not retrive");
                }
            }
        });
    }

    public static void closeConnection(Socket socket,ServerSocket serverSocket,  BufferedReader buffReader, BufferedWriter buffWriter){
        try{
            if(socket!= null){
                socket.close();
            }
            if(buffReader != null) {
                buffReader.close();
            }
            if(buffWriter!= null){
                buffWriter.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void sendMessage(String message){
        try{
            buffWriter.write(message);
            buffWriter.newLine();
            buffWriter.flush();

        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("Could Not Send Message.");
        }
    }
    public static void main(String[ ] args) {

        serverSide mainFrame =  new serverSide("Client Side App");
        mainFrame.setVisible(true);

        try {
            try {
                serverSocket = new ServerSocket(12345);
                socket = serverSocket.accept();
                buffReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            } catch(IOException e){
                e.printStackTrace();
                closeConnection(socket,serverSocket, buffReader, buffWriter);
            }



            while(true) {
                String fromClient;

                while((fromClient = buffReader.readLine()) != null){
                    fromClient = buffReader.readLine();
                    mainFrame.chatArea.setText(mainFrame.chatArea.getText().trim()+ "\n Client:\t" + fromClient);


                }



            }


        }catch(IOException e ){
            e.printStackTrace();
            closeConnection(socket, serverSocket,  buffReader,buffWriter);
        }

    }
}