package me.totalfreedom.service;

public interface FreedomService
{
    void start();

    void stop();

    boolean isRunning();

    String getName();

    boolean isRepeating();

    void setRepeating(long interval);

    boolean isDelayed();

    void setDelayed(long delay);

    long getInterval();

    long getDelay();
}
