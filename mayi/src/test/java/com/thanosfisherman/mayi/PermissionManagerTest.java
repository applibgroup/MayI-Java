/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanosfisherman.mayi;

import static org.mockito.Mockito.*;
import ohos.aafwk.ability.AbilitySlice;
import ohos.bundle.IBundleManager;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PermissionManagerTest {

    @InjectMocks
    PermissionManager permissionManager;
    @Mock
    AbilitySlice slice;
    @Mock
    PermissionResultMultiListener permissionResultMultiListener;

    @Before
    public void setUp() {
        permissionManager = PermissionManager.getInstance();
        permissionManager.setmAbility(slice);
    }

    @Test
    public void testGetBeanResults() {
        String[] allPermissions = {"SystemPermission.LOCATION", "SystemPermission.MICROPHONE"};
        int[] grantResults = {IBundleManager.PERMISSION_DENIED, IBundleManager.PERMISSION_GRANTED};
        final List<PermissionBean> beansResultList = new LinkedList<>();
        permissionManager.getBeanResults(beansResultList, allPermissions, grantResults);
        verify(slice).canRequestPermission(allPermissions[0]);
    }

    @Test
    public void testRequestPermissionResult() {
        int requestCode = 1001;
        String[] permissions = {"SystemPermission.LOCATION"};
        int[] grantResults = {IBundleManager.PERMISSION_DENIED};
        List<String> deniedPermissions = new ArrayList<String>();
        deniedPermissions.add("SystemPermission.LOCATION");
        List<String> grantedPermissions = new ArrayList<String>();
        grantedPermissions.add("SystemPermission.MICROPHONE");
        permissionManager.checkPermissions(deniedPermissions, grantedPermissions);
        permissionManager.setListeners(null, permissionResultMultiListener);
        permissionManager.requestPermissionsResult(requestCode, permissions, grantResults);
        PermissionBean pBean;
        pBean = new PermissionBean("SystemPermission.MICROPHONE");
        pBean.setGranted(true);
        pBean.setPermanentlyDenied(false);
        List<PermissionBean> permissionBeans = new LinkedList<>();
        permissionBeans.add(0, pBean);
        pBean = new PermissionBean("SystemPermission.LOCATION");
        pBean.setGranted(false);
        pBean.setPermanentlyDenied(true);
        permissionBeans.add(1, pBean);
        verify(permissionResultMultiListener).permissionResults(permissionBeans.toArray(new
                PermissionBean[permissionBeans.size()]));
    }

    @Test
    public void testRequestPermissions() {
        List<String> deniedPermissions = new ArrayList<String>();
        deniedPermissions.add("SystemPermission.LOCATION");
        List<String> grantedPermissions = new ArrayList<String>();
        grantedPermissions.add("SystemPermission.MICROPHONE");
        permissionManager.checkPermissions(deniedPermissions, grantedPermissions);
        permissionManager.requestPermissions();
        verify(slice).requestPermissionsFromUser(deniedPermissions.toArray(new
                String[deniedPermissions.size()]), 1001);
    }
}