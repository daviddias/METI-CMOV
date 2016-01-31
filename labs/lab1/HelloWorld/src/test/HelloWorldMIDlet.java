package test;


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class HelloWorldMIDlet
    extends MIDlet
{
    public HelloWorldMIDlet()
    {
    }


    public void startApp()
    {
        Displayable current = Display.getDisplay(this).getCurrent();
        if(current == null)
        {
            HelloScreen helloScreen = new HelloScreen(this, "Hello World.");
            Display.getDisplay(this).setCurrent(helloScreen);
        }
    }


    public void pauseApp()
    {
    }


    public void destroyApp(boolean b)
    {
    }


    // A convenience method for exiting
    void exitRequested()
    {
        destroyApp(false);
        notifyDestroyed();
    }
}


