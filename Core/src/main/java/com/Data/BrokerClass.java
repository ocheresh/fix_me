package com.Data;

public class BrokerClass {

    private String name;
    private String apple;
    private String orange;
    private String lemon;
    private String pineapple;
    private String money;

    public void setProductQuantPlus(String name, int quant, int price){
        if (name.equalsIgnoreCase("apple"))
            apple = String.valueOf(Integer.valueOf(apple) + quant);
        else if (name.equalsIgnoreCase("orange"))
            orange = String.valueOf(Integer.valueOf(orange) + quant);
        else if (name.equalsIgnoreCase("lemon"))
            lemon = String.valueOf(Integer.valueOf(lemon) + quant);
        else
            pineapple = String.valueOf(Integer.valueOf(pineapple) + quant);
        money = String.valueOf(Integer.valueOf(money) - (quant * price));
    }

    public void setProductQuantMinus(String name, int quant, int price){
        if (name.equalsIgnoreCase("apple"))
            apple = String.valueOf(Integer.valueOf(apple) - quant);
        else if (name.equalsIgnoreCase("orange"))
            orange = String.valueOf(Integer.valueOf(orange) - quant);
        else if (name.equalsIgnoreCase("lemon"))
            lemon = String.valueOf(Integer.valueOf(lemon) - quant);
        else
            pineapple = String.valueOf(Integer.valueOf(pineapple) - quant);
        money = String.valueOf(Integer.valueOf(money) + (quant * price));
    }

    public String getProduct(String name){
        if (name.equalsIgnoreCase("apple"))
            return apple;
        else if (name.equalsIgnoreCase("orange"))
            return orange;
        else if (name.equalsIgnoreCase("lemon"))
            return lemon;
        return pineapple;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApple() {
        return apple;
    }

    public void setApple(String apple) {
        this.apple = apple;
    }

    public String getOrange() {
        return orange;
    }

    public void setOrange(String orange) {
        this.orange = orange;
    }

    public String getLemon() {
        return lemon;
    }

    public void setLemon(String lemon) {
        this.lemon = lemon;
    }

    public String getPineapple() {
        return pineapple;
    }

    public void setPineapple(String pineapple) {
        this.pineapple = pineapple;
    }

    public BrokerClass() {
    }
}
