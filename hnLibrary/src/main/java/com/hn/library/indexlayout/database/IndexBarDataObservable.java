package com.hn.library.indexlayout.database;

import android.database.Observable;

/**
 */
public class IndexBarDataObservable extends Observable<IndexBarDataObserver> {

    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

}
