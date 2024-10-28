package EECS3311.view.dialogs;

import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.view.ListRenderers;
import EECS3311.view.SwingUtilities;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Vector;

public class PathInStoreDialog extends JDialog {
    protected PathInStoreDialog(JFrame parent) {
        super(parent, "Select Store", true);
        setPreferredSize(new Dimension(400, 400));
        setLocation(parent.getLocation().x + 100, parent.getLocation().y + 100);
    }

    public static void rise(JFrame parent, Vector<Item> items) {
        SelectStoreDialog dialog = new SelectStoreDialog(parent);
        dialog.setLayout(new BorderLayout());

        Location base = new Location("", 0, 0);
        items.sort(Comparator.comparingDouble(item -> item.getLocationInStore().distance(base)));

        DefaultListModel<Item> model = new DefaultListModel<>();
        for (Item item : items)
            model.addElement(item);
        JList<Item> itemsJList = new JList<>(model);
        itemsJList.setCellRenderer(new ListRenderers.Customer_PathInStoreList());
        itemsJList.setBackground(dialog.getBackground());

        JButton okBtn = new JButton("OK");
        JLabel heading = new JLabelFont("Buy your shopping list items in this order:", Font.ITALIC, 20);
        JLabel totalPrice = new JLabelFont("<html>Total price: <b style=\"color:rgb(255, 127, 39);\">"+(items.stream().mapToDouble(Item::getPrice).sum())+"</b> $ </html>", Font.PLAIN, 18);

        JPanel infoPanelPadding = new JPanelMargin(new BorderLayout(), 5, 10, 10, 10, true);
        JPanel infoPanel = new SwingUtilities.SequencePanel(BoxLayout.Y_AXIS);
        infoPanel.add(heading);
        infoPanel.add(totalPrice);
        infoPanelPadding.add(infoPanel);

        JPanel actsPanel = new JPanel(new BorderLayout());
        actsPanel.add(okBtn, BorderLayout.CENTER);

        dialog.add(SwingUtilities.getJScrollPage(itemsJList, 7, 7), BorderLayout.CENTER);
        dialog.add(actsPanel, BorderLayout.SOUTH);
        dialog.add(infoPanelPadding, BorderLayout.NORTH);

        okBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setVisible(true);
    }
}
