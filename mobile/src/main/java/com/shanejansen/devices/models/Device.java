package com.shanejansen.devices.models;

/**
 * Created by Shane Jansen on 3/6/16.
 */
public class Device {
    private int pin;
    private String name, type;
    private boolean isOn;

    public int getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }
}
