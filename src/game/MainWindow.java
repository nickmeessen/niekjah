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

import game.util.Repainter;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {

        JPanel playingField = new PlayingField();

        new Repainter(playingField).start();

        setContentPane(playingField);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Niekjoh! THE GAME.");
        setSize(366, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }


}
