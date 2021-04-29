package org.course.gateways;

import org.course.domain.Blueprint;
import org.course.domain.Car;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface Factory {

    @Gateway(requestChannel = "blueprintChannel", replyChannel = "outChannel", replyTimeout = 0)
    Car make(Blueprint blueprint);

}
