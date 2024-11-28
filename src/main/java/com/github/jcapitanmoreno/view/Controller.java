package com.github.jcapitanmoreno.view;

import java.io.IOException;

public abstract class Controller  {

    public abstract void onOpen(Object input) throws IOException;

    public abstract void onClose(Object output);

    public abstract void informationAlert (String text1,String text2, String text3);
    public abstract void errorAlert (String text1, String text2, String text3);
    public abstract void warningAlert (String text1, String text2, String text3);
}
