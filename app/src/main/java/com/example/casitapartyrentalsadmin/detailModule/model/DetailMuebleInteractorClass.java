package com.example.casitapartyrentalsadmin.detailModule.model;

import com.example.casitapartyrentalsadmin.common.BasicEventCallBack;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.detailModule.events.DetailMuebleEvent;
import com.example.casitapartyrentalsadmin.detailModule.model.dataAccess.RealtimeDatabase;

import org.greenrobot.eventbus.EventBus;

public class DetailMuebleInteractorClass implements DetailMuebleInteractor {
    private RealtimeDatabase mDatabase;

    public DetailMuebleInteractorClass(){
        mDatabase=new RealtimeDatabase();
    }


    @Override
    public void updateMueble(Mueble mueble) {
        mDatabase.updateMueble(mueble, new BasicEventCallBack() {
            @Override
            public void onSuccess() {
                post(DetailMuebleEvent.UPDATE_SUCCESS);
            }

            @Override
            public void onError() {
                post(DetailMuebleEvent.ERROR_SERVER);
            }
        });
    }

    private void post(int typeEvent) {
        DetailMuebleEvent event=new DetailMuebleEvent();
        event.setTypeEvent(typeEvent);
        EventBus.getDefault().post(event);
    }
}
