package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import ohos.bundle.IBundleManager;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import org.jetbrains.annotations.Nullable;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to handle the permission request, rationale and permission results.
 */
public class PermissionManager {
    private static PermissionManager singleInstance = null;
    public static final int PERMISSION_REQUEST_CODE = 1001;
    @Nullable
    private PermissionResultSingleListener mPermissionResultListener;
    @Nullable
    private PermissionResultMultiListener mPermissionsResultMultiListener;
    private List<String> mDeniedPermissions;
    private List<String> mGrantedPermissions;
    private boolean isShowingNativeDialog;
    private WeakReference<AbilitySlice> mAbility;

    /**
     * The function to return the instance of the singleton class.
     *
     *  @return returns the single instance of the class
     */
    public static PermissionManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new PermissionManager();
        }
        return singleInstance;
    }

    public void setmAbility(AbilitySlice activity) {
        this.mAbility = new WeakReference<>(activity);
    }

    /**
     * This method sets the beansResultList with the permissions.
     *
     * @param beansResultList contains the empty list to add the permission beans
     * @param permissions contains all the permissions whose result is requested
     * @param grantResults contains the result array received after the call back
     *                    method onRequestPermissionsFromUserResult
     */
    public void getBeanResults(List<PermissionBean> beansResultList, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            beansResultList.add(i, new PermissionBean(permissions[i]));
            if (grantResults[i] == IBundleManager.PERMISSION_DENIED) {
                beansResultList.get(i).setGranted(false);
                beansResultList.get(i).setPermanentlyDenied(!mAbility.get().canRequestPermission(permissions[i]));
            } else {
                beansResultList.get(i).setGranted(true);
                beansResultList.get(i).setPermanentlyDenied(false);
            }
        }
    }

    /**
     * This method sets the result permission beans to the listeners.
     *
     * @param requestCode contains the request code received from the
     *                    callback function onRequestPermissionsFromUserResult
     * @param permissions contains the permissions requested
     * @param grantResults contains the results of the permission requests
     *                    from the callback function onRequestPermissionsFromUserResult
     */
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            isShowingNativeDialog = false;
            if (grantResults.length == 0) {
                return;
            }
            final List<PermissionBean> beansResultList = new LinkedList<>();
            getBeanResults(beansResultList, permissions, grantResults);
            if (mPermissionResultListener != null) {
                mPermissionResultListener.permissionResult(beansResultList.get(0));
            } else if (mPermissionsResultMultiListener != null) {
                final List<PermissionBean> beansTotal = new LinkedList<>();
                for (String perm : mGrantedPermissions) {
                    final PermissionBean bean = new PermissionBean(perm);
                    if (mAbility.get().verifySelfPermission(perm) == IBundleManager.PERMISSION_GRANTED) {
                        bean.setGranted(true);
                        bean.setPermanentlyDenied(false);
                    } else {
                        bean.setGranted(false);
                        bean.setPermanentlyDenied(true);
                    }
                    beansTotal.add(bean);
                }
                beansTotal.addAll(beansResultList);
                mPermissionsResultMultiListener.permissionResults(beansTotal.toArray(new
                        PermissionBean[beansTotal.size()]));
            }
        }
    }

    /**
     * The method sets the permission variables.
     *
     * @param deniedPermissions contains the denied permissions, which are to requested again
     * @param grantedPermissions contains the already granted permissions
     */
    public void checkPermissions(List<String> deniedPermissions,
                                 List<String> grantedPermissions) {
        mDeniedPermissions = deniedPermissions;
        mGrantedPermissions = grantedPermissions;
    }

    void requestPermissions() {
        if (!isShowingNativeDialog) {
            mAbility.get().requestPermissionsFromUser(mDeniedPermissions.toArray(new
                    String[mDeniedPermissions.size()]), PERMISSION_REQUEST_CODE);
        }
        isShowingNativeDialog = true;
    }

    /**
     * Method to set the listeners.
     *
     * @param listenerResult Stores the listener for a single permission request,
     *                       null in case of multipermission request
     * @param listenerResultMulti Stores the listener for multi permission requests,
     *                       null in case of a single permission request
     */
    public void setListeners(PermissionResultSingleListener listenerResult, PermissionResultMultiListener
            listenerResultMulti) {
        mPermissionResultListener = listenerResult;
        mPermissionsResultMultiListener = listenerResultMulti;
    }

    /**
     * Closes the slice once the permission results are recorded.
     */
    public void closeSlice() {
        if (mAbility != null) {
            mAbility.get().terminate();
        }
    }
}
