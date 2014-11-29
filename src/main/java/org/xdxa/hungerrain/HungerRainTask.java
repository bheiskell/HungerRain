package org.xdxa.hungerrain;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.xdxa.hungerrain.environment.EnvironmentContext;
import org.xdxa.hungerrain.environment.EnvironmentContextFactory;
import org.xdxa.hungerrain.strategies.IEnvironmentHungerStrategy;

/**
 * Task which executes configured {@link IEnvironmentHungerStrategy}s on each {@link Player}.
 */
public class HungerRainTask extends BukkitRunnable {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("Minecraft");

    private final Server server;
    private final List<IEnvironmentHungerStrategy> strategies;
    private final EnvironmentContextFactory environmentContextFactory;

    /**
     * Initialize the instance.
     * @param server the Bukkit server
     * @param strategies the strategies to execute
     * @param environmentContextFactory the environment context factory
     */
    public HungerRainTask(final Server server,
                          final List<IEnvironmentHungerStrategy> strategies,
                          final EnvironmentContextFactory environmentContextFactory) {
        this.server = server;
        this.strategies = strategies;
        this.environmentContextFactory = environmentContextFactory;
    }

    @Override
    public void run() {
        executeStrategies(server.getOnlinePlayers());
    }

    private void executeStrategies(final Player[] players) {

        for (final Player player : players) {
            int foodLevel = player.getFoodLevel();

            // no point in wasting cycles on empty stomaches
            if (foodLevel == 0) {
                continue;
            }

            final EnvironmentContext environmentContext = environmentContextFactory.create(player.getLocation());
            for (final IEnvironmentHungerStrategy strategy : strategies) {
                foodLevel -= strategy.evaluate(environmentContext);
            }

            //player.sendMessage(environmentContext.toString());

            if (foodLevel < 0) {
                foodLevel = 0;
            }

            player.setFoodLevel(foodLevel);
        }
    }

}
