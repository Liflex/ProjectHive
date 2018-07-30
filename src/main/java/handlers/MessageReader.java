package handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import messages.imple.CommandRequestImpl;


public class MessageReader extends Thread{

    private final Kryo kryo;
    private final Input input;

    private CommandRequestImpl commandRequestImpl;

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
                    CommandRequestImpl commandRequestImpl = kryo.readObject(input, CommandRequestImpl.class);
                    System.out.println("Работает");
                    //new SimpleWorker(commandRequestImpl).join();
                    System.out.println("Закончили");
                    input.close();
                }
            }
        } catch (Exception e) {
            input.close();
        }
    }
}
