package com.example.vapcyclecalc;

public class SatWater {
    // in °C
    private float temperature;
    // in bar
    private float pressure;
    // in m^3/ kg
    private float satLiquidSpecificVolume;
    private float satVaporSpecificVolume;
    // in kJ / kg
    private float satLiquidEnthalpy;
    private float evapEnthalpy;
    private float satVaporEnthalpy;
    // in kJ / (kg * k)
    private float satLiquidEntropy;
    private float satVaporEntropy;

    public SatWater() {
    }

    public SatWater(float temperature, float pressure, float satLiquidSpecificVolume,
                    float satVaporSpecificVolume, float satLiquidEnthalpy,
                    float evapEnthalpy, float satVaporEnthalpy, float satLiquidEntropy,
                    float satVaporEntropy) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.satLiquidSpecificVolume = satLiquidSpecificVolume;
        this.satVaporSpecificVolume = satVaporSpecificVolume;
        this.satLiquidEnthalpy = satLiquidEnthalpy;
        this.evapEnthalpy = evapEnthalpy;
        this.satVaporEnthalpy = satVaporEnthalpy;
        this.satLiquidEntropy = satLiquidEntropy;
        this.satVaporEntropy = satVaporEntropy;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public float getSatLiquidSpecificVolume() {
        return satLiquidSpecificVolume;
    }

    public float getSatVaporSpecificVolume() {
        return satVaporSpecificVolume;
    }

    public float getSatLiquidEnthalpy() {
        return satLiquidEnthalpy;
    }

    public float getEvapEnthalpy() {
        return evapEnthalpy;
    }

    public float getSatVaporEnthalpy() {
        return satVaporEnthalpy;
    }

    public float getSatLiquidEntropy() {
        return satLiquidEntropy;
    }

    public float getSatVaporEntropy() {
        return satVaporEntropy;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setSatLiquidSpecificVolume(float satLiquidSpecificVolume) {
        this.satLiquidSpecificVolume = satLiquidSpecificVolume;
    }

    public void setSatVaporSpecificVolume(float satVaporSpecificVolume) {
        this.satVaporSpecificVolume = satVaporSpecificVolume;
    }

    public void setSatLiquidEnthalpy(float satLiquidEnthalpy) {
        this.satLiquidEnthalpy = satLiquidEnthalpy;
    }

    public void setEvapEnthalpy(float evapEnthalpy) {
        this.evapEnthalpy = evapEnthalpy;
    }

    public void setSatVaporEnthalpy(float satVaporEnthalpy) {
        this.satVaporEnthalpy = satVaporEnthalpy;
    }

    public void setSatLiquidEntropy(float satLiquidEntropy) {
        this.satLiquidEntropy = satLiquidEntropy;
    }

    public void setSatVaporEntropy(float satVaporEntropy) {
        this.satVaporEntropy = satVaporEntropy;
    }
}
