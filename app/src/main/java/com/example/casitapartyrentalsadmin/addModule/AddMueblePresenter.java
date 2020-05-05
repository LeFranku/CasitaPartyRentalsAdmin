package com.example.casitapartyrentalsadmin.addModule;

import com.example.casitapartyrentalsadmin.addModule.events.AddMuebleEvent;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

public interface AddMueblePresenter {
    void onCreate();
    void onDestroy();
    void addMueble(Mueble mueble);

    void onEventListener(AddMuebleEvent event);
}
