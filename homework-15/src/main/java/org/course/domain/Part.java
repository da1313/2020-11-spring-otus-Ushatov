package org.course.domain;

public abstract class Part {

    protected double quality;

    public Part(double quality) {
        this.quality = quality;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

}
