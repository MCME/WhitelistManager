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
package co.mcme.whitelistmanager.util;

import co.mcme.whitelistmanager.WhitelistManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class Util {

    private static final Logger log = WhitelistManager.getServerInstance().getLogger();

    public static boolean getBooleanFromUrl(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if (response.toString().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static void info(String msg) {
        log.info("[Whitelist] " + msg);
    }

    public static void warning(String msg) {
        log.warning("[Whitelist] " + msg);
    }

    public static void severe(String msg) {
        log.severe("[Whitelist] " + msg);
    }
}
