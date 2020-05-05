package com.example.casitapartyrentalsadmin.addModule.view;

public interface AddMuebleView {
    void enableUIElements();
    void disableUIElements();
    void showProgress();
    void hideProgress();

    void muebleAdded();
    void showError(int resMsg);
    void maxValueError(int resMsg);

}
