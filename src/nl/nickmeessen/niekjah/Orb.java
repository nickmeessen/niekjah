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
package nl.nickmeessen.niekjah;

import android.graphics.Bitmap;

public class Orb {

    private Bitmap orbImage;
    private int orbX;
    private int orbY;
    private boolean selected;

    public Orb(Bitmap img, int x, int y) {

        orbImage = img;
        orbX = x;
        orbY = y;
        selected = false;

    }

    public int getX() {
        return orbX;
    }

    public int getY() {
        return orbY;
    }

    public Bitmap getImage() {
        return orbImage;
    }

    public void setLocation(int x, int y) {
        orbX = x;
        orbY = y;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

}
