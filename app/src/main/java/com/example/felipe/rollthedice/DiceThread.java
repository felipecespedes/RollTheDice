package com.example.felipe.rollthedice;

/**
 * Created by Felipe Céspedes on 3/18/17.
 */

public class DiceThread extends Thread {

    private final int SLEEP_TIME = 50;

    private MainActivity activity;
    private boolean next;

    private int randomLeftDice, randomRightDice;

    private boolean wait;

    public DiceThread(MainActivity activity) {
        this.activity = activity;
        this.next = true;
        this.wait = false;
    }

    @Override
    public synchronized void run() {

        while (this.next) {

            if (this.isWaiting()) {
                try {
                    wait();
                    this.setWaitState(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.randomLeftDice = generateRandomNum(0, 5);
            this.randomRightDice = generateRandomNum(0, 5);

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    activity.changeDices(randomLeftDice, randomRightDice);
                }
            });

            try {
                this.sleep(this.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public boolean isWaiting() {
        return wait;
    }

    public void setWaitState(boolean wait) {
        this.wait = wait;
    }

    private int generateRandomNum(int min, int max) {

        return min + (int) (Math.random() * (max + 1));
    }

}
