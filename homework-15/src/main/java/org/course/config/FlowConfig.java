package org.course.config;

import org.course.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.Random;

@Configuration
public class FlowConfig {

    public static final double MIN_QUALITY = 0.8;
    public static final double QUALITY_SPREAD = 0.2;
    public static final double QUALITY_BREAKDOWN = 0.85;

    @Bean
    public MessageChannel blueprintChannel(){
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel outChannel(){
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel trashChannel(){
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow flow(){
        return IntegrationFlows
                .from("blueprintChannel")
                .log("INFO", message -> "Accepting blueprint: " + message.getPayload())
                .<Blueprint>split(b -> List.of(b.getEngine(), b.getBody(), b.getWheels()), e -> e.applySequence(true))
                .channel("assembleChannel")
                .log("INFO", message -> "Moving to assembly line :" + message.getPayload())
                .<Object, Class<?>>route(Object::getClass,
                        m -> m.subFlowMapping(BodyType.class, sf -> sf.transform(Message.class, mb -> MessageBuilder
                                .withPayload(new Body((BodyType) mb.getPayload(), getQuality()))
                                .copyHeaders(mb.getHeaders()).setHeader("part_type", Body.class).build()))
                        .subFlowMapping(EngineType.class, sf -> sf.transform(e -> new Engine((EngineType) e, getQuality()))
                                .enrichHeaders(h -> h.header("part_type", Engine.class)))
                        .subFlowMapping(WheelType.class, sf -> sf.transform(w -> new Wheels((WheelType) w, getQuality()))
                                .enrichHeaders(h -> h.header("part_type", Wheels.class))))
                .log("INFO", m -> m.getPayload() + " is created!")
                .channel("verifyChannel")
                .<Part>filter(b -> b.getQuality() > QUALITY_BREAKDOWN, f -> f.discardChannel("fixChannel"))
                .aggregate(a -> a.outputProcessor(g -> {
                    Body body = (Body) g.getMessages().stream().filter(m -> m.getPayload() instanceof Body)
                            .map(Message::getPayload).findFirst().orElseThrow();
                    Engine engine = (Engine) g.getMessages().stream().filter(m -> m.getPayload() instanceof Engine)
                            .map(Message::getPayload).findFirst().orElseThrow();
                    Wheels wheels = (Wheels) g.getMessages().stream().filter(m -> m.getPayload() instanceof Wheels)
                            .map(Message::getPayload).findFirst().orElseThrow();
                    return new Car(engine, body, wheels);
                }))
                .log("INFO", message -> "Car: " + message.getPayload() + " successfully created!")
                .channel("outChannel")
                .get();
    }

    @Bean
    public IntegrationFlow fixPartFlow(){

        return IntegrationFlows.from("fixChannel")
                .log("INFO", message -> "Part: " + message.getPayload() + " did not pass quality control! Trying again.")
                .route("headers.part_type",
                        r -> r.subFlowMapping(Body.class, sf -> sf.transform(b -> ((Body)b).getType()))
                                .subFlowMapping(Engine.class, sf -> sf.transform(e -> ((Engine)e).getType()))
                                .subFlowMapping(Wheels.class, sf -> sf.transform(w -> ((Wheels) w).getType())))
                .channel("assembleChannel")
                .get();
    }

    private double getQuality(){
        return MIN_QUALITY + new Random().nextDouble() * QUALITY_SPREAD;
    }

}
