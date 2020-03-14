
import com.Data.MarketData;
import com.Data.MessageData;
import com.Message.MyFixMessage;
import com.Message.SeparateMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import java.util.HashMap;
import java.util.Map;
import com.Data.BrokerData;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf tmp;

    private static final Map<String, Channel> brokers = new HashMap<>();
    private static final Map<String, Channel>   markets = new HashMap<>();
    private static int unique_id = 100000;
    private static boolean start = false;

    MyFixMessage myFixMessage = new MyFixMessage();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Error exception caught: " + cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        String correct_put_to_table[] = ctx.channel().localAddress().toString().split(":");

        if (start == false) {
            start = true;
            MarketData.connect();
            BrokerData.connect();
            MessageData.connect();
            BrokerData.deleteDataBase();
            MarketData.deleteDataBase();
            MessageData.deleteDataBase();
        }

        if (correct_put_to_table.length > 1 && correct_put_to_table[1].equals("5000")) {
            try {
                BrokerData.addBroker(String.valueOf(unique_id));
                System.out.println("New Broker connection " + unique_id);
                if (MarketData.size() > 0) {
                    String temp = myFixMessage.generatetoMarket(MarketData.getRandMarket(), String.valueOf(unique_id));
                    System.out.println(temp);
                    sendMessagetoMarket(temp);
                }
                brokers.put(String.valueOf(addUniqueId()), ctx.channel());
            }
            catch (Exception e) {
                System.out.println("Error Broker add: " + e.getMessage());
            }
        }
        else if (correct_put_to_table.length > 1 && correct_put_to_table[1].equals("5001")) {
            System.out.println("New Market connection " + unique_id);
            try {
                MarketData.addMarket(String.valueOf(unique_id));
                if (BrokerData.size() > 0) {
                    String temp = myFixMessage.generatetoBroker();
                    System.out.println(temp);
                    sendMessagetoBrokers(temp);
                }
                markets.put(String.valueOf(addUniqueId()), ctx.channel());
            }
            catch (Exception e){
                System.out.println("Error Market add: " + e.getMessage());
            }
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            String correct_put_to_table[] = ctx.channel().localAddress().toString().split(":");
            if (correct_put_to_table.length > 1 && correct_put_to_table[1].equals("5000")) {
                try {
                    cleanBroker();
                }
                catch (Exception e) {
                    System.out.println("Error Broker remove: " + e.getMessage());
                }
            }
            else if (correct_put_to_table.length > 1 && correct_put_to_table[1].equals("5001")) {
                try {
                    cleanMarket();
                }
                catch (Exception e) {
                    System.out.println("Error Market remove: " + e.getMessage());
                }
            }
        }
        catch (Exception e) {}

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String temp = msg.toString();
            System.out.println("Server send message: " + temp);
            if (SeparateMessage.MesForCheck(msg.toString()).equalsIgnoreCase(SeparateMessage.getCheckSum(msg.toString()))) {
                if (msg.toString().contains("->MARKET")) {
                    Thread.sleep(2000);
                    sendMessagetoMarket(msg.toString());
                } else if (msg.toString().contains("->BROKER")) {
                    Thread.sleep(2000);
                    sendMessagetoBroker(msg.toString());
                }
            }
            else
                System.out.println("Error in checksum: " + SeparateMessage.MesForCheck(msg.toString()) + " " + SeparateMessage.getCheckSum(msg.toString()));
        } catch (Exception e){}

    }

    private void cleanBroker() {
        brokers.forEach((k, v) -> {
            if (!(brokers.get(k).isActive())) {
                System.out.println("Broker deleted, unique id: " + k);
                BrokerData.deleteBroker(k);
                brokers.remove(k);
            }
        });

    }

    private void cleanMarket() {
        markets.forEach((k, v) -> {
            if (!(markets.get(k).isActive())) {
                System.out.println("Market deleted, unique id: " + k);
                MarketData.deleteMarket(k);
                markets.remove(k);
            }
        });
    }


    private void sendMessagetoBroker(String msg) {
        String res = SeparateMessage.getBroker(msg);
        brokers.forEach((k, v) -> {
            if (res.equalsIgnoreCase(k))
                if (brokers.get(k).isActive())
                    brokers.get(k).writeAndFlush(msg);
        });
    }

    private void sendMessagetoBrokers(String msg) {
        brokers.forEach((k, v) -> {
            if (brokers.get(k).isActive())
                brokers.get(k).writeAndFlush(msg);
            else {
                BrokerData.deleteBroker(k);
                System.out.println("Broker remove senMesss " + k);
                brokers.remove(k);
            }
        });
    }

    private void sendMessagetoMarket(String msg) {
        String splitMes[] = msg.split("\\|");
        String id_market = null;
        if (splitMes.length > 4)
            id_market = splitMes[6].replace("56=", "");
        if (id_market != null && markets.get(id_market).isActive()) {
            markets.get(id_market).writeAndFlush(msg);
        }
    }

    private int addUniqueId() {
        int temp = unique_id;
        if (unique_id < Integer.MAX_VALUE)
            unique_id++;
        return (temp);
    }
}
