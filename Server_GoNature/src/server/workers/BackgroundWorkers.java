package server.workers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gui.controller.ServerScreenController;
import jdbc.MySqlConnection;

public class BackgroundWorkers {
	private static final List<ScheduledExecutorService> executors = new ArrayList<>();
	
	public static void sendNotificationToClientsDayBeforeVisitWorker(ServerScreenController serverGui) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		 Runnable task = () -> {
	            Calendar calendar = Calendar.getInstance();
	            calendar.add(Calendar.HOUR, 24);
	            calendar.set(Calendar.MINUTE, 0);
	            calendar.set(Calendar.SECOND, 0);
	            calendar.set(Calendar.MILLISECOND, 0);

	            serverGui.printToLogConsole("Sending Reminders for Orders in  "+ calendar.getTime());
//	            MySqlConnection.getInstance().updateOrderStatusForUpcomingVisits();
	        };

	        long initialDelay = calculateInitialDelay();
	        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.HOURS.toMillis(1), TimeUnit.MILLISECONDS);
//	        long initialDelay = 5000;
//	        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.SECONDS.toMillis(5), TimeUnit.MILLISECONDS);
	        executors.add(scheduler);
	}
	
    public static void CancelOrdersThatDidntConfirmWorker(ServerScreenController serverGui) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 22);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            serverGui.printToLogConsole("Checking Confirmation for Orders in  "+ calendar.getTime());
//            MySqlConnection.getInstance().ChangeLatePendingConfirmationToCancelled();
        };

        long initialDelay = calculateInitialDelay();
        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.HOURS.toMillis(1), TimeUnit.MILLISECONDS);
//        long initialDelay = 5000;
//        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.SECONDS.toMillis(1), TimeUnit.MILLISECONDS);
        executors.add(scheduler);
    }
	
    private static long calculateInitialDelay() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        calendar.add(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long nextHour = calendar.getTimeInMillis();
        return nextHour - now;
    }
    
    public static void shutdownExecutors() {
        executors.forEach(executor -> {
            executor.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                        System.err.println("Executor service did not terminate");
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        });
    }
}
