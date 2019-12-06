package de.ff.jf.bftag.alarmmonitor.models;

public class Car {
    private String name;
    private int fms;

    public Car(String name, int fms) {
        this.name = name;
        this.fms = fms;
    }

    public String getName() {
        return name;
    }


    public int getFms() {
        return fms;
    }

    public void setFms(int fms) {
        this.fms = fms;
    }
}
