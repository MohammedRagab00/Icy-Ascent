package Models;

import java.util.ArrayList;

public class GameState {
    public GameState() {
        paused = false;
        speedFactor = 1;
        isMultipalyer = false;
        long start = System.currentTimeMillis();
        LastHomeGeneratedTime = start;
        LastPlaneGenerateTime = start;
        timeStart = start;
        fts = start;
        tte = start;
        Timer = 0;
    }
    
    public long pausedTime;
    public long Timer;
    public boolean paused;

    public String firstPlayerName = null;
    public String secondPlayerName;

    public int currentPlayer = 1;
    public boolean isMultipalyer = false;

    public int firstPlayerScore = 0;
    public int secondPlayerScore = 0;
    public ArrayList<GameObject> plans = new ArrayList<>();
    public ArrayList<GameObject> ships = new ArrayList<>();
    public ArrayList<GameObject> homes = new ArrayList<>();
    public ArrayList<GameObject> fuelTanks = new ArrayList<>();

    public int xBullet;
    public int yBullet;
    public int starttemp;
    public boolean flag;
    public long timeStart;
    public long currentTime;
    private double _speed;

    public long tts;
    public long tte;
    public long fts;
    public long fte;
    public long LastPlaneGenerateTime;
    public long pte;
    public long LastHomeGeneratedTime;
    public long hte;
    public boolean fired;
    public int firstPlayerLives = 3;
    public int secondPlayerLives = 3;
    public int tank = 100;
    public int planeIndex = 4;
    public int x = 45, y = 10;
    public double speedFactor;

    public void setSpeed(double speed) {
        _speed = speed;
    }

    public int getSpeed() {
        return (int) Math.ceil(_speed * speedFactor);
    }

    public String getCurrentPlayerName() {
        if (currentPlayer == 1) {
            return firstPlayerName;
        }
        return secondPlayerName;
    }

    public int getCurrentPlayerScore() {
        if (currentPlayer == 1) {
            return firstPlayerScore;
        }
        return secondPlayerScore;
    }

    public void setCurrentPlayerScore(int score) {
        if (currentPlayer == 1) {
            firstPlayerScore = score;
        } else {
            secondPlayerScore = score;
        }
    }

    public int getCurrentPlayerLives() {
        if (currentPlayer == 1) {
            return firstPlayerLives;
        }
        return secondPlayerLives;
    }

    public void setCurrentPlayerLives(int score) {
        if (currentPlayer == 1) {
            firstPlayerLives = score;
        } else {
            secondPlayerLives = score;
        }
    }

    public void newGame(){
        this.firstPlayerScore = 0;
        this.tank = 100;
        this.firstPlayerLives = 3;
        this.secondPlayerLives = 3;
        this.secondPlayerScore = 0;
        this.flag = true;
        this.plans = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.homes = new ArrayList<>();
        this.fuelTanks = new ArrayList<>();
        long start = System.currentTimeMillis();
        this.LastHomeGeneratedTime = start;
        this.LastPlaneGenerateTime = start;
        this.timeStart = start;
        this.fts = start;
        this.tte = start;
        this.paused = false;
        this.x = 45;
        this.planeIndex = 4;
        this.Timer = 0;
    }
}
