package tankrotationexample.game.immovable;

import tankrotationexample.game.immovable.ImmovableObjects;

import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class PowerUp extends ImmovableObjects {

    int type;
    int ogType;
    boolean used = true;

    public PowerUp(int x, int y, BufferedImage img, int type) {
        super(x, y, img);
        this.type = type;
        this.ogType = type;
    }

    public int getType() { return this.type;}

    public void gotUsed() {
        if(used) {
            used = false;
            type = 0;
            Timer timer = new Timer();
            timer.schedule( new TimerTask() {
                public void run() {
                    used = true;
                    type = ogType;
                }
            },30*1000,30*1000);
        }
    }

    @Override
    public boolean isDrawable() {
        return used;
    }
}


