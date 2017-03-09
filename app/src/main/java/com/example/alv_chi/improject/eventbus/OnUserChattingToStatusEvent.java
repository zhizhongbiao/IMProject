package com.example.alv_chi.improject.eventbus;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Alv_chi on 2017/3/7.
 */
public class OnUserChattingToStatusEvent {

    private Presence presence;

    public OnUserChattingToStatusEvent(Presence presence) {
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }
}
