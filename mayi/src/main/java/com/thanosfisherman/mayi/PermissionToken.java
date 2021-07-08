package com.thanosfisherman.mayi;

/**
 * Interface to define the continue or stop the permission request methods.
 */
public interface PermissionToken {

    /**
     * Continues with the permission request process.
     */
    void continuePermissionRequest();

    /**
     * Cancels the permission request process.
     */
    void skipPermissionRequest();
}
