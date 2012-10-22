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
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class ScoreManager {

    private RecordStore scoreStore;
    private ScoreComparator scoreComparator;
    private Vector scoreTable;
    private int currentScore;
    private int previousScore;
    private int highScore;
    private boolean lastScoreAdded;

    public ScoreManager() {
        
        scoreComparator = new ScoreComparator();
        scoreTable = new Vector();

        previousScore = 0;
        currentScore = 0;

        readStore();
    }

    public int getScore() {
        return currentScore;
    }

    public void setScore(int score) {
        currentScore = 0;
    }

    public int getPreviousScore() {
        return previousScore;
    }

    public void setPreviousScore(int score) {
        previousScore = score;
    }

    public void readStore() {

        scoreTable.removeAllElements();

        try {

            scoreStore = RecordStore.openRecordStore("HighScores", true);

            RecordEnumeration records = scoreStore.enumerateRecords(null, scoreComparator, false);

            int x = 0;

            while (records.hasNextElement()) {

                scoreTable.addElement(new String(records.nextRecord()));

                if (x == 9) {
                    break;
                }

                x += 1;

            }

            scoreStore.closeRecordStore();

        } catch (Exception ex) {
        }

    }

    public void writeStore() {

        try {

            RecordStore.deleteRecordStore("HighScores");

            scoreStore = RecordStore.openRecordStore("HighScores", true);

            for (Enumeration e = scoreTable.elements(); e.hasMoreElements();) {

                String score = (String) e.nextElement();

                byte[] scoreBytes = score.getBytes();

                scoreStore.addRecord(scoreBytes, 0, scoreBytes.length);

            }

            scoreStore.closeRecordStore();

        } catch (Exception ex) {
        }

    }

    public void addScore(String name) {

        if (currentScore > highScore) {

            if (!lastScoreAdded) {

                scoreTable.addElement(name + " :: " + currentScore);

                writeStore();
                readStore();

                lastScoreAdded = true;
            }

        }
    }

    public Vector getScores() {
        return scoreTable;
    }

    public void reset() {

        previousScore = 0;
        currentScore = 0;

        lastScoreAdded = false;

    }

    public void increaseScore(int i) {

        currentScore += i;

    }
}
