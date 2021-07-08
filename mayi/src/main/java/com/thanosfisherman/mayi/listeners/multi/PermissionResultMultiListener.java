package com.thanosfisherman.mayi.listeners.multi;

import com.thanosfisherman.mayi.PermissionBean;

/**
 * Interface used to define permissionResults method for MultiPermission Listener.
 */
@FunctionalInterface
public interface PermissionResultMultiListener {
    void permissionResults(PermissionBean[] permissions);
}

