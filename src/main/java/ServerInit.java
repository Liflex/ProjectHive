import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import messages.imple.SimpleCommand;
import messages.interf.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

public class ServerInit extends Thread {

    private final ResourceBundle rb = ResourceBundle.getBundle("config"); // prop.properties
    private int port;


    public ServerInit() {
        port = Integer.parseInt(rb.getString("port"));
    }


    @Override
    public void run() {
        ServerSocket listener = null;
        BufferedReader is;
        ObjectOutputStream oos;
        Socket socketOfServer = null;

        // Try to open a server socket on port 7071


        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            System.out.println("Server is waiting to accept user...");

            // Accept client connection request
            // Get new Socket at Server.
            socketOfServer = listener.accept();
            System.out.println("Accept a client!");

            // Open input and output streams
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            oos = new ObjectOutputStream(socketOfServer.getOutputStream());

            Kryo kryo = new Kryo();
            kryo.register(SimpleCommand.class);
            kryo.register(Command.class);

            SimpleCommand simpleCommand = new SimpleCommand();
            simpleCommand.setText("Hello");

            Output output = new Output(oos);
            kryo.writeObject(output, simpleCommand);
            output.close();

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }


}
