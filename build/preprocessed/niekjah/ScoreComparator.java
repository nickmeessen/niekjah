/*
Niekjah! THE GAME.
Copyright (C) 2010
Nick Meessen

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
----------------------------------------------------------------------------
 */
package niekjah;

import javax.microedition.rms.RecordComparator;

public class ScoreComparator implements RecordComparator {

    public int compare(byte[] arg0, byte[] arg1) {

        String recordA = new String(arg0);
        String recordB = new String(arg1);

        int smoothieA = Integer.parseInt(recordA.substring(recordA.indexOf(" :: ") + 1));
        int smoothieB = Integer.parseInt(recordB.substring(recordA.indexOf(" :: ") + 1));

        if (smoothieA < smoothieB) {
            return RecordComparator.FOLLOWS;
        } else if (smoothieA > smoothieB) {
            return RecordComparator.PRECEDES;
        } else {
            return RecordComparator.EQUIVALENT;
        }

    }
}
