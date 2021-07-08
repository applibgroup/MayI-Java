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

import static org.mockito.Mockito.verify;
import ohos.aafwk.ability.AbilitySlice;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MayiTest {

    @InjectMocks
    Mayi mayi;
    @Mock
    PermissionResultSingleListener response;
    @Mock
    PermissionResultMultiListener response1;
    @Mock
    RationaleSingleListener rationale;
    @Mock
    RationaleMultiListener rationale1;
    @Mock
    MayiErrorListener errorListener;
    @Mock
    AbilitySlice slice;
    @Mock
    MayiErrorListener mErrorListener;

    @Before
    public void setUp() throws Exception {
        mayi = (Mayi)Mayi.withActivity(slice);
    }

    @Test
    public void testWithPermission() {
        Mayi mayi1 = (Mayi)mayi.withPermission("SystemPermission.Location");
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testWithPermissionWithNull() {
        Mayi mayi1 = (Mayi)mayi.withPermission(null);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testWithPermissions() {
        String permissions[] = {"SystemPermission.Location", "SystemPermission.Microphone"};
        Mayi mayi1 = (Mayi)mayi.withPermissions(permissions);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testWithPermissionsWithNull() {
        String permissions[] = null;
        Mayi mayi1 = (Mayi)mayi.withPermissions(permissions);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnResult() {
        Mayi mayi1 = (Mayi)mayi.onResult(response);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnResultMulti() {
        Mayi mayi1 = (Mayi)mayi.onResult(response1);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnRationale() {
        Mayi mayi1 = (Mayi)mayi.onRationale(rationale);
        Assert.assertNotNull(mayi1);
    }


    @Test
    public void testOnRationaleMulti() {
        Mayi mayi1 = (Mayi)mayi.onRationale(rationale1);
        Assert.assertNotNull(mayi1);
    }


    @Test
    public void testOnErrorListener() {
        Mayi mayi1 = (Mayi)mayi.onErrorListener(errorListener);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testCheck() throws NullPointerException {
        try {
            mayi.onErrorListener(mErrorListener);
            Mayi mayi1 = (Mayi)mayi.withPermissions(null);
            mayi1.check();
        } catch(Exception e) {
            verify(mErrorListener).onError(e);
        }
    }

    @Test
    public void testCheckWithNull() throws NullPointerException {
        try {
            mayi.onErrorListener(mErrorListener);
            String permissions[] = {null, "SystemPermission.Location"};
            Mayi mayi1 = (Mayi)mayi.withPermissions(permissions);
            mayi1.check();
        } catch(Exception e) {
            verify(mErrorListener).onError(e);
        }
    }

    @Test
    public void testCheckWithNotNull() throws NullPointerException {
        String permissions[] = {"SystemPermission.Microphone", "SystemPermission.Location"};
        Mayi mayi1 = (Mayi)mayi.withPermissions(permissions);
        Mayi mayi2 = (Mayi)mayi1.onResult(response1);
        response=null;
        Mayi mayi3 = (Mayi)mayi2.onResult(response);
        mayi3.check();
        final PermissionBean[] beans = new PermissionBean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            beans[i] = new PermissionBean(permissions[i]);
            beans[i].setGranted(true);
            beans[i].setPermanentlyDenied(false);
        }
        verify(response1).permissionResults(beans);
    }
}