package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

class JToast extends JFrame {

    private final float MAX_OPACITY = 0.8f;
    private final float OPACITY_INCREMENT = 0.05f;
    private final int FADE_REFRESH_RATE = 20;

    private final int WINDOW_RADIUS = 15;
    private final int CHARACTER_LENGTH_MULTIPLIER = 9;
    private final int DISTANCE_FROM_PARENT_BOTTOM = 100;

    private Color backgroundColor;


    public JToast(JFrame owner, String toastText,Color color) {

        if(color==null){
            backgroundColor = new Color(0, 0, 0, 170);
        } else {
            backgroundColor = color;
        }

        setTitle("Transparent JFrame Demo");
        setLayout(new GridBagLayout());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setFocusableWindowState(false);

        setOpacity(0.4f);

        JLabel b1 = new JLabel(toastText);
        b1.setForeground(Color.WHITE);
        b1.setOpaque(false);
        add(b1);

        setSize(toastText.length() * CHARACTER_LENGTH_MULTIPLIER, 50);

        int x = (int) (owner.getLocation().getX() + (owner.getWidth() / 2) - (this.getWidth()/2));
        int y = (int) (owner.getLocation().getY() + owner.getHeight() - DISTANCE_FROM_PARENT_BOTTOM);
        setLocation(new Point(x, y));

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        getContentPane().setBackground(backgroundColor);

    }


    public void fadeIn() {
        setOpacity(0);
        setVisible(true);

        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 0;


            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += OPACITY_INCREMENT;
                setOpacity(Math.min(opacity, MAX_OPACITY));
                if (opacity >= MAX_OPACITY) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }


    public void fadeOut() {
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = MAX_OPACITY;


            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= OPACITY_INCREMENT;
                setOpacity(Math.max(opacity, 0));
                if (opacity <= 0) {
                    timer.stop();
                    setVisible(false);
                    dispose();
                }
            }
        });

        setOpacity(MAX_OPACITY);
        timer.start();
    }


    public static void makeToast(final JFrame owner, final String toastText, final int durationSec,Color color) {
        new Thread(() -> {
            try {
                JToast toastFrame = new JToast(owner, toastText,color);
                toastFrame.fadeIn();
                Thread.sleep(durationSec * 1000);
                toastFrame.fadeOut();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}