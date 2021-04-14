package org.course.gateways;

import org.assertj.core.api.Assertions;
import org.course.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FactoryTest {

    @Autowired
    private Factory factory;

    @Test
    void shouldCreateCarObjectFromBlueprint() {

        Blueprint blueprint = new Blueprint(EngineType.V1, BodyType.SEDAN, WheelType.S16);

        Car car = factory.make(blueprint);

        Assertions.assertThat(car).extracting(Car::getBody).extracting(Body::getType).isEqualTo(blueprint.getBody());
        Assertions.assertThat(car).extracting(Car::getEngine).extracting(Engine::getType).isEqualTo(blueprint.getEngine());
        Assertions.assertThat(car).extracting(Car::getWheels).extracting(Wheels::getType).isEqualTo(blueprint.getWheels());

    }
}