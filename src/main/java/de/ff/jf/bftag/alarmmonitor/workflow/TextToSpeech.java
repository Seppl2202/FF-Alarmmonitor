package de.ff.jf.bftag.alarmmonitor.workflow;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TextToSpeech {

    public static TextToSpeech textToSpeech = new TextToSpeech();
    private List<String> voiceNames = new ArrayList<>();
    private Random r = new Random();

    public TextToSpeech() {
        voiceNames.add("dfki-pavoque-neutral-hsmm");
        voiceNames.add("bits3-hsmm");
        voiceNames.add("bits1-hsmm");
    }

    public void play(String alarm, List<String> cars, Gong g) {
        System.err.println("Playing");

        MaryInterface marytts = null;
        try {
            marytts = new LocalMaryInterface();
            marytts.setLocale(new Locale("de"));
        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        }
        marytts.setVoice(voiceNames.get(r.nextInt(3)));
        AudioInputStream audio = null;
        try {
            Thread.sleep(1000);
            audio = marytts.generateAudio(alarm);
        } catch (SynthesisException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            if (g.isPlaying() == false) {
                break;
            }
        }
        AudioPlayer player = new AudioPlayer(audio);
        player.start();
        try {
            player.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioInputStream audio2 = null;
        StringBuilder b = new StringBuilder();
        b.append("Einsatz für,");
        cars.forEach(c -> {
            b.append(c);
            b.append(",");
        });
        try {
            Thread.sleep(1000);
            audio2 = marytts.generateAudio(b.toString());
        } catch (SynthesisException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            if (g.isPlaying() == false) {
                break;
            }
        }
        AudioPlayer player2 = new AudioPlayer(audio2);
        player2.start();
        try {
            player2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void speak(String text) {
        MaryInterface marytts = null;
        try {
            marytts = new LocalMaryInterface();
            marytts.setLocale(new Locale("de"));
        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        }
        marytts.setVoice(voiceNames.get(r.nextInt(3)));
        AudioInputStream audio = null;
        try {
            Thread.sleep(1000);
            audio = marytts.generateAudio(text);
        } catch (SynthesisException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioPlayer player2 = new AudioPlayer(audio);
        player2.start();
    }
}
