package com.example.alv_chi.improject.eventbus;


import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Alv_chi on 2017/3/7.
 */
public class OnUserStatusChangeEvent {

    private Presence presence;

    public OnUserStatusChangeEvent(Presence presence) {
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }
}
