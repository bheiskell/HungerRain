package org.xdxa.hungerrain;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.GameMode;
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
    private static final Logger LOG = Logger.getLogger("Minecraft.HungerRain");

    private final Server server;
    private final List<IEnvironmentHungerStrategy> strategies;
    private final Set<String> debuggers;
    private final EnvironmentContextFactory environmentContextFactory;

    /**
     * Initialize the instance.
     * @param server the Bukkit server
     * @param strategies the strategies to execute
     * @param debuggers players which receieve debug output
     * @param environmentContextFactory the environment context factory
     */
    public HungerRainTask(final Server server,
                          final List<IEnvironmentHungerStrategy> strategies,
                          final Set<String> debuggers,
                          final EnvironmentContextFactory environmentContextFactory) {
        this.server = server;
        this.strategies = strategies;
        this.debuggers = debuggers;
        this.environmentContextFactory = environmentContextFactory;
    }

    @Override
    public void run() {
        executeStrategies(server.getOnlinePlayers());
    }

    private void executeStrategies(final Player[] players) {

        for (final Player player : players) {
            final int initialFoodLevel = player.getFoodLevel();
            int foodLevel = initialFoodLevel;

            if (GameMode.CREATIVE.equals(player.getGameMode())) {
                sendMessage(player, "Skipping creative player");
                continue;
            }

            if (foodLevel == 0) {
                sendMessage(player, "Skipping empty stomache");
                continue;
            }

            final EnvironmentContext environmentContext = environmentContextFactory.create(player.getLocation());
            for (final IEnvironmentHungerStrategy strategy : strategies) {
                foodLevel -= strategy.evaluate(environmentContext);
            }

            sendMessage(player, environmentContext);

            if (initialFoodLevel != foodLevel) {
                sendMessage(player, "Food decreased: ", initialFoodLevel, " -> ", foodLevel);
            }

            if (foodLevel < 0) {
                foodLevel = 0;
            }

            player.setFoodLevel(foodLevel);
        }
    }

    private void sendMessage(final Player player, final Object ... messages) {
        if (debuggers.contains(player.getName())) {
            final StringBuffer sb = new StringBuffer();
            for (final Object message : messages) {
                sb.append(message);
            }

            player.sendMessage(sb.toString());
        }
    }
}
