package de.ff.jf.bftag.alarmmonitor;

import java.security.Key;
import java.util.List;

public class Alarm {
    private List<String> alarmedCars;
    private Keyword keyword;
    private Address address;


    public Alarm() {
    }

    public Alarm(List<String> alarmedCars, Keyword keyword, Address address) {
        this.alarmedCars = alarmedCars;
        this.keyword = keyword;
        this.address = address;
    }

    public void setAlarmedCars(List<String> alarmedCars) {
        this.alarmedCars = alarmedCars;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getAlarmedCars() {
        return alarmedCars;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public Address getAddress() {
        return address;
    }
}
