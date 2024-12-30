package io.papermc.terraformer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.constants.TerraformItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class Terraformer extends JavaPlugin implements Listener {
    private final Map<UUID, TerraformerProperties> terraformers = new HashMap<>();

    public void setTerraformer(Player player, TerraformerProperties properties) {
        terraformers.put(player.getUniqueId(), properties);
    }

    public void removeTerraformer(Player player) {
        terraformers.remove(player.getUniqueId());
    }

    public TerraformerProperties getTerraformer(Player player) {
        return terraformers.get(player.getUniqueId());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        if (this.getCommand("terraform") != null) {
            this.getCommand("terraform").setExecutor(new TerraformCommand(this));
            getLogger().info("Terraform command registered successfully!");
        }

        getLogger().info("Terraformer plugin has been enabled!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (terraformers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (terraformers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (terraformers.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        TerraformerProperties properties = terraformers.get(player.getUniqueId());

        if (properties == null) {
            return;
        }

        if (event.getItem() != null && event.getItem().getType() == Material.BRUSH) {
            ItemMeta meta = event.getItem().getItemMeta();

            if (meta.customName().equals(TerraformItems.TERRAFORMER_BRUSH)) {
                if (event.getAction().isLeftClick()) {
                    Stack<BlockState> undoStates = properties.History.undo();
                    if (undoStates == null) {
                        player.sendMessage(Component.text(Messages.NOTHING_TO_UNDO)
                                .color(NamedTextColor.RED));
                        return;
                    }
                    undo(undoStates);
                }
                if (event.getAction().isRightClick()) {
                    Block targetBlock = player.getTargetBlock(null, 256);

                    if (targetBlock != null) {
                        Location targetLocation = targetBlock.getLocation();
                        brush(properties, targetLocation, false);
                    }
                }
            }
        }
    }

    public void brush(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        if (properties.Brush == BrushType.BALL) {
            brushBall(properties, targetLocation, isRedo);
        }
    }

    private void brushBall(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        Stack<BlockState> states = new Stack<>();
        int brushSize = properties.BrushSize;

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int y = -brushSize; y <= brushSize; y++) {
                for (int z = -brushSize; z <= brushSize; z++) {
                    Location loc = targetLocation.clone().add(x, y, z);
                    if (loc.distance(targetLocation) <= brushSize) {
                        states.push(
                                new BlockState(loc.clone(), loc.getBlock().getType(), targetLocation, properties.Brush,
                                        properties.BrushSize));
                        loc.getBlock().setType(Material.STONE);
                    }
                }
            }
        }

        if (!isRedo) {
            properties.History.pushModification(states);
        } else {
            properties.History.pushRedo(states);
        }
    }

    public void undo(Stack<BlockState> states) {
        states.forEach(state -> state.location().getBlock().setType(state.material()));
    }
}
