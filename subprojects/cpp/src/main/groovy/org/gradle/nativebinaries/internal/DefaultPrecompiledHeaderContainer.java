/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.nativebinaries.internal;

import org.gradle.api.internal.AbstractNamedDomainObjectContainer;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.nativebinaries.PrecompiledHeader;
import org.gradle.nativebinaries.PrecompiledHeaderContainer;

public class DefaultPrecompiledHeaderContainer extends AbstractNamedDomainObjectContainer<PrecompiledHeader> implements PrecompiledHeaderContainer {

    public DefaultPrecompiledHeaderContainer(Instantiator instantiator) {
        super(PrecompiledHeader.class, instantiator);
    }

    @Override
    protected PrecompiledHeader doCreate(String name) {
        return getInstantiator().newInstance(DefaultPrecompiledHeader.class, name, getInstantiator());
    }
}