package org.xdxa.hungerrain.command;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * {@link CommandExecutor} for toggling player's debug output.
 */
public class HungerRainCommandExectutor implements CommandExecutor {

    private static final Logger LOG = Logger.getLogger("Minecraft.HungerRain");

    private final Set<String> debuggers;

    /**
     * Initialize the instance.
     * @param debuggers {@link Set} containing the debug enabled player names.
     */
    public HungerRainCommandExectutor(final Set<String> debuggers) {
        this.debuggers = debuggers;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (args.length == 0) {
            LOG.fine("No arguments provided");;
            return false;
        }

        if (!"debug".equalsIgnoreCase(args[0])) {
            LOG.fine("Incorrect command: " + args[0]);;
            return false;
        }

        final String name = sender.getName();

        if (debuggers.contains(name)) {
            debuggers.remove(name);
        } else {
            debuggers.add(name);
        }

        return true;
    }

}
