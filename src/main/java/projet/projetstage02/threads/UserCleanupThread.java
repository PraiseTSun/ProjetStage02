package projet.projetstage02.threads;

import lombok.AllArgsConstructor;
import projet.projetstage02.service.ApplicationService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class UserCleanupThread extends Thread{
    ApplicationService applicationService;

    @Override
    public void run() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
                applicationService.deleteUnconfirmedUsers();
        },0,6, TimeUnit.HOURS);
    }
}
