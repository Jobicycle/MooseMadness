package actors;

import java.awt.event.KeyEvent;

public interface KeyboardControllable {
    public void triggerKeyPress(KeyEvent e);

    public void triggerKeyRelease(KeyEvent e);
}
