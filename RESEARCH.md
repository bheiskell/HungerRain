# Research

This document outlines how the server side weather events are interpreted on the Minecraft client.
Use this document as a starting point if HungerRain stops working due to a new release of the Minecraft client.

## Reverse Engineering

"Mod Coder Pack 9.08 for Minecraft 1.7.10" was used to reverse engineer the client.
See their documentation for usage details.

## Analysis

`EntityRender#renderRainSnow`
- This method defines the process for rendering rain/snow.
- Method Overview
    1. Is `BiomeGenBase#canSpawnLightningBolt()` or `BiomeGenBase#canEnableSnow()` true?
        - This short-circuits dry biomes.
        - Snowing is exclusively determined by temperature, not `BiomeGenBase#canEnableSnow()`
    1. Get `World#getPrecipitationHeight(x, y)`
    1. If above this height, either rain or snow will occur
    1. If `Biome.getFloatTemperature(x, y - rainRange, z) >= 0.15`: rain else snow

- Fancy Graphics will change whether the client perceives rain/snow
    - If `GameSettings#fancyGraphics` is on, `rainRange` is defined as `10`. Otherwise it's set to `5`.
    - The `y` value passed into `BiomeGenBase#getFloatTemperature(x, y, z)` is defined as the player's `y` - `rainRange`
    - This will cause the temperature selected to vary depending upon that setting.


`World#getPrecipitationHeight(x, y)`
- Determines the highest point where rain or snow can fall without getting blocked
- Calls `Chunk.getPrecipitationHeight(x & 15, z & 15)`
    1. Starting at the highest block, traverse down until `Block#blocksMovement()` or `Block#isLiquid()` 
    1. Return the `y` height above the block

`BiomeGenBase#getFloatTemperature(x, y, z)`
- Obtains the temperature at a particular location
    1. At y > 64, return the Biome temperature modified +- 4 degrees with deterministic random noise
    1. At y <= 64, return the Biome temperature, which is equivalent to Bukkit's `World#getTemperature(x, z)`

`WorldChunkManager#getTemperatureAtHeight(temperature, precipitationHeight)`
- Documented as adjusting the temperature based upon the precipitationHeight, but is currently implemented as a no-op
  returning the passed in `temperature`.

