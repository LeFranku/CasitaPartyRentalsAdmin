package com.example.casitapartyrentalsadmin.detailModule;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.detailModule.events.DetailMuebleEvent;
import com.example.casitapartyrentalsadmin.detailModule.model.DetailMuebleInteractor;
import com.example.casitapartyrentalsadmin.detailModule.model.DetailMuebleInteractorClass;
import com.example.casitapartyrentalsadmin.detailModule.view.DetailMuebleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DetailMueblePresenterClass implements DetailMueblePresenter {
    private DetailMuebleInteractor mInteractor;
    private DetailMuebleView mView;

    public DetailMueblePresenterClass(DetailMuebleView mView){
        this.mView=mView;
        this.mInteractor=new DetailMuebleInteractorClass();
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
    public void updateMueble(Mueble mueble) {
        if (setProgress()){
            mInteractor.updateMueble(mueble);
        }
    }

    private boolean setProgress() {
        if (mView!=null){
            mView.showProgress();
            mView.disableUIElements();
            return true;
        }
        return false;
    }
    @Subscribe
    @Override
    public void onEventListener(DetailMuebleEvent event) {
        if (mView!=null){
            mView.hideProgress();
            mView.enableUIElements();
            switch (event.getTypeEvent()){
                case DetailMuebleEvent.UPDATE_SUCCESS:
                mView.updateSuccess();
                    break;
                case DetailMuebleEvent.ERROR_SERVER:
                    mView.updateError();
            }
        }
    }
}
