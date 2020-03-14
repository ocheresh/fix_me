package com.Helper;

import com.Data.BrokerClass;
import com.Data.BrokerData;
import com.Data.MarketClass;
import com.Data.MarketData;

public class Solution {

    private String instrument  = null; // 54 buy, sell, exeuted or reject
    private String quantity    = null; // 38
    private String broker      = null; // 49
    private String market      = null; // 56
    private String price       = null; // 44
    private String number      = null; // 34
    private String date        = null; // 52
    private String shareproduct= null; // 55

    public boolean setInfo(String msg) {
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            broker = temp[2].replace("49=", "");
            instrument = temp[3].replace("54=", "");
            quantity = temp[4].replace("38=", "");
            market = temp[6].replace("56=", "");
            price = temp[7].replace("44=", "");
            shareproduct = temp[8].replace("55=", "");
            return (true);
        }
        return false;
    }

    public boolean mesExeutedReject(String msg) {
        BrokerClass brokerClass = BrokerData.getBroker(broker);
        MarketClass marketClass = MarketData.getMarket(market);
        if (msg.contains("54=D")){
            int price_int = Integer.valueOf(price);
            int quantity_int = Integer.valueOf(quantity);
            if (price_int < Integer.valueOf(marketClass.getProductPrice(shareproduct))) //якщо ціна Брокера менше ціни Ринка то помилка
                return (false);
            if (price_int * quantity_int > Integer.valueOf(brokerClass.getMoney()))//якщо кількість помножена на ціну більше за кошти то помилка
                return (false);
            brokerClass.setProductQuantPlus(shareproduct, quantity_int, price_int);
            BrokerData.deleteBroker(broker);
            BrokerData.addBroker(brokerClass);
            return true;
        }
        else if (msg.contains("54=F")){
            int price_int = Integer.valueOf(price);
            int quantity_int = Integer.valueOf(quantity);
            if (price_int > Integer.valueOf(marketClass.getProductPrice(shareproduct))) //якщо ціна Брокера менше ціни Ринка то помилка
                return (false);
            if (quantity_int > Integer.valueOf(brokerClass.getProduct(shareproduct))) //якщо кількіть більше ніж є наявна у брокера то помилка
                return (false);
            brokerClass.setProductQuantMinus(shareproduct, quantity_int, price_int);
            BrokerData.deleteBroker(broker);
            BrokerData.addBroker(brokerClass);
            return true;
        }
        else {
            System.out.println("Error in solution mesExeutedReject " + msg);
        }
        return false;
    }

}
