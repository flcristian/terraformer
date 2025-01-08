package ro.flcristian.terraformer.utility;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;

public class SkullTexturesApplier {
    public static void applyTextures(ItemMeta meta, String textureURL) {
        SkullMeta skullMeta = (SkullMeta) meta;
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URL.of(URI.create(
                    textureURL), null));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        profile.setTextures(textures);
        skullMeta.setPlayerProfile(profile);
    }
}
