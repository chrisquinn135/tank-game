package tankrotationexample.game.movable;

import tankrotationexample.GameConstants;
import tankrotationexample.game.Collidable;
import tankrotationexample.game.Observable;
import tankrotationexample.game.Observer;
import tankrotationexample.game.Tank;
import tankrotationexample.game.immovable.BreakableWall;
import tankrotationexample.game.immovable.ImmovableObjects;
import tankrotationexample.game.immovable.UnBreakableWall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends MovableObjects implements Observer {
    private int R = 6;
    private boolean isAlive = true;
    private boolean hidden = true;
    private Tank myTank;
    //BufferedImage bulletImg;
    //Rectangle hitBox;

    public Bullet(int x, int y, int vx, int vy, float angle, BufferedImage bulletImg, Tank t) {
        super(x, y, vx, vy, angle, bulletImg);
        this.myTank = t;
        //hitBox = new Rectangle(x,y,this.bulletImg.getWidth(), this.bulletImg.getHeight());
    }

    public void hidden() {
        hidden = false;
    }

    public boolean myTankCheck(Tank check) {
        if(this.myTank.equals(check)) {
            return true;
        }
        else {
            return false;
        }
    }

    // have to find a way to use usper and not this
    @Override
    public void moveForwards(int R) {
        super.moveForwards(R);
        super.setHitBox();
    }

    @Override
    public void update(Observable obv) {
        moveForwards(R);
    }

    @Override
    public void checkBorder() {
        if (super.getX() < 30) {
            super.setX(30);
            isAlive = false;
        }
        if (super.getX() >= GameConstants.WORLD_WIDTH - 88) {
           super.setX(GameConstants.WORLD_WIDTH - 88);
           isAlive = false;
        }
        if (super.getY() < 40) {
            super.setY(40);
            isAlive = false;
        }
        if (super.getY() >= GameConstants.WORLD_HEIGHT - 80) {
            super.setY(GameConstants.WORLD_HEIGHT - 80);
            isAlive = false;
        }
    }

    @Override
    public void drawImage(Graphics g) {
        if(isDrawable() && hidden == false) {   //hidden == false
            super.drawImage(g);
        }
    }

    @Override
    public void handleCollision(Collidable c2) {
        if(isAlive) {
            if(c2 instanceof Tank) {
                if(!myTankCheck((Tank) c2)) {
                    ((Tank) c2).hitTank();
                    isAlive = false;
                }
            } else if(c2 instanceof BreakableWall) {
                isAlive = false;
            } else if (c2 instanceof UnBreakableWall) {
                isAlive = false;
            }
        }
    }

    @Override
    public boolean isDrawable() {
        return isAlive;
    }
}
