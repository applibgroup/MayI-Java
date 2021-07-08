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

import ohos.aafwk.ability.AbilitySlice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.ref.WeakReference;

@RunWith(MockitoJUnitRunner.class)
public class PermissionMatcherTest {
    String[] permissions = {"SystemPermission.LOCATION", "SystemPermission.MICROPHONE"};
    PermissionMatcher pMatcher;
    @Mock
    WeakReference<AbilitySlice> slice;

    @Test(expected = IllegalArgumentException.class)
    public void testMatcher() {
        pMatcher = new PermissionMatcher(null, slice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatcherWithNullSlice() {
        pMatcher = new PermissionMatcher(permissions, null);
    }
}