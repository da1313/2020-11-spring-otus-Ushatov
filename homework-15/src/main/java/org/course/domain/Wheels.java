package org.course.domain;

public class Wheels extends Part{

    private WheelType type;

    public Wheels(WheelType type, double quality) {
        super(quality);
        this.type = type;
    }

    public WheelType getType() {
        return type;
    }

    public void setType(WheelType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Wheel{" +
                "quality=" + quality +
                ", type=" + type +
                "} ";
    }
}
