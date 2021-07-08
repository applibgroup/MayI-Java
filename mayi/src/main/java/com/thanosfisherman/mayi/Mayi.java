package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import com.thanosfisherman.mayi.listeners.IPermissionBuilder;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;
import org.jetbrains.annotations.Nullable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to build the permission and initiating the request for permissions.
 */
public class Mayi implements IPermissionBuilder,
        IPermissionBuilder.Permission,
        IPermissionBuilder.SinglePermissionBuilder,
        IPermissionBuilder.MultiPermissionBuilder {
    private final WeakReference<AbilitySlice> mActivity;
    private String[] mPermissions;
    @Nullable
    private PermissionResultSingleListener mPermissionResultListener;
    @Nullable
    private RationaleSingleListener mRationaleSingleListener;
    @Nullable
    private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable
    private RationaleMultiListener mRationaleMultiListener;
    private MayiErrorListener mErrorListener;
    private List<String> mDeniedPermissions;
    private List<String> mGrantedPermissions;
    private final List<String> mRationalePermissions = new LinkedList<>();
    private boolean isRationaleCalled = false;
    private boolean isResultCalled = false;

    private Mayi(AbilitySlice activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    public static IPermissionBuilder.Permission withActivity(AbilitySlice activity) {
        return new Mayi(activity);
    }

    @Override
    public SinglePermissionBuilder withPermission(String permission) {
        mPermissions = new String[]{permission};
        return this;
    }

    @Override
    public MultiPermissionBuilder withPermissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }

    @Override
    public SinglePermissionBuilder onResult(PermissionResultSingleListener response) {
        if (!isResultCalled) {
            mPermissionResultListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onResult(PermissionResultMultiListener response) {
        if (!isResultCalled) {
            mPermissionsResultMultiListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public SinglePermissionBuilder onRationale(RationaleSingleListener rationale) {
        if (!isRationaleCalled) {
            mRationaleSingleListener = rationale;
            isRationaleCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onRationale(RationaleMultiListener rationale) {
        if (!isRationaleCalled) {
            mRationaleMultiListener = rationale;
            isRationaleCalled = true;
        }
        return this;
    }

    @Override
    public IPermissionBuilder onErrorListener(MayiErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    @Override
    public void check() {
        try {
            if (mPermissions == null || mPermissions.length == 0) {
                throw new NullPointerException("You must specify at least one valid permission to check");
            }
            if (Arrays.asList(mPermissions).contains(null)) {
                throw new NullPointerException("Permissions arguments must NOT contain null values");
            }
            final PermissionMatcher matcher = new PermissionMatcher(mPermissions, mActivity);
            if (matcher.areAllPermissionsGranted()) {
                grandEverything();
            } else {
                mDeniedPermissions = matcher.getDeniedPermissions();
                mGrantedPermissions = matcher.getGrantedPermissions();
                checkPermissionsUtil();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mErrorListener != null) {
                mErrorListener.onError(e);
            }
        }
    }


    /**
     * Method to check the denied permission and call the rationale if the permission can be requested.
     */
    public void checkPermissionsUtil() {
        mRationalePermissions.clear();
        final List<PermissionBean> rationaleBeanList = new LinkedList<>();
        for (String deniedPermission : mDeniedPermissions) {
            if (mActivity.get().canRequestPermission(deniedPermission)) {
                final PermissionBean beanRationale = new PermissionBean(deniedPermission);
                beanRationale.setGranted(false);
                beanRationale.setPermanentlyDenied(false);
                rationaleBeanList.add(beanRationale);
                mRationalePermissions.add(deniedPermission);
            }
        }
        if (mRationaleSingleListener != null) {
            mRationaleSingleListener.onRationale(rationaleBeanList.get(0), new PermissionRationaleToken(this));
        } else if (mRationaleMultiListener != null) {
            mRationaleMultiListener.onRationale(rationaleBeanList.toArray(new
                    PermissionBean[rationaleBeanList.size()]), new PermissionRationaleToken(this));
        }
    }

    private void grandEverything() {
        final PermissionBean[] beans = new PermissionBean[mPermissions.length];
        for (int i = 0; i < mPermissions.length; i++) {
            if (mActivity.get().verifySelfPermission(mPermissions[i]) == IBundleManager.PERMISSION_GRANTED) {
                beans[i] = new PermissionBean(mPermissions[i]);
                beans[i].setGranted(true);
                beans[i].setPermanentlyDenied(false);
            } else {
                beans[i] = new PermissionBean(mPermissions[i]);
                beans[i].setGranted(false);
                beans[i].setPermanentlyDenied(true);
            }
        }
        if (mPermissionResultListener != null) {
            mPermissionResultListener.permissionResult(beans[0]);
        } else if (mPermissionsResultMultiListener != null) {
            mPermissionsResultMultiListener.permissionResults(beans);
        }
    }

    public void onContinuePermissionRequest() {
        initializeFragmentAndCheck(mDeniedPermissions, mGrantedPermissions);
    }

    /**
     * Shows the results as the permission requests have been skipped.
     */
    public void onSkipPermissionRequest() {
        if (mPermissionResultListener != null) {
            final PermissionBean beanRationale = new PermissionBean(mRationalePermissions.get(0));
            beanRationale.setGranted(false);
            beanRationale.setPermanentlyDenied(false);
            mPermissionResultListener.permissionResult(beanRationale);
        } else if (mPermissionsResultMultiListener != null) {
            final List<PermissionBean> totalBeanList = new LinkedList<>();
            for (String perm : mPermissions) {
                final PermissionBean bean = new PermissionBean(perm);
                if (mActivity.get().verifySelfPermission(perm) == IBundleManager.PERMISSION_GRANTED) {
                    bean.setGranted(true);
                    bean.setPermanentlyDenied(false);
                } else if (mRationalePermissions.contains(perm)) {
                    bean.setGranted(false);
                    bean.setPermanentlyDenied(false);
                } else {
                    bean.setGranted(false);
                    bean.setPermanentlyDenied(true);
                }
                totalBeanList.add(bean);
            }
            mPermissionsResultMultiListener.permissionResults(totalBeanList.toArray(new
                    PermissionBean[totalBeanList.size()]));
        }
    }

    private void initializeFragmentAndCheck(List<String> deniedPermissions, List<String> grantedPermissions) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.setListeners(mPermissionResultListener, mPermissionsResultMultiListener);
        permissionManager.checkPermissions(deniedPermissions, grantedPermissions);
        MayiSlice mayislice = new MayiSlice();
        mActivity.get().present(mayislice, new Intent());
    }
}