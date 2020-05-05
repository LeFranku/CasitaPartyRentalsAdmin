package com.example.casitapartyrentalsadmin.mainModule.view;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;

public interface MainView {
    void showProgress();
    void hideProgress();

    void add(Mueble mueble);
    void update(Mueble mueble);
    void remove (Mueble mueble);

    void removeFail();
    void OnShowError(int resMsg);
}
