package com.example.casitapartyrentalsadmin.mainModule;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.mainModule.events.MainEvent;

public interface MainPresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void remove(Mueble mueble);

    void onEventListener(MainEvent event);
}
