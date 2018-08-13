package ru.dmitartur.socketserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.dmitartur.socketserver.handlers.MessageReader;
import ru.dmitartur.socketserver.handlers.MessageWriter;
import ru.dmitartur.socketserver.messages.imple.CommandRequestImpl;
import ru.dmitartur.socketserver.messages.imple.CommandResponseImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

public class SocketServerInit extends Thread {

    private final Kryo kryo = new Kryo();
    //private final ResourceBundle rb = ResourceBundle.getBundle("config"); // prop.properties
    private int port;


    public SocketServerInit() {
    }


    @Override
    public void run() {
        kryo.register(CommandResponseImpl.class);
        kryo.register(CommandRequestImpl.class);
        ServerSocket listener = null;
        Socket socketOfServer = null;
        // Try to open a server socket on port 7071


        try {
            listener = new ServerSocket(7070);
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
