package tankrotationexample.game.immovable;

import tankrotationexample.game.Collidable;
import tankrotationexample.game.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public abstract class ImmovableObjects extends GameObject implements Collidable {

    // position of the object
    private int x;
    private int y;

    // image of the object
    private BufferedImage img;
    private Rectangle hitBox;

    public ImmovableObjects(int x, int y, BufferedImage img) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x,y,this.img.getWidth(),this.img.getHeight());
    }

    @Override
    public Rectangle getHitBox() { return this.hitBox.getBounds(); }

    public void drawImage(Graphics g) {
        if(isDrawable()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, x,y, null);
        }
    }

}
