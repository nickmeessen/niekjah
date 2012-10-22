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
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class OrbManager {

    private HashMap<String, Orb> orbMap;
    private HashMap<String, Orb> previousMap;
    private HashMap<String, Bitmap> orbImageList;
    private ArrayList<Orb> orbsToRemove;
    private ArrayList<Orb> matchingOrbs;
    private ArrayList<Orb> matchingOrbs2;
    private ArrayList<Orb> selectedOrbs;
    private BubbleController bubbleController;
    private Random random;

    public OrbManager(BubbleController bc) {

        bubbleController = bc;

        random = new Random();
        orbMap = new HashMap<String, Orb>();
        previousMap = new HashMap<String, Orb>();
        orbImageList = new HashMap<String, Bitmap>();

        selectedOrbs = new ArrayList<Orb>();
        orbsToRemove = new ArrayList<Orb>();

        orbImageList.put("Aqua", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_aqua));
        orbImageList.put("Green", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_green));
        orbImageList.put("Orange", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_orange));
        orbImageList.put("Pink", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_pink));
        orbImageList.put("Purple", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_purple));
        orbImageList.put("Red", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_red));
        orbImageList.put("Yellow", BitmapFactory.decodeResource(bc.getView().getContext().getResources(), R.drawable.orb_yellow));

        reset();

    }

    public Collection<Orb> getOrbs() {
        return orbMap.values();
    }

    public Orb generateOrb(int x, int y) {

        Bitmap[] orbImages = new Bitmap[7];

        int i = 0;

        for (Bitmap orb : orbImageList.values()) {

            orbImages[i] = orb;
            i += 1;

        }

        return new Orb(orbImages[random.nextInt(6)], x, y);

    }

    public void clickOrb(int x, int y) {

//        backup();

        Orb matchedOrb = null;

        matchingOrbs = new ArrayList<Orb>();

        for (Orb orb : orbMap.values()) {

            // @todo change 20 -> 40?
            if ((x > orb.getX()) && (x < (orb.getX() + 20)) && (y > orb.getY()) && (y < (orb.getY() + 20))) {

                matchedOrb = orb;

                clickOrb(matchedOrb);

                break;

            }

        }

        if (selectedOrbs.contains(matchedOrb) && (matchingOrbs.size() > 1)) {

            for (Orb orb : matchingOrbs) {
                orbsToRemove.add(orb);
            }

            selectedOrbs.clear();

        } else if (selectedOrbs.size() != 0) {

            selectedOrbs.clear();

        }


        if (matchingOrbs.size() > 1) {

            for (Orb orb : matchingOrbs) {

                selectedOrbs.add(orb);
                orb.setSelected(true);

            }

        }
    }

    public void clickOrb(Orb orb) {

        matchingOrbs.add(orb);

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
    
    public boolean checkRemoving() {

        for (Orb orb : orbsToRemove) {
            orbMap.remove(orb.getX() + "." + orb.getY());
        }

        orbsToRemove.clear();

        return false;

    }

    public boolean checkGravity() {

        // Check for orbs with no orb beneath them and drop them.

        try {

            ArrayList<Orb> orbsToMove = new ArrayList<Orb>();

            for (Orb orb : orbMap.values()) {

                if ((getOrbSouthOfOrb(orb) == null) && (orb.getY() <= bubbleController.getView().getHeight() - 60)) {
                    orbsToMove.add(orb);
                }

            }

            if (!orbsToMove.isEmpty()) {

                for (Orb orb : orbsToMove) {
                    moveOrbDown(orb);
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

            ArrayList<Orb> orbsToMove = new ArrayList<Orb>();

            for (Orb orb : orbMap.values()) {

                if ((getOrbEastOfOrb(orb) == null) && (orb.getX() <= bubbleController.getView().getWidth() - 60)) {
                    orbsToMove.add(orb);
                }
            }

            if (!orbsToMove.isEmpty()) {

                for (Orb orb : orbsToMove) {
                    moveOrbRight(orb);
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

        // @todo change 25 -> 50
        int x = (bubbleController.getView().getWidth() - (bubbleController.getView().getMaxColumns() * 25)) / 3;
        int y = (bubbleController.getView().getHeight() - (bubbleController.getView().getMaxRows() * 25)) / 3;

        int a = 0;

        // @todo change 24 -> 48
        int maxY = y + (24 * (bubbleController.getView().getMaxRows() - 1));

        while (a != bubbleController.getView().getMaxColumns()) {

            if (orbMap.get(x + "." + maxY) == null) {
                result = moveColumn(x - 24);
            }

            a += 1;
            x += 24;

        }

        /* new shiet */

        // @todo change 25 -> 50

        x = (bubbleController.getView().getWidth() - (bubbleController.getView().getMaxColumns() * 25)) / 3;
        y = (bubbleController.getView().getHeight() - (bubbleController.getView().getMaxRows() * 25)) / 3;


        // @todo change 24 -> 48
        if (orbMap.get(x + "." + (maxY - 24)) == null) {

            int i = 0;

            while (i != bubbleController.getView().getMaxRows()) {

                orbMap.put(x + "." + y, generateOrb(x, y));

                i += 1;
                y += 24;

            }

        }

        return result;

    }

    public boolean checkGameOver() {

        for (Orb orb : orbMap.values()) {

            if (testClick(orb)) {
                return false;
            }

        }

        return true;
    }

    public boolean testClick(Orb orb) {

        matchingOrbs2 = new ArrayList<Orb>();

        return testOrb(orb);
    }

    public boolean testOrb(Orb orb) {

        matchingOrbs2.add(orb);

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

        return matchingOrbs2.size() > 1;

    }

    // @todo change 24 -> 48
    public Orb getOrbNorthOfOrb(Orb orb) {
        return orbMap.get(orb.getX() + "." + (orb.getY() - 24));
    }

    public Orb getOrbSouthOfOrb(Orb orb) {
        return orbMap.get(orb.getX() + "." + (orb.getY() + 24));
    }

    public Orb getOrbWestOfOrb(Orb orb) {
        return orbMap.get((orb.getX() - 24) + "." + orb.getY());
    }

    public Orb getOrbEastOfOrb(Orb orb) {
        return orbMap.get((orb.getX() + 24) + "." + orb.getY());
    }

    public void moveOrbDown(Orb orb) {

        int x = orb.getX();
        int y = orb.getY();

        orbMap.get(x + "." + y).setLocation(x, (y + 24));
        orbMap.put(x + "." + (y + 24), orb);
        orbMap.remove(x + "." + y);

    }

    public void moveOrbRight(Orb orb) {

        int x = orb.getX();
        int y = orb.getY();

        orbMap.get(x + "." + y).setLocation((x + 24), y);
        orbMap.put((x + 24) + "." + y, orb);
        orbMap.remove(x + "." + y);

    }

    public boolean moveColumn(int x) {

        ArrayList<Orb> orbsToMove = new ArrayList<Orb>();

        for (Orb orb : orbMap.values()) {

            if (orb.getX() == x) {
                orbsToMove.add(orb);
            }

        }

        if (!orbsToMove.isEmpty()) {

            for (Orb orb : orbsToMove) {
                moveOrbRight(orb);
            }

            return true;

        } else {
            return false;
        }

    }

    public void undo() {

        orbMap.clear();

        for (String key : previousMap.keySet()) {

            int x = Integer.parseInt(key.substring(0, key.indexOf(".")));
            int y = Integer.parseInt(key.substring(key.indexOf(".") + 1, key.length()));

            previousMap.get(key).setLocation(x, y);

            orbMap.put(key, previousMap.get(key));

        }

    }

    public void backup() {

        previousMap.clear();

        for (String key : orbMap.keySet()) {

            previousMap.put(key, orbMap.get(key));

        }

    }

    public void reset() {

        orbMap.clear();

        int x = (bubbleController.getView().getWidth() - (bubbleController.getView().getMaxColumns() * 25)) / 3;
        int y = (bubbleController.getView().getHeight() - (bubbleController.getView().getMaxRows() * 25)) / 3;

        int a = 0;
        int b = 0;

        while (b != bubbleController.getView().getMaxRows()) {

            orbMap.put(x + "." + y, generateOrb(x, y));

            if (a != bubbleController.getView().getMaxColumns()) {

                x += 24;
                a += 1;

            } else {

                a = 0;

                b += 1;
                y += 24;

                x = (bubbleController.getView().getWidth() - (bubbleController.getView().getMaxColumns() * 25)) / 3;

            }

        }

//        backup();

    }
}
