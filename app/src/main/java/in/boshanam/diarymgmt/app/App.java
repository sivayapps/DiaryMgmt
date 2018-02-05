package in.boshanam.diarymgmt.app;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * //TODO check for improvements of this class
 * Created by Siva on 2/5/2018.
 */

public class App {

    private App() {
    }

    private Executor executor;
    private static App instance;

    public synchronized static App getInstance() {
        if (instance == null) {
            instance = new App();
            instance.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        }
        return instance;
    }

    public Executor getExecutor() {
        return executor;
    }
}
