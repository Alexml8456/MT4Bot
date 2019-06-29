package com.alex.interfaces;

public interface SessionStorage<T> {

    void storeSession(T session);

    T getSession();
}
