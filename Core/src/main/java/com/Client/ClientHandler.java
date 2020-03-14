package com.Client;

import com.Data.MarketData;
import com.Data.MessageData;
import com.Helper.Solution;
import com.Message.MyFixMessage;
import com.Message.SeparateMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    public MyFixMessage myFixMessage = new MyFixMessage();
    Solution solution = new Solution();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New connection...");;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        try {
            System.out.println(msg.toString());
            solution.setInfo(msg.toString());
            MessageData.addMessage(msg.toString());
            if (msg.toString().contains("->MARKET")) {
                Thread.sleep(2000);
                if (solution.mesExeutedReject(msg.toString())) {
                    ctx.writeAndFlush(myFixMessage.generatetoBrokerExeuted(msg.toString()));
                }
                else {
                    ctx.writeAndFlush(myFixMessage.generatetoBrokerReject(msg.toString()));
                }
            } else if (msg.toString().contains("->BROKER")) {
                Thread.sleep(2000);
                ctx.writeAndFlush(myFixMessage.generatetoMarket(MarketData.getRandMarket(), SeparateMessage.getBroker(msg.toString())));
            }
        }
        catch (Exception e) {
            System.out.println("Error ClientHandler: " + e.getMessage());
        }
    }
}
