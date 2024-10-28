package EECS3311.view.dialogs;

import EECS3311.model.Store;
import EECS3311.view.ListRenderers;
import EECS3311.view.SwingUtilities.*;
import EECS3311.view.SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.function.Consumer;

public class SelectStoreDialog extends JDialog {
    protected SelectStoreDialog(JFrame parent) {
        super(parent, "Select Store", true);
        setPreferredSize(new Dimension(400, 400));
        setLocation(parent.getLocation().x + 100 , parent.getLocation().y + 100);
    }

    public static void rise(JFrame parent, Vector<Store> stores, Consumer<Store> callBack) {
        SelectStoreDialog dialog = new SelectStoreDialog(parent);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<Store> model = new DefaultListModel<>();
        for (Store store : stores)
            model.addElement(store);
        JList<Store> storeJList = new JList<>(model);
        storeJList.setCellRenderer(new ListRenderers.Admin_SelectStoreDialogList());
        storeJList.setBackground(dialog.getBackground());

        JButton okBtn = new JButton("Select"),
                cancelBtn = new JButton("Cancel");
        okBtn.setEnabled(false);

        JPanel actsPanel = new JPanelMargin(new BorderLayout(), 10, 15, 10, 15, true);
        actsPanel.add(cancelBtn, BorderLayout.WEST);
        actsPanel.add(okBtn, BorderLayout.EAST);

        dialog.add(SwingUtilities.getJScrollPage(storeJList, 7, 7), BorderLayout.CENTER);
        dialog.add(actsPanel, BorderLayout.SOUTH);

        storeJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                okBtn.setEnabled(true);
        });
        cancelBtn.addActionListener(e -> {
            callBack.accept(null);
            dialog.dispose();
        });
        okBtn.addActionListener(e -> {
            callBack.accept(stores.get(storeJList.getSelectedIndex()));
            dialog.dispose();
        });

        dialog.pack();
        dialog.setVisible(true);
    }
}
