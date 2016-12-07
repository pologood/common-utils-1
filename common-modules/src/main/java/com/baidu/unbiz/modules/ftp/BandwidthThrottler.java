package com.baidu.unbiz.modules.ftp;

import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * BandwidthThrottler
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年6月1日 下午2:53:35
 */
public class BandwidthThrottler extends LoggerSupport {

    private long lastTime;
    private long lastBytes;
    private int thresholdBytesPerSec = -1;

    public BandwidthThrottler(int thresholdBytesPerSec) {
        this.thresholdBytesPerSec = thresholdBytesPerSec;
    }

    public void setThreshold(int thresholdBytesPerSec) {
        this.thresholdBytesPerSec = thresholdBytesPerSec;
    }

    public int getThreshold() {
        return thresholdBytesPerSec;
    }

    public void throttleTransfer(long bytesSoFar) {
        long time = System.currentTimeMillis();
        long diffBytes = bytesSoFar - lastBytes;
        long diffTime = time - lastTime;
        if (diffTime == 0) {
            return;
        }

        double rate = ((double) diffBytes / (double) diffTime) * 1000.0;
        if (logger.isDebugEnabled()) {
            logger.debug("rate= {}", rate);
        }

        while (rate > thresholdBytesPerSec) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Sleeping to decrease transfer rate (rate = {} bytes/s", rate);
                }

                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            diffTime = System.currentTimeMillis() - lastTime;
            rate = ((double) diffBytes / (double) diffTime) * 1000.0;
        }
        lastTime = time;
        lastBytes = bytesSoFar;
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
        lastBytes = 0;
    }
}
