/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.game.immovable.BreakableWall;
import tankrotationexample.game.immovable.ImmovableObjects;
import tankrotationexample.game.immovable.PowerUp;
import tankrotationexample.game.immovable.UnBreakableWall;
import tankrotationexample.game.movable.Bullet;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class    TRE extends JPanel implements Runnable, Observable,Sound {

    // tank rules

    // creates the world, need to add the walls
    private BufferedImage world;
    private Tank t1;
    private Tank t2;

    private Clip music;

    //observable
    private List<Observer> observers;
    // keeps track of how many times the game has been run
    static long tickCount = 0;

    private List<GameObject> gameObjects;

    private Launcher lf;
    // you can use this as time counter, animation
    private long tick = 0;

    public TRE(Launcher lf){
        this.lf = lf;
    }

    @Override
    public void run(){
       try {
           this.resetGame();
           while (true) {
                this.tick++;
                this.notifyObservers();
                this.checkCollisions();
                tickCount++;
                this.repaint();   // redraw game

                Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when drawn 2000 frames have been drawn
                 */
                if(t1.getLife() == 0) {
                    Thread.sleep(2000);
                    music.stop();
                    this.lf.setFrame("Player2Wins");
                    return;
                }
               if(t2.getLife() == 0) {
                   Thread.sleep(2000);
                   music.stop();
                   this.lf.setFrame("Player1Wins");
                   return;
               }
            }
       } catch (InterruptedException ignored) {
           System.out.println(ignored);
       }
    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.tick = 0;
        try {
            AudioInputStream music1 = AudioSystem.getAudioInputStream(TRE.class.getClassLoader().getResource("Music.wav"));
            music = AudioSystem.getClip();
            music.open(music1);
            music.start();
            music.loop(5);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        gameInitialize();
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        // creates the world (need to add the walls)
        // eventually needs to change game_screen_width to world screen width
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                                       GameConstants.WORLD_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);

        gameObjects = new ArrayList();
        observers = new ArrayList();

        
        
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            InputStreamReader isr = new InputStreamReader(TRE.class.getClassLoader().getResourceAsStream("MAP-1.csv"));
            BufferedReader mapReader  = new BufferedReader(isr);

            // get the amount of rows in the map
            String row = mapReader.readLine();
            String[] mapInfo = row.split(",");
            int numColumns = Integer.parseInt(mapInfo[0]);

            int numRows = Integer.parseInt(mapInfo[1]);

            // read the map and put into array list
            for(int j = 0; j < numRows; j++) {
                row = mapReader.readLine();
                mapInfo = row.split(",");

                for(int i = 0; i < numColumns; i++) {
                    switch(mapInfo[i]) {
                        case "2":
                            addGameObject(new BreakableWall(i*30,j*30,Resource.getResourceImage("breakWallImg")));
                            break;
                        case "5":
                            Random x = new Random();            // randomly generates the powerups
                            int z = x.nextInt(3) + 1;
                            switch (z) {
                                case 1:
                                    addGameObject(new PowerUp(i*30,j*30,Resource.getResourceImage("powerUpBullet"),z));
                                    break;
                                case 2:
                                    addGameObject(new PowerUp(i*30,j*30,Resource.getResourceImage("powerUpShield"),z));
                                    break;
                                case 3:
                                    addGameObject(new PowerUp(i*30,j*30,Resource.getResourceImage("powerUpSpeed"),z));
                                    break;
                            }
                            break;
                        case "3":
                        case "9":
                            addGameObject(new UnBreakableWall(i*30,j*30,Resource.getResourceImage("unBreakWallImg")));
                            break;
                        default:
                            // adding a floor makes the ground way to laggy yikes
                            //this.gameObjects.add(new Floor(i*30,j*30,Resource.getResourceImage("floor")));
                            break;
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(500, 500, 0, 0, 0, Resource.getResourceImage("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(2010 - 550, 500, 0, 0, 180, Resource.getResourceImage("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        attachObserver(t1);
        attachObserver(t2);
        addGameObject(t1);
        addGameObject(t2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        // sets the color of the background
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0,0,GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);



        for(int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).drawImage(buffer);
        }

        BufferedImage leftHalf = checkDimension(t1);
        BufferedImage rightHalf = checkDimension(t2);

        BufferedImage miniMap = world.getSubimage(0,0,GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT); // creates the mini map

        // get the small version of the map
        g2.drawImage(leftHalf,0,0,null);
        g2.drawImage(rightHalf,GameConstants.SCREEN_WIDTH/2 + 5,0,null); // code the border

        g2.scale(.12, .12);                                                     // does the mini map drawing
        g2.drawImage(miniMap, (int) ((double)GameConstants.SCREEN_WIDTH / .24 - miniMap.getWidth() / 2), (int) ((double)GameConstants.SCREEN_HEIGHT / .24 - miniMap.getWidth() / 2),null);

        for(int k = 0; k < t1.getLife(); k++) {
            g2.drawImage(Resource.getResourceImage("TANKLIFE"),650 * k +200,7000,null);
        }
        for(int k = 0; k < t2.getLife(); k++) {
            g2.drawImage(Resource.getResourceImage("TANKLIFE"),- 650 * k + 10100,7000,null);
        }
    }

    private BufferedImage checkDimension(Tank tank) {
        BufferedImage checked;
        int x;
        int y;
        if(tank.getY()-GameConstants.SCREEN_HEIGHT/2 > 0 && tank.getY()+ GameConstants.SCREEN_HEIGHT/2 < GameConstants.WORLD_HEIGHT) {
            y = tank.getY()-GameConstants.SCREEN_HEIGHT/2;
        }
        else if (tank.getY()+GameConstants.SCREEN_HEIGHT/2 + tank.getHitBox().getHeight()< GameConstants.WORLD_HEIGHT){
            y = 0;
        }
        else {
            y = GameConstants.WORLD_HEIGHT - GameConstants.SCREEN_HEIGHT;
        }

        if(tank.getX()-GameConstants.SCREEN_WIDTH/4 > 0 && tank.getX()+ GameConstants.SCREEN_WIDTH/4 < GameConstants.WORLD_WIDTH) {

            x = tank.getX()-GameConstants.SCREEN_WIDTH/4;
        }
        else if (tank.getX()+GameConstants.SCREEN_WIDTH/4 + tank.getHitBox().getWidth()< GameConstants.WORLD_WIDTH){
            x = 0;
        }
        else {
            x = GameConstants.WORLD_WIDTH - GameConstants.SCREEN_WIDTH /2 ;
        }
        checked = world.getSubimage(x,y,GameConstants.SCREEN_WIDTH/2, GameConstants.SCREEN_HEIGHT);

        return checked;
    }

    private void checkCollisions() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject c1 = gameObjects.get(i);
            if(!c1.isCollidable()) continue;
            if(c1 instanceof ImmovableObjects) continue;
            for (int j = 0; j < gameObjects.size(); j ++) {
                if(j == i) continue;
                GameObject c2 = gameObjects.get(j);
                if(!c2.isCollidable()) continue;

                if(c1.getHitBox().getBounds().intersects(c2.getHitBox().getBounds())) {
                    c1.handleCollision(c2);
                    if(c1 instanceof PowerUp) {
                        ((PowerUp) c1).gotUsed();
                    }
                    if(c2 instanceof PowerUp) {
                        ((PowerUp) c2).gotUsed();
                    }
                }
                if (c1 instanceof Tank) {
                    final Tank t = (Tank) c1;
                    for(Bullet b : t.getAmmo()) {
                        if(c2.getHitBox().getBounds().intersects(b.getHitBox().getBounds())) {
                            b.handleCollision(c2);
                            if(c2 instanceof BreakableWall) {
                                ((BreakableWall) c2).hitWall();
                                if(((BreakableWall) c2).getHealth() == 0) {
                                    removeGameObject(c2);
                                }
                            }
                        }
                    }
                }
                if(c2 instanceof Tank) {
                    final Tank t = (Tank) c2;
                    for(Bullet b : t.getAmmo()) {
                        if(c1.getHitBox().getBounds().intersects(b.getHitBox().getBounds())) {
                            b.handleCollision(c1);
                            if(c1 instanceof BreakableWall) {
                                ((BreakableWall) c1).hitWall();
                                if(((BreakableWall) c1).getHealth() == 0) {
                                    removeGameObject(c1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void addGameObject(GameObject x) { this.gameObjects.add(x); }
    public void removeGameObject(GameObject x) { this.gameObjects.remove(x);
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < this.observers.size();i++) {
            if(!((Drawable) this.observers.get(i)).isDrawable()) {
                System.out.println("removed " + this.observers.get(i));
                this.observers.remove(i);
                continue;
            }
            this.observers.get(i).update(this);

        }
    }

    @Override
    public void attachObserver(Observer obv) {
        this.observers.add(obv);
    }

    @Override
    public void detachObserver(Observer obv) {
        this.observers.remove(obv);
    }
}
