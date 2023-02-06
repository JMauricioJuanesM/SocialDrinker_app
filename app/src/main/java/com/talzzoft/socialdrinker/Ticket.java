package com.talzzoft.socialdrinker;

public class Ticket {

    private String type ="SA";
    private String litros = "0";
    private String max_hour = "22";

    Ticket(String type){
        this.type = type;
        switch (type){
            case "SA":
                litros = "0";
                max_hour = "22";
            case "CA":
                litros = "1.5";
                max_hour = "22";

            case "LQ":
                litros = "6";
                max_hour = "3";

            default:
                type="CA";
                litros = "0";
                max_hour = "22";
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLitros() {
        return litros;
    }

    public void setLitros(String litros) {
        this.litros = litros;
    }

    public String getMax_hour() {
        return max_hour;
    }

    public void setMax_hour(String max_hour) {
        this.max_hour = max_hour;
    }

}
