package de.ff.jf.bftag.alarmmonitor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApplicationConfigurationRepository {
    private static ApplicationConfigurationRepository instance;
    private static ApplicationConfiguration configuration;

    private ApplicationConfigurationRepository() {
        parseConfiguration();
    }


    private void parseConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            configuration = objectMapper.readValue(Files.readString(Paths.get(RessourceFolderURL.configurtationFolderBaseURL + "/applicationConfiguration.json")), ApplicationConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ApplicationConfiguration getInstance() {
        if (instance == null) {
            instance = new ApplicationConfigurationRepository();
        }
        return configuration;
    }

}
