package org.xdxa.hungerrain.environment;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.xdxa.hungerrain.minecraft.MinecraftSimplexNoiseGenerator;

/**
 * Factory for the {@link EnvironmentContext} which abstracts much of Bukkit and a few hackish behaviors of Minecraft.
 */
public class EnvironmentContextFactory {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("Minecraft.HungerRain");

    /**
     * When Fancy Graphics is enabled, the block selected for temperature evaluation is 10 blocks beneath the player.
     * Their head is one block above that location.
     */
    private static final int FANCY_GRAPHICS_OFFSET = -10 + 1;

    private final MinecraftSimplexNoiseGenerator noiseGenerator;

    /**
     * Initialize the instance with the expected Random seed.
     */
    public EnvironmentContextFactory() {
        this(new MinecraftSimplexNoiseGenerator(new Random(1234L)));
    }

    /**
     * Initialize the instance with a {@link MinecraftSimplexNoiseGenerator}.
     * @param noiseGenerator the noise generator
     */
    public EnvironmentContextFactory(final MinecraftSimplexNoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    /**
     * Create an instance of {@link EnvironmentContext}.
     * @param location the player location
     * @return the instance
     */
    public EnvironmentContext create(final Location location) {
        final World world = location.getWorld();
        final Block block = location.getBlock();

        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();

        final float biomeTemperature = (float)world.getTemperature(x, z);
        final float clientTemperature = getTemperatureNoise(biomeTemperature, x, y + FANCY_GRAPHICS_OFFSET, z);

        final byte lightLevel = block.getLightLevel(); // 0-15
        final boolean isExposed = isExposed(location);
        final boolean inWater = location.getBlock().isLiquid();

        return new EnvironmentContext(
                block.getBiome(),
                world.hasStorm(),
                clientTemperature,
                lightLevel,
                isExposed,
                inWater);
    }

    /**
     * Is the player exposed to the elements?
     *
     * This method doesn't use {@link World#getHighestBlockAt(Location)}, because it returns the highest light
     * restricting block, which means it skips glass.
     *
     * @param location the player location
     * @return true if the player is exposed
     */
    private boolean isExposed(final Location location) {
        final Location current = location.clone();

        // Skip the player's lower body, because snow and other non-blocking objects will register as not empty. In this
        // case we're not concerned about non-blocking objects at the torso level, e.g., tall grass.
        current.add(0, 1, 0);

        while (current.getBlock().isEmpty() && current.getBlockY() < location.getWorld().getMaxHeight()) {
            current.add(0, 1, 0);
        }

        return current.getBlockY() == location.getWorld().getMaxHeight();
    }

    /**
     * Get the temperature at a given location within a Biome utilizing the biome's base temperature and a noise generator.
     * @param biomeTemperature the biome's temperature
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the temperature
     */
    public final float getTemperatureNoise(final float biomeTemperature, final int x, final int y, final int z) {
        if (y > 64) {
            final float noise = (float)noiseGenerator.noise(x * 1.0D / 8.0D, z * 1.0D / 8.0D) * 4.0F;
            return biomeTemperature - (noise + y - 64.0F) * 0.05F / 30.0F;
        } else {
            return biomeTemperature;
        }
    }
}
