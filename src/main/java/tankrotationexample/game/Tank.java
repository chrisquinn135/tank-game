package tankrotationexample.game;



import tankrotationexample.Launcher;
import tankrotationexample.game.immovable.PowerUp;
import tankrotationexample.game.movable.Bullet;
import tankrotationexample.game.movable.MovableObjects;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class Tank extends MovableObjects implements Observer, Sound{

    private final int originalX;
    private final int originalY;
    private int R = 2;                            //speed
    private final float ROTATIONSPEED = 3.0f;           // rotation speed

    private int health = 5;
    private int life = 3;
    Timer timer = new Timer();

    private final ArrayList<Bullet> ammo;

    private boolean powerUpTriple = false;

    private boolean UpPressed;              // buttons pressed
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super(x,y,vx,vy,angle,img);          // constructor for the tank
        this.originalX = x;
        this.originalY = y;
        this.ammo = new ArrayList<>();
    }

    ArrayList<Bullet> getAmmo() { return ammo; }

    void toggleUpPressed() {
        this.UpPressed = true;
    }                       // for when the keys are pressed

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() { this.ShootPressed = true; }

    void unToggleShootPressed() { this.ShootPressed = false; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    @Override
    public void update(Observable obv) {                     // every new frame draw this is activated
        if (this.UpPressed) {
            this.moveForwards(R);
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed && TRE.tickCount % 40 == 0) {
            if(powerUpTriple) {
                Bullet b1 = new Bullet(super.getX()+super.getImg().getWidth()/2 - 12,super.getY() + super.getImg().getHeight()/2 - 12,super.getVx(), super.getVy(),super.getAngle(),Resource.getResourceImage("bullet"),this);
                Bullet b2 = new Bullet(super.getX()+super.getImg().getWidth()/2 - 12,super.getY() + super.getImg().getHeight()/2 - 12,super.getVx(), super.getVy(),super.getAngle() + 30,Resource.getResourceImage("bullet"),this);
                Bullet b3 = new Bullet(super.getX()+super.getImg().getWidth()/2 - 12,super.getY() + super.getImg().getHeight()/2 - 12,super.getVx(), super.getVy(),super.getAngle() - 30,Resource.getResourceImage("bullet"),this);
                this.ammo.add(b1);
                this.ammo.add(b2);
                this.ammo.add(b3);
            } else {
                Bullet b = new Bullet(super.getX()+super.getImg().getWidth()/2 - 12,super.getY() + super.getImg().getHeight()/2 - 12,super.getVx(), super.getVy(),super.getAngle(),Resource.getResourceImage("bullet"),this);
                this.ammo.add(b);
            }
        }

        List<Bullet> toRemove = new ArrayList();
        for (Bullet b : ammo) {
            if(b.getHitBox().intersects(this.getHitBox())) {
                b.update(obv);
            }
            else {
                b.hidden();
                b.update(obv);
            }

            // need to figure out a way to delete the bullet or at least remove the thing
            if(!b.isDrawable()) {
                toRemove.add(b);
            }
        }
        ammo.removeAll(toRemove);
    }

    // rotating the tank
    private void rotateLeft() {
        super.setAngle(super.getAngle() - this.ROTATIONSPEED);
    }

    private void rotateRight() {
        super.setAngle(super.getAngle() + this.ROTATIONSPEED);
    }

    // move forward or backwards
    private void moveBackwards() {
            super.setVx((int) Math.round(R * Math.cos(Math.toRadians(super.getAngle()))));
            super.setVy((int) Math.round(R * Math.sin(Math.toRadians(super.getAngle()))));
            super.setX(super.getX() - super.getVx());
            super.setY(super.getY() - super.getVy());
            // keeps the tank on screen
            super.checkBorder();
            moveBound();
    }

    @Override
    public void moveForwards(int R) {
        super.moveForwards(R);
        moveBound();

    }

    @Override
    public void handleCollision(Collidable c2) {
        if(c2 instanceof Bullet) {
            this.hitTank();
            c2.handleCollision(this);
        } else if (c2 instanceof PowerUp){
            switch(((PowerUp) c2).getType()) {
                case 1:
                    powerUpTriple = true;
                    timer.schedule( new TimerTask() {
                        public void run() {
                            powerUpTriple = false;
                        }
                        },30*1000,30*1000);
                    break;
                case 2:
                    this.health = 5;
                    break;
                case 3:
                    R = 6;
                    timer = new Timer();
                    timer.schedule( new TimerTask() {
                        public void run() {
                            R = 2;
                        }
                    },3*1000,3*1000);
                    break;
                case 0:
                    break;
            }
        } else{
            int newX = this.getX();
            int newY = this.getY();
            if(this.DownPressed) {
                newX += this.getVx();
                newY += this.getVy();
            }
            if(this.UpPressed) {
                newX -= this.getVx();
                newY -= this.getVy();
            }
            this.setX(newX);
            this.setY(newY);
        }
    }

    public int getLife() { return this.life; }

    public void hitTank() {
        if(this.health > 1) {
            this.smallExp();
            --this.health;
        } else if (this.life > 1){
            this.largeExp();
            super.setX(originalX);
            super.setY(originalY);
            powerUpTriple = false;
            this.R = 2;
            this.health = 5;
            --this.life;
            super.setHitBox();
        } else {
            this.largeExp();
            this.life = 0;
        }
    }

    //moves the hitbox
    private void moveBound() {
        super.setHitBox();
    }


    @Override
    public String toString() {
        return "x=" + super.getX() + ", y=" + super.getY() + ", angle=" + super.getAngle();
    }

    // draw things; might need to add a lot of stuff
    @Override
    public void drawImage(Graphics g) {
        if(isDrawable()) {
            super.drawImage(g);
            for(Bullet b : ammo) {
                b.drawImage(g);
            }
            g.setColor(Color.WHITE);
            g.fillRect(super.getX(), super.getY() - 20, 50, 8);
            g.setColor(Color.GREEN);
            g.fillRect(super.getX(), super.getY() - 20, 10 * health, 8);
        }
    }


    @Override
    public boolean isDrawable() {
        return life != 0;
    }
}
