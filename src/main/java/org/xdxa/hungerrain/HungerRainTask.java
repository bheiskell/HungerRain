package org.xdxa.hungerrain;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.xdxa.hungerrain.environment.EnvironmentContext;
import org.xdxa.hungerrain.environment.EnvironmentContextFactory;
import org.xdxa.hungerrain.strategies.IEnvironmentHungerStrategy;

import com.google.common.collect.Lists;

/**
 * Task which executes configured {@link IEnvironmentHungerStrategy}s on each {@link Player}.
 */
public class HungerRainTask extends BukkitRunnable {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger("Minecraft.HungerRain");

    private static final int TOTAL_ARMOR_SLOTS = 4;
    private static final List<Material> LEATHER_ARMOR = Lists.newArrayList(
            Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_HELMET);

    private final Server server;
    private final List<IEnvironmentHungerStrategy> strategies;
    private final long armorDamageFrequency;
    private final Set<String> debuggers;
    private final EnvironmentContextFactory environmentContextFactory;

    private long armorIterations = 0;

    /**
     * Initialize the instance.
     * @param server the Bukkit server
     * @param strategies the strategies to execute
     * @param armorDamageFrequency the number of times {@link #run()} is executed before an armor set takes damage
     * @param debuggers players which receieve debug output
     * @param environmentContextFactory the environment context factory
     */
    public HungerRainTask(final Server server,
                          final List<IEnvironmentHungerStrategy> strategies,
                          final long armorDamageFrequency,
                          final Set<String> debuggers,
                          final EnvironmentContextFactory environmentContextFactory) {
        this.server = server;
        this.strategies = strategies;
        this.armorDamageFrequency = armorDamageFrequency;
        this.debuggers = debuggers;
        this.environmentContextFactory = environmentContextFactory;
    }

    @Override
    public void run() {
        executeStrategies(server.getOnlinePlayers());
    }

    private void executeStrategies(final Player[] players) {

        for (final Player player : players) {
            final int initialFoodLevel = player.getFoodLevel();

            if (GameMode.CREATIVE.equals(player.getGameMode())) {
                sendMessage(player, "Skipping creative player");
                continue;
            }

            if (initialFoodLevel == 0) {
                sendMessage(player, "Skipping empty stomache");
                continue;
            }

            int foodDamage = 0;
            final EnvironmentContext environmentContext = environmentContextFactory.create(player.getLocation());
            for (final IEnvironmentHungerStrategy strategy : strategies) {
                foodDamage += strategy.evaluate(environmentContext);
            }

            foodDamage = protectWithArmor(foodDamage, player);

            int foodLevel = initialFoodLevel - foodDamage;

            sendMessage(player, environmentContext);

            if (initialFoodLevel != foodLevel) {
                sendMessage(player, "Food decreased: ", initialFoodLevel, " -> ", foodLevel);
            }

            if (foodLevel < 0) {
                foodLevel = 0;
            }

            player.setFoodLevel(foodLevel);
        }
    }

    /**
     * If the player is wearing leather armor, prevent the hunger damage and instead damage the armor.
     * TODO: Find a better abstraction for this logic.
     * @param foodDamage the food damage to be delivered
     * @param player the player
     * @return the food damage to be delivered (either the input or zero depending upon equipment)
     */
    private int protectWithArmor(final int foodDamage, final Player player) {
        final EntityEquipment entityEquipment = player.getEquipment();
        final ItemStack[] armorContents = entityEquipment.getArmorContents();

        // We don't want to inflict armor damage every time.
        armorIterations = (armorIterations + 1) % armorDamageFrequency;

        int armorCount = 0;
        for (final ItemStack armor : armorContents) {
            if (LEATHER_ARMOR.contains(armor.getType())) {
                armorCount++;
            }
        }

        if (armorCount == TOTAL_ARMOR_SLOTS) {

            if (armorIterations != 0) {
                sendMessage(player, "Armor damage in ", armorDamageFrequency - armorIterations);
                return 0;
            }

            sendMessage(player, "Armor prevented hunger damage, durability damage dealt: ", foodDamage);

            for (int i = 0; i < armorContents.length; i++) { // need the index to remove destroyed armor

                final ItemStack armor = armorContents[i];
                if (LEATHER_ARMOR.contains(armor.getType())) {

                    final short durability = (short)(armor.getDurability() + foodDamage);
                    final short maxDurability = armor.getType().getMaxDurability();

                    if (durability < maxDurability) {
                        armor.setDurability(durability);
                        sendMessage(player, armor.getType(), " durability: ", durability, " / ", maxDurability);
                    } else {
                        armorContents[i] = null;
                    }
                }
            }
            // armorContents is a copy, which needs to be re-set on the entityEquipment
            entityEquipment.setArmorContents(armorContents);

            return 0;
        }

        return foodDamage;
    }

    private void sendMessage(final Player player, final Object ... messages) {
        if (debuggers.contains(player.getName())) {
            final StringBuffer sb = new StringBuffer();
            for (final Object message : messages) {
                sb.append(message);
            }

            player.sendMessage(sb.toString());
        }
    }
}
