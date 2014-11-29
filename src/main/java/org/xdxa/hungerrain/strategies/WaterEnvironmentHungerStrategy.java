package org.xdxa.hungerrain.strategies;

import java.util.logging.Logger;

import org.xdxa.hungerrain.environment.EnvironmentContext;

/**
 * Rules for hunger depletion when swimming in water.
 */
public class WaterEnvironmentHungerStrategy implements IEnvironmentHungerStrategy {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("Minecraft");

    /**
     * Default depletion rate.
     */
    public static final int DEFAULT_DEPLETION_RATE = 1;

    private final int depletionRate;

    /**
     * Initialize the instance.
     * @param depletionRate the depletion rate
     */
    public WaterEnvironmentHungerStrategy(final int depletionRate) {
        this.depletionRate = depletionRate;
    }

    @Override
    public int evaluate(final EnvironmentContext environmentContext) {

        if (environmentContext.inWater()) {
            return depletionRate;
        } else {
            return 0;
        }
    }
}
