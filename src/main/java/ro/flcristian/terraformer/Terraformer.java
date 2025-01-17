package ro.flcristian.terraformer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import ro.flcristian.terraformer.constants.Messages;
import ro.flcristian.terraformer.constants.TerraformItems;
import ro.flcristian.terraformer.terraformer_properties.TerraformerProperties;
import ro.flcristian.terraformer.terraformer_properties.block_history.BlockHistoryStates;
import ro.flcristian.terraformer.terraformer_properties.block_history.BrushAction;
import ro.flcristian.terraformer.terraformer_properties.properties.menus.BrushSettings;
import ro.flcristian.terraformer.terraformer_properties.properties.menus.MaterialSettings;
import ro.flcristian.terraformer.terraformer_properties.properties.menus.SelectPaintMode;
import ro.flcristian.terraformer.utility.SkullTexturesApplier;

import java.io.File;
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
                new TerraformerProperties(true, currentProperties.Brush,
                        currentProperties.BrushHistory, currentProperties.MaterialHistory, currentProperties.History,
                        currentProperties.Palette));
    }

    public void removeTerraformer(Player player) {
        TerraformerProperties properties = terraformers.get(player.getUniqueId());
        player.getInventory().clear();

        if (properties == null) {
            return;
        }

        terraformers.put(player.getUniqueId(),
                new TerraformerProperties(false, properties.Brush, properties.BrushHistory, properties.MaterialHistory,
                        properties.History, properties.Palette));
    }

    public TerraformerProperties getTerraformer(Player player) {
        return terraformers.get(player.getUniqueId());
    }

    @Override
    public void onEnable() {
        File pluginFolder = getDataFolder();
        File schematicsFolder = new File(pluginFolder, "Schematics");

        if (!pluginFolder.exists()) {
            if (pluginFolder.mkdirs()) {
                getLogger().info("Created Terraformer directory!");
            } else {
                getLogger().severe("Failed to create Terraformer directory!");
            }
        }

        if (!schematicsFolder.exists()) {
            if (schematicsFolder.mkdirs()) {
                getLogger().info("Created Schematics directory!");
            } else {
                getLogger().severe("Failed to create Schematics directory!");
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this);

        if (this.getCommand("terraform") != null) {
            this.getCommand("terraform").setExecutor(new TerraformCommand(this));
            getLogger().info("Terraform command registered successfully!");
        }
        getCommand("terraform").setTabCompleter(new TerraformTabCompleter(this));
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
        if (terraformers.containsKey(player.getUniqueId()) && event.getMessage().toLowerCase().startsWith("/clear")
                && event.getMessage().length() == 6) {
            setTerraformerInventory(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        if (itemInHand == null)
            return;

        ItemMeta meta = itemInHand.getItemMeta();
        if (meta == null)
            return;

        if (meta.customName() != null) {
            Component[] terraformerItems = TerraformItems.values();

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
            ItemStack item = event.getItem();
            if (item == null)
                return;

            ItemMeta meta = item.getItemMeta();
            if (meta == null)
                return;

            if (meta.customName() != null) {
                if (meta.customName().equals(TerraformItems.TERRAFORMER_BRUSH)) {
                    if (event.getAction().isRightClick()) {
                        Block targetBlock = player.getTargetBlock(null, 256);

                        if (targetBlock != null) {
                            Location targetLocation = targetBlock.getLocation();
                            properties.Brush.applyBrush(this, player, targetLocation);
                        }
                        event.setCancelled(true);
                    }
                }

                if (meta.customName().equals(TerraformItems.TERRAFORMER_UNDO)) {
                    if (event.getAction().isRightClick()) {
                        BlockHistoryStates undoStates = properties.History.undo();
                        if (undoStates == null) {
                            player.sendMessage(Messages.NOTHING_TO_UNDO);
                            return;
                        }
                        undo(undoStates.states(), properties.Brush.BlockUpdates);
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
                        properties.applyRedo(this, player, redoAction);
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

                if (meta.customName().equals(TerraformItems.TERRAFORMER_BRUSH_SETTINGS)) {
                    if (event.getAction().isRightClick()) {
                        properties.Brush.Type.openBrushSettings(this, player, properties);
                    }
                }

                if (meta.customName().equals(TerraformItems.TERRAFORMER_MATERIAL_SETTINGS)) {
                    if (event.getAction().isRightClick()) {
                        properties.Brush.Type.openMaterialSettings(this, player, properties);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        Player player = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;

        if (player == null) {
            return;
        }

        TerraformerProperties properties = terraformers.get(player.getUniqueId());

        if (properties == null || properties.IsTerraformer == false) {
            return;
        }

        Inventory inventory = event.getInventory();
        if (inventory != null) {
            if (inventory.getHolder() instanceof MaterialSettings settings) {
                settings.onInventoryClick(this, event, player, properties);
            }
            if (inventory.getHolder() instanceof BrushSettings brushSettings) {
                brushSettings.onInventoryClick(this, event, player, properties);
            }
            if (inventory.getHolder() instanceof SelectPaintMode paintMode) {
                paintMode.onInventoryClick(this, event, player, properties);
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
        ItemStack undo = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta undoMeta = undo.getItemMeta();
        undoMeta.customName(TerraformItems.TERRAFORMER_UNDO);
        SkullTexturesApplier.applyTextures(undoMeta,
                "http://textures.minecraft.net/texture/5d9c93f8b9f2f8f91aa4377551c2738002a78816d612f39f142fc91a3d713ad");
        undo.setItemMeta(undoMeta);
        player.getInventory().setItem(3, undo);

        // Redo Item
        ItemStack redo = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta redoMeta = redo.getItemMeta();
        redoMeta.customName(TerraformItems.TERRAFORMER_REDO);
        SkullTexturesApplier.applyTextures(redoMeta,
                "http://textures.minecraft.net/texture/479e8cf21b839b255a2836e251941c5fdc99af01559e3733d5325ccfa3d922aa");
        redo.setItemMeta(redoMeta);
        player.getInventory().setItem(5, redo);

        // Leave Terraform Mode Item
        ItemStack leave = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta leaveMeta = leave.getItemMeta();
        SkullTexturesApplier.applyTextures(leaveMeta,
                "http://textures.minecraft.net/texture/ed0a1420844ce237a45d2e7e544d135841e9f82d09e203267cf8896c8515e360");
        leaveMeta.customName(TerraformItems.TERRAFORMER_LEAVE);
        leave.setItemMeta(leaveMeta);
        player.getInventory().setItem(8, leave);

        // Open Brush Settings Item
        ItemStack materialSettings = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta materialSettingsMeta = materialSettings.getItemMeta();
        SkullTexturesApplier.applyTextures(materialSettingsMeta,
                "http://textures.minecraft.net/texture/95d2cb38458da17fb6cdacf787161602a2493cbf93233636253cff07cd88a9c0");
        materialSettingsMeta.customName(TerraformItems.TERRAFORMER_MATERIAL_SETTINGS);
        materialSettings.setItemMeta(materialSettingsMeta);
        player.getInventory().setItem(0, materialSettings);

        // Open Brush Settings Item
        ItemStack brushSettings = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta brushSettingsMeta = brushSettings.getItemMeta();
        SkullTexturesApplier.applyTextures(brushSettingsMeta,
                "http://textures.minecraft.net/texture/ac5d73cb4dc065314630b7dc50fd44f1833637f07dddca7feddc208c21504d65");
        brushSettingsMeta.customName(TerraformItems.TERRAFORMER_BRUSH_SETTINGS);
        brushSettings.setItemMeta(brushSettingsMeta);
        player.getInventory().setItem(1, brushSettings);
    }

    // Undo

    public void undo(Stack<BlockState> states, boolean blockUpdates) {
        for (BlockState state : states) {
            state.update(true, blockUpdates);
        }
    }
}
