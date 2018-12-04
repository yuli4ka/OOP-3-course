package colorlines.color_lines;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GameLogic gameLogic = new GameLogic();
    Dialog scoreDialog;
    Dialog gameOverDialog;
    ArrayList<Integer> arrayListScore;

    TextView tvScore;
    Button lastCellClicked, currentCellClicked, btNewGame;

    ArrayList<GameButton> allGameButton;

    int idCell = 0;
    Animation animationBounce;
    ArrayList<Button> allButtons = new ArrayList<>();

    TableLayout tablelayout;
    ImageView imageViewLeftBall, imageViewCenterBall, imageViewRightBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNewGame = (Button)findViewById(R.id.btNewGame);
        tvScore = (TextView)findViewById(R.id.tvCurentScore);

        imageViewLeftBall = (ImageView)findViewById(R.id.imageviewleftball);
        imageViewCenterBall = (ImageView)findViewById(R.id.imageviewcenterball);
        imageViewRightBall = (ImageView)findViewById(R.id.imageviewrightball);

        scoreDialog = new Dialog(this);
        gameOverDialog = new Dialog(this);
        randomNextBalls();
        arrayListScore = new ArrayList<>();

        allGameButton = new ArrayList<GameButton>();

        btNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewGame();
            }
        });

        tablelayout = (TableLayout) findViewById(R.id.tablelayout);
        animationBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ballanim);

        for(int i = 0; i < 9; i++) {
            TableRow row = (TableRow)tablelayout.getChildAt(i);  //gets each row in the tableLayout
            for(int j = 0; j < 9; j++){
                GameButton button = (GameButton)row.getChildAt(j); // get child index on particular row
                DataManager.idAllCells.put(button, idCell);
                button.setBtnId(idCell);
                allGameButton.add(button);
                idCell++;
                gameLogic.getAvailableCells().add(button);
                setButtonListener(button); //add button listener to each cell
                allButtons.add(button); //allButtons is an arrayList that holds all buttons. used in the animationAlgorithm method to clear all animation
            }
        }

        gameLogic.createThreeBalls(tablelayout); //create the starting 3 balls

        Integer [] startOfRow={0, 9, 9*2, 9*3, 9*4, 9*5, 9*6, 9*7, 9*8};
        Integer [] endOfRow={9-1, 9*2-1, 9*3-1, 9*4-1, 9*5-1, 9*6-1, 9*7-1, 9*8-1, 9*9-1};

        for(Integer i = 0; i < allGameButton.size(); i++) {
            if(!Arrays.asList(endOfRow).contains(i)) {
                if(i < 80) {
                    allGameButton.get(i).setRightNeighbor(allGameButton.get(i + 1));
                }
            }
            if(!Arrays.asList(startOfRow).contains(i)) {
                if(i > 0) {
                    allGameButton.get(i).setLeftNeighbor(allGameButton.get(i - 1));
                }
            }
            if(i > 8) {
                allGameButton.get(i).setTopNeighbor(allGameButton.get(i - 9));
            }
            if(i < 71) {
                allGameButton.get(i).setBottomNeighbor(allGameButton.get(i + 9));
            }
        }
    }

    private void startNewGame() {
        finish();
        DataManager.idAllCells = new HashMap<>(); //reset the HashSet idAllCells
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void setButtonListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkCurrentClick(button); //checks if the current cell was clicked or a different
                animationAlgorithm(button);
                tvScore.setText("score: " + gameLogic.getScore());
                if(gameLogic.isGameOver()) {
                    gameOver();
                }
            }
        });
    }

    private void checkCurrentClick(Button button) {
        if(lastCellClicked == null && currentCellClicked == null) {
            lastCellClicked = button;
            currentCellClicked = button;
        }
        else {
            lastCellClicked = currentCellClicked;
            currentCellClicked = button;
        }

        if(lastCellClicked.getAnimation() != null &&
                gameLogic.getAvailableCells().contains(currentCellClicked) &&
                isMovementPossible(lastCellClicked, currentCellClicked)) {
            moveSuccessful(lastCellClicked, currentCellClicked); //move the current balls to the wanted location.  currentCellClicked == wanted location.
        }
    }

    private void moveSuccessful(Button currentButton, Button wantedLocationButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            wantedLocationButton.setBackground(currentButton.getBackground());
        }
        wantedLocationButton.setTag(currentButton.getTag());
        gameLogic.getAvailableCells().remove(wantedLocationButton);
        gameLogic.removeButton(currentButton);
        if (!gameLogic.checkSuccessionAllDirections(wantedLocationButton)) {
            gameLogic.createThreeBalls(tablelayout); //creates new balls.
            randomNextBalls();
        }
    }

    private void gameOver() {
        Toast.makeText(this, "have luck next time", Toast.LENGTH_SHORT).show();
        gameOverDialog.setContentView(R.layout.dialoggameover);
        gameOverDialog.setTitle("GAME  OVER!");
        Button btNewGame = gameOverDialog.findViewById(R.id.dialoggameover_btnewgame);
        gameOverDialog.show();
        btNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewGame();
            }
        });
    }

    private void animationAlgorithm(Button button) {
        if(button.getAnimation() == null) {
            if (!(gameLogic.getAvailableCells().contains(button))) {
                //an animation can be valid to balls only. not empty cells.
                gameLogic.clearAllAnimation(); //clearing all animation
                button.startAnimation(animationBounce);
            }
        }
        else {
            gameLogic.clearAllAnimation();
        }
    }

    private boolean isMovementPossible(Button currentButton, Button wantedLocationButton) {
        return new PathAlgorithm(((GameButton)currentButton).getBtnId(), ((GameButton)wantedLocationButton).getBtnId(), gameLogic).checkPath();

    }

    public void randomNextBalls() {
        Random random = new Random();
        int [] colors = {R.drawable.blueball, R.drawable.greenball, R.drawable.purpleball, R.drawable.redball, R.drawable.yellowball};
        int  [] threeRandomColors = {colors[random.nextInt(colors.length)], colors[random.nextInt(colors.length)], colors[random.nextInt(colors.length)]};
        gameLogic.setColorToThreeBalls(threeRandomColors);
        ImageView [] threeBalls = {imageViewLeftBall, imageViewCenterBall,  imageViewRightBall};
        for(int i = 0; i < 3; i++) {
            threeBalls[i].setImageResource(threeRandomColors[i]);
        }
    }
}

