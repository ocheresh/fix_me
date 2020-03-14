package com.Data;

public class MarketClass {

    private String name;
    private String apple;
    private String orange;
    private String lemon;
    private String pineapple;

    public String getProductPrice(String name){
        if (name.equalsIgnoreCase("apple"))
            return apple;
        else if (name.equalsIgnoreCase("orange"))
            return orange;
        else if (name.equalsIgnoreCase("lemon"))
            return lemon;
        return pineapple;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApple(String apple) {
        this.apple = apple;
    }

    public void setOrange(String orange) {
        this.orange = orange;
    }

    public void setLemon(String lemon) {
        this.lemon = lemon;
    }

    public void setPineapple(String pineapple) {
        this.pineapple = pineapple;
    }

    public MarketClass() {
    }
}
