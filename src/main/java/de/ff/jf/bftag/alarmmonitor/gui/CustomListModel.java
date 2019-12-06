package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Meeting;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomListModel extends DefaultListModel<Meeting> {

    private List<Meeting> elements = new ArrayList<>();

    public CustomListModel() {
        registerTimerTask();
    }

    @Override
    public int getSize() {
        return elements.size() < 3 ? elements.size() : 3;
    }

    public void addElement(Meeting element) {
        super.addElement(element);
        elements.add(element);
    }

    @Override
    public Meeting getElementAt(int index) {
        return elements.get(index);
    }

    private void registerTimerTask() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> sort(), 0, 1, TimeUnit.SECONDS);
    }

    private void sort() {
        int beforeSize = elements.size();
        elements.removeIf((e) -> e.getEndDate().isBefore(LocalDateTime.now()));
        if (elements.size() == beforeSize) {
            return;
        }
        sortList();
    }

    private void sortList() {
        Collections.sort(elements);
        super.removeAllElements();
        elements.forEach(e -> super.addElement(e));
        fireContentsChanged(this, 0, 1);
    }
}


