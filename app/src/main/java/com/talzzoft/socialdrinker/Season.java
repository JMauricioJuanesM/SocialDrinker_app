package com.talzzoft.socialdrinker;

public class Season {
    private String type = "n";
    private String sa_week = "2";
    private String ca_week = "1";
    private String lq_month = "1";

    Season(int g_month){
        if(g_month == 10 || g_month == 6 || g_month == 2) {
            type = "s";
            sa_week = "3";
            ca_week = "0";
            lq_month = "0";
        } else if(g_month == 12 || g_month == 8 || g_month == 4) {
            type = "v";
            sa_week = "3";
            ca_week = "2";
            lq_month = "3";
        } else{
            type = "n";
            sa_week = "2";
            ca_week = "1";
            lq_month = "1";
        }
    }

    Season(String type){
        this.type = type;
        switch (type){
            case "n":
                sa_week = "2";
                ca_week = "1";
                lq_month = "1";

            case "s":
                sa_week = "3";
                ca_week = "0";
                lq_month = "0";

            case "v":
                sa_week = "3";
                ca_week = "2";
                lq_month = "3";

            default:
                type = "n";
                sa_week = "2";
                ca_week = "1";
                lq_month = "1";
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSa_week() {
        return sa_week;
    }

    public void setSa_week(String sa_week) {
        this.sa_week = sa_week;
    }

    public String getCa_week() {
        return ca_week;
    }

    public void setCa_week(String ca_week) {
        this.ca_week = ca_week;
    }

    public String getLq_month() {
        return lq_month;
    }

    public void setLq_month(String lq_month) {
        this.lq_month = lq_month;
    }
}
