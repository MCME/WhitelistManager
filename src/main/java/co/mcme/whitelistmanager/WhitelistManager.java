/*  This file is part of WhitelistManager.
 * 
 *  WhitelistManager is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WhitelistManager is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WhitelistManager.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.mcme.whitelistmanager;

import co.mcme.whitelistmanager.util.Cache;
import co.mcme.whitelistmanager.util.Util;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class WhitelistManager extends JavaPlugin implements Listener {

    public static final Logger log = Bukkit.getLogger();
    private static final ObjectMapper jsonMapper = new ObjectMapper().configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    File cacheFile;
    private Cache cache;
    @Getter
    private static Server serverInstance;
    @Getter
    private static WhitelistManager pluginInstance;

    @Override
    public void onEnable() {
        getServer().setWhitelist(false);
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        cacheFile = new File(this.getDataFolder(), "cache.json");
        if (cacheFile.exists()) {
            loadCache();
        } else {
            cache = new Cache();
        }
        serverInstance = getServer();
        pluginInstance = this;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        Player joining = event.getPlayer();
        boolean listed = false;
        if (!joining.isBanned()) {
            if (cache.getCache().contains(joining.getName())) {
                Util.info(joining.getName() + " is cached as whitelisted, allowed");
                event.allow();
                return;
            }
            try {
                listed = checkWhitelist(joining);
            } catch (Exception ex) {
                Util.severe(ex.toString());
                log.severe(ex.toString());
            }
            if (listed) {
                Util.info(joining.getName() + " is whitelisted, allowed");
                event.allow();
                cacheUsername(joining.getName());
            } else {
                Util.info(joining.getName() + " is not whitelisted, disallowed");
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You must apply for whitelist at http://whitelist.mcme.co");
            }
        } else {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned. http://mcme.co/o/lookup/" + joining.getName());
        }
    }

    public boolean checkWhitelist(Player p) throws Exception {
        return Util.getBooleanFromUrl(getConfig().getString("accesscontrol.urls.whitelist") + p.getName());
    }

    private void cacheUsername(String name) {
        if (!cache.getCache().contains(name)) {
            cache.getCache().add(name);
        }
        try {
            jsonMapper.writeValue(cacheFile, cache);
        } catch (IOException ex) {
            Util.severe(ex.toString());
        }
    }

    private void loadCache() {
        try {
            cache = jsonMapper.readValue(cacheFile, Cache.class);
        } catch (IOException ex) {
            Util.severe(ex.toString());
        }
    }
}
