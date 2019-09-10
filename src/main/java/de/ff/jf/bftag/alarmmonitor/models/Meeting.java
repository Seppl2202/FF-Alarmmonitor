package de.ff.jf.bftag.alarmmonitor.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Meeting implements Comparable<Meeting> {
    private final String description;
    private final String location;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Meeting(String description, String location, LocalDateTime startDate, LocalDateTime endDate) {
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return description + " <br/>" + "von " + startDate.format(formatter) + " bis " + endDate.format(formatter) + "<br/>" + location;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public int compareTo(Meeting o) {
        return getStartDate().compareTo(o.getStartDate());
    }
}
