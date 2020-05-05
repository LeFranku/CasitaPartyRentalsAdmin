package com.example.casitapartyrentalsadmin.addModule.model;

import com.example.casitapartyrentalsadmin.addModule.events.AddMuebleEvent;
import com.example.casitapartyrentalsadmin.addModule.model.dataAccess.RealTimeDatabase;
import com.example.casitapartyrentalsadmin.common.BasicErrorEventCallback;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

import org.greenrobot.eventbus.EventBus;


public class AddMuebleInteractorClass implements AddMuebleInteractor{

    private RealTimeDatabase mDatabase;

    public AddMuebleInteractorClass() {
        mDatabase= new RealTimeDatabase();
    }

    @Override
    public void addMueble(Mueble mueble) {
        mDatabase.addMueble(mueble, new BasicErrorEventCallback() {
            @Override
            public void onSuccess() {
                post(AddMuebleEvent.SUCCESS_ADD);
            }

            @Override
            public void onError(int typeEvent, int resMsg) {
                post(typeEvent,resMsg);
            }
        });
    }

    private void post(int typeEvent) {
        post(typeEvent,0);
    }

    private void post(int typeEvent, int resMsg) {
        AddMuebleEvent event = new AddMuebleEvent();
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }
}