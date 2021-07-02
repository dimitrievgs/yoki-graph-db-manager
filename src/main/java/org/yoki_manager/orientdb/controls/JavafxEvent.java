package org.yoki_manager.orientdb.controls;

import javafx.event.Event;
import javafx.event.EventType;
import org.yoki_manager.orientdb.controls.titledpanes.TitlesPanesEventHandler;

public abstract class JavafxEvent extends Event {

    public static final EventType<JavafxEvent> JAVAFX_EVENT_TYPE = new EventType(ANY);

    public JavafxEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(TitlesPanesEventHandler handler);
}