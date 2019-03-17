package smarthome.library.common;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;

import java.util.Objects;

import static smarthome.library.common.constants.Constants.STATE_PENDING_READ;
import static smarthome.library.common.constants.Constants.STATE_PENDING_WRITE;
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
    @Expose public String name;
    @Expose public String serveState;
    @Expose public ControllerType type;
    @Expose public long guid;
    @Expose public String state;
    @Expose public String deviceType;

    public void setNewState(String newState) {
        state = newState;
    }

    public void setPendingWrite() {
        serveState = STATE_PENDING_WRITE;
    }

    public void setPendingRead() {
        serveState = STATE_PENDING_READ;
    }

    public void setUpToDate() {
        serveState = STATE_UP_TO_DATE;
    }

    @Exclude
    public boolean isPendingWrite() {
        return STATE_PENDING_WRITE.equals(serveState);
    }

    @Exclude
    public boolean isPendingRead() {
        return STATE_PENDING_READ.equals(serveState);
    }

    @Exclude
    public boolean isUpToDate() {
       return STATE_UP_TO_DATE.equals(serveState);
    }

    public boolean isIdentical(BaseController controller) {
        return equals(controller) && Objects.equals(name, controller.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseController) {
            return ((BaseController) obj).guid == guid;
        }
        return super.equals(obj);
    }
}
