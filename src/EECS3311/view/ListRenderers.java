package EECS3311.view;

import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Customer;
import EECS3311.model.users.Manager;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class ListRenderers {
    public static final Color primaryColor = new Color(255, 127, 39);

    private static String availabilityLabelText(boolean isAvailable) {
        return isAvailable ? "<html><b style=\"color:rgb(46, 125, 50);\">Available &nbsp &nbsp&nbsp ✅</b></html>" :
                "<html><b style=\"color:rgb(244, 67, 54);\">Unavailable ❌</b></html>";
    }


    public static class PreferredStoresRenderer extends JPanel implements ListCellRenderer<Store> {
        private final Customer customer;

        public PreferredStoresRenderer(Customer customer) {
            setOpaque(true);
            this.customer = customer;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Store> list, Store value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel nameLabel = new JLabelFont(" " + value.getName(), Font.PLAIN, 18);
            JLabel itemsCountLabel = new JLabelFont("<html> &nbsp <b style=\"color:rgb(255, 127, 39);\">" + customer.getShoppingLists().get(value).size() + "</b> items in shopping list</html>", Font.ITALIC, 14);

            JPanel panel = new SequencePanel(BoxLayout.Y_AXIS);
            panel.add(nameLabel);
            panel.add(itemsCountLabel);

            if (isSelected) {
                panel.setBorder(BorderFactory.createMatteBorder(1, 10, 1, 1, primaryColor));
            } else {
                panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            }

            JPanel padding = new JPanel(new BorderLayout());
            padding.setBorder(new EmptyBorder(5, 10, 5, 10));
            padding.add(panel, BorderLayout.CENTER);
            return padding;
        }
    }


    public static class CustomerAllStoresRenderer implements JPanelList.Renderer<Store> {
        private final Consumer<Store> onAddBtnClick;
        private final Customer customer;

        public CustomerAllStoresRenderer(Consumer<Store> onAddBtnClick, Customer customer) {
            this.onAddBtnClick = onAddBtnClick;
            this.customer = customer;
        }

        @Override
        public JPanel render(Store value) {
            JLabel nameLabel = new JLabelFont(value.getName(), Font.PLAIN, 35);
            JLabel locationLabel = new JLabelFont("<html><b>Address:</b> <i>" + value.getLocation().getLocation() + "</i></html>", Font.PLAIN, 14);
            JLabel distanceLabel = new JLabelFont("<html><b>Distance:</b> <b style=\"color:rgb(255, 127, 39);\">" + customer.getLocation().distance(value.getLocation()) + "</b></html>", Font.PLAIN, 14);
            JButton addBtn = new JButton("Add");
            addBtn.addActionListener(e -> onAddBtnClick.accept(value));
            addBtn.setFont(new Font("", Font.PLAIN, 16));
            JPanel panel0 = new JPanel();
            BoxLayout layout0 = new BoxLayout(panel0, BoxLayout.Y_AXIS);
            panel0.setBorder(new EmptyBorder(0, 5, 10, 0));
            panel0.setLayout(layout0);
            panel0.add(nameLabel);
            panel0.add(locationLabel);
            panel0.add(distanceLabel);

            JPanel panel1 = new SequencePanel(BoxLayout.Y_AXIS);
            panel1.setBorder(new EmptyBorder(15, 10, 0, 10));
            panel1.add(addBtn);

            JPanel panel2 = new JPanel(new BorderLayout());
            panel2.add(panel0, BorderLayout.CENTER);
            panel2.add(panel1, BorderLayout.EAST);

            panel2.setBorder(BorderFactory.createRaisedBevelBorder());
            panel2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    panel2.setBorder(BorderFactory.createRaisedBevelBorder());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel2.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            });

            JPanel padding = new JPanel(new BorderLayout());
            padding.setBorder(new EmptyBorder(10, 10, 5, 10));
            padding.add(panel2, BorderLayout.CENTER);
            return padding;
        }
    }


    public static class CustomerShoppingListRenderer implements JPanelList.Renderer<Item> {
        Consumer<Item> removeItem;

        public CustomerShoppingListRenderer(Consumer<Item> removeItem) {
            this.removeItem = removeItem;
        }

        @Override
        public JPanel render(Item item) {
            JLabel nameLabel = new JLabelFont(item.getName(), Font.PLAIN, 20);
            JLabel priceLabel = new JLabelFont("<html><b>Price: </b>" + item.getPrice() + "$ </html>", Font.PLAIN, 16);
            JLabel availLabel = new JLabelFont(availabilityLabelText(item.isAvailable()) , Font.PLAIN, 12);

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, primaryColor));

            JPanel panel0 = new JPanelMargin(new BorderLayout(), 5, 0, 5, 5, false);

            JPanel panel1 = new SequencePanel(BoxLayout.Y_AXIS);
            panel1.add(nameLabel);
            panel1.add(priceLabel);
            panel1.add(availLabel);

            JPanel panel2 = new SequencePanel(BoxLayout.Y_AXIS);
            panel2.setBorder(new EmptyBorder(5, 30, 0, 0));
            panel2.add(new TextButton("Remove", e -> removeItem.accept(item)));

            panel0.add(panel1, BorderLayout.CENTER);
            panel0.add(panel2, BorderLayout.EAST);

            panel.add(panel0);
            return panel;
        }
    }


    public static class CustomerStoreAllItemsRenderer implements JPanelList.Renderer<Item> {
        Consumer<Item> addItem;

        public CustomerStoreAllItemsRenderer(Consumer<Item> addItem) {
            this.addItem = addItem;
        }

        @Override
        public JPanel render(Item item) {
            JLabel nameLabel = new JLabelFont(item.getName(), Font.PLAIN, 26);
//            JLabel descLabel = new JLabelFont(item.getDescription(), Font.PLAIN, 18);
            JTextArea descriptionText = new JTextArea(item.getDescription());
            descriptionText.setFont(new Font("", Font.PLAIN, 16));
            JLabel locationLabel = new JLabelFont("<html><b>Location in Store: </b> <i>" + item.getLocationInStore().getLocation() + "</i></html>", Font.PLAIN, 14);
            JLabel priceLabel = new JLabelFont("<html><b>Price:</b>" + item.getPrice() + "$</html>", Font.PLAIN, 14);
            JLabel sizeLabel = new JLabelFont("<html><b>Size</b>: " + item.getSize() + "</html>", Font.PLAIN, 14);
            JLabel availLabel = new JLabelFont(availabilityLabelText(item.isAvailable()) , Font.PLAIN, 14);
            JLabel categoryLabel = new JLabelFont("<html>[<b style=\"color:rgb(255, 127, 39);\">" + item.getCategory().name() + "</b>]</html>", Font.PLAIN, 18);

            JButton addBtn = new JButton("Add");
            addBtn.addActionListener(e -> addItem.accept(item));


            SequencePanel panel1 = new SequencePanel(BoxLayout.X_AXIS);
            panel1.addAll(nameLabel, Box.createHorizontalStrut(15), categoryLabel);
            panel1.setAlignmentX(Component.LEFT_ALIGNMENT);

            descriptionText.setEditable(false);
            descriptionText.setLineWrap(true);
            descriptionText.setWrapStyleWord(true);
            descriptionText.setColumns(10);
            descriptionText.setBackground(panel1.getBackground());
            JScrollPane descriptionText_SP = new JScrollPane(descriptionText);
            descriptionText_SP.setBorder(new EmptyBorder(0,0,10,10));
            descriptionText_SP.setAlignmentX(Component.LEFT_ALIGNMENT);

            SequencePanel panel2 = new SequencePanel(BoxLayout.Y_AXIS);
            panel2.addAll(panel1, descriptionText_SP, locationLabel);

            SequencePanel panel3 = new SequencePanel(BoxLayout.Y_AXIS);
            panel3.addAll(priceLabel, sizeLabel, availLabel, Box.createVerticalStrut(5));
            JPanel panel4 = new JPanel(new BorderLayout());
            panel4.add(panel3, BorderLayout.NORTH);
            panel4.add(addBtn, BorderLayout.SOUTH);

            JPanel panel5 = new JPanelMargin(new BorderLayout(), 5, 15, 10, 15, true);

            JPanel panel6 = new JPanelMargin(new BorderLayout(), 5, 5, 5, 5, true);
            panel6.add(panel2, BorderLayout.CENTER);
            panel6.add(panel4, BorderLayout.EAST);

            panel5.add(panel6);
            panel5.setBorder(BorderFactory.createRaisedBevelBorder());
            panel5.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createRaisedBevelBorder());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            });
            return panel5;
        }
    }


    public static class Manager_Admin_AllStoresRenderer extends JPanel implements ListCellRenderer<Store> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Store> list, Store value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel nameLabel = new JLabelFont(" " + value.getName(), Font.BOLD, 18);
            JLabel itemsCount = new JLabelFont("<html> &nbsp [ <b style=\"color:rgb(255, 127, 39);\">" + value.getItems().size() + "</b> items]</html>", Font.PLAIN, 14);

            JPanel sequencePanel = new SequencePanel(BoxLayout.Y_AXIS);
            sequencePanel.add(nameLabel);
            sequencePanel.add(itemsCount);

            if (isSelected) {
                sequencePanel.setBorder(BorderFactory.createMatteBorder(1, 10, 1, 1, primaryColor));
            } else {
                sequencePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            }

            JPanel padding = new JPanelMargin(new BorderLayout(), 5, 10, 5, 10, true);
            padding.add(sequencePanel, BorderLayout.CENTER);
            return padding;
        }
    }


    public static class Manager_Admin_AllItemsRenderer implements JPanelList.Renderer<Item> {
        private Consumer<Item> removeItem;
        private Consumer<Item> editItem;

        public Manager_Admin_AllItemsRenderer(Consumer<Item> removeItem, Consumer<Item> editItem) {
            this.removeItem = removeItem;
            this.editItem = editItem;
        }

        @Override
        public JPanel render(Item item) {
            JLabel nameLabel = new JLabelFont(item.getName(), Font.PLAIN, 26);
            JTextArea descriptionText = new JTextArea(item.getDescription());
            descriptionText.setFont(new Font("", Font.PLAIN, 16));
            JLabel locationLabel = new JLabelFont("<html><b>Location in Store: </b> <i>" + item.getLocationInStore().getLocation() + "</i></html>", Font.PLAIN, 14);
            JLabel price_sizeLabel = new JLabelFont("<html><b>Price:</b>" + item.getPrice() + "$ &nbsp&nbsp <b>Size</b>:" + item.getSize() + "&nbsp &nbsp &nbsp" + availabilityLabelText(item.isAvailable()) +"</html>", Font.PLAIN, 14);
            JLabel categoryLabel = new JLabelFont("[" + item.getCategory().name() + "]", Font.PLAIN, 18);
            categoryLabel.setForeground(primaryColor);
            JButton editBtn = new JButton("Edit");
            JButton remove = new JButton("Remove");
            editBtn.addActionListener(e -> editItem.accept(item));
            remove.addActionListener(e -> removeItem.accept(item));
            SequencePanel panel1 = new SequencePanel(BoxLayout.X_AXIS);
            panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel1.addAll(nameLabel, Box.createHorizontalStrut(15), categoryLabel);


            descriptionText.setEditable(false);
            descriptionText.setLineWrap(true);
            descriptionText.setWrapStyleWord(true);
            descriptionText.setColumns(10);
            descriptionText.setBackground(panel1.getBackground());
            JScrollPane descriptionText_SP = new JScrollPane(descriptionText);
            descriptionText_SP.setBorder(new EmptyBorder(0,0,10,10));
            SequencePanel panel2 = new SequencePanel(BoxLayout.Y_AXIS);
            descriptionText_SP.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel2.addAll(panel1,  descriptionText_SP, locationLabel, price_sizeLabel);

            SequencePanel panel3 = new SequencePanel(BoxLayout.Y_AXIS);
            remove.setAlignmentX(Component.RIGHT_ALIGNMENT);
            editBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
            panel3.addAll(editBtn, Box.createVerticalStrut(10), remove);


            JPanel panel5 = new JPanelMargin(new BorderLayout(), 5, 15, 10, 15, true);

            JPanel panel6 = new JPanelMargin(new BorderLayout(), 5, 5, 5, 5, true);
            panel6.add(panel2, BorderLayout.CENTER);
            panel6.add(panel3, BorderLayout.EAST);

            panel5.add(panel6);
            panel5.setBorder(BorderFactory.createRaisedBevelBorder());
            panel5.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createRaisedBevelBorder());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            });
            return panel5;
        }
    }


    public static class Admin_AllManagersRenderer extends JPanel implements ListCellRenderer<Manager> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Manager> list, Manager value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel nameLabel = new JLabelFont(" " + value.getName(), Font.BOLD, 18);
            JLabel itemsCount = new JLabelFont("<html> &nbsp [ <b style=\"color:rgb(255, 127, 39);\">" + value.getStores().size() + "</b> stores]</html>", Font.PLAIN, 14);

            JPanel sequencePanel = new SequencePanel(BoxLayout.Y_AXIS);
            sequencePanel.add(nameLabel);
            sequencePanel.add(itemsCount);

            if (isSelected) {
                sequencePanel.setBorder(BorderFactory.createMatteBorder(1, 10, 1, 1, primaryColor));
            } else {
                sequencePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            }

            JPanel padding = new JPanelMargin(new BorderLayout(), 5, 10, 5, 10, true);
            padding.add(sequencePanel, BorderLayout.CENTER);
            return padding;
        }
    }


    public static class Admin_UnderManageStoresRenderer implements JPanelList.Renderer<Store> {
        private Consumer<Store> removeStore;

        public Admin_UnderManageStoresRenderer(Consumer<Store> removeStore) {
            this.removeStore = removeStore;
        }

        @Override
        public JPanel render(Store store) {
            JLabel nameLabel = new JLabelFont("<html>" + store.getName() + " [<b style=\"color:rgb(255, 127, 39);\">" + store.getItems().size() + "</b> items]", Font.PLAIN, 26);
            JLabel locationLabel = new JLabelFont("<html><b>Address:</b> <i>" + store.getLocation().getLocation() + "</i></html>", Font.PLAIN, 14);

            JButton remove = new JButton("Remove");
            remove.addActionListener(e -> removeStore.accept(store));

            SequencePanel panel2 = new SequencePanel(BoxLayout.Y_AXIS);
            panel2.addAll(nameLabel, locationLabel);

            SequencePanel panel3 = new SequencePanel(BoxLayout.Y_AXIS);
            remove.setAlignmentX(Component.RIGHT_ALIGNMENT);
            panel3.addAll(remove);

            JPanel panel5 = new JPanelMargin(new BorderLayout(), 5, 15, 10, 15, true);

            JPanel panel6 = new JPanelMargin(new BorderLayout(), 5, 5, 5, 5, true);
            panel6.add(panel2, BorderLayout.CENTER);
            panel6.add(panel3, BorderLayout.EAST);

            panel5.add(panel6);
            panel5.setBorder(BorderFactory.createRaisedBevelBorder());
            panel5.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createRaisedBevelBorder());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel5.setBorder(BorderFactory.createLoweredBevelBorder());
                }
            });
            return panel5;
        }
    }


    public static class Admin_SelectStoreDialogList extends JPanel implements ListCellRenderer<Store>{

        @Override
        public Component getListCellRendererComponent(JList<? extends Store> list, Store value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel nameLabel = new JLabelFont(" " + value.getName(), Font.BOLD, 18);
            JLabel itemsCount = new JLabelFont("<html> &nbsp [ <b style=\"color:rgb(255, 127, 39);\">" + value.getItems().size() + "</b> items]</html>", Font.PLAIN, 14);

            JPanel sequencePanel = new SequencePanel(BoxLayout.Y_AXIS);
            sequencePanel.add(nameLabel);
            sequencePanel.add(itemsCount);

            if (isSelected) {
                sequencePanel.setBorder(BorderFactory.createMatteBorder(1, 10, 1, 1, primaryColor));
            } else {
                sequencePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            }

            JPanel padding = new JPanelMargin(new BorderLayout(), 5, 10, 5, 10, true);
            padding.add(sequencePanel, BorderLayout.CENTER);
            return padding;
        }
    }

    public static class Customer_PathInStoreList extends JPanel implements ListCellRenderer<Item>{

        @Override
        public Component getListCellRendererComponent(JList<? extends Item> list, Item value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel indexLabel = new JLabelFont(index + 1 +"-", Font.BOLD, 25);
            JLabel nameLabel = new JLabelFont(value.getName(), Font.PLAIN, 20);
            JLabel categoryLabel = new JLabelFont("<html> [<b style=\"color:rgb(255, 127, 39);\">" + value.getCategory().name()+"</b>] </html>", Font.PLAIN, 18);

            JPanel panel =new SequencePanel(BoxLayout.X_AXIS);
            panel.add(indexLabel);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(nameLabel);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(categoryLabel);

            JPanel padding = new JPanelMargin(new BorderLayout(), 5, 5, 5, 5 , true);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            padding.add(panel);
            return padding;
        }
    }
}
