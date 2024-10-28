package EECS3311.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.function.Consumer;

public class SwingUtilities {

    public static JLabel getSwingImage(int width, int height, String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            JLabel res = new JLabel(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_FAST)));
            res.setPreferredSize(new Dimension(width, height));
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return new JLabel("FILE NOT FOUND");
        }
    }

    public static JScrollPane getJScrollPage(Component component, int yUnits, int xUnits){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(xUnits);
        scrollPane.getVerticalScrollBar().setUnitIncrement(yUnits);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    public static class TextButton extends JLabel {
        String txt;

        public TextButton(String text, Consumer<MouseEvent> onClickListener) {
            setText(text);
            setForeground(Color.BLUE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClickListener.accept(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TextButton.super.setText(TextButton.this.txt);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    TextButton.super.setText("<html><a href=''>" + TextButton.this.txt + "</a></html>");
                }
            });
        }

        @Override
        public void setText(String text) {
            this.txt = text;
            super.setText(text);
        }
    }

    public static class ImageButton extends JLabel {
        public ImageButton(String imgPath, int width, int height, Consumer<MouseEvent> onClickListener) {
            try {
                BufferedImage img = ImageIO.read(new File(imgPath));
                setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_FAST)));
            } catch (IOException e) {
                setText("FILE NOT FOUND");
            }
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClickListener.accept(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(new EmptyBorder(0, 0, 0, 0));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createRaisedBevelBorder());
                }
            });
        }
    }


    public static class JPanelList<T> extends JPanel {
        private JPanel mainList = new JPanel(new GridBagLayout());
        private Renderer<T> renderer = null;

        public static interface Renderer<T> {
            public JPanel render(T item);
        }

        public JPanelList(){
            this(null);
        }

        public JPanelList(Renderer<T> renderer) {
            this.renderer = renderer;
            setLayout(new BorderLayout());
            clearAndReset();
        }

        private void addPanel(JPanel panel) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainList.add(panel, gbc, 0);
            validate();
            repaint();
        }

        public void setRenderer(Renderer<T> renderer) {
            this.renderer = renderer;
        }

        public void addItem(T item) {
            addPanel(renderer.render(item));
        }

        public void addItems(Vector<T> items) {
            for (T item : items)
                addPanel(renderer.render(item));
        }

        public void clearAndReset(){
            mainList.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.weighty = 1;
            mainList.add(new JPanel(), gbc);
            add(mainList, BorderLayout.CENTER);
        }
    }


    public static class SequencePanel extends JPanel {
        public SequencePanel(int axis) {
            BoxLayout boxLayout = new BoxLayout(this, axis);
            setLayout(boxLayout);
        }

        public void addAll(Component...components){
            for(Component component : components)
                add(component);
        }
    }

    public static class JLabelFont extends JLabel {
        public JLabelFont(String txt, int style, int size) {
            super(txt);
            setFont(new Font("", style, size));
        }
    }

    public static class JPanelMargin extends JPanel {
        private JPanel panel = new JPanel();

        public JPanelMargin(int top, int left, int bottom, int right, boolean expanded) {
            this(new FlowLayout(), top, left, bottom, right, expanded);
        }

        public JPanelMargin(LayoutManager manager, int top, int left, int bottom, int right, boolean expanded) {
            super.setBorder(new EmptyBorder(top, left, bottom, right));
            super.setLayout(expanded ? new BorderLayout() : new FlowLayout());
            super.add(panel);
            panel.setLayout(manager);
        }

        public JPanel getChildPanel() {
            return panel;
        }

        @Override
        public void setLayout(LayoutManager mgr) {
            if (panel == null)
                super.setLayout(mgr);
            else
                panel.setLayout(mgr);
        }

        @Override
        public Component add(Component comp) {
            return panel.add(comp);
        }

        @Override
        public void add(Component comp, Object constraints) {
            panel.add(comp, constraints);
        }

        @Override
        public void setBorder(Border border) {
            if (panel == null)
                super.setBorder(border);
            else
                panel.setBorder(border);
        }
    }
}
