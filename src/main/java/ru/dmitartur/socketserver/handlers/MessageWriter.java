package ru.dmitartur.socketserver.handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import ru.dmitartur.socketserver.messages.imple.CommandResponseImpl;


public class MessageWriter extends Thread {

    private final Kryo kryo;
    private final Output output;

    public MessageWriter(Output output, Kryo kryo) {
        this.output = output;
        this.kryo = kryo;
    }

    public void response (CommandResponseImpl commandResponseImpl) {
        kryo.writeObject(output, commandResponseImpl);
    }
}
