package org.course.domain;

public class Body extends Part{

    private BodyType type;

    public Body(BodyType type, double quality) {
        super(quality);
        this.type = type;
    }

    public BodyType getType() {
        return type;
    }

    public void setType(BodyType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Body{" +
                "type=" + type +
                ", quality=" + quality +
                "} ";
    }
}
