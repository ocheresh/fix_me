package com.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientSample {

    private final int port;
    private ChannelFuture f;
    private String host;

    public ClientSample(int port) {
        this.port = port;
        host = "localhost";
        try {
            run();
        }
        catch (Exception e) {};
    }

    public void run(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder(), new StringDecoder(), new ClientHandler());
                }
            });
            f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception | UnknownError  e) {System.out.println("Error Client" +
                "sample: " + e.getMessage());}
        finally {
            workerGroup.shutdownGracefully();
        }
    }
}

