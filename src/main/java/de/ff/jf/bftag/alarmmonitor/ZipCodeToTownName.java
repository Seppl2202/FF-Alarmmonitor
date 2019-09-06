package de.ff.jf.bftag.alarmmonitor;

import java.util.HashMap;
import java.util.Map;

public class ZipCodeToTownName {
    private static Map<Integer, String> townNames = new HashMap<>();

    static {
        townNames.put(76707, "Hambrücken");
    }


    public static String zipToName(int zip) {
        if (!(townNames.containsKey(zip))) {
            throw new IllegalArgumentException("No town name found for postal code " + zip);
        }
        return townNames.get(zip);
    }


}
