package de.ff.jf.bftag.alarmmonitor.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    private String name;
    private int fms;
    private int id;

    public Car(String name, int fms, int id) {
        this.name = name;
        this.fms = fms;
        this.id = id;
    }

    public Car() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return " [ " + this.name + ", current FMS: " + this.fms + ", ID: " + this.id + " ]";
    }

    /**
     * FMS state is NOT part of hash code, since it will change during different operations
     * @return
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 21).append(this.name).append(this.id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Car)) return false;
        if(obj == this) return true;
        Car otherCar = (Car) obj;
        return otherCar.getName().equals(this.name) && otherCar.getId() == this.id;
    }
}
