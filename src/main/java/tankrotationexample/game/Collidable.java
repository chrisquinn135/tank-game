package tankrotationexample.game;

import tankrotationexample.game.immovable.ImmovableObjects;
import tankrotationexample.game.movable.Bullet;

import java.awt.*;

public interface Collidable {

    default boolean isCollidable() {
        if(this instanceof Collidable) {
            return true;
        }
        return false;
    }

    Rectangle getHitBox();

    default void handleCollision(Collidable c2) {
    }


}
