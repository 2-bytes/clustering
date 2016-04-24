package cclusteringmodified.ui;

/**
 *
 * @author Vitalii Umanets
 */
import cclusteringmodified.Cluster;
import cclusteringmodified.Point;
import cclusteringmodified.utils.ExcelDataLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainForm extends JFrame {

    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 600;

    private class Canvas extends JPanel{

        private final Color[] colorspace = new Color[]{Color.BLUE, Color.GREEN,
            Color.MAGENTA, Color.YELLOW, Color.RED, Color.CYAN, Color.PINK, Color.BLACK, Color.ORANGE, Color.WHITE};
        private final Cluster[] clusters;

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
                    //g.drawChars(p.tag.toCharArray(), 0, p.tag.length(), x, this.getHeight()-y);
                    g.fillOval(x-1, this.getHeight()-y-1, 2, 2);
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
        this.setTitle("Кластеризация k-means");
        canvas.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()){
                    case MouseEvent.BUTTON1:
                        cclusteringmodified.CClustering.singleIteration();
                        canvas.repaint();
                        break;
                    case MouseEvent.BUTTON2:
                        try {
                            ExcelDataLoader.savetoXLSX(clusters);
                        } catch (IOException ex){
                            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;

                }
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
    }

}
