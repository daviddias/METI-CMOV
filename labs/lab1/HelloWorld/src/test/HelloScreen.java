package test;

import javax.microedition.lcdui.*;



class HelloScreen
    extends TextBox
    implements CommandListener
{
    private final HelloWorldMIDlet midlet;
    private final Command exitCommand;


    HelloScreen(HelloWorldMIDlet midlet, String string)
    {
        super("HelloWorldMIDlet", string, 256, 0);
        this.midlet = midlet;
        exitCommand = new Command("Exit", Command.EXIT, 1);
        addCommand(exitCommand);
        setCommandListener(this);
    }


    public void commandAction(Command c, Displayable d)
    {
        if (c == exitCommand)
        {
            midlet.exitRequested();
        }
    }
}
