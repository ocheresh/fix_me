




//import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.concurrent.*;

public class RouterThread {

//    private AsynchronousServerSocketChannel server;
    private Future<AsynchronousSocketChannel> acceptFuture;
    private AsynchronousSocketChannel clientChannel;

    public RouterThread(Future<AsynchronousSocketChannel> acceptFuturet)
    {
        acceptFuture = acceptFuturet;
        runServer();
    }

    public void runServer() {
        try {
            clientChannel = acceptFuture.get();
            if ((clientChannel != null) && (clientChannel.isOpen())) {
                while (true) {
                    ByteBuffer buffer = ByteBuffer.allocate(32);
                    Future<Integer> readResult = clientChannel.read(buffer);

                    // perform other computations

                    readResult.get();

                    buffer.flip();
                    Future<Integer> writeResult = clientChannel.write(buffer);

                    // perform other computations
                    String read = new String(buffer.array()).trim();
                    if (read.equals("Stop") || !(clientChannel.isOpen()))
                    {
                        this.downService();
                        break;
                    }
                    if (!read.equals(""))
                        System.out.println("Client say: " + read);
                    writeResult.get();
                    buffer.clear();
                }
                this.downService();
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
    private void downService() {
        try {
            clientChannel.close();
        }
        catch (Exception ignored) {}
    }


//    private Socket socket;
//    private BufferedReader in;
//    private BufferedWriter out;
//
//    public RouterThread(Socket socket) throws IOException {
//        this.socket = socket;
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        start();
//        System.out.println("Create new server");
//    }
//
//    @Override
//    public void run()
//    {
//        String word;
//        try
//        {
//            word = in.readLine();
//            try {
//                out.write(word + "\n");
//                out.flush();
//            }catch (IOException ignored) {}
//            try{
//                while (true)
//                {
//                    word = in.readLine();
//                    if(word.equals("stop"))
//                    {
//                        this.downService();
//                        break;
//                    }
//                    System.out.println("Echoing: " + word);
//                    this.send(word);
//                }
//            } catch (NullPointerException ignored) {}
//        } catch (IOException e) {}
//
//    }
//
//    private void send(String msg)
//    {
//        try {
//            out.write(msg + "\n");
//            out.flush();
//        }catch (IOException ignored) {}
//    }
//
//    private void downService() {
//        try {
//            if(!socket.isClosed()) {
//                socket.close();
//                in.close();
//                out.close();
////                for (ServerSomthing vr : Server.serverList) {
////                    if(vr.equals(this)) vr.interrupt();
////                    Server.serverList.remove(this);
////                }
//            }
//        } catch (IOException ignored) {}
//    }

}
