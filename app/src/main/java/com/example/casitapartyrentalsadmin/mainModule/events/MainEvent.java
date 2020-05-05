package com.example.casitapartyrentalsadmin.mainModule.events;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

public class MainEvent {
    public static final int SUCCESS_ADD=0;
    public static final int SUCCESS_UPDATE=1;
    public static final int SUCCESS_REMOVE=2;
    public static final int ERROR_SERVER=100;
    public static final int ERROR_TO_REMOVE=101;


    private Mueble mueble;
    private int typeEvent;
    private int resMsg;

    public MainEvent() {
    }

    public Mueble getMueble() {
        return mueble;
    }

    public void setMueble(Mueble mueble) {
        this.mueble = mueble;
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getResMsg() {
        return resMsg;
    }

    public void setResMsg(int resMsg) {
        this.resMsg = resMsg;
    }

}
