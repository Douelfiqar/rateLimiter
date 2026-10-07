import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String []args) throws InterruptedException {
        RateLimiter limiter = new RateLimiter();

        // Create a pool of 100 threads
                ExecutorService executor =
                        Executors.newFixedThreadPool(100);

        // Create a closed starting gate
                CountDownLatch latch = new CountDownLatch(1);

        // Count accepted requests
                AtomicInteger accepted = new AtomicInteger();

        // Submit 100 tasks
                for (int i = 0; i < 100; i++) {

                    executor.submit(() -> {
                        try {
                            // Wait for the gate to open
                            latch.await();

                            // All requests target the same user
                            if (limiter.allowRequest(1)) {
                                accepted.incrementAndGet();
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }

        // Open the gate
                latch.countDown();

        // Stop accepting new tasks
                executor.shutdown();

        // Wait for existing tasks to finish
                boolean finished =
                        executor.awaitTermination(30, TimeUnit.SECONDS);

                if (!finished) {
                    throw new IllegalStateException("Test did not finish");
                }

                System.out.println("Accepted: " + accepted.get());
    }
}
