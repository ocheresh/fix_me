package com.Message;

public class SeparateMessage {

    public static String getDate(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[5].replace("52=", "");
        }
        return null;
    }

    public static String getNumber(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[1].replace("34=", "");
        }
        return null;
    }

    public static String getInstrument(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[3].replace("54=", "");
        }
        return null;
    }

    public static String getCheckSum(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[9].replace("10=", "");
        }
        return null;
    }

    public static String getQuantuty(String msg) {
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[4].replace("38=", "");
        }
        return null;
    }

    public static String getMarket(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[6].replace("56=", "");
        }
        return null;
    }

    public static String getShareproduct(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[8].replace("55=", "");
        }
        return null;
    }

    public static String getPrice(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[7].replace("44=", "");
        }
        return null;
    }

    public static String getBroker(String msg){
        String temp[] = msg.split("\\|");
        if (temp.length == 10){
            return temp[2].replace("49=", "");
        }
        return null;
    }

    public static String MesForCheck(String msg){
        String temp[] = SeparateMessage.cleanMessfromSend(msg).split("\\|");
        String res = new String();
        long sum = 0;
        if (temp.length == 10){
            for (int i = 0; i < 9; i++){
                res += temp[i] + "|";
            }
            for (byte b : res.getBytes())
                sum += b;
            sum = sum % 256;
            return (Long.toString(sum));
        }
        return null;
    }

    private static String cleanMessfromSend(String msg){
        if (msg.contains("BROKER->MARKET: "))
            return msg.replace("BROKER->MARKET: ", "");
        else if (msg.contains("MARKET->BROKER: "))
            return (msg.replace("MARKET->BROKER: ", ""));
        return (null);
    }
}
