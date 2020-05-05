package com.example.casitapartyrentalsadmin.mainModule;

import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.mainModule.events.MainEvent;
import com.example.casitapartyrentalsadmin.mainModule.model.MainInteractor;
import com.example.casitapartyrentalsadmin.mainModule.model.MainInteractorClass;
import com.example.casitapartyrentalsadmin.mainModule.view.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainPresenterClass implements MainPresenter {
    private MainView mView;
    private MainInteractor mInteractor;

    public MainPresenterClass(MainView mView) {
        this.mView = mView;
        this.mInteractor=new MainInteractorClass();
    }

    @Override
    public void onCreate() {
        setProgress();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mInteractor.unsubcribeToMuebles();
    }

    @Override
    public void onResume() {
        mInteractor.subscribeToMuebes();
    }

    @Override
    public void onDestroy() {
    EventBus.getDefault().unregister(this);
    mView=null;
    }

    @Override
    public void remove(Mueble mueble) {
        if (setProgress()){
            mInteractor.removeMueble(mueble);
        }
    }

    private boolean setProgress() {
        if (mView!=null){
            mView.showProgress();
            return true;
        }
        return false;
    }

    @Subscribe
    @Override
    public void onEventListener(MainEvent event) {
        if(mView!=null){
            mView.hideProgress();

            switch (event.getTypeEvent()){
                case MainEvent.SUCCESS_ADD:
                    mView.add(event.getMueble());
                    break;
                case MainEvent.SUCCESS_UPDATE:
                    mView.update(event.getMueble());
                    break;
                case MainEvent.SUCCESS_REMOVE:
                    mView.remove(event.getMueble());
                    break;
                case MainEvent.ERROR_SERVER:
                    mView.OnShowError(event.getResMsg());
                    break;
                case MainEvent.ERROR_TO_REMOVE:
                    mView.removeFail();
                    break;
            }
        }
    }
}
