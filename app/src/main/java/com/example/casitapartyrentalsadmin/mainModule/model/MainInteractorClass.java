package com.example.casitapartyrentalsadmin.mainModule.model;

import com.example.casitapartyrentalsadmin.common.BasicErrorEventCallback;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.mainModule.events.MainEvent;
import com.example.casitapartyrentalsadmin.mainModule.model.dataAcces.MueblesEventListener;
import com.example.casitapartyrentalsadmin.mainModule.model.dataAcces.RealtimeDatabase;

import org.greenrobot.eventbus.EventBus;

public class MainInteractorClass implements MainInteractor {
    private RealtimeDatabase mDatabase;

    public MainInteractorClass() {
        mDatabase= new RealtimeDatabase();
    }

    @Override
    public void subscribeToMuebes() {
        mDatabase.subscribeToMuebles(new MueblesEventListener() {
            @Override
            public void onChildAdded(Mueble mueble) {
                post(mueble, MainEvent.SUCCESS_ADD);
            }

            @Override
            public void onChildUpdated(Mueble mueble) {
                post(mueble,MainEvent.SUCCESS_UPDATE);
            }

            @Override
            public void onChildRemoved(Mueble mueble) {
                post(mueble,MainEvent.SUCCESS_REMOVE);
            }

            @Override
            public void onError(int resMsg) {
                post(MainEvent.ERROR_SERVER,resMsg);
            }
        });
    }

    @Override
    public void unsubcribeToMuebles() {
        mDatabase.unsubscribeToMuebles();
    }

    @Override
    public void removeMueble(Mueble mueble) {
        mDatabase.removeMueble(mueble, new BasicErrorEventCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.SUCCESS_REMOVE);
            }

            @Override
            public void onError(int typeEvent, int resMsg) {
                post(typeEvent,resMsg);
            }
        });
    }
    private void post(int typeEvent){
        post(null,typeEvent,0);
    }

    private void post(int typeEvent,int resMsg){
        post(null,typeEvent,resMsg);
    }
    private void post(Mueble mueble, int typeEvent, int resMsg) {
        MainEvent event= new MainEvent();
        event.setMueble(mueble);
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }
    private void post(Mueble mueble, int typeEvent) {
       post(mueble,typeEvent,0);
    }
}
