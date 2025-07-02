package services;

import java.util.Timer;
import java.util.TimerTask;

public class AutoAssignScheduler {

    private final Timer timer = new Timer();
    private final OrderAssignmentService service = new OrderAssignmentService();

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("🔔 Auto-assign orders triggered at: " + new java.util.Date());
                service.autoAssignOrders();
            }
        }, 0, 2 * 60 * 1000);
    }

    public void stop() {
        timer.cancel();
    }
}
