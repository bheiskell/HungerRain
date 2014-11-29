/**
 *
 */
package org.xdxa.hungerrain.strategies;

import org.xdxa.hungerrain.environment.EnvironmentContext;


/**
 * Strategy for evaluating environmental conditions and dealing hunger damage accordingly.
 */
public interface IEnvironmentHungerStrategy {

    /**
     * Evaluate the provided environmental information and return a quantity of hunger damage to deal to the player.
     * @param environmentContext Hunger Rain specific player environmental information
     * @return the quantity of damage to deal to the player's food level
     */
    public int evaluate(final EnvironmentContext environmentContext);
}
