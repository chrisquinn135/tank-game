package tankrotationexample.game.immovable;

import tankrotationexample.game.Observable;
import tankrotationexample.game.Observer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends ImmovableObjects {

    private int health = 2;

    // create the wall
    public BreakableWall(int x, int y, BufferedImage img) {
        super(x,y,img);
    }

    // get health of the wall
    public int getHealth() {
        return this.health;
    }

    // the wall gets hit
    public void hitWall() {
        if(this.getHealth() > 1) {
            --this.health;
        } else {
            setHealth(0);
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void drawImage(Graphics g) {
        if (isDrawable()) {
            super.drawImage(g);
        }
    }

    @Override
    public boolean isDrawable() {
        if(this.health != 0) {
            return true;
        } else {
            return false; }
    }
}
