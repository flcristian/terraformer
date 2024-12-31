package io.papermc.terraformer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;

import io.papermc.terraformer.constants.Messages;
import io.papermc.terraformer.constants.TerraformItems;
import io.papermc.terraformer.terraformer_properties.TerraformerProperties;
import io.papermc.terraformer.terraformer_properties.block_history.BlockStateHistory;
import io.papermc.terraformer.terraformer_properties.block_history.BrushAction;
import io.papermc.terraformer.terraformer_properties.properties.BrushType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class Terraformer extends JavaPlugin implements Listener {
    private final Map<UUID, TerraformerProperties> terraformers = new HashMap<>();

    public void setTerraformer(Player player) {
        TerraformerProperties currentProperties = terraformers.get(player.getUniqueId());
        setTerraformerInventory(player);

        if (currentProperties == null) {
            terraformers.put(player.getUniqueId(), new TerraformerProperties());
            return;
        }

        terraformers.put(player.getUniqueId(),
                new TerraformerProperties(currentProperties.Brush, currentProperties.BrushSize,
                        currentProperties.History, currentProperties.Palette, true));
    }

    public void removeTerraformer(Player player) {
        TerraformerProperties properties = terraformers.get(player.getUniqueId());
        player.getInventory().clear();

        if (properties == null) {
            return;
        }

        terraformers.put(player.getUniqueId(), new TerraformerProperties(properties.Brush, properties.BrushSize,
                properties.History, properties.Palette, false));
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
    public void onInventoryInteract(InventoryInteractEvent event) {
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
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (terraformers.containsKey(player.getUniqueId()) && event.getMessage().toLowerCase().startsWith("/clear")) {
            setTerraformerInventory(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (terraformers.containsKey(player.getUniqueId())) {
            ItemMeta meta = event.getItemInHand().getItemMeta();
            Component[] terraformerItems = new Component[] {
                    TerraformItems.TERRAFORMER_BRUSH,
                    TerraformItems.TERRAFORMER_UNDO,
                    TerraformItems.TERRAFORMER_REDO,
                    TerraformItems.TERRAFORMER_LEAVE
            };

            for (Component item : terraformerItems) {
                if (meta.customName().equals(item)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        TerraformerProperties properties = terraformers.get(player.getUniqueId());

        if (properties == null || properties.IsTerraformer == false) {
            return;
        }

        if (event.getItem() != null) {
            ItemMeta meta = event.getItem().getItemMeta();

            if (meta.customName().equals(TerraformItems.TERRAFORMER_BRUSH)) {
                if (event.getAction().isRightClick()) {
                    Block targetBlock = player.getTargetBlock(null, 256);

                    if (targetBlock != null) {
                        Location targetLocation = targetBlock.getLocation();
                        brush(properties, targetLocation, false);
                    }
                    event.setCancelled(true);
                }
            }

            if (meta.customName().equals(TerraformItems.TERRAFORMER_UNDO)) {
                if (event.getAction().isRightClick()) {
                    Stack<BlockStateHistory> undoStates = properties.History.undo();
                    if (undoStates == null) {
                        player.sendMessage(Messages.NOTHING_TO_UNDO);
                        return;
                    }
                    undo(undoStates);
                    player.sendMessage(Messages.UNDO_SUCCESSFUL);
                    event.setCancelled(true);
                }
            }

            if (meta.customName().equals(TerraformItems.TERRAFORMER_REDO)) {
                if (event.getAction().isRightClick()) {
                    BrushAction redoAction = properties.History.redo();
                    if (redoAction == null) {
                        player.sendMessage(Messages.NOTHING_TO_REDO);
                        return;
                    }
                    brush(properties, redoAction.targetLocation(), true);
                    player.sendMessage(Messages.REDO_SUCCESSFUL);
                    event.setCancelled(true);
                }
            }

            if (meta.customName().equals(TerraformItems.TERRAFORMER_LEAVE)) {
                if (event.getAction().isRightClick()) {
                    removeTerraformer(player);
                    player.sendMessage(Messages.STOP_TERRAFORM);
                }
            }
        }
    }

    public void setTerraformerInventory(Player player) {
        player.getInventory().clear();

        // Brush Item
        ItemStack brush = new ItemStack(Material.BRUSH);
        ItemMeta brushMeta = brush.getItemMeta();
        brushMeta.customName(TerraformItems.TERRAFORMER_BRUSH);
        brush.setItemMeta(brushMeta);
        player.getInventory().setItem(4, brush);

        // Undo Item
        ItemStack undo = new ItemStack(Material.AMETHYST_BLOCK);
        ItemMeta undoMeta = undo.getItemMeta();
        undoMeta.customName(TerraformItems.TERRAFORMER_UNDO);
        undo.setItemMeta(undoMeta);
        player.getInventory().setItem(3, undo);

        // Redo Item
        ItemStack redo = new ItemStack(Material.PRISMARINE);
        ItemMeta redoMeta = redo.getItemMeta();
        redoMeta.customName(TerraformItems.TERRAFORMER_REDO);
        redo.setItemMeta(redoMeta);
        player.getInventory().setItem(5, redo);

        // Leave Terraform Mode Item
        ItemStack leave = new ItemStack(Material.BARRIER);
        ItemMeta leaveMeta = leave.getItemMeta();
        leaveMeta.customName(TerraformItems.TERRAFORMER_LEAVE);
        leave.setItemMeta(leaveMeta);
        player.getInventory().setItem(0, leave);
    }

    public void brush(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        if (properties.Brush == BrushType.BALL) {
            brushBall(properties, targetLocation, isRedo);
        }
    }

    private void brushBall(TerraformerProperties properties, Location targetLocation, boolean isRedo) {
        Stack<BlockStateHistory> states = new Stack<>();
        int brushSize = properties.BrushSize;

        for (int x = -brushSize; x <= brushSize; x++) {
            for (int y = -brushSize; y <= brushSize; y++) {
                for (int z = -brushSize; z <= brushSize; z++) {
                    Location loc = targetLocation.clone().add(x, y, z);
                    if (loc.distance(targetLocation) <= brushSize) {
                        Block block = loc.getBlock();
                        BlockState blockState = block.getState();
                        states.push(
                                new BlockStateHistory(loc.clone(), blockState, targetLocation,
                                        properties.Brush, brushSize));
                    }
                }
            }
        }

        if (!isRedo) {
            properties.History.pushModification(states);
        } else {
            properties.History.pushRedo(states);
        }

        for (BlockStateHistory state : states) {
            Block block = state.location().getBlock();
            if (!block.getType().isSolid()) {
                block.setType(Material.STONE);
            }
        }

        for (BlockStateHistory state : states) {
            Block block = state.location().getBlock();
            if (block.getType().isSolid()) {
                block.setType(Material.STONE);

            }
        }
    }

    public void undo(Stack<BlockStateHistory> states) {
        for (BlockStateHistory state : states) {
            state.blockState().update(true);
        }
    }

    public void openBrushSettings(Player player, TerraformerProperties properties) {
        BrushSettings settings = new BrushSettings(this, properties.Brush, properties.BrushSize);

        player.openInventory(settings.getInventory());
    }
}
