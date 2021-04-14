package org.course.domain;

public class Blueprint {
    private EngineType engine;
    private BodyType body;
    private WheelType wheels;

    public Blueprint(EngineType engine, BodyType body, WheelType wheels) {
        this.engine = engine;
        this.body = body;
        this.wheels = wheels;
    }

    public EngineType getEngine() {
        return engine;
    }

    public void setEngine(EngineType engine) {
        this.engine = engine;
    }

    public BodyType getBody() {
        return body;
    }

    public void setBody(BodyType body) {
        this.body = body;
    }

    public WheelType getWheels() {
        return wheels;
    }

    public void setWheels(WheelType wheels) {
        this.wheels = wheels;
    }

    @Override
    public String toString() {
        return "Blueprint{" +
                "engine=" + engine +
                ", body=" + body +
                ", wheels=" + wheels +
                '}';
    }
}
