package com.example.casitapartyrentalsadmin.mainModule.model;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

public interface MainInteractor {
    void subscribeToMuebes();
    void unsubcribeToMuebles();

    void removeMueble(Mueble mueble);
}
