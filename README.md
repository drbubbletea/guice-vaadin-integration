Guice Vaadin
======================

Guice Vaadin is the official [Guice](https://github.com/google/guice) integration for [Vaadin Framework](https://github.com/vaadin/framework).

#  usage

## setting up the servlet

first step is to set up the GuiceVaadinServlet, which needs a packagesToScan parameter holding the 
names of all packages that should be scanned for UIs, Views, ViewChangeListeners and VaadinServiceInitListeners. 
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

## Scopes

Available scopes are UIScope and VaadinSessionScope, similar to what the Spring addon offers.
UIScope is what MUST be the scope for all Vaadin-components, since they must belong to exactly one
UI. VaadinSessionScope may be used to sync data between multiple tabs in the same browser.   

```java
import com.vaadin.guice.annotation.UIScope;

@UIScope
public class MyButton extends Button {
}
```

## Guice-Module loading

Since Guice is configured via so called Modules, we need a way to load these modules. All Modules
in the packages contained by 'packagesToScan' will be instantiated and loaded by default. 

```java
package org.mycompany.ui;

import com.google.inject.AbstractModule;

//will be loaded, since the 'org.mycompany.ui'-package is included in
//the packagesToScan  
public class MyModule extends AbstractModule{
    protected void configure(){
        //...    
    }
}
```

The alternative way to load modules is via the @Import-Annotation. This was 
introduced to make sort of an addon-development for guice-vaadin possible, similar
to what Spring offers with it's own @Import-annotation.

First, a new Annotation is needed that points to the module to be loaded via @Import:

```java
package org.mycompany.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.Type)
@Retention(RUNTIME)
@Import(SomeModule.class)
public @interface UseSomeModule {    
}
```

Second, the annotation is pinned on the servlet and that's it.

```java
    package org.mypackage;

    @UseSomeModule
    @javax.servlet.annotation.WebServlet(name = "Guice-Vaadin-Servlet", urlPatterns = "/*")
    @com.vaadin.guice.annotation.PackagesToScan({"org.mycompany.ui", "org.mycompany.moreui"})
    public class MyServlet extends com.vaadin.guice.server.GuiceVaadinServlet{
    }
```

Copyright 2015-2017 Vaadin Ltd.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
