package cclusteringmodified.ui;

/**
 *
 * @author Vitalii Umanets
 */
import cclusteringmodified.Cluster;
import cclusteringmodified.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sun.swing.UIAction;


public class MainForm extends JFrame {

    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 600;

    private class Canvas extends JPanel{

        private final Color[] colorspace = new Color[]{Color.BLUE, Color.GREEN,
            Color.MAGENTA, Color.YELLOW, Color.RED, Color.CYAN, Color.PINK, Color.BLACK};
        private Cluster[] clusters;

        public Canvas(Cluster[] clusters) {
            this.clusters = clusters;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            this.setBackground(Color.LIGHT_GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            for(int i=0;i<clusters.length;i++){
                g.setColor(colorspace[i]);
                double[] center = clusters[i].getCentroid();
                int xc = (int)(center[0]*(this.getWidth()-1));
                int yc = (int)(center[1]*(this.getHeight()-1));
                g.fillRect(xc-4, this.getHeight()-yc-4, 8, 8);
                for(Point p :clusters[i].getPoints()){
                    int x = (int)(p.coordinates[0]*(this.getWidth()-1));
                    int y = (int)(p.coordinates[1]*(this.getHeight()-1));
                    g.fillOval(x-4, this.getHeight()-y-4, 8, 8);
                }
            }
        }

    }

    public MainForm(Cluster[] clusters){
        super();
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        Canvas canvas = new Canvas(clusters);
        //JButton nextStepBtn = new JButton();
        //nextStepBtn.setText("Next");
        //nextStepBtn.setSize(50, 15);
        canvas.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                cclusteringmodified.CClustering.singleIteration();
                canvas.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
        this.add(canvas);
        //canvas.setSize(300, 300);
    }

}