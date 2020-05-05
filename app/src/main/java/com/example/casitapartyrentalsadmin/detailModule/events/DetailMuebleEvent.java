package com.example.casitapartyrentalsadmin.detailModule.events;

public class DetailMuebleEvent {
    public static final int UPDATE_SUCCESS=0;
    public static final int ERROR_SERVER=100;

    private int typeEvent;

    public DetailMuebleEvent() {
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }
}
