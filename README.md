Guice Vaadin
======================

Guice Vaadin is the official [Guice](https://github.com/google/guice) integration for [Vaadin Framework](https://github.com/vaadin/framework).

#  usage

## setting up the servlet

first step is to set up the GuiceVaadinServlet, which needs a packagesToScan parameter holding the 
names of all packages that should be scanned for UIs, Views, ViewChangeListeners, RequestHandler, VaadinServiceInitListeners
and custom implementation of UildRequestHandler. 
Sub-packages of these packages are scanned as well. 

This can be done either by subclassing GuiceVaadinServlet and annotating it with @PackagesToScan, or by
configuring a GuiceVaadinServlet in the deployment-descriptor.

### configuration in java

```java
    package org.mypackage;

    @javax.servlet.annotation.WebServlet(name = "Guice-Vaadin-Servlet", urlPatterns = "/*")
    @com.vaadin.guice.annotation.PackagesToScan({"org.mycompany.ui", "org.mycompany.moreui"})
    public class MyServlet extends com.vaadin.guice.server.GuiceVaadinServlet{
    }
```

### configuration in xml

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee" version="2.5">
    <servlet>
        <init-param>
            <param-name>packagesToScan</param-name>
            <param-value>org.mycompany.ui, org.mycompany.moreui</param-value>
        </init-param>
        <servlet-name>Guice-Vaadin-Servlet</servlet-name>
        <servlet-class>com.vaadin.guice.server.GuiceVaadinServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Guice-Vaadin-Servlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```

## setting up UIs

All packages in packagesToScan and their sub-packages are scanned for Vaadin-UI's. These UI's need to have a 
GuiceUI-annotation. 

```java
@com.vaadin.guice.annotation.GuiceUI
public class MyUI extends com.vaadin.ui.UI {
}
```

In order to set up a Vaadin-Navigator, a 'viewContainer' is to be configured. A viewContainer is the second parameter
to the Navigator's constructor. The Content of a UI can also be configured via the annotation

```java
@UIScope
public class MyViewContainer extends Panel {
}

@UIScope
public class Content extends VerticalLayout {
   @Inject
   Content(MyHeader header, MyViewContainer viewContainer){
      addComponents(header, viewContainer);
   }
}

@GuiceUI(content = Content.class, viewContainer = MyViewContainer.class)
public class MyUI extends com.vaadin.ui.UI {
    public void init(com.vaadin.server.VaadinRequest request){
         // can be left empty
    }
}
```

## Limitation - UildRequestHandler

It's only possible to register one custom UildRequestHandler with Vaadin Guice. GuiceVaadinServlet throws an 
IllegalStateException if more than on custom implementation of UildRequestHandler was found in packagesToScan.

# Copyright

Copyright 2015-2018 Vaadin Ltd.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
