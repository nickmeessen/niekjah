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

import java.util.Enumeration;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

public class BubbleCanvas extends GameCanvas implements Runnable {

    private Graphics graphics;
    private Arrow arrow;
    private Display mainDisplay;
    private Image bg;
    private volatile Thread thread;
    private OrbManager orbManager;
    private boolean gameOver;

    public BubbleCanvas(Display dp) {

        super(true);

        setFullScreenMode(true);

        graphics = getGraphics();
        mainDisplay = dp;

        orbManager = new OrbManager(this);

        gameOver = false;

        int x = (getWidth() - (getMaxColumns() * 25)) / 3;
        int y = (getHeight() - (getMaxRows() * 25)) / 3;

        arrow = new Arrow(x + 12, y + 12);

        try {
            bg = Image.createImage("/niekjah/img/bg.png");
        } catch (Exception ex) {
        }

    }

    protected void hideNotify() {
        thread = null;
    }

    public void run() {

        while (thread == Thread.currentThread()) {

            try {

                int state = getKeyStates();

                graphics.setColor(0x333333);
                graphics.fillRect(0, 0, getWidth(), getHeight());

                for (Enumeration e = orbManager.getOrbs(); e.hasMoreElements();) {

                    Orb orb = (Orb) e.nextElement();

                    graphics.drawImage(orb.getImage(), orb.getX(), orb.getY(), 0);
                }

                graphics.setColor(0xFFFFFF);
                graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
                graphics.drawString("SCORE : " + orbManager.getScore(), 5, getHeight() - 20, Graphics.TOP | Graphics.LEFT);


                if (!gameOver) {

                    graphics.drawImage(arrow.getImage(), arrow.getX(), arrow.getY(), 0);

                    if (!orbManager.checkGravity()) {

                        if (!orbManager.checkGravityRight()) {

                            if (!orbManager.checkEmptyColumns()) {
                                gameOver = orbManager.checkGameOver();
                            }
                        }

                    }

                    if ((state & FIRE_PRESSED) != 0) {
                        orbManager.clickOrb(arrow.getX(), arrow.getY());
                    }

                    if (((state & LEFT_PRESSED) != 0) && ((arrow.getX() - 24) >= ((getWidth() - (getMaxColumns() * 25)) / 3))) {
                        arrow.setLocation(arrow.getX() - 24, arrow.getY());
                    }

                    if (((state & RIGHT_PRESSED) != 0) && ((arrow.getX() - 24) <= (getMaxColumns() * 25) - 12)) {
                        arrow.setLocation(arrow.getX() + 24, arrow.getY());
                    }

                    if (((state & UP_PRESSED) != 0) && ((arrow.getY() - 24) >= ((getHeight() - (getMaxRows() * 25)) / 3) + 12)) {
                        arrow.setLocation(arrow.getX(), arrow.getY() - 24);
                    }

                    if (((state & DOWN_PRESSED) != 0) && ((arrow.getY() - 24) <= (getMaxRows() * 25) - 60)) {
                        arrow.setLocation(arrow.getX(), arrow.getY() + 24);
                    }

//                    if ((state & ) != 0) {
//                        orbManager.undo();
//                    }
//
//                    if ((state & ) != 0) {
//                        orbManager.reset();
//                    }


                    if ((state & GAME_A_PRESSED) != 0) {
                        System.out.println("A");
                    }
                    if ((state & GAME_B_PRESSED) != 0) {
                        System.out.println("B");
                    }
                    if ((state & GAME_C_PRESSED) != 0) {
                        System.out.println("C");
                    }
                    if ((state & GAME_D_PRESSED) != 0) {
                        System.out.println("D");
                    }


                    if ((state & GAME_A_PRESSED) != 0) {
//                        testFunction();
                    }

                } else {

                    graphics.drawImage(bg, 0, 0, 0);

                    graphics.drawString("GAME OVER!", 20, 10, Graphics.TOP | Graphics.LEFT);

                    graphics.drawString("High Scores", 60, 40, Graphics.TOP | Graphics.LEFT);

                    int i = 1;
                    int y = 60;

                    for (Enumeration e = orbManager.getScores().elements(); e.hasMoreElements();) {

                        String score = (String) e.nextElement();

                        graphics.drawString(i + ". " + score, 60, y, Graphics.TOP | Graphics.LEFT);

                        i++;
                        y += 16;

                    }

                    if ((state & FIRE_PRESSED) != 0) {

                        arrow.setLocation(((getWidth() - (getMaxColumns() * 25)) / 3) + 12, ((getHeight() - (getMaxRows() * 25)) / 3) + 12);

                        orbManager.reset();

                        gameOver = false;

                    }

                }

                if (state != 0) {
                    Thread.sleep(150);
                } else {
                    Thread.sleep(5);
                }

                flushGraphics();


            } catch (Exception ex) {
            }
        }
    }

    protected void showNotify() {

        thread = new Thread(this);
        thread.start();
    }

    public int getMaxRows() {
        return (getHeight() / 25);
    }

    public int getMaxColumns() {
        return (getWidth() / 25) - 1;
    }

    public void vibrate() {
        mainDisplay.vibrate(100);
    }

    protected void sizeChanged(int w, int h) {

        super.sizeChanged(w, h);

        orbManager.reset();
    }

    public void testFunction() {

        Form form = new Form("New Highscore!", null);

        form.addCommand(new Command("Submit", Command.OK, 1));
        form.addCommand(new Command("Discard", Command.BACK, 1));

        form.setCommandListener(new CommandListener() {

            public void commandAction(Command cmd, Displayable dp) {

                // compare commands with cmd parameter and do action.
               
            }


        });

        TextField nameField = new TextField("Name", "Niekjah", 32, TextField.INITIAL_CAPS_WORD);

        form.append("Congratulations! ^^ \n You made a new highscore!\n\nIf you like to save your score, please enter your name in below.\n");

        form.append(nameField);

       
        mainDisplay.setCurrent(form);

    }
}
