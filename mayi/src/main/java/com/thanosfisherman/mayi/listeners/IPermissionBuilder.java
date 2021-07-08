package com.thanosfisherman.mayi.listeners;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

/**
 * Interface to define all the methods related to a Permission.
 */
public interface IPermissionBuilder {
    IPermissionBuilder onErrorListener(MayiErrorListener errorListener);

    void check();

    /**
     * Interface to define the withPermission methods for MayI class.
     */
    interface Permission {

        SinglePermissionBuilder withPermission(String permission);

        IPermissionBuilder.MultiPermissionBuilder withPermissions(String... permissions);
    }

    /**
     * Interface to define onResult and onRationale methods for SinglePermission.
     */
    interface SinglePermissionBuilder extends IPermissionBuilder {

        SinglePermissionBuilder onResult(PermissionResultSingleListener response);

        SinglePermissionBuilder onRationale(RationaleSingleListener rationale);
    }

    /**
     * Interface to define onResult and onRationale methods for MultiPermissions.
     */
    interface MultiPermissionBuilder extends IPermissionBuilder {

        MultiPermissionBuilder onResult(PermissionResultMultiListener response);

        MultiPermissionBuilder onRationale(RationaleMultiListener rationale);
    }
}

