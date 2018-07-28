package messages.imple;

import messages.interf.Command;

public class SimpleCommand extends Command {

    private String text;
    private Object object;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
