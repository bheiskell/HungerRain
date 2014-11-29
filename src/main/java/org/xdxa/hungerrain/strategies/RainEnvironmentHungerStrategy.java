package org.xdxa.hungerrain.strategies;

import java.util.logging.Logger;

import org.xdxa.hungerrain.environment.EnvironmentContext;

/**
 * Rules for hunger depletion when standing in the rain.
 */
public class RainEnvironmentHungerStrategy implements IEnvironmentHungerStrategy {
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
    public RainEnvironmentHungerStrategy(final int depletionRate) {
        this.depletionRate = depletionRate;
    }

    @Override
    public int evaluate(final EnvironmentContext environmentContext) {

        if (environmentContext.isRaining() && environmentContext.isExposed()) {
            return depletionRate;
        } else {
            return 0;
        }
    }
}
