package com.ipvision.view.utility;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class ANSIColorLayout extends PatternLayout {

    public static final String DEFAULT_COLOR_ALL = "\u001B[1;37m"; //BLACK
    public static final String DEFAULT_COLOR_FATAL = "\u001B[0;31m"; //RED
    public static final String DEFAULT_COLOR_ERROR = "\u001B[0;31m"; //RED
    public static final String DEFAULT_COLOR_WARN = "\u001B[1;32m"; //GREEN
    public static final String DEFAULT_COLOR_INFO = "\u001B[1;37m"; //BLACK
    public static final String DEFAULT_COLOR_DEBUG = "\u001B[0;34m"; //BLUE
    public static final String DEFAULT_COLOR_RESET = "\u001B[1;37m"; //BLACK
    public static final String DEFAULT_COLOR_STACKTRACE = "\u001B[0;33m"; //GRAY
    public static final String DEFAULT_COLOR = "\u001B[1;37m"; //BLACK

    public ANSIColorLayout() {
        setDefaultColors();
    }

    public ANSIColorLayout(String string) {
        super(string);
        setDefaultColors();
    }

    private void setDefaultColors() {
        all = DEFAULT_COLOR_ALL;
        fatal = DEFAULT_COLOR_FATAL;
        error = DEFAULT_COLOR_ERROR;
        warn = DEFAULT_COLOR_WARN;
        info = DEFAULT_COLOR_INFO;
        debug = DEFAULT_COLOR_DEBUG;
        stacktrace = DEFAULT_COLOR_STACKTRACE;
        defaultcolor = DEFAULT_COLOR;
    }
    private String all;

    public String getAll() {
        return all;
    }

    public void setAll(String inp) {
        all = inp;
    }
    private String fatal;

    public String getFatal() {
        return fatal;
    }

    public void setFatal(String inp) {
        fatal = inp;
    }
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String inp) {
        error = inp;
    }
    private String warn;

    public String getWarn() {
        return warn;
    }

    public void setWarn(String inp) {
        warn = inp;
    }
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String inp) {
        info = inp;
    }
    private String debug;

    public String getDebug() {
        return debug;
    }

    public void setDebug(String inp) {
        debug = inp;
    }
    private String stacktrace;

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String inp) {
        stacktrace = inp;
    }
    private String defaultcolor;

    public String getDefaultcolor() {
        return defaultcolor;
    }

    public void setDefaultcolor(String inp) {
        defaultcolor = inp;
    }

    @Override
    public String format(LoggingEvent loggingEvent) {

        StringBuilder oBuffer = new StringBuilder();
        switch (loggingEvent.getLevel().toInt()) {
            case Level.ALL_INT:
                oBuffer.append(all);
                break;
            case Level.FATAL_INT:
                oBuffer.append(fatal);
                break;
            case Level.ERROR_INT:
                oBuffer.append(error);
                break;
            case Level.WARN_INT:
                oBuffer.append(warn);
                break;
            case Level.INFO_INT:
                oBuffer.append(info);
                break;
            case Level.DEBUG_INT:
                oBuffer.append(debug);
                break;
        }
        oBuffer.append(super.format(loggingEvent));
        oBuffer.append(defaultcolor);
        return oBuffer.toString();
    }
}
