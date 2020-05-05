package com.example.casitapartyrentalsadmin.detailModule.view;

public interface DetailMuebleView {
    void showProgress();
    void hideProgress();
    void enableUIElements();
    void disableUIElements();

    void updateSuccess();
    void updateError();
}
