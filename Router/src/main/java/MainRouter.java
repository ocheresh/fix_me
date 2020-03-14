
import com.Data.BrokerData;
import com.Data.MarketData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class MainRouter implements Runnable {

    private int ports[] = new int[2];
    static final int PORT_BROKER = 5000;
    static final int PORT_MARKET = 5001;
    private ChannelFuture f[] = new ChannelFuture[2];

    public MainRouter() {
        ports[0] = PORT_BROKER;
        ports[1] = PORT_MARKET;
    }

    public static void main(String[] args) throws Exception {
        new MainRouter().run();
    }

    @Override
    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("Server created......");
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    try {
                        ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ProcessingHandler());
                    }
                   catch (Exception | UnknownError e) {System.out.println("Error MainRouter: " + e.getMessage());}
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            f[0] = b.bind(PORT_BROKER).sync();
            f[1] = b.bind(PORT_MARKET).sync();
            f[0].channel().closeFuture().sync();
            f[1].channel().closeFuture().sync();
        }
        catch (Exception | UnknownError e){ System.out.println("Error server: " + e.getMessage());}
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            BrokerData.closeDBbroker();
            MarketData.closeDBmarket();
        }
    }

}
