package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.marytts.Gong;
import de.ff.jf.bftag.alarmmonitor.marytts.TextToSpeech;
import de.ff.jf.bftag.alarmmonitor.marytts.ZipCodeToTownName;

public class PlaySounds implements WorkflowStep {


    @Override
    public void executeStep() {
        Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
        Gong g = new Gong();
        g.play();

        if (houseNumberContainsAdditionalLetter(alarm.getAddress().getNumber())) {
            System.err.println("add");
            TextToSpeech.textToSpeech.play(alarm.getKeyword().getKeyword() + ", " + alarm.getAddress().getStreet() + " " + splitAdditionalLetterHouseNumberForTTS(alarm.getAddress().getNumber()) + "; " + ZipCodeToTownName.zipToName(alarm.getAddress().getZipCode()), alarm.getAlarmedCars(), g);
        } else {
            TextToSpeech.textToSpeech.play(alarm.getKeyword().getKeyword() + ", " + alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber() + ", " + ZipCodeToTownName.zipToName(alarm.getAddress().getZipCode()), alarm.getAlarmedCars(), g);
        }
    }

    private boolean houseNumberContainsAdditionalLetter(String houseNumber) {
        char[] chars = houseNumber.toCharArray();
        if (chars.length == 0) {
            return false;
        }
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                return true;
            }
        }
        return false;
    }

    private String splitAdditionalLetterHouseNumberForTTS(String houseNumber) {
        char[] chars = houseNumber.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                String number = houseNumber.substring(0, houseNumber.indexOf(Character.toString(chars[i])));
                String add = houseNumber.substring(houseNumber.indexOf(Character.toString(chars[i])), houseNumber.length());
                StringBuilder b = new StringBuilder();
                b.append(number);
                b.append("/");
                b.append(add.toUpperCase());
                b.append(" ");
                return b.toString();
            }
        }
        return houseNumber;
    }
}
