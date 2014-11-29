package org.xdxa.hungerrain.environment;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;

/**
 * {@link Player}'s environmental information.
 */
public class EnvironmentContext {

    /**
     * The {@link Biome}'s category.
     */
    private static enum BiomeWeatherPattern {
        /**
         * Biomes which support rain/snow. These biomes will show snow when the temperature is below freezing.
         */
        RAIN_AND_SNOW,

        /**
         * Biomes which do not have weather events.
         */
        DRY,

        /**
         * Unknown recognized Biome type. This class may need to be updated.
         */
        UNDEFINED
    }

    private static final double FREEZING = 0.15;

    private final Biome biome;
    private final boolean isStorming;
    private final float temperature;
    private final byte lightLevel;
    private final boolean isExposed;
    private final boolean inWater;

    /**
     * Initialize the instance.
     * @param biome the biome type
     * @param isStorming is the world currently storming?
     * @param temperature the temperature to evaluate
     * @param inWater is the player in water?
     * @param isExposed is the player exposed to the elements?
     * @param lightLevel the player's light level
     */
    public EnvironmentContext(final Biome biome,
                             final boolean isStorming,
                             final float temperature,
                             final byte lightLevel,
                             final boolean isExposed,
                             final boolean inWater) {
        this.biome = biome;
        this.isStorming = isStorming;
        this.temperature = temperature;
        this.lightLevel = lightLevel;
        this.isExposed = isExposed;
        this.inWater = inWater;
    }

    /**
     * Is it raining?
     * @return true if the client is in a location where it is raining
     */
    public boolean isRaining() {
        return isStorming && BiomeWeatherPattern.RAIN_AND_SNOW.equals(getCategory(biome)) && temperature >= FREEZING;
    }

    /**
     * Is it snowing?
     * @return true if the client is in a location where it is snowing
     */
    public boolean isSnowing() {
        return isStorming && BiomeWeatherPattern.RAIN_AND_SNOW.equals(getCategory(biome)) && temperature < FREEZING;
    }

    /**
     * Get the player's current light level.
     * @return the player's light level
     */
    public byte getLightLevel() {
        return lightLevel;
    }

    /**
     * Is the player exposed to weather?
     * @return true if exposed
     */
    public boolean isExposed() {
        return isExposed;
    }

    /**
     * Is the player in water?
     * @return true if in water
     */
    public boolean inWater() {
        return inWater;
    }

    /**
     * Determine the Biome's weather pattern. These values were determined by reverse engineering the client.
     * @param biome the biome
     * @return the weather pattern
     */
    private static BiomeWeatherPattern getCategory(final Biome biome) {
        switch (biome) {

        // No weather events occur in dry biomes.
        case DESERT:
        case DESERT_HILLS:
        case DESERT_MOUNTAINS:
        case HELL:
        case SKY:
        case SAVANNA:
        case SAVANNA_MOUNTAINS:
        case SAVANNA_PLATEAU:
        case SAVANNA_PLATEAU_MOUNTAINS:
        case MESA:
        case MESA_BRYCE:
        case MESA_PLATEAU:
        case MESA_PLATEAU_FOREST:
        case MESA_PLATEAU_FOREST_MOUNTAINS:
        case MESA_PLATEAU_MOUNTAINS:
            return BiomeWeatherPattern.DRY;

        // Biomes below support both rain and snow.
        case FROZEN_OCEAN:
        case FROZEN_RIVER:
        case ICE_MOUNTAINS:
        case ICE_PLAINS:
        case ICE_PLAINS_SPIKES:
        case COLD_BEACH:
        case COLD_TAIGA:
        case COLD_TAIGA_HILLS:
        case COLD_TAIGA_MOUNTAINS:

        // The biomes below are not flagged by the client with BiomeGenBase#enableSnow, the client actually doesn't
        // require this field to display snow.
        case BEACH:
        case BIRCH_FOREST:
        case BIRCH_FOREST_HILLS:
        case BIRCH_FOREST_HILLS_MOUNTAINS:
        case BIRCH_FOREST_MOUNTAINS:
        case DEEP_OCEAN:
        case EXTREME_HILLS:
        case EXTREME_HILLS_MOUNTAINS:
        case EXTREME_HILLS_PLUS:
        case EXTREME_HILLS_PLUS_MOUNTAINS:
        case FLOWER_FOREST:
        case FOREST:
        case FOREST_HILLS:
        case JUNGLE:
        case JUNGLE_EDGE:
        case JUNGLE_EDGE_MOUNTAINS:
        case JUNGLE_HILLS:
        case JUNGLE_MOUNTAINS:
        case MEGA_SPRUCE_TAIGA:
        case MEGA_SPRUCE_TAIGA_HILLS:
        case MEGA_TAIGA:
        case MEGA_TAIGA_HILLS:
        case MUSHROOM_ISLAND:
        case MUSHROOM_SHORE:
        case OCEAN:
        case PLAINS:
        case RIVER:
        case ROOFED_FOREST:
        case ROOFED_FOREST_MOUNTAINS:
        case SMALL_MOUNTAINS:
        case STONE_BEACH:
        case SUNFLOWER_PLAINS:
        case SWAMPLAND:
        case SWAMPLAND_MOUNTAINS:
        case TAIGA:
        case TAIGA_HILLS:
        case TAIGA_MOUNTAINS:
            return BiomeWeatherPattern.RAIN_AND_SNOW;

        default:
            return BiomeWeatherPattern.UNDEFINED;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(EnvironmentContext.class)
                .add("biome", biome)
                .add("isStorming", isStorming)
                .add("temperature", temperature)
                .add("lightLevel", lightLevel)
                .add("isExposed", isExposed)
                .add("inWater", inWater)
                .add("isRaining", isRaining())
                .add("isSnowing", isSnowing())
                .toString();
    }
}
