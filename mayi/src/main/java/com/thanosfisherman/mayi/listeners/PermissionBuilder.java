package com.thanosfisherman.mayi.listeners;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

/**
 * Interface to define all the methods related to a Permission.
 */
public interface PermissionBuilder {
    PermissionBuilder onErrorListener(MayiErrorListener errorListener);

    void check();

    /**
     * Interface to define the withPermission methods for MayI class.
     */
    interface Permission {

        SinglePermissionBuilder withPermission(String permission);

        PermissionBuilder.MultiPermissionBuilder withPermissions(String... permissions);
    }

    /**
     * Interface to define onResult and onRationale methods for SinglePermission.
     */
    interface SinglePermissionBuilder extends PermissionBuilder {

        SinglePermissionBuilder onResult(PermissionResultSingleListener response);

        SinglePermissionBuilder onRationale(RationaleSingleListener rationale);
    }

    /**
     * Interface to define onResult and onRationale methods for MultiPermissions.
     */
    interface MultiPermissionBuilder extends PermissionBuilder {

        MultiPermissionBuilder onResult(PermissionResultMultiListener response);

        MultiPermissionBuilder onRationale(RationaleMultiListener rationale);
    }
}

