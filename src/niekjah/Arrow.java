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

import java.io.IOException;
import javax.microedition.lcdui.Image;

public class Arrow {

    private Image arrowImg;
    private int arrowX;
    private int arrowY;

    public Arrow(int x, int y) {

        try {
            arrowImg = Image.createImage("/niekjah/img/arrow.png");
        } catch (IOException ex) {
        }

        arrowX = x;
        arrowY = y;

    }

    public int getX() {
        return arrowX;
    }

    public int getY() {
        return arrowY;
    }

    public Image getImage() {
        return arrowImg;
    }

    public void setLocation(int x, int y) {
        arrowX = x;
        arrowY = y;
    }
}
