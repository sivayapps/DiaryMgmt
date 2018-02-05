package in.boshanam.diarymgmt.app;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is for sharing state across application(between activities).
 * Revise design/approach for sharing state when better solution found
 * All methods in this are synchronized and be cautious while using this class to avoid performance issues.
 *
 * Created by Siva on 2/5/2018.
 */

public class SharedState {
    private SharedState() {}

    private Map<String, Object> state = new HashMap<>();

    private static SharedState instance;

    public synchronized static SharedState getInstance() {
        if (instance == null) {
            instance = new SharedState();
        }
        return instance;
    }

    public <T> T getState(String key) {
        return (T) state.get(key);
    }

    public <T> T setState(String key, T data) {
        state.put(key, data);
        return data;
    }

}
