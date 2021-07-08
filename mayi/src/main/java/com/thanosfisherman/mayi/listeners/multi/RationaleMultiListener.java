package com.thanosfisherman.mayi.listeners.multi;

import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

/**
 * Interface used to define onRationale method for MultiPermission Listener.
 */
@FunctionalInterface
public interface RationaleMultiListener {
    void onRationale(PermissionBean[] permissions, PermissionToken token);
}
