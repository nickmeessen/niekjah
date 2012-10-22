/*
    Niekjoh!! THE GAME.
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
package game;

import game.util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class PlayingField extends JPanel {

    private Image panelBg;
    private OrbManager orbManager;

    public PlayingField() {

        panelBg = ImageLoader.load("img/bg.png");
        orbManager = new OrbManager();

        addMouseListener(new PlayingFieldListener(orbManager));
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        try {

            g2d.drawImage(panelBg, 0, 0, null);

            for (Orb orb : orbManager.getOrbs()) {
                g2d.drawImage(orb.getImage(), orb.getX(), orb.getY(), null);
            }

            orbManager.checkGravity();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

}
