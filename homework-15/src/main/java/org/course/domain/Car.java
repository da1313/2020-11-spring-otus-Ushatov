package org.course.domain;

public class Car {
    private Engine engine;
    private Body body;
    private Wheels wheels;

    public Car(Engine engine, Body body, Wheels wheels) {
        this.engine = engine;
        this.body = body;
        this.wheels = wheels;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Wheels getWheels() {
        return wheels;
    }

    public void setWheels(Wheels wheels) {
        this.wheels = wheels;
    }
}
