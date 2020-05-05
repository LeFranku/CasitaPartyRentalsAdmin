package com.example.casitapartyrentalsadmin.detailModule;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.detailModule.events.DetailMuebleEvent;

public interface DetailMueblePresenter {
    //Ciclo de vida de android.
    void onCreate();
    void onDestroy();
    //
    void updateMueble(Mueble mueble);

    void onEventListener(DetailMuebleEvent event);
}
