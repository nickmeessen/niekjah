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

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class OrbManager {

    private HashMap<String, Orb> orbMap;
    private HashMap<String, Image> orbImageList;
    private ArrayList<Orb> matchingOrbs;

    public OrbManager() {


        orbMap = new HashMap<String, Orb>();
        orbImageList = new HashMap<String, Image>();

        orbImageList.put("Aqua", ImageLoader.load("img/orbs/aqua.png"));
        orbImageList.put("Green", ImageLoader.load("img/orbs/green.png"));
        orbImageList.put("Orange", ImageLoader.load("img/orbs/orange.png"));
        orbImageList.put("Pink", ImageLoader.load("img/orbs/pink.png"));
        orbImageList.put("Purple", ImageLoader.load("img/orbs/purple.png"));
        orbImageList.put("Red", ImageLoader.load("img/orbs/red.png"));
        orbImageList.put("Yellow", ImageLoader.load("img/orbs/yellow.png"));

        reset();

    }

    public Collection<Orb> getOrbs() {
        return orbMap.values();
    }

    public Orb generateOrb(Point p) {

        ArrayList<Image> orbs = new ArrayList<Image>(orbImageList.values());

        Collections.shuffle(orbs);

        return new Orb(orbs.get(0), p);

    }

    public void clickOrb(Point p) {

        matchingOrbs = new ArrayList<Orb>();

        for (Orb orb : orbMap.values()) {

            if ((p.getX() > orb.getX()) && (p.getX() < (orb.getX() + 20)) && (p.getY() > orb.getY()) && (p.getY() < (orb.getY() + 20))) {

                clickOrb(orb);

                break;

            }

        }

        if (matchingOrbs.size() > 1) {

            for (Orb orb : matchingOrbs) {

                removeOrb(orb);

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

                    if(!matchingOrbs.contains(north)) {
                        clickOrb(north);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (south.getImage() == orb.getImage()) {
                    if(!matchingOrbs.contains(south)) {
                        clickOrb(south);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (west.getImage() == orb.getImage()) {
                    if(!matchingOrbs.contains(west)) {
                        clickOrb(west);
                    }
                }

            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

            try {

                if (east.getImage() == orb.getImage()) {
                    if(!matchingOrbs.contains(east)) {
                        clickOrb(east);
                    }
                }
            } catch (Exception ex) {
                // empty neighbour probably, ignore.
            }

        }

//        removeOrb(orb);


//        if (matchingOrbs.size() > 1) {
//
//            for (Orb orb : matchingOrbs) {
//
//                removeOrb(orb);
//
//            }
//
//        }

    }

    public void checkGravity() {

        /*

            @todo empty columns fillen.


         */

//        boolean empty = true;
//
//        int x = 5;
//        int y = 5;
//
//        int a = 0;
//        int b = 0;
//
//        while (b != 20) {
//
//            if(orbMap.get(x + "." + y) != null) {
//                empty = false;
//            }
//
//
//            if (a != 14) {
//
//                x = x + 24;
//
//                a++;
//
//            } else {
//
//                b++;
//
//                a = 0;
//                x = 5;
//
//                y = y + 24;
//
//            }
//
//        }



        try {

            ArrayList<Orb> orbsToMove = new ArrayList<Orb>();

            for (Orb orb : orbMap.values()) {
                
                if ((getOrbSouthOfOrb(orb) == null) && (orb.getY() < 458)) {
                    orbsToMove.add(orb);
                }

            }

            for (Orb orb : orbsToMove) {
                moveOrb(orb);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

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

    public void moveOrb(Orb orb) {

        int x = orb.getX();
        int y = orb.getY();

        orbMap.get(x + "." + y).setLocation(new Point(x, (y + 24)));        
        orbMap.put(x + "." + (y + 24), orb);
        orbMap.remove(x + "." + y);

    }

    public void removeOrb(Orb orb) {
        orbMap.remove(orb.getX() + "." + orb.getY());
    }

    public void reset() {

        int x = 5;
        int y = 5;

        int a = 0;
        int b = 0;

        while (b != 20) {

            orbMap.put(x + "." + y, generateOrb(new Point(x, y)));

            if (a != 14) {

                x = x + 24;

                a++;

            } else {

                b++;

                a = 0;
                x = 5;

                y = y + 24;

            }

        }

    }
}
