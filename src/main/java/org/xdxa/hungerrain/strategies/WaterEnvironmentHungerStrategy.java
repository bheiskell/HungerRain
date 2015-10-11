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
     * Default shallow depletion rate.
     */
    public static final int DEFAULT_DEPLETION_RATE = 1;

    /**
     * Default deep depletion rate.
     */
    public static final int DEFAULT_DEEP_DEPLETION_RATE = 1;

    private final int depletionRate;
    private final int deepDepletionRate;

    /**
     * Initialize the instance.
     * @param depletionRate the shallow depletion rate
     * @param deepDepletionRate  the deep water deletion rate
     */
    public WaterEnvironmentHungerStrategy(final int depletionRate, final int deepDepletionRate) {
        this.depletionRate = depletionRate;
        this.deepDepletionRate = deepDepletionRate;
    }

    @Override
    public int evaluate(final EnvironmentContext environmentContext) {

        if (environmentContext.inWater()) {
            return depletionRate;
        }

        if (environmentContext.inDeepWater()) {
            return deepDepletionRate;
        }

        return 0;
    }
}
