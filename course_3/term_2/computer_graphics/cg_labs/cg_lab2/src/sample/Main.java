package sample;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.lang.reflect.Array;
import javax.swing.*;

@SuppressWarnings("serial")
class Skeleton extends JPanel implements ActionListener {
    private static int maxWidth;
    private static int maxHeight;

    // for movement animation
    private double tx = 10;
    private double ty = 10;

    private double counter = 0;

    private double angle = 0;

    private int radius = 370;
    private int radiusExtention = 100;

    Timer timer;

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);


        g2d.setBackground(new Color(0, 128, 128));
        g2d.clearRect(0, 0, maxWidth, maxHeight);


        g2d.translate(maxWidth/2, maxHeight/2);
        BasicStroke bs3 = new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs3);
        g2d.drawRect(
                -(radius + radiusExtention),
                -(radius + radiusExtention),
                (radius + radiusExtention)*2,
                (radius + radiusExtention)*2
        );

        g2d.translate(tx, ty);


        g2d.setPaint(new Color(0, 255, 0));
        double bodyPoints[][] = {
                { -369, 117 }, { -259, 30  },
                { -91, 105 }, { -198, 151 },
                { -162, 224 }, { -313, 235 },
        };
        GeneralPath body = new GeneralPath();
        body.moveTo(bodyPoints[0][0], bodyPoints[0][1]);
        for (int k = 1; k < bodyPoints.length; k++)
            body.lineTo(bodyPoints[k][0], bodyPoints[k][1]);
        body.closePath();

        g2d.rotate(angle, body.getBounds2D().getCenterX(),
                body.getBounds2D().getCenterY());


        g2d.fill(body);

        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        Polygon tail = new Polygon();
        tail.addPoint(-175, 151);
        tail.addPoint(-108, 135);
        tail.addPoint(-150, 205);

        GradientPaint gp = new GradientPaint(-108, 151,
                Color.YELLOW, -175, 135, Color.GREEN, true);
        g2d.setPaint(gp);

        g2d.drawPolygon(tail);

        g2d.fillPolygon(tail);

        g2d.setPaint(Color.BLACK);

        BasicStroke bs1 = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs1);
        g2d.drawLine(-369, 117, -198, 151);

        BasicStroke lines = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(lines);
        g2d.drawLine(-411, 35, -341, 97);
        g2d.drawLine(-421, 205,  -340, 176);


        BasicStroke bs2 = new BasicStroke(0, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs2);
        g2d.setPaint(new Color(0, 128, 0));
        g2d.drawRect(-292, 97, 10, 10);
        g2d.drawRect(-310, 160, 10, 10);
        g2d.fillRect(-292, 97, 10, 10);
        g2d.fillRect(-310, 160, 10, 10);

    }

    public Skeleton() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        int counterMax = 1500;
        // movement
        if(counter < counterMax/4){
            tx += 1;
        } else if(counter < 2 * counterMax/4){
            ty -= 1;
        } else if(counter < 3 * counterMax/4){
            tx -= 1;
        } else if(counter < counterMax){
            ty += 1;
        }
        counter += 1;
        if(counterMax <= counter){
            counter = 0;
        }

        angle += 0.01;

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("cg_lab2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 1100);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new Skeleton());
        frame.setVisible(true);

        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }
}