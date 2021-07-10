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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PermissionBeanTest {
    private static final String LOCATION_SYSTEM_PERMISSION = "ohos.permission.LOCATION";
    @InjectMocks
    PermissionBean pBean;
    @Mock
    Object o;

    @Before
    public void setUp() {
        pBean = new PermissionBean(LOCATION_SYSTEM_PERMISSION);
    }

    @Test
    public void testEquals() {
        boolean result = pBean.equals(o);
        assertFalse(result);
    }

    @Test
    public void testEqualsTrue() {
        PermissionBean pBean1 = new PermissionBean(LOCATION_SYSTEM_PERMISSION);
        boolean result = pBean.equals(pBean1);
        assertTrue(result);
    }

    @Test
    public void testHashCode(){
        int result = LOCATION_SYSTEM_PERMISSION.hashCode();
        result = 31 * result;
        assertEquals(result, pBean.hashCode());
    }

    @Test
    public void testToString() {
        String result = "Permission{" + "name='" + pBean.getSimpleName() + "'" + ", isGranted=" + pBean.isGranted()
                + ", isPermanentlyDenied=" + pBean.isPermanentlyDenied() + "}";
        assertEquals(pBean.toString(), result);
    }

    @Test
    public void testGetName() {
        assertEquals(LOCATION_SYSTEM_PERMISSION, pBean.getName());
    }

    @Test
    public void testGetSimpleNameTrue() {
        String result = LOCATION_SYSTEM_PERMISSION.split("\\.")[2];
        assertEquals(pBean.getSimpleName(), result);
    }

    @Test
    public void testIsGranted() {
        assertFalse(pBean.isGranted());
    }

    @Test
    public void testSetGranted() {
        pBean.setGranted(true);
        assertTrue(pBean.isGranted());
    }

    @Test
    public void testIsPermanentlyDenied() {
        assertFalse(pBean.isPermanentlyDenied());
    }

    @Test
    public void testSetPermanentlyDenied() {
        pBean.setPermanentlyDenied(true);
        assertTrue(pBean.isPermanentlyDenied());
    }
}