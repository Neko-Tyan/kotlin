/*
 * Copyright 2010-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.lang.resolve;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor;

import java.util.Set;

import static org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor.Kind.DELEGATION;
import static org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor.Kind.FAKE_OVERRIDE;
import static org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor.Kind.SYNTHESIZED;

public final class CallableMemberDescriptorUtils {

    private CallableMemberDescriptorUtils() {
    }

    @NotNull
    public static Set<CallableMemberDescriptor> getOverriddenDeclarationDescriptors(@NotNull CallableMemberDescriptor descriptor) {
        Set<CallableMemberDescriptor> result = Sets.newHashSet();
        Set<? extends CallableMemberDescriptor> overriddenDescriptors = descriptor.getOverriddenDescriptors();
        for (CallableMemberDescriptor overriddenDescriptor : overriddenDescriptors) {
            if (overriddenDescriptor.getKind() == CallableMemberDescriptor.Kind.DECLARATION) {
                result.add(overriddenDescriptor);
            }
            else if (overriddenDescriptor.getKind() == FAKE_OVERRIDE || overriddenDescriptor.getKind() == DELEGATION) {
                result.addAll(getOverriddenDeclarationDescriptors(overriddenDescriptor));
            }
            else if (overriddenDescriptor.getKind() == SYNTHESIZED) {
                //do nothing
            }
        }
        return OverridingUtil.filterOverrides(result);
    }
}
