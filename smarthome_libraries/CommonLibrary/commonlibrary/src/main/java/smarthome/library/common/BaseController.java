package smarthome.library.common;

import com.google.gson.annotations.Expose;

import static smarthome.library.common.constants.Constants.STATE_PENDING;
import static smarthome.library.common.constants.Constants.STATE_UP_TO_DATE;

/**
 * Basic controller interface
 * <br>All controllers must implement this interface
 */
public class BaseController {
    /**
     * field indicates user request state:
     * 1. "pending" - raspberry processing controller state
     * 2. "up to date" - synchronized with real controller state
     */
    @Expose private String serveState;
    @Expose public ControllerType type;
    @Expose public long guid;
    @Expose public String state;

    public void setNewState(String newState) {}// todo is "String" enough for state?

    public void setPending() {
        serveState = STATE_PENDING;
    }

    public void setUpToDate() {
        serveState = STATE_UP_TO_DATE;
    }

    public boolean isPending() {
        return serveState.equals(STATE_PENDING);
    }

    public boolean isUpToDate() {
       return serveState.equals(STATE_UP_TO_DATE);
    }
}