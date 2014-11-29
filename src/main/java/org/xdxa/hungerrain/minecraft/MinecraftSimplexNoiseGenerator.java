package org.xdxa.hungerrain.minecraft;

import java.util.Random;

import org.bukkit.util.noise.SimplexNoiseGenerator;

/**
 * Noise generator whose initialization is consistent with the SimplexNoiseGenerator used in the Minecraft client.
 */
public class MinecraftSimplexNoiseGenerator extends SimplexNoiseGenerator {

    /**
     * Initialize the instance with a {@link Random} instance.
     * @param random the random instance
     */
    public MinecraftSimplexNoiseGenerator(final Random random) {
        // Providing the parent class with an arbitrary Random instance. These values will be overwritten.
        super(new Random());

        // Minecraft's noise generator utilizes the first three values for another purpose. Skipping these.
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();

        // Initialize the perm array with values from 0 to 255, then shuffle it. First half is a duplicate of the second
        // half to avoid handling index wrapping logic.
        for (int i = 0; i < 256; this.perm[i] = i++);
        for (int i = 0; i < 256; ++i) {
            final int tmp = this.perm[i];
            final int iSwap = random.nextInt(256 - i) + i;

            this.perm[i] = this.perm[iSwap];
            this.perm[iSwap] = tmp;
            this.perm[i + 256] = this.perm[i];
        }
    }

    /**
     * Bukkit's {@link SimplexNoiseGenerator} adds an offset to the x/y values. This is not consistent with the behavior
     * on the client side. Correcting it here.
     */
    @Override
    public double noise(final double xin, final double yin) {
        return super.noise(xin - this.offsetX, yin - this.offsetY);
    }
}
