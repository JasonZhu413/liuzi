package com.liuzi.util.deplayed;


import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedItem implements Delayed {

    private long MILLISECONDS_ORIGIN = System.currentTimeMillis();

    private String key;
    private Map<String, Object> params;
    private long milliseconds;
    
    public DelayedItem(String key, Map<String, Object> params,
    		long milliseconds) {
        this.milliseconds = milliseconds;
        this.key = key;
        this.params = params;
    }

    private final long now() {
        return System.currentTimeMillis() - MILLISECONDS_ORIGIN;
    }

    /**
     * 如果超时，或者Map缓存中已经没有该元素，都会导致失效
     * @param unit
     * @return
     */
    public long getDelay(TimeUnit unit) {
    	return unit.convert(milliseconds - now(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DelayedItem) {
            return key.equals(((DelayedItem) obj).getKey());
        } else {
            return false;
        }
    }

    public int compareTo(Delayed o) {
        if (o == this) {
            return 0;
        }

        //根据距离下次超时时间的长短来排优先级，越接近下次超时时间的优先级越高
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public String getKey() {
        return key;
    }

	public Map<String, Object> getParams() {
		return params;
	}
}