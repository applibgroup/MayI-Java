package com.thanosfisherman.mayi.listeners.single;

import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

/**
 * Interface used to define onRationale method for SinglePermission Listener.
 */
@FunctionalInterface
public interface RationaleSingleListener {
    void onRationale(PermissionBean permission, PermissionToken token);
}