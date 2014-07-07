package com.mb.holochartstest;

import android.graphics.Path;
import android.graphics.Region;

public class Bar {

    private int color;
    private String name;
    private float value;
    private float currentValue;
    private Path path;
    private Region region;

    public Bar() {
        currentValue = 0.0f;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public Path getPath() {
        return path;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public Region getRegion() {
        return region;
    }
    public void setRegion(Region region) {
        this.region = region;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }
}