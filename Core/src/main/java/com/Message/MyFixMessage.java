package com.Message;

import com.Data.BrokerData;
import com.Data.MarketData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyFixMessage {

    private String instrument; //  54 buy, sell, exeuted or reject
    private String quantity; // 38
    private String broker; //49
    private String market; // 56
    private String price; // 44
    private String number; // 34
    private String date; // 52
    private String shareproduct; //55
    private static int number_of_message = 0;

    public MyFixMessage() {}

    private String byu = "D";
    private String sell = "F";
    private String exeuted = "A";
    private String reject = "3";

    public String generatetoMarket(String idMarket, String idBroker)
    {
        String temp = "8=FIX.4.2|";
        number = "34=" + number_of_message++ + "|";
        broker = "49=" + idBroker + "|";
        if ((int)(Math.random() * 10) % 2 == 0)
            instrument = "54=" + byu + "|";
        else
            instrument = "54=" + sell + "|";
        quantity = "38=" + (int)(Math.random()*100) + "|";
        date = "52=" + date_text() + "|";
        market = "56=" + idMarket + "|";
        price = "44=" + (int)(Math.random() * 10000) + "|";
        shareproduct = "55=" + MarketData.getRandomProduct() + "|";
        temp += number + broker + instrument + quantity + date + market + price + shareproduct;
        temp = "BROKER->MARKET: " + temp + setCheckSum(temp);
        return (temp);
    }

    public String generatetoBrokerExeuted(String msg)
    {
        String temp = "8=FIX.4.2|";
        if (MarketData.size() > 0) {
            number = "34=" + number_of_message++ + "|";
            broker = "49=" + SeparateMessage.getBroker(msg) + "|";
            instrument = "54=" + exeuted + "|";
            quantity = "38=" + SeparateMessage.getQuantuty(msg) + "|";
            date = "52=" + date_text() + "|";
            market = "56=" + SeparateMessage.getMarket(msg) + "|";
            price = "44=" + SeparateMessage.getPrice(msg) + "|";
            shareproduct = "55=" + SeparateMessage.getShareproduct(msg) + "|";
            temp += number + broker + instrument + quantity + date + market + price + shareproduct;
            temp = "MARKET->BROKER: " + temp + setCheckSum(temp);
        }
        else
            temp = "Please add some Market. Market is null";
        return (temp);
    }

    public String generatetoBrokerReject(String msg)
    {
        String temp = "8=FIX.4.2|";
        if (MarketData.size() > 0) {
            number = "34=" + number_of_message++ + "|";
            broker = "49=" + SeparateMessage.getBroker(msg) + "|";
            instrument = "54=" + reject + "|";
            quantity = "38=" + SeparateMessage.getQuantuty(msg) + "|";
            date = "52=" + date_text() + "|";
            market = "56=" + SeparateMessage.getMarket(msg) + "|";
            price = "44=" + SeparateMessage.getPrice(msg) + "|";
            shareproduct = "55=" + MarketData.getRandomProduct() + "|";
            temp += number + broker + instrument + quantity + date + market + price + shareproduct;
            temp = "MARKET->BROKER: " + temp + setCheckSum(temp);
        }
        else
            temp = "Please add some Market. Market is null";
        return (temp);
    }

    public String generatetoBroker()
    {
        String temp = "8=FIX.4.2|";
        if (MarketData.size() > 0) {
            number = "34=" + number_of_message++ + "|";
            broker = "49=" + BrokerData.getRandBroker() + "|";
            if ((int)(Math.random() * 10) % 2 == 0)
                instrument = "54=" + exeuted + "|";
            else
                instrument = "54=" + reject + "|";
            quantity = "38=" + (int) (Math.random() * 100) + "|";
            date = "52=" + date_text() + "|";
            market = "56=" + MarketData.getRandMarket() + "|";
            price = "44=" + (int) (Math.random() * 10000) + "|";
            shareproduct = "55=" + MarketData.getRandomProduct() + "|";
            temp += number + broker + instrument + quantity + date + market + price + shareproduct;
            temp = "MARKET->BROKER: " + temp + setCheckSum(temp);
        }
        else
            temp = "Please add some Market. Market is null";
        return (temp);
    }

    private String setCheckSum(String mes)
    {
        long sum = 0;
        for (byte b : mes.getBytes())
            sum += b;
        sum = sum % 256;
        return ("10=" + Long.toString(sum) + "|");
    }

    private String date_text()
    {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
}
