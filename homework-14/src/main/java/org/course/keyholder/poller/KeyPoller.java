package org.course.keyholder.poller;

import org.course.keyholder.EntityName;

public interface KeyPoller {

    long getNextKey(EntityName entityName);

}
