package com.example.casitapartyrentalsadmin.mainModule.model.dataAcces;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

public interface MueblesEventListener {
    void onChildAdded(Mueble mueble);
    void onChildUpdated(Mueble mueble);
    void onChildRemoved(Mueble mueble);

    void onError(int resMsg);

}
