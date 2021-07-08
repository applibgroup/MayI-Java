package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import ohos.bundle.IBundleManager;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to process the permissions and classify them to granted and denied permissions.
 */
class PermissionMatcher {
    private List<String> mDeniedPermissions;
    private List<String> mGrantedPermissions;
    private boolean isAllGranted = true;

    PermissionMatcher(String[] permissions, WeakReference<AbilitySlice> activity) {
        if (permissions == null || permissions.length <= 0 || activity == null) {
            throw new IllegalArgumentException("You must have at least 1 permission specified or abilitySlice is null");
        }
        this.mDeniedPermissions = new LinkedList<>();
        this.mGrantedPermissions = new LinkedList<>();
        for (String perm : permissions) {
            if (activity.get().canRequestPermission(perm)
                    && activity.get().verifySelfPermission(perm) != IBundleManager.PERMISSION_GRANTED) {
                mDeniedPermissions.add(perm);
                isAllGranted = false;
            } else {
                mGrantedPermissions.add(perm);
            }
        }
    }

    boolean areAllPermissionsGranted() {
        return isAllGranted;
    }

    List<String> getDeniedPermissions() {
        return mDeniedPermissions;
    }

    List<String> getGrantedPermissions() {
        return mGrantedPermissions;
    }
}
