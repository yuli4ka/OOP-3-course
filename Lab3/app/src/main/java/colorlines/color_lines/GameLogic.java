package colorlines.color_lines;

import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;


public class GameLogic {
    int score;
    TableLayout tableLayout;
    boolean gameOver = false;
    int colorIndex;
    private  HashSet<Button> availableCells = new HashSet<>(); //contains empty cell
    int numberOfBallsSuccession = 1;
    HashSet <Button> buttonToExplosion = new HashSet();
    boolean alreadyCheckReverse = false;

    public boolean checkSuccessionAllDirections(Button button) {
        boolean doExplosion = false;
        for(int direction = 0; direction < 8; direction++) {
            if(checkSuccessionByDirection(button, direction)) //0=left, 1=left-up, 2=up, 3=up-right, 4=right 5=right-down 6=down 7=down-left ;
                doExplosion = true;
        }
        return doExplosion;
    }

    public boolean checkSuccessionByDirection(Button button, int direction) {
        int currentLocation = DataManager.idAllCells.get(button);
        Button nearButton= getKeyByValue(DataManager.idAllCells, currentLocation-1);

        Button [] buttonsDirection = {getKeyByValue(DataManager.idAllCells, currentLocation-1), getKeyByValue(DataManager.idAllCells, currentLocation-10),
                getKeyByValue(DataManager.idAllCells, currentLocation-9), getKeyByValue(DataManager.idAllCells, currentLocation-8),
                getKeyByValue(DataManager.idAllCells, currentLocation+1),getKeyByValue(DataManager.idAllCells, currentLocation+10),
                getKeyByValue(DataManager.idAllCells,currentLocation+9), getKeyByValue(DataManager.idAllCells, currentLocation+8)};

        for(int i = 0; i < buttonsDirection.length; i++) {
            if(direction == i) {
                nearButton = buttonsDirection[i];
            }
        }
        buttonToExplosion.add(button);

        checkIfNearButtonIsSameColor(button,nearButton,direction);

        if(numberOfBallsSuccession > 1 && !alreadyCheckReverse) {
            numberOfBallsSuccession = 1;

            if (direction == 0) { //checks also direction 4 {
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 1), 4);

            } else if (direction == 1) {  //checks also direction 5
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 10), 5);
            } else if (direction == 2) { //up. checks also down(direction 6)
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 9), 6);
            }
            else if(direction == 3) { //3- up-right. checks also down-right(7)
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 8), 7);
            }
            else if(direction == 4) {
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 1), 0);
            }
            else if(direction == 5) {
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 10), 1);
            }
            else if(direction == 6) {
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 9), 2);
            }
            else if(direction == 7) {
                alreadyCheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 8), 3);
            }
        }
        if(numberOfBallsSuccession > 4) { //if there is more then 5 balls in the same color in a row
            numberOfBallsSuccession = 1;
            doExplosion(buttonToExplosion);
            buttonToExplosion.clear();
            alreadyCheckReverse = false;
            return true;
        }
        else
        {
            numberOfBallsSuccession = 1;
            buttonToExplosion.clear();
            alreadyCheckReverse = false;
            return false;
        }
    }

    private void checkIfNearButtonIsSameColor(Button button, Button nearButton, int direction) {
        if(nearButton != null && button != null) {
            if (!getAvailableCells().contains(nearButton)) {
                try {
                    if (button.getTag().equals(nearButton.getTag())) { //checks if the nearButton and button are the same color
                        numberOfBallsSuccession++;
                        checkSuccessionByDirection(nearButton, direction); //doing a recursion to check if there one more ball in the same color and direction
                        buttonToExplosion.add(nearButton); //add the nearButton to the explosion array
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doExplosion(HashSet<Button> buttonToExplosion) {
        int numberOfBalls=0;
        for(Button button: buttonToExplosion) {
            numberOfBalls++;
            removeButton(button);
        }
        score = score + numberOfBalls * (numberOfBalls - 2);
        clearAllAnimation(); //reset the animation
    }

    public int getScore() {
        return score;
    }


    public void clearAllAnimation() {
        for (int i = 0; i < 81; i++) {
            getKeyByValue(DataManager.idAllCells, i).clearAnimation();
        }
    }



    public boolean isGameOver() {
        return gameOver;
    }

    public HashSet<Button> getAvailableCells() {
        return availableCells;
    }

    public void createThreeBalls(TableLayout tablelayout) {
        this.tableLayout = tablelayout;
        for(int i = 0; i < 3; i++) {
            colorIndex = i;
            createBall();
        }
    }

    public void createBall() { //in random location with random color
        if (!(availableCells.size() == 0)) {
            Random random = new Random();
            int randomLocation = random.nextInt(DataManager.idAllCells.size());

            if (getAvailableCells().contains(getKeyByValue(DataManager.idAllCells, randomLocation))) {
                Log.i("getAvailableCells", "inside getAvailableCells");
                Button button = getKeyByValue(DataManager.idAllCells, randomLocation);

                for(int i = 0; i < 3; i++) {
                    if(i == colorIndex){
                        button.setBackgroundResource(this.threeRandomColors[i]);
                        button.setTag(this.threeRandomColors[i]);
                    }
                }

                getAvailableCells().remove(button);
                checkSuccessionAllDirections(button);

                if(availableCells.size() == 0)
                {
                    gameOver = true;
                }
            } else {
                createBall(); //need a different location
            }
        } else {
            gameOver = true;
        }
    }

    int [] threeRandomColors;
    public void setColorToThreeBalls(int [] threeRandomColors) {
        this.threeRandomColors=threeRandomColors;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void removeButton(Button Button) {
        Button.setTag(null);
        Button.setBackgroundResource(R.drawable.cell); //set the old cell to be a cell drawable. will look like empty to the players
        getAvailableCells().add(Button);
    }

}

