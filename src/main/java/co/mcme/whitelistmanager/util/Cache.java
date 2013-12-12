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

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Cache {

    @Getter
    @Setter
    private ArrayList<String> cache = new ArrayList();
}
