package tankrotationexample.game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javax.imageio.ImageIO.read;

public class Resource {

    private static Map<String, BufferedImage> resources;

    static {
        Resource.resources = new HashMap<>();
        try {
            Resource.resources.put("tank1", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png"))));
            Resource.resources.put("tank2", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank2.png"))));
            Resource.resources.put("bullet", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Shell.png"))));
            Resource.resources.put("unBreakWallImg", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall1.gif"))));
            Resource.resources.put("breakWallImg", ImageIO.read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall2.gif"))));
            Resource.resources.put("powerUpBullet", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Pickup.gif"))));
            Resource.resources.put("powerUpShield", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Medkit.jpg"))));
            Resource.resources.put("powerUpSpeed", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Speed.jpg"))));
            Resource.resources.put("floor", read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp"))));
            Resource.resources.put("TANKLIFE",read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Tank1Health.png"))));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-5);
        }
    }

    public static BufferedImage getResourceImage(String key) {
        return Resource.resources.get(key);
    }
//


}
