package org.xdxa.hungerrain.environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.block.Biome;
import org.junit.Test;
import org.xdxa.hungerrain.environment.EnvironmentContext;

/**
 * Test {@link EnvironmentContext}.
 */
@SuppressWarnings("javadoc")
public class EnvironmentContextTest {

    EnvironmentContext environmentContext;

    @Test
    public void testDesert() {
        environmentContext = new EnvironmentContext(Biome.DESERT, true, 0, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.DESERT, false, 0, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.DESERT, true, 0.15F, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.DESERT, false, 0.15F, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());
    }

    @Test
    public void testPlains() {
        environmentContext = new EnvironmentContext(Biome.PLAINS, true, 0, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertTrue(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.PLAINS, false, 0, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.PLAINS, true, 0.15F, (byte)0, false, false);
        assertTrue(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());

        environmentContext = new EnvironmentContext(Biome.PLAINS, false, 0.15F, (byte)0, false, false);
        assertFalse(environmentContext.isRaining());
        assertFalse(environmentContext.isSnowing());
    }
}
