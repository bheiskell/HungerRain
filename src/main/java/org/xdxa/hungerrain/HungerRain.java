
package org.xdxa.hungerrain;

import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.xdxa.hungerrain.command.HungerRainCommandExectutor;
import org.xdxa.hungerrain.environment.EnvironmentContextFactory;
import org.xdxa.hungerrain.strategies.IEnvironmentHungerStrategy;
import org.xdxa.hungerrain.strategies.RainEnvironmentHungerStrategy;
import org.xdxa.hungerrain.strategies.SnowEnvironmentHungerStrategy;
import org.xdxa.hungerrain.strategies.WaterEnvironmentHungerStrategy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Hunger Rain Bukkit plugin.
 */
public class HungerRain extends JavaPlugin {
    private static final String COMMENT =
"HungerRain has the following settings:\n" +
"  frequency:              the number of server ticks per-execution of each of the hunger rules\n" +
"  armor-damage-frequency: number of times a leather armor set can prevent damage before its durability decreases\n" +
"  *-depletion-rate:       rate at which hunger will deplete per execution of the rules\n\n" +
"Additionally, the snow rule allows the player to travel within a certain light threshold to stay warm.";

    private static final String KEY_FREQUENCY                 = "frequency";
    private static final String KEY_ARMOR_DAMAGE_FREQUENCY    = "armor-damage-frequency";
    private static final String KEY_RAIN_DEPLETION_RATE       = "rain-depletion-rate";
    private static final String KEY_WATER_DEPLETION_RATE      = "water-depletion-rate";
    private static final String KEY_SNOW_DEPLETION_RATE       = "snow-depletion-rate";
    private static final String KEY_SNOW_REQUIRED_LIGHT_LEVEL = "snow-required-light-level";

    private static final long DEFAULT_FREQUENCY              = 100; // 5 seconds
    private static final long DEFAULT_ARMOR_DAMAGE_FREQUENCY = 5;   // 100 * 5 = 25 seconds

    private static final Set<String> debuggers = Sets.newHashSet();

    @Override
    public void onDisable() {
        getLogger().info("Disabling HungerRain");
    }

    @Override
    public void onEnable() {
        getLogger().info("Enabling HungerRain");

        final PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

        final FileConfiguration config = getConfig();
        config.options().header(COMMENT);
        config.options().copyHeader(true);
        config.options().copyDefaults(true);

        config.addDefault(KEY_FREQUENCY,                 DEFAULT_FREQUENCY);
        config.addDefault(KEY_ARMOR_DAMAGE_FREQUENCY,    DEFAULT_ARMOR_DAMAGE_FREQUENCY);
        config.addDefault(KEY_RAIN_DEPLETION_RATE,       RainEnvironmentHungerStrategy.DEFAULT_DEPLETION_RATE);
        config.addDefault(KEY_WATER_DEPLETION_RATE,      WaterEnvironmentHungerStrategy.DEFAULT_DEPLETION_RATE);
        config.addDefault(KEY_SNOW_DEPLETION_RATE,       SnowEnvironmentHungerStrategy.DEFAULT_DEPLETION_RATE);
        config.addDefault(KEY_SNOW_REQUIRED_LIGHT_LEVEL, SnowEnvironmentHungerStrategy.DEFAULT_REQUIRED_LIGHT_LEVEL);

        final long delay                = config.getInt(KEY_FREQUENCY);
        final long armorDamageFrequency = config.getInt(KEY_ARMOR_DAMAGE_FREQUENCY);
        final int rainDepletionRate     = config.getInt(KEY_RAIN_DEPLETION_RATE);
        final int waterDepletionRate    = config.getInt(KEY_WATER_DEPLETION_RATE);
        final int snowDepletionRate     = config.getInt(KEY_SNOW_DEPLETION_RATE);
        final int snowRequiredLight     = config.getInt(KEY_SNOW_REQUIRED_LIGHT_LEVEL);

        saveConfig();

        final List<IEnvironmentHungerStrategy> strategies = Lists.newArrayList(
            new RainEnvironmentHungerStrategy(rainDepletionRate),
            new SnowEnvironmentHungerStrategy(snowDepletionRate, snowRequiredLight),
            new WaterEnvironmentHungerStrategy(waterDepletionRate)
        );

        final EnvironmentContextFactory environmentContextFactory = new EnvironmentContextFactory();

        final HungerRainTask hungerRainTask = new HungerRainTask(
                this.getServer(),
                strategies,
                armorDamageFrequency,
                debuggers,
                environmentContextFactory);
        hungerRainTask.runTaskTimer(this, delay, delay);

        this.getCommand("hr").setExecutor(new HungerRainCommandExectutor(debuggers));
    }
}
