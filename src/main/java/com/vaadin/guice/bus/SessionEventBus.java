package com.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import com.vaadin.guice.annotation.VaadinSessionScope;

/**
 * This class serves as a means to allow VaadinSession-scope communication between objects.
 * UIEventBus is intended for events that are of 'VaadinSession-scope' interest, like updates to
 * data that is used by multiple {@link com.vaadin.ui.UI}'s of the same {@link
 * com.vaadin.server.VaadinSession}. It is VaadinSession-scoped and therefore not prone to memory
 * leaks.
 *
 * <code> {@literal @}Inject private SessionEventBus sessionEventBus;
 *
 * ... sessionEventBus.post(new DataSetInSessionScopeChangedEvent()); ...
 *
 * </code> </pre>
 *
 * @author Bernd Hopp (bernd@vaadin.com)
 */
@VaadinSessionScope
public final class SessionEventBus extends EventBus {
    SessionEventBus() {
    }
}
