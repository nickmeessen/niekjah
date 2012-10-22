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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class Initializer extends Activity {

    private static final int MENU_NEW = Menu.FIRST;
    private static final int MENU_STOP = Menu.FIRST + 1;

    private BubbleController bubbleController;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_NEW:
                bubbleController.newGame();
                return true;
            case MENU_STOP:
                System.exit(0);
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_NEW, 0, "New Game");
        menu.add(0, MENU_STOP, 0, "Quit Game");

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        bubbleController = new BubbleController((BubbleView) findViewById(R.id.game));

        bubbleController.setState(BubbleController.STATE_RUNNING);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bubbleController.pauseGame();
    }

    @Override
    public void onBackPressed() {
        bubbleController.undoMove();
    }


}
