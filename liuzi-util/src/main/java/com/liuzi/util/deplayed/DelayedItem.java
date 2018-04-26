package com.liuzi.util.deplayed;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayedItem<T> implements Delayed {

    private long MILLISECONDS_ORIGIN = System.currentTimeMillis();

    private T item;
    private long milliseconds;
    private Integer checkTimesLeft;

    public DelayedItem(T item, long milliseconds, Integer checkTimes) {
        this.milliseconds = milliseconds;
        this.item = item;
        this.checkTimesLeft = checkTimes;
    }

    private final long now() {
        return System.currentTimeMillis() - MILLISECONDS_ORIGIN;
    }

    public void setMilliseconds(long milliseconds) {
        MILLISECONDS_ORIGIN = System.currentTimeMillis();
        this.milliseconds = milliseconds;
    }

    /**
     * 如果超时，或者Map缓存中已经没有该元素，都会导致失效
     *
     * @param unit
     * @return
     */
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(milliseconds - now(), TimeUnit.MILLISECONDS);

//        LOGGER.debug("=============key:" + item + ",time:" + milliseconds + " , now:" + now() + ",times:{}", checkTimesLeft);
        return d;
    }

    public void setCheckTimesLeft(Integer times) {
        this.checkTimesLeft = times;
    }

    public Integer getCheckTimesLeft() {
        return this.checkTimesLeft;
    }

    public boolean isEnd() {
        if (checkTimesLeft == null) {
            return false;
        }
        
        if (checkTimesLeft.intValue() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DelayedItem) {
            return item.equals(((DelayedItem) obj).getItem());
        } else {
            return false;
        }
    }

    public int compareTo(Delayed o) {
        if (o == this) {
            return 0;
        }
//        if (o instanceof DelayItem) {
//            DelayItem x = (DelayItem) o;
//            long diff = milliseconds - x.milliseconds;
//            if (diff < 0)
//                return -1;
//            else if (diff > 0)
//                return 1;
//            else
//                return 1;
//        }

        //根据距离下次超时时间的长短来排优先级，越接近下次超时时间的优先级越高
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getItem() {
        return item;
    }
}