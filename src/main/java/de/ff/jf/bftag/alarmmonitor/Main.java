package de.ff.jf.bftag.alarmmonitor;


import de.ff.jf.bftag.alarmmonitor.gui.ImageUtility;
import de.ff.jf.bftag.alarmmonitor.gui.Monitor;
import de.ff.jf.bftag.alarmmonitor.gui.UserHomeImageList;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class Main {
    public static Monitor m;
    public static ExecutorService executorService = Executors.newFixedThreadPool(20);
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);


    public static void main(String[] args) throws InterruptedException, IOException {

       SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.headless(false);
        //List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        //loggers.add(LogManager.getRootLogger());
        //for (Logger logger : loggers) {
        //    logger.setLevel(Level.WARN);
       // }
        //SpringApplication.run(Main.class, args);
        ConfigurableApplicationContext context = builder.run(args);
        new ImageUtility();
        new UserHomeImageList();
        List<String> cars = new ArrayList<>();
        cars.add("MTW");
        cars.add("LF16/12");
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        m = new Monitor();
//        Alarm alarm = new Alarm(cars, new Keyword("Containerbrand", "B1"), new Address(76707, "Wagbachstraße", 16, "Hambrücken"));
//        Thread.sleep(5000);
//        m.triggerAlarm(alarm);
//        Thread.sleep(10000);
//        m.stopFlasher();
//        List<String> cars2 = new ArrayList<>();
//        cars2.add("RW");
//        cars2.add("LF16/12");
//        Alarm alarm2 = new Alarm(cars2, new Keyword("Eingeklemmte Person", "VU2"), new Address(76707, "Hauptstraße", 2, "Hambrücken"));
//        Thread.sleep(5000);
//        m.triggerAlarm(alarm2);
//        Thread.sleep(10000);
//        m.stopFlasher();
//        List<String> cars3 = new ArrayList<>();
//        cars3.add("RW");
//        cars3.add("LF16/12");
//        cars3.add("LF16-TS");
//        cars3.add("MTW");
//        Alarm alarm3 = new Alarm(cars3, new Keyword("Dachstuhlbrand", "B3"), new Address(76707, "Beethovenweg", 4, "Hambrücken"));
//        Thread.sleep(5000);
//        m.triggerAlarm(alarm3);
//        Thread.sleep(10000);
//        m.stopFlasher();
    }

    public static Monitor getMonitor() {
        return m;
    }
}
