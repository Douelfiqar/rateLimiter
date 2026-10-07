import java.time.LocalTime;

public class Window {
    long firstCall;
    Integer count;

    public Window(){
        this.count = 0;
    }

    public Window(long firstCall) {
        this.firstCall = firstCall;
        this.count = 0;
    }

    public long getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(long firstCall) {
        this.firstCall = firstCall;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
