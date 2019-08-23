package de.ff.jf.bftag.alarmmonitor;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextToSpeech {

    private List<String> voiceNames = new ArrayList<>();
    private Random r = new Random();
    public static TextToSpeech textToSpeech = new TextToSpeech();

    public TextToSpeech() {
        voiceNames.add("dfki-pavoque-neutral-hsmm");
        voiceNames.add("bits3-hsmm");
        voiceNames.add("bits1-hsmm");
    }

    public static void main(String[] args) throws MaryConfigurationException {
        MaryInterface maryInterface = new LocalMaryInterface();
        Voice.getAvailableVoices().forEach(System.out::println);
    }

    public void play(String alarm, List<String> cars, Gong g) {

        MaryInterface marytts = null;
        try {
            marytts = new LocalMaryInterface();
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
        cars.forEach( c -> {
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
}
