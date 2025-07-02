package services;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SchedulerListener implements ServletContextListener {

    private AutoAssignScheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = new AutoAssignScheduler();
        scheduler.start();
        System.out.println("✅ AutoAssignScheduler started!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.stop();
        System.out.println("❌ AutoAssignScheduler stopped!");
    }
}
