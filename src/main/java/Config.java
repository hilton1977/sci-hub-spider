import java.util.concurrent.*;

public class Config {
    private static ThreadPoolExecutor POOL = new ThreadPoolExecutor(5, 0, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    private static MainUI mainUI;

    public static ExecutorService threadPool() {
        return POOL;
    }

    public static MainUI getMainUI() {
        return mainUI;
    }

    public static void main(String[] args) {
        Config.mainUI = new MainUI();
    }
}
