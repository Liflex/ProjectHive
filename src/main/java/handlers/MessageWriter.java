package handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import messages.imple.SimpleResponse;


public class MessageWriter extends Thread {

    private final Kryo kryo;
    private final Output output;

    public MessageWriter(Output output, Kryo kryo) {
        this.output = output;
        this.kryo = kryo;
    }

    public void response (SimpleResponse simpleResponse) {
        kryo.writeObject(output, simpleResponse);
    }
}
