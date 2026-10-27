package com.example.verificationcodejavademo.model;

import java.io.Serializable;

/**
 * @author:ZJL
 */
public class Point implements Serializable {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
