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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Image;

public class OrbManager {

    private ScoreManager scoreManager;
    private Hashtable orbMap;
    private Hashtable previousMap;
    private Hashtable orbImageList;
    private Vector matchingOrbs;
    private Vector matchingOrbs2;
    private BubbleCanvas canvas;
    private Random random;

    public OrbManager(BubbleCanvas cv) {

        canvas = cv;

        scoreManager = new ScoreManager();

        random = new Random();
        orbMap = new Hashtable();
        previousMap = new Hashtable();
        orbImageList = new Hashtable();

        try {

            orbImageList.put("Aqua", Image.createImage("/niekjah/img/orbs/aqua.png"));
            orbImageList.put("Green", Image.createImage("/niekjah/img/orbs/green.png"));
            orbImageList.put("Orange", Image.createImage("/niekjah/img/orbs/orange.png"));
            orbImageList.put("Pink", Image.createImage("/niekjah/img/orbs/pink.png"));
            orbImageList.put("Purple", Image.createImage("/niekjah/img/orbs/purple.png"));
            orbImageList.put("Red", Image.createImage("/niekjah/img/orbs/red.png"));
            orbImageList.put("Yellow", Image.createImage("/niekjah/img/orbs/yellow.png"));

        } catch (IOException ex) {
        }

        reset();

    }

    public Enumeration getOrbs() {
        return orbMap.elements();
    }

    public Orb generateOrb(int x, int y) {

        Image[] orbImages = new Image[7];

        int i = 0;

        for (Enumeration e = orbImageList.elements(); e.hasMoreElements();) {

            orbImages[i] = (Image) e.nextElement();

            i += 1;
        }

        return new Orb(orbImages[random.nextInt(6)], x, y);

    }

    public void clickOrb(int x, int y) {

        backup();

        matchingOrbs = new Vector();

        for (Enumeration e = orbMap.elements(); e.hasMoreElements();) {

            Orb orb = (Orb) e.nextElement();

            if ((x > orb.getX()) && (x < (orb.getX() + 20)) && (y > orb.getY()) && (y < (orb.getY() + 20))) {

                clickOrb(orb);

                break;

            }

        }

        if (matchingOrbs.size() > 1) {

            scoreManager.increaseScore(matchingOrbs.size() * matchingOrbs.size());

            canvas.vibrate();

            for (Enumeration e = matchingOrbs.elements(); e.hasMoreElements();) {

                removeOrb((Orb) e.nextElement());

            }


        }
    }

    public void clickOrb(Orb orb) {

        matchingOrbs.addElement(orb);

        if (orb != null) {

            Orb north = getOrbNorthOfOrb(orb);
            Orb south = getOrbSouthOfOrb(orb);
            Orb west = getOrbWestOfOrb(orb);
            Orb east = getOrbEastOfOrb(orb);

            try {

                if (north.getImage() == orb.getImage()) {

                    if (!matchingOrbs.contains(north)) {
                        clickOrb(north);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (south.getImage() == orb.getImage()) {
                    if (!matchingOrbs.contains(south)) {
                        clickOrb(south);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (west.getImage() == orb.getImage()) {
                    if (!matchingOrbs.contains(west)) {
                        clickOrb(west);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (east.getImage() == orb.getImage()) {
                    if (!matchingOrbs.contains(east)) {
                        clickOrb(east);
                    }
                }
            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

        }
    }

    public boolean checkGravity() {

        // Check for orbs with no orb beneath them and drop them.

        try {

            Vector orbsToMove = new Vector();

            for (Enumeration e = orbMap.elements(); e.hasMoreElements();) {

                Orb orb = (Orb) e.nextElement();

                if ((getOrbSouthOfOrb(orb) == null) && (orb.getY() <= canvas.getHeight() - 60)) {
                    orbsToMove.addElement(orb);
                }

            }

            if (!orbsToMove.isEmpty()) {

                for (Enumeration e = orbsToMove.elements(); e.hasMoreElements();) {
                    moveOrbDown((Orb) e.nextElement());
                }

                return true;

            } else {

                return false;

            }

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean checkGravityRight() {

        // Check for orbs with no orb right of them and move them there.

        try {

            Vector orbsToMove = new Vector();

            for (Enumeration e = orbMap.elements(); e.hasMoreElements();) {

                Orb orb = (Orb) e.nextElement();

                if ((getOrbEastOfOrb(orb) == null) && (orb.getX() <= canvas.getWidth() - 40)) {
                    orbsToMove.addElement(orb);
                }

            }

            if (!orbsToMove.isEmpty()) {

                for (Enumeration e = orbsToMove.elements(); e.hasMoreElements();) {
                    moveOrbRight((Orb) e.nextElement());
                }

                return true;

            } else {

                return false;

            }

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean checkEmptyColumns() {

        // Check for empty columns and fill em up with their left neighbour column.
        // If most left neighbour column is empty, add a whole new column.

        boolean result = false;

        int x = (canvas.getWidth() - (canvas.getMaxColumns() * 25)) / 3;
        int y = (canvas.getHeight() - (canvas.getMaxRows() * 25)) / 3;

        int a = 0;

        int maxY = y + (24 * (canvas.getMaxRows() - 1));

        while (a != canvas.getMaxColumns()) {

            if (orbMap.get(x + "." + maxY) == null) {
                result = moveColumn(x - 24);
            }

            a += 1;
            x += 24;

        }

        /* new shiet */

        x = (canvas.getWidth() - (canvas.getMaxColumns() * 25)) / 3;
        y = (canvas.getHeight() - (canvas.getMaxRows() * 25)) / 3;

        if (orbMap.get(x + "." + (maxY - 24)) == null) {

            System.out.println("ADDING COLLUMN ZOMRG");

            int i = 0;

            while (i != canvas.getMaxRows()) {

                orbMap.put(x + "." + y, generateOrb(x, y));

                i += 1;
                y += 24;

            }

        }

        return result;

    }

    public boolean checkGameOver() {

        for (Enumeration e = orbMap.elements(); e.hasMoreElements();) {

            Orb orb = (Orb) e.nextElement();

            if (testClick(orb)) {

                return false;
            }

        }

        scoreManager.addScore("<Name>");

        return true;
    }

    public boolean testClick(Orb orb) {

        matchingOrbs2 = new Vector();

        return testOrb(orb);
    }

    public boolean testOrb(Orb orb) {

        matchingOrbs2.addElement(orb);

        if (orb != null) {

            Orb north = getOrbNorthOfOrb(orb);
            Orb south = getOrbSouthOfOrb(orb);
            Orb west = getOrbWestOfOrb(orb);
            Orb east = getOrbEastOfOrb(orb);

            try {

                if (north.getImage() == orb.getImage()) {

                    if (!matchingOrbs2.contains(north)) {
                        testOrb(north);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (south.getImage() == orb.getImage()) {
                    if (!matchingOrbs2.contains(south)) {
                        testOrb(south);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (west.getImage() == orb.getImage()) {
                    if (!matchingOrbs2.contains(west)) {
                        testOrb(west);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (east.getImage() == orb.getImage()) {
                    if (!matchingOrbs2.contains(east)) {
                        testOrb(east);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

        }

        if (matchingOrbs2.size() > 1) {
            return true;
        } else {
            return false;
        }

    }

    public Orb getOrbNorthOfOrb(Orb orb) {
        return (Orb) orbMap.get(orb.getX() + "." + (orb.getY() - 24));
    }

    public Orb getOrbSouthOfOrb(Orb orb) {
        return (Orb) orbMap.get(orb.getX() + "." + (orb.getY() + 24));
    }

    public Orb getOrbWestOfOrb(Orb orb) {
        return (Orb) orbMap.get((orb.getX() - 24) + "." + orb.getY());
    }

    public Orb getOrbEastOfOrb(Orb orb) {
        return (Orb) orbMap.get((orb.getX() + 24) + "." + orb.getY());
    }

    public void moveOrbDown(Orb orb) {

        int x = orb.getX();
        int y = orb.getY();

        ((Orb) orbMap.get(x + "." + y)).setLocation(x, (y + 24));
        orbMap.put(x + "." + (y + 24), orb);
        orbMap.remove(x + "." + y);

    }

    public void moveOrbRight(Orb orb) {

        int x = orb.getX();
        int y = orb.getY();

        ((Orb) orbMap.get(x + "." + y)).setLocation((x + 24), y);
        orbMap.put((x + 24) + "." + y, orb);
        orbMap.remove(x + "." + y);

    }

    public boolean moveColumn(int x) {

        Vector orbsToMove = new Vector();

        for (Enumeration e = orbMap.elements(); e.hasMoreElements();) {

            Orb orb = (Orb) e.nextElement();

            if (orb.getX() == x) {
                orbsToMove.addElement(orb);
            }

        }

        if (!orbsToMove.isEmpty()) {

            for (Enumeration e = orbsToMove.elements(); e.hasMoreElements();) {
                moveOrbRight((Orb) e.nextElement());
            }

            return true;

        } else {
            return false;
        }

    }

    public void removeOrb(Orb orb) {
        orbMap.remove(orb.getX() + "." + orb.getY());
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    public Vector getScores() {
        return scoreManager.getScores();
    }

    public void undo() {

        scoreManager.setScore(scoreManager.getPreviousScore());

        orbMap.clear();

        for (Enumeration e = previousMap.keys(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            int x = Integer.parseInt(key.substring(0, key.indexOf(".")));
            int y = Integer.parseInt(key.substring(key.indexOf(".") + 1, key.length()));

            ((Orb) previousMap.get(key)).setLocation(x, y);

            orbMap.put(key, previousMap.get(key));

        }

    }

    public void backup() {

        scoreManager.setPreviousScore(scoreManager.getScore());

        previousMap.clear();

        for (Enumeration e = orbMap.keys(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            previousMap.put(key, orbMap.get(key));

        }

    }

    public void reset() {

        orbMap.clear();
        scoreManager.reset();

        int x = (canvas.getWidth() - (canvas.getMaxColumns() * 25)) / 3;
        int y = (canvas.getHeight() - (canvas.getMaxRows() * 25)) / 3;

        int a = 0;
        int b = 0;

        while (b != canvas.getMaxRows()) {

            orbMap.put(x + "." + y, generateOrb(x, y));

            if (a != canvas.getMaxColumns()) {

                x += 24;
                a += 1;

            } else {

                a = 0;

                b += 1;
                y += 24;

                x = (canvas.getWidth() - (canvas.getMaxColumns() * 25)) / 3;

            }

        }

        backup();

    }
}
