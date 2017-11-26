package com.example.georgios.plans.model;

/**
 * Created by Georgios on 25/11/2017.
 */

public class NumberString {
    private long number;
    private String str;

    public void setNumber(Long idPlan) {
        this.number = idPlan;
    }

    public void setStr(String idUser) {this.str = idUser;}

    public long getNumber(){return this.number;}

    public String getStr(){
        return this.str;
    }
}
