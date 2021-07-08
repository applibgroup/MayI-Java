package com.thanosfisherman.mayi.listeners;

/**
 * Interface to define omError method for onErrorListener method.
 */
@FunctionalInterface
public interface MayiErrorListener {
    void onError(Exception e);
}
