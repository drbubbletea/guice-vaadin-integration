/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.guice.server;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.VaadinSession;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

class VaadinSessionScope implements Scope, Serializable, SessionDestroyListener {

    private final transient Map<VaadinSession, Map<Key<?>, Object>> scopeMapsBySession;

    @SuppressWarnings("WeakerAccess")
    public VaadinSessionScope(){
        scopeMapsBySession = Collections.synchronizedMap(new WeakHashMap<>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
        return () -> {
            final VaadinSession vaadinSession = checkNotNull(VaadinSession.getCurrent());

            synchronized (vaadinSession){
                Map<Key<?>, Object> scopeMap = scopeMapsBySession.get(vaadinSession);

                if (scopeMap == null) {
                    scopeMap = new HashMap<>();
                    scopeMapsBySession.put(vaadinSession, scopeMap);
                }

                T result = (T)scopeMap.get(key);

                if(result == null){
                    result = provider.get();
                    scopeMap.put(key, result);
                }

                return result;
            }
        };
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
            scopeMapsBySession.remove(event.getSession());
    }
}
