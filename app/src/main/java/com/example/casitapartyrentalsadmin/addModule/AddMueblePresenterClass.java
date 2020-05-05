package com.example.casitapartyrentalsadmin.addModule;

import com.example.casitapartyrentalsadmin.addModule.events.AddMuebleEvent;
import com.example.casitapartyrentalsadmin.addModule.model.AddMuebleInteractor;
import com.example.casitapartyrentalsadmin.addModule.model.AddMuebleInteractorClass;
import com.example.casitapartyrentalsadmin.addModule.view.AddMuebleView;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AddMueblePresenterClass implements AddMueblePresenter {
    private AddMuebleView mView;
    private AddMuebleInteractor mInteractor;


    public AddMueblePresenterClass(AddMuebleView mView) {
        this.mView = mView;
        this.mInteractor=new AddMuebleInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
    EventBus.getDefault().unregister(this);
    mView=null;
    }



    @Override
    public void addMueble(Mueble mueble) {
        if (setProgress()){
            mInteractor.addMueble(mueble);
        }
    }


    private boolean setProgress() {
        if (mView!=null){
            mView.disableUIElements();
            mView.showProgress();
            return true;
        }
        return false;
    }

    @Subscribe
    @Override
    public void onEventListener(AddMuebleEvent event) {
        if (mView!= null){
            mView.hideProgress();
            mView.enableUIElements();
            switch (event.getTypeEvent()){
                case AddMuebleEvent.SUCCESS_ADD:
                    mView.muebleAdded();
                    break;
                case AddMuebleEvent.ERROR_MAX_VALUE:
                        mView.maxValueError(event.getResMsg());
                    break;
                case AddMuebleEvent.ERROR_SERVER:
                        mView.showError(event.getResMsg());
                    break;
            }
        }
    }
}
