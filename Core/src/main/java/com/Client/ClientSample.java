package com.Client;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ClientSample {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String addr;
    private int port;
    private String nickname;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;

    public ClientSample(String addr, int port)
    {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        }
        catch (IOException e)
        {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        }
        catch (IOException e)
        {
            ClientSample.this.downService();
        }
    }

    private void pressNickname()
    {
        System.out.println("Press your name: ");
        try {
            nickname = inputUser.readLine();
            out.write("Hello " + nickname + "\n");
            out.flush();
        }catch (IOException ignored) {}
    }

    private void downService()
    {
        try {
            if (!socket.isClosed())
            {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    private class ReadMsg extends Thread
    {
        @Override
        public void run()
        {
            String str;
            try {
                while (true)
                {
                    str = in.readLine();
                    if (str.equalsIgnoreCase("stop"))
                    {
                        ClientSample.this.downService();
                        break;
                    }
                    System.out.println(str);
                }
            }catch (IOException e)
            {
                ClientSample.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread
    {
        @Override
        public void run()
        {
            while (true)
            {
                String userWord;
                try {
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    userWord = inputUser.readLine();
                    if (userWord.equalsIgnoreCase("stop"))
                    {
                        out.write("stop" + "\n");
                        ClientSample.this.downService();
                        break;
                    }
                    else
                    {
                        out.write("(" + dtime + ") " + nickname + ": " + userWord + "\n");
                    }
                    out.flush();
                }
                catch (IOException e) {}
            }
        }
    }
}
