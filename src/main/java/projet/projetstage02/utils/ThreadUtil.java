package projet.projetstage02.utils;

import org.springframework.context.annotation.Bean;
import projet.projetstage02.threadpool.ThreadPool;

public class ThreadUtil {
    private static ThreadPool threadPool;

    @Bean
    public static ThreadPool threadPool() {
        if (threadPool == null) {
            threadPool = new ThreadPool();
            threadPool.start();
        }
        return threadPool;
    }
}
