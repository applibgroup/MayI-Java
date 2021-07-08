package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

/**
 * Slice to display the system dialog prompt.
 */
public class MayiSlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_permission_slice);
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.setmAbility(this);
        permissionManager.requestPermissions();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
