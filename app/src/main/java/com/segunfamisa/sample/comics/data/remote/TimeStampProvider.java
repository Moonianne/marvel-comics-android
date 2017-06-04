package com.segunfamisa.sample.comics.data.remote;


/**
 * Wrapper to provide the current time stamp.
 */
public class TimeStampProvider {

    /**
     * Get current time stamp
     *
     * @return - string value of the current time stamp.
     */
    public String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

}
