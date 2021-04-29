package org.course.domain;

public class Engine extends Part{

    private EngineType type;

    public Engine(EngineType type, double quality) {
        super(quality);
        this.type = type;
    }

    public EngineType getType() {
        return type;
    }

    public void setType(EngineType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "type=" + type +
                ", quality=" + quality +
                "} ";
    }
}
