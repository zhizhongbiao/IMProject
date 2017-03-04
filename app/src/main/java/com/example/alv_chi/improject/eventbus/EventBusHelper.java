package com.example.alv_chi.improject.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Alv_chi on 2017/3/4.
 */

public class EventBusHelper {

    private static EventBusHelper eventBusHelper;

    public static EventBusHelper getEventBusHelperInstance() {
        if (eventBusHelper == null) {
            eventBusHelper = new EventBusHelper();
        }

        return eventBusHelper;
    }

    public EventBus getEventBusInstance()
    {
        return EventBus.getDefault();
    }
}
