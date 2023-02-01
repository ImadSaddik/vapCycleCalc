package com.example.vapcyclecalc;

public class SuperheatedWaterVapor {
    // in °C
    private float temperature;
    private float satTemperature;
    // in bar
    private float pressure;
    // in m^3/ kg
    private float specificVolume;
    // in kJ / kg
    private float enthalpy;
    // in kJ / (kg * k)
    private float entropy;

    public SuperheatedWaterVapor(float temperature, float satTemperature, float specificVolume,
                                 float enthalpy, float entropy, float pressure) {
        this.temperature = temperature;
        this.satTemperature = satTemperature;
        this.pressure = pressure;
        this.specificVolume = specificVolume;
        this.enthalpy = enthalpy;
        this.entropy = entropy;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getSatTemperature() {
        return satTemperature;
    }

    public float getPressure() {
        return pressure;
    }

    public float getSpecificVolume() {
        return specificVolume;
    }

    public float getEnthalpy() {
        return enthalpy;
    }

    public float getEntropy() {
        return entropy;
    }
}
