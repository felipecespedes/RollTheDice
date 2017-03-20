package com.example.felipe.rollthedice;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ImageView imgLeftDice, imgRightDice;
    private Button btnAction;
    private TextView txtResult;
    private ToggleButton toggleColor;

    private int resources[];
    private DiceThread diceThread;

    private int selectedLeftDice, selectedRightDice;
    private enum DICE_COLOR {
        RED,
        GREEN
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imgLeftDice = (ImageView) findViewById(R.id.img_dice_left);
        this.imgRightDice = (ImageView) findViewById(R.id.img_dice_right);

        this.btnAction = (Button) findViewById(R.id.btn_action);

        this.txtResult = (TextView) findViewById(R.id.txt_result);

        this.toggleColor = (ToggleButton) findViewById(R.id.toggle_color);

        this.init();
    }

    private void init() {

        this.resources = new int[6];

        this.btnAction.setText(getResources().getString(R.string.roll_dice));

        // Initialize red dices
        this.changeDiceColor(DICE_COLOR.RED);
    }

    public void toggleColor(View v) {

        if (this.toggleColor.isChecked()) {

            this.changeDiceColor(DICE_COLOR.GREEN);
        }
        else {

            this.changeDiceColor(DICE_COLOR.RED);
        }
    }

    public void action(View v) {

        if (this.diceThread == null) {
            this.diceThread = new DiceThread(this);
            this.diceThread.start();
            this.btnAction.setText(getResources().getString(R.string.throw_dice));

            return;
        }

        if (this.diceThread.isAlive()) {

            if (this.diceThread.wait) {

                this.btnAction.setText(getResources().getString(R.string.throw_dice));
                this.clearDisplay();

                synchronized (this.diceThread) {
                    this.diceThread.notify();
                }

            } else {

                this.btnAction.setText(getResources().getString(R.string.roll_dice));
                this.diceThread.wait = true;

                this.displayResult();
            }

        }

    }

    public void changeDices(int leftDiceResource, int rightDiceResource) {

        this.selectedLeftDice = leftDiceResource;
        this.selectedRightDice = rightDiceResource;

        this.imgLeftDice.setImageResource(this.resources[this.selectedLeftDice]);
        this.imgRightDice.setImageResource(this.resources[this.selectedRightDice]);

        // Set results in numbers
        int result = (this.selectedLeftDice + 1) + (this.selectedRightDice + 1);
        this.txtResult.setText(getResources().getString(R.string.result) + ": " + result);
    }

    private void changeDiceColor(DICE_COLOR color) {

        if (color == DICE_COLOR.RED) {

            this.resources[0] = R.mipmap.red_dice_one;
            this.resources[1] = R.mipmap.red_dice_two;
            this.resources[2] = R.mipmap.red_dice_three;
            this.resources[3] = R.mipmap.red_dice_four;
            this.resources[4] = R.mipmap.red_dice_five;
            this.resources[5] = R.mipmap.red_dice_six;

            this.toggleColor.setText(getResources().getString(R.string.red_color));
            this.toggleColor.setBackgroundColor(Color.parseColor("#d32f2f"));

        } else if (color == DICE_COLOR.GREEN) {

            this.resources[0] = R.mipmap.green_dice_one;
            this.resources[1] = R.mipmap.green_dice_two;
            this.resources[2] = R.mipmap.green_dice_three;
            this.resources[3] = R.mipmap.green_dice_four;
            this.resources[4] = R.mipmap.green_dice_five;
            this.resources[5] = R.mipmap.green_dice_six;

            this.toggleColor.setText(getResources().getString(R.string.green_color));
            this.toggleColor.setBackgroundColor(Color.parseColor("#388E3C"));
        }

        this.imgLeftDice.setImageResource(this.resources[this.selectedLeftDice]);
        this.imgRightDice.setImageResource(this.resources[this.selectedRightDice]);
    }

    private void clearDisplay() {

        this.txtResult.setText(null);
        this.txtResult.setVisibility(View.INVISIBLE);
    }

    private void displayResult() {

        this.txtResult.setVisibility(View.VISIBLE);
    }

}
