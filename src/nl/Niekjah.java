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

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class Niekjah extends MIDlet {

    private Display display;
    private BubbleCanvas bubbleCanvas;

    public Niekjah() {
        display = Display.getDisplay(this);
    }

    public void startApp() {

        bubbleCanvas = new BubbleCanvas(display);

        bubbleCanvas.run();

        display.setCurrent(bubbleCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        System.exit(0);
    }

}
