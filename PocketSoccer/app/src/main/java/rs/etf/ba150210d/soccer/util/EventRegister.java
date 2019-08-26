package rs.etf.ba150210d.soccer.util;

/**
 * Generic class for registering events. Like a mutable boolean.
 */
public class EventRegister {
    private boolean isRegistered;

    public EventRegister() {
        isRegistered = false;
    }

    public void register() {
        isRegistered = true;
    }

    public boolean check() {
        return isRegistered;
    }

    public void reset() {
        isRegistered = false;
    }
}
