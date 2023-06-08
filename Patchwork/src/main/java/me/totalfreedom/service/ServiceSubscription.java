package me.totalfreedom.service;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class ServiceSubscription<T extends Service>
{
    private final T service;
    private final boolean async;
    private final Executor executor;
    private final int serviceId;

    private boolean isActive = false;

    ServiceSubscription(@NotNull final JavaPlugin plugin, @NotNull final T service)
    {
        this(plugin, service, 1L, false);
    }

    ServiceSubscription(@NotNull final JavaPlugin plugin, @NotNull final T service, final boolean async)
    {
        this(plugin, service, 1L, async);
    }

    ServiceSubscription(@NotNull final JavaPlugin plugin, @NotNull final T service, final long interval)
    {
        this(plugin, service, interval, false);
    }

    ServiceSubscription(@NotNull final JavaPlugin plugin, @NotNull final T service,
        final long interval, final boolean async)
    {
        this.service = service;
        this.async = async;

        final int[] tempId = new int[1];

        if (async)
        {
            this.executor = r ->
            {
                final BukkitTask task = Bukkit.getScheduler()
                                              .runTaskTimerAsynchronously(plugin, r, 0, interval);
                tempId[0] = task.getTaskId();
            };
        } else
        {
            this.executor = r ->
            {
                final BukkitTask task = Bukkit.getScheduler()
                                              .runTaskTimer(plugin, r, 0, interval);
                tempId[0] = task.getTaskId();
            };
        }

        this.serviceId = tempId[0];
    }

    public void start()
    {
        this.isActive = true;
        this.executor.execute(service::tick);
    }

    public void stop()
    {
        this.isActive = false;
        Bukkit.getScheduler()
              .cancelTask(this.getServiceId());
    }

    public int getServiceId()
    {
        return serviceId;
    }

    @NotNull
    public T getService()
    {
        return service;
    }

    public boolean isAsync()
    {
        return async;
    }

    public boolean isActive()
    {
        return isActive;
    }
}
