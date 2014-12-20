package org.xdxa.hungerrain.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.bukkit.entity.Player;
import org.junit.Test;
import org.mockito.Mockito;
import org.xdxa.hungerrain.command.HungerRainCommandExectutor;

import com.google.common.collect.Sets;

/**
 * Test the debug command.
 */
public class HungerRainCommandExectutorTest {

    /**
     * Test the debug command.
     */
    @Test
    public void testDebug() {

        final Set<String> debuggers = Sets.newHashSet();

        final HungerRainCommandExectutor debug = new HungerRainCommandExectutor(debuggers);

        final String[] args = new String[] { "debug" };
        final Player player = Mockito.mock(Player.class);

        Mockito.when(player.getName()).thenReturn("PLAYER");

        assertTrue("Debug command should return true", debug.onCommand(player, null, null, args));

        assertTrue("Debuggers should contain player", debuggers.contains("PLAYER"));

        assertTrue("Debug command should return true", debug.onCommand(player, null, null, args));

        assertFalse("Debuggers shouldn't contain player", debuggers.contains("PLAYER"));
    }

    /**
     * Test bad input for the debug command.
     */
    @Test
    public void testDebugInputHandling() {
        final HungerRainCommandExectutor debug = new HungerRainCommandExectutor(null);

        assertFalse("Wrong command should return false", debug.onCommand(null, null, null, new String[] { "invalid" }));

        assertFalse("Empty command should return false", debug.onCommand(null, null, null, new String[] {  }));
    }
}
