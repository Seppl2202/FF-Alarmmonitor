package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Car;
import de.ff.jf.bftag.alarmmonitor.workflow.TextToSpeech;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FMSListModel extends DefaultListModel<Car> {
    private List<Car> elements = new ArrayList<>();
    private TextToSpeech textToSpeech;

    public FMSListModel() {
        textToSpeech = new TextToSpeech();
    }

    public FMSListModel(List<Car> cars) {
        this.elements = cars;
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    public void addElement(Car element) {
        super.addElement(element);
        elements.add(element);
    }

    public int updateFMSState(String car,int carId, int state) {
        elements.stream().filter(e -> e.getName().equalsIgnoreCase(car) && e.getId() == carId).collect(Collectors.toList()).get(0).setFms(state);
        if (state == 4) {
            textToSpeech.speak(car + " ist an der Einsatzstelle angekommen.");
        }
        if (state == 5) {
            textToSpeech.speak(car + " hat einen Sprechwunsch!");
        }
        fireContentsChanged(this, 0, 1);
        return state;
    }

    @Override
    public Car getElementAt(int index) {
        return elements.get(index);
    }
}
