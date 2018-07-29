import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import handlers.MessageReader;
import handlers.MessageWriter;
import messages.imple.SimpleRequest;
import messages.imple.SimpleResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

public class ServerInit extends Thread {

    private final Kryo kryo = new Kryo();
    private final ResourceBundle rb = ResourceBundle.getBundle("config"); // prop.properties
    private int port;


    public ServerInit() {
        port = Integer.parseInt(rb.getString("port"));
    }


    @Override
    public void run() {
        kryo.register(SimpleResponse.class);
        kryo.register(SimpleRequest.class);
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

            MessageWriter messageWriter = new MessageWriter(new Output(new ObjectOutputStream(socketOfServer.getOutputStream())), kryo);
            MessageReader messageReader = new MessageReader(new Input(new ObjectInputStream(socketOfServer.getInputStream())), kryo);

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }


}
