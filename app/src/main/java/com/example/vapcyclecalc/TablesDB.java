package com.example.vapcyclecalc;

import java.util.ArrayList;
import java.util.List;

public class TablesDB {

    private static final String SAT_TEMPERATURE_KEY = "SAT_TEMP_LIST";
    private static final String SAT_PRESSURE_KEY = "SAT_PRESS_LIST";
    public static final String SUPERHEATED_KEY = "SUPER_HEATED_LIST";

    public static List<SatWaterTemperature> satWaterTemperatureList = new ArrayList<>();
    public static List<SatWaterPressure> satWaterPressureList = new ArrayList<>();
    public static List<SuperheatedWaterVapor> superheatedWaterVaporList = new ArrayList<>();

    public static void setSatWaterTemperatureList() {
        populateSatLists(Utils.satTempStr, SAT_TEMPERATURE_KEY);
    }
    public static void setSatWaterPressureList() {
        populateSatLists(Utils.satPressStr, SAT_PRESSURE_KEY);
    }
    public static void setSuperheatedWaterVaporList() {
        populateSuperHeatedList();
    }

    private static void populateSuperHeatedList() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (String str : Utils.superHeatedSTr.replace("\n", " ").split(" ")) {
            if (counter != 4) {
                sb.append(str).append("f, ");
            }
            counter++;
            if (counter == 8) {
                String[] values = sb.substring(0, sb.length()-2).split(",");
                List<Float> valuesToFloat = new ArrayList<>();

                for (String value : values) {
                    valuesToFloat.add(Float.valueOf(value));
                }
                // Creating an object from the list
                superheatedWaterVaporList.add((SuperheatedWaterVapor) makeObjectFromList(valuesToFloat, SUPERHEATED_KEY));
                // Resetting the sb in order to grab another line
                sb.setLength(0);
                counter = 1;
            }
        }
    }

    private static void populateSatLists(String s, String key) {
        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (String str : s.replace("\n", " ").split(" ")) {
            if (counter != 5 && counter != 6 && counter != 12) {
                sb.append(str).append("f, ");
            }
            counter++;
            if (counter == 13) {
                String[] values = sb.substring(0, sb.length()-2).split(",");
                List<Float> valuesToFloat = new ArrayList<>();

                for (String value : values) {
                    valuesToFloat.add(Float.valueOf(value));
                }
                // Creating an object from the list
                switch (key) {
                    case SAT_TEMPERATURE_KEY:
                        satWaterTemperatureList.add((SatWaterTemperature) makeObjectFromList(valuesToFloat, key));
                        break;
                    case SAT_PRESSURE_KEY:
                        satWaterPressureList.add((SatWaterPressure) makeObjectFromList(valuesToFloat, key));
                        break;
                }
                // Resetting the sb in order to grab another line
                sb.setLength(0);
                counter = 1;
            }
        }
    }

    private static Object makeObjectFromList(List<Float> valuesToFloat, String key) {
        switch (key) {
            case SAT_TEMPERATURE_KEY:
                return new SatWaterTemperature(valuesToFloat.get(0),
                        valuesToFloat.get(1), valuesToFloat.get(2), valuesToFloat.get(3),
                        valuesToFloat.get(4), valuesToFloat.get(5), valuesToFloat.get(6),
                        valuesToFloat.get(7), valuesToFloat.get(8));
            case SAT_PRESSURE_KEY:
                return new SatWaterPressure(valuesToFloat.get(0),
                        valuesToFloat.get(1), valuesToFloat.get(2), valuesToFloat.get(3),
                        valuesToFloat.get(4), valuesToFloat.get(5), valuesToFloat.get(6),
                        valuesToFloat.get(7), valuesToFloat.get(8));
            case SUPERHEATED_KEY:
                return new SuperheatedWaterVapor(valuesToFloat.get(0), valuesToFloat.get(1),
                        valuesToFloat.get(2), valuesToFloat.get(3), valuesToFloat.get(4),
                        valuesToFloat.get(5));
            default: return null;
        }
    }
}
