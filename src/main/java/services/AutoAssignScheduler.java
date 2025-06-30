package services;

import services.OrderAssignmentService;

import java.util.Timer;
import java.util.TimerTask;

public class AutoAssignScheduler {

    private final Timer timer = new Timer();
    private final OrderAssignmentService service = new OrderAssignmentService();

    /**
     * Bắt đầu Scheduler — cứ 30 phút chạy 1 lần.
     */
    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("🔔 Auto-assign orders triggered at: " + new java.util.Date());
                service.autoAssignOrders();
            }
        }, 0, 30 * 60 * 1000); // 30 phút = 30 * 60 * 1000 ms
    }

    /**
     * Dừng Scheduler nếu cần.
     */
    public void stop() {
        timer.cancel();
    }
}
