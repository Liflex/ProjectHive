package handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import messages.imple.SimpleRequest;


public class MessageReader extends Thread{

    private final Kryo kryo;
    private final Input input;

    private SimpleRequest simpleRequest;

    public MessageReader(Input input, Kryo kryo)  {
       this.input = input;
       this.kryo = kryo;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (input.available() != 0) {
                    SimpleRequest simpleRequest = kryo.readObject(input, SimpleRequest.class);
                    System.out.println("Работает");
                    //new SimpleWorker(simpleRequest).join();
                    System.out.println("Закончили");
                    input.close();
                }
            }
        } catch (Exception e) {
            input.close();
        }
    }
}
