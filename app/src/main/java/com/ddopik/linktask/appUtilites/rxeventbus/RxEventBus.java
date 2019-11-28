package com.ddopik.linktask.appUtilites.rxeventbus;

import com.ddopik.attendonb.utilites.rxeventbus.RxAppStatsEvent;
import com.ddopik.attendonb.utilites.rxeventbus.RxForceRefreshEvent;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {

    private static RxEventBus instance;

    private PublishSubject<RxAppStatsEvent> connectionSubject = PublishSubject.create();
    private PublishSubject<RxForceRefreshEvent> refreshSubject = PublishSubject.create();

    private RxEventBus() {
    }

    public static RxEventBus getInstance() {
        if (instance == null)
            instance = new RxEventBus();
        return instance;
    }

    public void post(RxAppStatsEvent event) {
        connectionSubject.onNext(event);
    }
    public void post(RxForceRefreshEvent event) {
        refreshSubject.onNext(event);
    }

    public PublishSubject<RxAppStatsEvent> getConnectionStatsSubject() {
        return connectionSubject;
    }

    public PublishSubject<RxForceRefreshEvent> getRefreshStatsSubject() {
        return refreshSubject;
    }
}
