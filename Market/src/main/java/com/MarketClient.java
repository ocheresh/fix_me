package com;

import java.io.IOException;
import com.Client.ClientSample;


public class MarketClient {

    private static final int SERVER_PORT = 5000;
    private static String ipAddr = "localhost";

    public static void main(String[] args) throws IOException
    {
        new ClientSample(ipAddr, SERVER_PORT);
    }

}
