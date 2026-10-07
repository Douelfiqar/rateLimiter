import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private ConcurrentHashMap<Integer, Window> memory = new ConcurrentHashMap<>();
    private final long FIXED_INTEVAL = 10_000_000_000L;

    public boolean allowRequest(Integer id){
        if(id == null)
            throw new RuntimeException("Required Id");

        Window window = memory.computeIfAbsent(id, key -> new Window(System.nanoTime()));

        synchronized (window) {
            long interval = System.nanoTime() - window.firstCall;
            if (interval < FIXED_INTEVAL){
                if(window.count < 10) {
                    window.setCount(window.getCount() + 1);
                    return true;
                }
                else {
                    return false;
                }
            }else {
                window.setFirstCall(System.nanoTime());
                window.setCount(1);
                return true;
            }
        }
    }
}
