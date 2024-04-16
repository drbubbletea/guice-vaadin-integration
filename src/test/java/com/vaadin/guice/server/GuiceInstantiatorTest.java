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

import com.google.inject.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.di.*;
import com.vaadin.flow.server.*;
import jakarta.servlet.*;
import org.junit.*;
import org.mockito.*;

import java.util.*;
import java.util.stream.*;

public class GuiceInstantiatorTest {

    public static class RouteTarget1 extends Div {
    }

    @Singleton
    public static class RouteTarget2 extends Div {
    }

    public static class TestVaadinServiceInitListener
            implements VaadinServiceInitListener {

        @Override
        public void serviceInit(ServiceInitEvent event) {
        }

    }

    public static GuiceVaadinServlet getServlet() throws ServletException {
        GuiceVaadinServlet servlet = new TestServlet();

        ServletConfig config = Mockito.mock(ServletConfig.class);
        ServletContext servletContext = Mockito.mock(ServletContext.class);

        Mockito.when(config.getServletContext()).thenReturn(servletContext);

        Mockito.when(config.getInitParameterNames())
                .thenReturn(Collections.emptyEnumeration());

        Mockito.when(servletContext.getInitParameterNames())
                .thenReturn(Collections.emptyEnumeration());

        VaadinService vaadinService = Mockito.mock(VaadinService.class);

        VaadinService.setCurrent(vaadinService);

        servlet.init(config);
        servlet.servletInitialized();

        return servlet;
    }
}
