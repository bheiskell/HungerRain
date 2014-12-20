package org.xdxa.hungerrain.environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test the Bukkit interface used to construct the {@link EnvironmentContext}.
 *
 * Caution: This logic may not actually align with Bukkit's actual implementation.
 */
public class EnvironmentContextFactoryTest {

    @Mock private Location playerLocation;
    @Mock private Block playerBlock;
    @Mock private World world;

    private EnvironmentContextFactory ecf;
    private int playerLocationY;

    /**
     * Setup partially mocked {@link World}.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ecf = new EnvironmentContextFactory();

        when(playerLocation.getBlock()).thenReturn(playerBlock);
        when(playerLocation.getWorld()).thenReturn(world);
        when(playerLocation.clone()).thenReturn(playerLocation);

        // Add stubbing to track expected changes to the y values
        when(playerLocation.add(0, 1, 0)).thenAnswer(new Answer<Location>() {
            @Override
            public Location answer(final InvocationOnMock invocation) throws Throwable {
                playerLocationY += 1;
                return null;
            }
        });
        when(playerLocation.getBlockY()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(final InvocationOnMock invocation) throws Throwable {
                return playerLocationY;
            }
        });
    }

    /**
     * Test no exposure.
     */
    @Test
    public void testIsntExposed() {
        when(playerBlock.isEmpty()).thenReturn(true, true, false);

        final EnvironmentContext ec = ecf.create(playerLocation);

        // called twice for the isEmpty == true and once to skip lower body
        verify(playerLocation, times(3)).add(0, 1, 0);

        assertFalse("Player shouldn't be unexposed", ec.isExposed());
    }

    /**
     * Test exposure.
     */
    @Test
    public void testIsExposed() {
        // we're going to set the player's torso to max height, which will skip the isEmpty() loop
        when(playerBlock.isEmpty()).thenReturn(false); // at max height, isEmpty returns false
        when(world.getMaxHeight()).thenReturn(256);
        playerLocationY = 255; // setting to one block below max height, to check the torso

        final EnvironmentContext ec = ecf.create(playerLocation);

        // should only get called to skip the player's lower body
        verify(playerLocation, times(1)).add(0, 1, 0);

        assertTrue("Player should be unexposed", ec.isExposed());
    }
}
