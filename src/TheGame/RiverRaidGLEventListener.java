package TheGame;

import Config.Constants;
import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import Models.GameObject;
import Models.GameState;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import static Config.Constants.*;
import Screens.Lose;

public class RiverRaidGLEventListener extends KeyHandling implements GLEventListener {

    JFrame gameJframe;
    GameState gameState;
    GLUT g = new GLUT();
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final AudioManager audioManager = new AudioManager();

    public RiverRaidGLEventListener(JFrame gameJframe, GameState initialGameState) {
        this.gameJframe = gameJframe;
        this.gameState = initialGameState;
    }

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(Constants.assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        this.myInit();
    }

    public void myInit() {
        long start = System.currentTimeMillis();
        this.gameState.LastHomeGeneratedTime = start;
        this.gameState.LastPlaneGenerateTime = start;
        this.gameState.timeStart = start;
        this.gameState.fts = start;
        this.gameState.tte = start;
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        setPaused();
        DrawBackground(gl);
        drawStart(gl);

        if (!this.gameState.paused && this.gameState.getCurrentPlayerLives() > 0) {
            this.gameState.currentTime = System.currentTimeMillis();
        }

        if (!this.gameState.paused) {
            this.gameState.Timer += 1;
            handleKeyPress();
            generateAPlaneOrShip();
            generateHome();
            generateFuel();
            changePos();
            checkTank();
            CollisionManager.destroy(this);
            CollisionManager.remove(this);
            emptyTank();
        }

        drawPlansAndShipsAndHomes(gl);
        DrawSprite(gl, this.gameState.x, this.gameState.y, this.gameState.planeIndex, 0.8F, 0);

        gl.glRasterPos2f(-.97f, .9f);
        g.glutBitmapString(5, "Score: ");
        g.glutBitmapString(5, Integer.toString(this.gameState.getCurrentPlayerScore()));

        gl.glRasterPos2f(-.97f, .8f);
        g.glutBitmapString(5, "Timer: ");
        g.glutBitmapString(5, Long.toString(this.gameState.Timer / 25));

        gl.glRasterPos2f(-.97f, .7f);
        g.glutBitmapString(5, "Hearts: ");
        g.glutBitmapString(5, Integer.toString(this.gameState.getCurrentPlayerLives()));

        gl.glRasterPos2f(-.97f, .6f);
        g.glutBitmapString(5, "Tank: ");
        g.glutBitmapString(5, Integer.toString(this.gameState.tank));

        gl.glEnd();

        gl.glRasterPos2f(-.8f, .84f);
        if (this.gameState.getSpeed() < 10) {
            this.gameState.setSpeed(2 + this.gameState.getCurrentPlayerScore() / 4000);
        }

        this.gameState.starttemp++;
        this.gameState.starttemp = Math.min(this.gameState.starttemp, 150);

        if (this.gameState.fired) {
            DrawSprite(gl, this.gameState.xBullet, this.gameState.yBullet, 7, 0.1f, 0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale, float angle) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]); // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glRotatef(angle, 0, 1, 0);

        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        // System.out.println(x + " " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]); // Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void drawStart(GL gl) {
        DrawSprite(gl, 45, 45 - this.gameState.starttemp, 9, 10, 0);
    }

    public void generateAPlaneOrShip() {
        this.gameState.pte = System.currentTimeMillis();
        if ((this.gameState.pte - this.gameState.LastPlaneGenerateTime) + this.gameState.getSpeed() * 200L > 1500) {
            this.gameState.LastPlaneGenerateTime = this.gameState.pte;
            int temp = (int) (Math.random() * 44 + 23);
            double temp1 = Math.random();
            if (temp1 < 0.5) {
                this.gameState.plans.add(new GameObject(temp, Math.random() > 0.5));
            }
            if (temp1 > 0.5) {
                this.gameState.ships.add(new GameObject(temp, Math.random() > 0.5));

            }
        }
    }

    public void generateFuel() {
        this.gameState.fte = System.currentTimeMillis();
        if ((this.gameState.fte - this.gameState.fts) + this.gameState.getSpeed() * 200L > 5000) {
            this.gameState.fts = this.gameState.fte;
            this.gameState.fuelTanks.add(new GameObject((int) (Math.random() * 44 + 23), Math.random() > 0.5));
        }
    }

    public void emptyTank() {
        this.gameState.tte = System.currentTimeMillis();
        if (this.gameState.tte - this.gameState.tts > 400) {
            this.gameState.tts = this.gameState.tte;

            this.gameState.tank -= (int) (Math.random() * 3 + 1);
            this.gameState.tank = Math.max(this.gameState.tank, 0);
        }
    }

    public void checkTank() {
        if (this.gameState.tank < 1) {
            crashed();
        }
    }

    public void crashed() {
        try {
            audioManager.playCrashSound();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        this.gameState.x = 45;
        this.gameState.y = 10;
        this.gameState.tank = 100;
        this.gameState.fired = false;
        this.gameState.setCurrentPlayerLives(this.gameState.getCurrentPlayerLives() - 1);
        this.gameState.plans = new ArrayList<>();
        this.gameState.ships = new ArrayList<>();
        this.gameState.homes = new ArrayList<>();
        this.gameState.fuelTanks = new ArrayList<>();
        int firstPlayerScore = this.gameState.firstPlayerScore, secondPlayerScore = 0;
        boolean isMultiPlayer = false;
        if (this.gameState.getCurrentPlayerLives() == 0) {
            if (this.gameState.isMultipalyer && this.gameState.currentPlayer == 1) {
                this.startSecondPlayer();
                return;
            } else if (this.gameState.isMultipalyer) {

                secondPlayerScore = this.gameState.secondPlayerScore;

                isMultiPlayer = true;

            }
            this.gameState.paused = true;
            scoreBoard.updateHighScore(this.gameState);

            new Lose("Player1", "Player2", firstPlayerScore, secondPlayerScore, isMultiPlayer).setVisible(true);
            this.gameJframe.setVisible(false);
        }
        if (this.gameState.getCurrentPlayerLives() > 0) {
            this.gameState.starttemp = 0;
        }

    }

    public void drawPlansAndShipsAndHomes(GL gl) {
        for (GameObject plan : this.gameState.plans) {
            DrawSprite(gl, plan.x, plan.y, 1, 1, plan.left ? 0 : 180);
        }
        for (GameObject ship : this.gameState.ships) {
            DrawSprite(gl, ship.x, ship.y, 0, 1, ship.left ? 180 : 0);
        }
        for (GameObject home : this.gameState.homes) {
            DrawSprite(gl, home.x, home.y, 6, 1.5f, home.left ? 0 : 180);
        }
        for (GameObject full : this.gameState.fuelTanks) {
            DrawSprite(gl, full.x, full.y, 8, 1, full.left ? 0 : 180);
        }

    }

    public void generateHome() {

        this.gameState.hte = System.currentTimeMillis();
        if ((this.gameState.hte - this.gameState.LastHomeGeneratedTime) + this.gameState.getSpeed() * 300L > 3000) {
            this.gameState.LastHomeGeneratedTime = this.gameState.hte;
            int temp;
            if (Math.random() > 0.5) {
                temp = (int) (Math.random() * 11 + 1);
            } else {
                temp = (int) (Math.random() * 11 + 78);
            }
            this.gameState.homes.add(new GameObject(temp, Math.random() > 0.5));

        }
    }

    public void changePos() {
        for (GameObject plan : this.gameState.plans) {
            plan.y -= this.gameState.getSpeed();
            if (plan.y < 50) {
                if (plan.left && plan.x > 67) {
                    plan.left = false;
                }
                if (!plan.left && plan.x < 23) {
                    plan.left = true;
                }
                if (plan.left) {
                    plan.x += 2;
                } else {
                    plan.x -= 2;
                }
            }
            if (plan.y < 0) {
                plan.remove = true;
            }
        }
        for (GameObject ship : this.gameState.ships) {
            ship.y -= this.gameState.getSpeed();
            if (ship.y < 50) {
                if (ship.left && ship.x > 67) {
                    ship.left = false;
                }
                if (!ship.left && ship.x < 23) {
                    ship.left = true;
                }
                if (ship.left) {
                    ship.x++;
                } else {
                    ship.x--;
                }
            }
            if (ship.y < 0) {
                ship.remove = true;
            }
        }
        for (GameObject home : this.gameState.homes) {
            home.y -= this.gameState.getSpeed();
            if (home.y < 0) {
                home.remove = true;
            }
        }
        for (GameObject full : this.gameState.fuelTanks) {
            full.y -= this.gameState.getSpeed();
            if (full.y < 0) {
                full.remove = true;
            }
        }
        if (this.gameState.fired) {
            this.gameState.yBullet += 5;
        }
    }

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (this.gameState.x > 20) {
                this.gameState.x -= 3;
                this.gameState.planeIndex = 3;
            }
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (this.gameState.x < maxWidth - 30) {
                this.gameState.x += 3;
                this.gameState.planeIndex = 5;
            }
        } else {
            this.gameState.planeIndex = 4;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (this.gameState.yBullet > 90 || !this.gameState.fired) {
                this.gameState.xBullet = this.gameState.x;
                this.gameState.yBullet = 10;
                this.gameState.fired = true;
                try {
                    audioManager.playShotSound();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            this.gameState.setSpeed(this.gameState.getSpeed() + 2);
        }

    }

    public void setPaused() {
        if (isKeyPressed(KeyEvent.VK_P) && this.gameState.getCurrentPlayerLives() > 0) {
            this.gameState.paused = !this.gameState.paused;
            this.gameState.pausedTime = System.currentTimeMillis();
        }
    }

    public void startSecondPlayer() {
        this.gameState.currentPlayer = 2;
        this.gameState.Timer = 0;
        this.gameState.currentTime = System.currentTimeMillis();
        this.gameState.pausedTime = System.currentTimeMillis();

        this.keyBits = new BitSet(256);
        String MSG = "HI " + this.gameState.getCurrentPlayerName() + " , Are You Ready?";
        JOptionPane.showConfirmDialog(null, MSG, "Ready MSG?", JOptionPane.YES_OPTION);
    }
}
