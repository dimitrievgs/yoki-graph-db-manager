package org.yoki_manager.orientdb.controls.titledpanes;

import javafx.event.EventType;
import org.yoki_manager.orientdb.controls.JavafxEvent;

public class TitlesPanesEvent2 extends JavafxEvent {

    public static final EventType<JavafxEvent> TITLES_PANES_EVENT_TYPE_2 = new EventType(JAVAFX_EVENT_TYPE, "TitlesPanesEvent2");

    private final String param;

    public TitlesPanesEvent2(String param) {
        super(TITLES_PANES_EVENT_TYPE_2);
        this.param = param;
    }

    @Override
    public void invokeHandler(TitlesPanesEventHandler handler) {
        handler.onEvent2(param);
    }

}
