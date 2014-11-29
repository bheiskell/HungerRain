package org.xdxa.hungerrain.environment;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.xdxa.hungerrain.minecraft.MinecraftSimplexNoiseGenerator;

/**
 * Factory for the {@link EnvironmentContext} which abstracts much of Bukkit and a few hackish behaviors of Minecraft.
 */
public class EnvironmentContextFactory {

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
        final Block block = world.getBlockAt(location);

        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();

        final float biomeTemperature = (float)world.getTemperature(x, z);
        final float clientTemperature = getTemperatureNoise(biomeTemperature, x, y + FANCY_GRAPHICS_OFFSET, z);

        final byte lightLevel = block.getLightLevel(); // 0-15
        final boolean isExposed = world.getHighestBlockAt(location).getY() == y;
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
