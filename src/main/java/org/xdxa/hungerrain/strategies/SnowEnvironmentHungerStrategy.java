package org.xdxa.hungerrain.strategies;

import java.util.logging.Logger;

import org.xdxa.hungerrain.environment.EnvironmentContext;

/**
 * Rules for hunger depletion when standing in the snow.
 */
public class SnowEnvironmentHungerStrategy implements IEnvironmentHungerStrategy {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("Minecraft");

    /**
     * Default depletion rate.
     */
    public static final int DEFAULT_DEPLETION_RATE = 2;

    /**
     * Default light level required.
     */
    public static final int DEFAULT_REQUIRED_LIGHT_LEVEL = 10;


    private final int depletionRate;
    private final int requiredLightLevel;

    /**
     * Initialize the instance.
     * @param depletionRate the depletion rate
     * @param requiredLightLevel the required light level
     */
    public SnowEnvironmentHungerStrategy(final int depletionRate, final int requiredLightLevel) {
        this.depletionRate = depletionRate;
        this.requiredLightLevel = requiredLightLevel;
    }

    @Override
    public int evaluate(final EnvironmentContext environmentContext) {

        if (environmentContext.isSnowing() && environmentContext.isExposed()
         && environmentContext.getLightLevel() < requiredLightLevel) {
            return depletionRate;
        } else {
            return 0;
        }
    }
}
