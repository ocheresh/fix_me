package com;

import java.io.IOException;
import com.Client.ClientSample;


public class MarketClient {

    private static final int SERVER_PORT = 5001;

    public static void main(String[] args) throws IOException
    {
        new ClientSample(SERVER_PORT);
    }

}
