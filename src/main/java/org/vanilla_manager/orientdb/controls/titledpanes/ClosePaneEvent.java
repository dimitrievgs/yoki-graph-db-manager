package org.vanilla_manager.orientdb.controls.titledpanes;

import javafx.event.EventType;
import org.vanilla_manager.orientdb.controls.JavafxEvent;

public class ClosePaneEvent extends JavafxEvent {

    public static final EventType<JavafxEvent> CLOSE_PANE = new EventType(JAVAFX_EVENT_TYPE, "ClosePaneEvent");

    private final int param;

    public ClosePaneEvent(int param) {
        super(CLOSE_PANE);
        this.param = param;
    }

    @Override
    public void invokeHandler(TitlesPanesEventHandler handler) {
        handler.onClosePaneEvent(param);
    }
}