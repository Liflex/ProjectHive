package ru.dmitartur.socketserver.messages.imple;


import ru.dmitartur.socketserver.messages.interf.Command;

public class CommandRequestImpl extends Command {

    private String text;
    private Object object;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
