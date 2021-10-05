package tankrotationexample.menus;

import tankrotationexample.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player1WinsPanel extends JPanel {

    private BufferedImage menuBackground;
    private JButton start;
    private JButton exit;
    private Launcher lf;

    public Player1WinsPanel(Launcher lf) {                        // lf is a launcher
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Player1Wins.png"));       // starts the game
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);                                        // sets background black
        this.setLayout(null);


        exit = new JButton("Congrats!!");                                             // exits game
        exit.setSize(new Dimension(200,100));
        exit.setFont(new Font("Courier New", Font.BOLD ,24));
        exit.setBounds(150,400,150,50);
        exit.addActionListener((actionEvent -> {
            this.lf.setFrame("end");
        }));

        this.add(exit);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }
}

