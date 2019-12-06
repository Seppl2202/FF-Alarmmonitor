package de.ff.jf.bftag.alarmmonitor.models;

public class Address {
    private int zipCode;
    private String street;
    private String number;
    private String location;


    public Address() {
    }

    public Address(int zipCode, String street, String number, String location) {
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.location = location;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getLocation() {
        return location;
    }
}
