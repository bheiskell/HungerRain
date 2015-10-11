package org.xdxa.hungerrain;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xdxa.hungerrain.environment.EnvironmentContextFactory;
import org.xdxa.hungerrain.strategies.IEnvironmentHungerStrategy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Test {@link HungerRainTask}.
 */
public class HungerRainTaskTest {

    @Mock EnvironmentContextFactory contextFactory;
    @Mock IEnvironmentHungerStrategy strategy;
    @Mock Server server;
    @Mock Player player;

    private Set<String> debuggers;
    private HungerRainTask task;

    /**
     * Setup the server and insert our mocks.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        debuggers = Sets.newHashSet("test-player");

        task = new HungerRainTask(
                server,
                Lists.newArrayList(strategy),
                1,
                true,
                true,
                debuggers,
                contextFactory);

        // inverted syntax to circumvent generics compiler ambiguity
        doReturn(Lists.newArrayList(player)).when(server).getOnlinePlayers();

        when(player.getName()).thenReturn("test-player");
    }

    /**
     * Test that the disable permission works correctly.
     */
    @Test
    public void testDisablePermission() {
        when(player.hasPermission("hungerrain.disable")).thenReturn(true);

        task.run();

        verify(player).sendMessage("Disabled for player");
        verify(player, never()).setFoodLevel(anyInt());
    }

}
