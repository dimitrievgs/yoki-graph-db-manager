package org.yoki_manager.orientdb.controls.titledpanes;

import javafx.event.EventHandler;
import org.yoki_manager.orientdb.controls.JavafxEvent;

public abstract class TitlesPanesEventHandler implements EventHandler<JavafxEvent> {

    public abstract void onClosePaneEvent(int param0);

    public abstract void onEvent2(String param0);

    @Override
    public void handle(JavafxEvent event) {
        event.invokeHandler(this);
    }
}