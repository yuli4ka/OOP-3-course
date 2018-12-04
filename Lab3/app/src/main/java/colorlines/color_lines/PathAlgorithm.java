package colorlines.color_lines;

import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathAlgorithm {
    boolean [] spacesChecked = new boolean[81];  //true - checked, false - unchecked.
    int originBtnIdx;
    int destinationBtnIdx;
    private GameLogic logic;

    public PathAlgorithm(int originIdx, int destIdx, GameLogic logic){
        this.destinationBtnIdx = destIdx;
        this.originBtnIdx = originIdx;
        this.logic = logic;
        Arrays.fill(spacesChecked, false);
    }

    public boolean checkPath(){
        Button originBtn= logic.getKeyByValue(DataManager.idAllCells, originBtnIdx);  //gets the button of the originBtn by the id location
        List<GameButton> pathList = getEmptyNeighbors((GameButton) originBtn); //holds the empty neighbors (only the close one)
        while(true){
            Log.i("Path algo", "Length of pathList: " + pathList.size());

            for(GameButton gb: pathList){
                if(gb.getBtnId() == destinationBtnIdx) //if the ball has reached to his destination
                    return true;
            }

            // we haven't reached the destination yet
            ArrayList<GameButton> tempList = new ArrayList<>();
            for(GameButton gb: pathList){
                tempList.addAll(getEmptyNeighbors(gb));
                // pathList hold a empty neighbors(parents) and temList holds the empty neighbors of the pathList
            }

            if(tempList.size() == 0) //there is no more uncheck places (and yet did not get to the destination)
                return false;

            pathList = tempList;
        }
    }

    private List<GameButton> getEmptyNeighbors(GameButton target){ //all the empty close neighbors of a button
        ArrayList<GameButton> list = new ArrayList<>();
        if(checkNeighborValidity(target.getRightNeighbor()))
            list.add(target.getRightNeighbor());
        if(checkNeighborValidity(target.getLeftNeighbor()))
            list.add(target.getLeftNeighbor());
        if(checkNeighborValidity(target.getTopNeighbor()))
            list.add(target.getTopNeighbor());
        if(checkNeighborValidity(target.getBottomNeighbor()))
            list.add(target.getBottomNeighbor());
        for(GameButton gb: list)
            spacesChecked[gb.getBtnId()] = true;
        return list;
    }
    private boolean checkNeighborValidity(GameButton button){
        return button != null
                && !spacesChecked[button.getBtnId()] //checks if we already checked the cell
                && logic.getAvailableCells().contains(button); //checks if the button is empty cell
    }
}

