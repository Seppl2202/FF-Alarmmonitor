package de.ff.jf.bftag.alarmmonitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ff.jf.bftag.alarmmonitor.models.Car;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class CarRepository {

    private static CarRepository instance;

    private List<Car> cars;

    private CarRepository() {
        cars= new LinkedList<>();
        parseCars();
    }

    private void parseCars() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
        cars = objectMapper.readValue(readCarConfigurationFile(), new TypeReference<List<Car>>(){});
        } catch (Exception e)  {
            Logger.getLogger(CarRepository.class.getName()).warning("Parsing cars failed: ");
            e.printStackTrace();
        }

    }

    private String readCarConfigurationFile() throws IOException {
        return Files.readString(Paths.get(RessourceFolderURL.configurtationFolderBaseURL + "/cars.json"), StandardCharsets.UTF_8);
    }

    public List<Car> getCars() {
        return cars;
    }

    public Car getById(int id) throws  CarNotFoundException {
        return cars.stream().filter(c -> c.getId() == id).findAny().orElseThrow(CarNotFoundException::new);
    }

    public Car getByName(String name) throws CarNotFoundException {
        return cars.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findAny().orElseThrow(CarNotFoundException::new);
    }

    public static synchronized CarRepository getInstance() {
        if (instance == null) {
            instance = new CarRepository();
        }
        return instance;
    }
}
