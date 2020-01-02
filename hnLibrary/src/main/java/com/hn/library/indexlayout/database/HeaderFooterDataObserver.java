package com.hn.library.indexlayout.database;

/**
 */
public class HeaderFooterDataObserver<T> {
    /**
     * This method is called when the entire data set has changed,
     * refresh UI
     */
    public void onChanged() {
        // Do nothing
    }

    public void onAdd(boolean header, T preData, T data) {
        // Do nothing
    }

    public void onRemove(boolean header, T object) {
        // Do nothing
    }
//
//    public void onAddAll(int itemType, int position, Collection<T> datas) {
//        // Do nothing
//    }
//
//    public void onRemoveAll(int itemType, Collection<T> datas) {
//        // Do nothing
//    }
}
