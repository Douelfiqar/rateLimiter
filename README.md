Java Rate Limiter
A small, in-memory rate limiter built with plain Java.
Rules
- Each user can make 10 requests in 10 seconds.
- The first request starts that user's 10-second window.
- Further requests in the same window are rejected once the count reaches 10.
- After 10 seconds, the next request starts a new window and resets the count.
- Users have independent limits.
  How it works
  RateLimiter.allowRequest(Integer userId) returns true when a request is allowed and false when the limit is reached.
  Each user has a Window containing:
- firstCall: start time, measured with System.nanoTime()
- count: number of accepted requests in the current window
  A ConcurrentHashMap<Integer, Window> stores the windows. computeIfAbsent() safely creates a window for a new user. synchronized(window) protects the counter and timestamp for that user, without blocking requests from other users.
  Try it
  RateLimiter limiter = new RateLimiter();

```
for (int i = 1; i <= 12; i++) {
System.out.println("Request " + i + ": " + limiter.allowRequest(1));
}
```

If all calls happen within 10 seconds, requests 1–10 return true; requests 11–12 return false.
To test concurrent access, submit 100 calls for the same user through an ExecutorService and wait for the tasks to finish. The expected result within one window is 10 accepted, 90 rejected. A CountDownLatch can make the calls start closer together when testing for race conditions.
Limitations
- Single JVM: two running application instances do not share the same counters.
- Memory only: all counters disappear when the process restarts.
- No eviction: entries for inactive users stay in memory.
- Test timing: a concurrent test is not guaranteed to start every request at exactly the same moment.