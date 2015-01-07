package com.monitor;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by nick on 1/7/15.
 */
public class ConsoleLogger implements ILogger {

    @Override
    public void log(String text) {

        Calendar c = new GregorianCalendar();

        System.out.println(String.format("%tT", c.getInstance()) + " - " + text);

    }
}
