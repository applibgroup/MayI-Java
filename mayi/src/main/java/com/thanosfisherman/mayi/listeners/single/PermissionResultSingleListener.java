package com.thanosfisherman.mayi.listeners.single;

import com.thanosfisherman.mayi.PermissionBean;

/**
 * Interface used to define permissionResult method for SinglePermission Listener.
 */
@FunctionalInterface
public interface PermissionResultSingleListener {
    void permissionResult(PermissionBean permission);
}