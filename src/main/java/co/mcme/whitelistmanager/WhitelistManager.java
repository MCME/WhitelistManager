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

import co.mcme.whitelistmanager.util.Util;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WhitelistManager extends JavaPlugin implements Listener {

    public static final Logger log = Bukkit.getLogger();
    ArrayList<String> cache = new ArrayList();

    @Override
    public void onEnable() {
        getServer().setWhitelist(false);
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        Player joining = event.getPlayer();
        boolean listed = false;
        if (!joining.isBanned()) {
            if (cache.contains(joining.getName())) {
                event.allow();
                return;
            }
            try {
                listed = checkWhitelist(joining);
            } catch (Exception ex) {
                log.severe(ex.toString());
            }
            if (listed) {
                log.log(Level.INFO, "{0} is whitelisted, allowed", joining.getName());
                event.allow();
                cache.add(joining.getName());
            } else {
                log.log(Level.INFO, "{0} is not whitelisted, disallowed", joining.getName());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You must apply for whitelist at http://whitelist.mcme.co");
            }
        } else {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned. http://mcme.co/o/lookup/" + joining.getName());
        }
    }

    public boolean checkWhitelist(Player p) throws Exception {
        log.log(Level.INFO, "Checking whitelist status of {0}", p.getName());
        return Util.getBooleanFromUrl(getConfig().getString("accesscontrol.urls.whitelist") + p.getName());
    }
}
