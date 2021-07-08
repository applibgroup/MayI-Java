package com.thanosfisherman.mayi;

import java.lang.ref.WeakReference;

final class PermissionRationaleToken implements PermissionToken {
    private final WeakReference<Mayi> mayiWeakReference;
    private boolean isTokenResolved = false;

    PermissionRationaleToken(Mayi mayiObject) {
        this.mayiWeakReference = new WeakReference<>(mayiObject);
    }

    @Override
    public void continuePermissionRequest() {
        if (!isTokenResolved) {
            mayiWeakReference.get().onContinuePermissionRequest();
            isTokenResolved = true;
        }
    }

    @Override
    public void skipPermissionRequest() {
        if (!isTokenResolved) {
            mayiWeakReference.get().onSkipPermissionRequest();
            isTokenResolved = true;
        }
    }
}
