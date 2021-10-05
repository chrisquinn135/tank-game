package tankrotationexample.game.immovable;

import tankrotationexample.game.Collidable;
import tankrotationexample.game.immovable.ImmovableObjects;

import java.awt.image.BufferedImage;

public class UnBreakableWall extends ImmovableObjects implements Collidable {

    public UnBreakableWall(int x, int y, BufferedImage img) {
        super(x, y ,img);
    }

    @Override
    public boolean isDrawable() {
        return true;
    }
}
