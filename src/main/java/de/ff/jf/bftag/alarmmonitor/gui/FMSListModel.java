package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Car;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FMSListModel extends DefaultListModel<Car> {
    private List<Car> elements = new ArrayList<>();

    public FMSListModel() {

    }

    @Override
    public int getSize() {
        return elements.size();
    }

    public void addElement(Car element) {
        super.addElement(element);
        elements.add(element);
    }

    public int updateFMSState(String car, int state) {
        elements.stream().filter(e -> e.getName().equalsIgnoreCase(car)).collect(Collectors.toList()).get(0).setFms(state);
        fireContentsChanged(this, 0, 1);
        return state;
    }

    @Override
    public Car getElementAt(int index) {
        return elements.get(index);
    }
}
