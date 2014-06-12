package com.yofreke.alfred;

import org.newdawn.slick.util.LogSystem;

public class SilentLogSystem implements LogSystem {
   
    @Override
    public void error(String message, Throwable e) {
    }

    @Override
    public void error(Throwable e) {
    }

    @Override
    public void error(String message) {
    }

    @Override
    public void warn(String message) {
    }

    @Override
    public  void info(String message) {
    }

    @Override
    public void debug(String message) {
    }

    @Override
    public void warn(String message, Throwable e) {
    }
}