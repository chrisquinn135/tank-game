package tankrotationexample.game.movable;

import tankrotationexample.GameConstants;
import tankrotationexample.game.Collidable;
import tankrotationexample.game.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class MovableObjects extends GameObject implements Collidable {

    private int x;
    private int y;
    private int vx, vy;
    private float angle;
    private BufferedImage img;
    Rectangle hitBox;

    public MovableObjects(int x, int y, int vx, int vy, float angle, BufferedImage img) {
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.hitBox = new Rectangle(x,y,this.img.getWidth(), this.img.getHeight());
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getAngle() { return this.angle; }

    public int getVx() { return this.vx; }

    public int getVy() { return this.vy; }

    public BufferedImage getImg() { return this.img; }

    public void setX(int x){ this.x = x; }

    public void setY(int y) { this. y = y;}

    public void setAngle(float angle) { this.angle = angle;}

    public void setVx(int vx) { this.vx = vx; }

    public void setVy(int vy) { this.vy = vy; }

    public void setHitBox() {
        this.hitBox.setLocation(this.x, this.y);
    }

    public void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.WORLD_WIDTH - 88) {
            x = GameConstants.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.WORLD_HEIGHT - 80) {
            y = GameConstants.WORLD_HEIGHT - 80;
        }
    }

    public void moveForwards(int R) {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(getX(), getY());
        rotation.rotate(Math.toRadians(getAngle()), getImg().getWidth() / 2.0, getImg().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }

}
