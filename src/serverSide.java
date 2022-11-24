import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class serverSide extends JFrame{


    private JTextField textBox;
    private JButton sendButton;
    private JTextArea chatArea;
    private JPanel mainPanel;
    private  static Socket socket;
    private static InputStreamReader inStream;
    private  static BufferedWriter buffWriter;
    private static  BufferedReader buffReader;
    private static ServerSocket serverSocket;

    /**
     * @param title gets the title so that the frame can update it when the program is ran.
     *              Function: Creates the server side UI to be used throughout the program
     *
     */
    public serverSide(String title){
        //Gets the title of the fram
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        //When the send message button is pressed, send the message to the client
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textBox.getText();
                if(!message.isEmpty()){
                    chatArea.setText(chatArea.getText().trim()+ "\n Server:\t" + message );
                    sendMessage(message);

                }
                else {
                    System.out.println("Could not retrieve");
                }
            }
        });
    }


    /**
     * Method: closeConnection
     * Function: Closes the connection of all of the thing that the server uses, and throws and exception if it does not work
     * @param socket  Used to close the socket connection
     * @param serverSocket Used to close the serverSocket connection
     * @param buffReader Used to close the reader Connection
     * @param buffWriter Used to close the writer
     */
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


    /**
     * Method: sendMessage
     * Function: Sends a message to the client
     * @param message : Message to send to the client
     */
    public void sendMessage(String message){
        //Attempts to send the message to the client by writing to the buffer and flushes the buffer out, throws and exception if it cannot.
        try{

            buffWriter.write(message);
            buffWriter.newLine();
            buffWriter.flush();

        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("Could Not Send Message.");
        }
    }


    /**
     * Main Method that handles the runtime of the program
     * Function: Will load the JFrame so that the user can type out to the screen and send messages.
     * Will attempt to establish a connection to the client so that it can send messages back and forth to one another.
     */
    public static void main(String[ ] args) {

        serverSide mainFrame =  new serverSide("Server Side Application");
        mainFrame.setVisible(true);
        //Attempts to establish a connection the client so that messages can be sent back and forth
        try {
            try {
                //Creates all of the required tools to send messages
                serverSocket = new ServerSocket(12345);
                socket = serverSocket.accept(); //Establishes the connection to the client
                //Creates a buffered reader and writer so that the application can send and receive messages
                buffReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            } catch(IOException e){ //If there is an exception, close the connection
                e.printStackTrace();
                closeConnection(socket,serverSocket, buffReader, buffWriter);
            }


            //While the connection is still establish, get the client's message and update the textbox.
            while(true) {
                String fromClient;

                while(!Objects.equals(fromClient = buffReader.readLine(), "null")){ //While the object does not equal null, update the chat box.
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