package com;

import java.io.IOException;

import com.Client.ClientSample;

public class BrokerClient {

    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) throws IOException {
        new ClientSample(SERVER_PORT);
    }

}
