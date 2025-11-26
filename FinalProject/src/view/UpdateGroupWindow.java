package view;

import controler.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.FileService;
import static style.style.*;

public class UpdateGroupWindow extends JFrame {

    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, panelNameGroup, panelDescGroup, panelTable, panelBtn;
    private JLabel label1, label2, label3, label4;
    private JTextField nameGroup, descGroup;
    private JButton btnSave, btnCancel;
    private JTable tableContact;
    private DefaultTableModel phoneTableModel;
    private GroupController groupController;
    private ContactController contactController;
    private Group group;

    public UpdateGroupWindow(Group g, GroupController groupControlle, ContactController c) {
        group = g;
        this.groupController = groupControlle;
        contactController = c;
        setTitle("Projet NFA035");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        label1 = new JLabel("Gestion des contacts", JLabel.CENTER);
        label1.setFont(new Font("Arial", Font.BOLD, 36));
        label1.setForeground(Color.BLUE);
        mainPanel.add(label1, BorderLayout.NORTH);

        label2 = new JLabel("update Group", JLabel.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 36));
        label2.setForeground(Color.RED);
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(label2, BorderLayout.WEST);
        leftPanel.add(Box.createVerticalStrut(30));

        JPanel blueColumn = new JPanel();
        blueColumn.setBackground(Color.CYAN);
        blueColumn.setPreferredSize(new Dimension(100, 1000));
        leftPanel.add(blueColumn, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        label3 = new JLabel("Group name");
        styleJLabel(label3);
        nameGroup = new JTextField();
        styletxtField(nameGroup);
        nameGroup.setText(group.getName());
        panelNameGroup = new JPanel();
        panelNameGroup.add(label3);
        panelNameGroup.add(nameGroup);

        label4 = new JLabel("Descreption");
        styleJLabel(label4);
        descGroup = new JTextField();
        styletxtField(descGroup);
        descGroup.setText(group.getDescription());
        panelDescGroup = new JPanel();
        panelDescGroup.add(label4);
        panelDescGroup.add(descGroup);

        FileService f = new FileService();
        String[] columnNames = {"Contact Name", "City", "Add to Group"};
        phoneTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };

        tableContact = new JTable(phoneTableModel);
        tableContact.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        tableContact.setFont(new Font("Arial", Font.PLAIN, 16));
        tableContact.setRowHeight(26);
        ArrayList<Contact> contacts = group.getContacts();
        for (Contact cc : contactController.getContacts()) {
            if (contacts.contains(cc)) {
                phoneTableModel.addRow(new Object[]{cc.getFname() + " " + cc.getLname(), cc.getCity(), true});
            } else {
                phoneTableModel.addRow(new Object[]{cc.getFname() + " " + cc.getLname(), cc.getCity(), false});
            }
        }
        int rowCount = phoneTableModel.getRowCount();
        int rowHeight = tableContact.getRowHeight();
        int headerHeight = tableContact.getTableHeader().getPreferredSize().height;
        JScrollPane tableScrollPane = new JScrollPane(tableContact);
        int visibleRows = Math.min(rowCount, 10);
        tableScrollPane.setPreferredSize(new Dimension(500, visibleRows * rowHeight + headerHeight));
        panelTable = new JPanel();
        panelTable.add(tableScrollPane);

        btnSave = new JButton("save Group");
        styleJButton(btnSave);
        btnCancel = new JButton("cancel");
        styleJButton(btnCancel);
        panelBtn = new JPanel();
        panelBtn.add(btnSave);
        panelBtn.add(btnCancel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(panelNameGroup);
        rightPanel.add(panelDescGroup);
        rightPanel.add(panelTable);
        rightPanel.add(panelBtn);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Vous voulez quitter cette fenêtre ?", "Confirm message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    new GroupWindow(contactController, groupController);
                } else {

                }
            }

        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String groupName = nameGroup.getText();
                String groupDesc = descGroup.getText();

                if (groupName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Group name is required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (group == null) {
                    JOptionPane.showMessageDialog(null, "Group not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Contact> selectedContacts = new ArrayList<>();
                for (int i = 0; i < phoneTableModel.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) phoneTableModel.getValueAt(i, 2);
                    if (isSelected != null && isSelected) {
                        String fullName = (String) phoneTableModel.getValueAt(i, 0);
                        String city = (String) phoneTableModel.getValueAt(i, 1);
                        String[] parts = fullName.split(" ");
                        String fname = parts[0];
                        String lname = parts[1];

                        Contact c = new Contact(fname, lname, city);
                        Contact contactAddGroup = contactController.getContact(c);
                        if (!contactAddGroup.equals(new Contact("", "", ""))) {
                            selectedContacts.add(contactAddGroup);
                        }
                    }
                }
                group.setName(groupName);
                group.setDescription(groupDesc);

                group.getContacts().clear();
                group.getContacts().addAll(selectedContacts);

                group.notifyModification();

                boolean b = groupController.modifierGroupe(group, group);
                if (b) {
                    f.sauvegarderGroupes(groupController);
                    f.sauvegarderContacts(contactController);
                    JOptionPane.showMessageDialog(null, "Group updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new GroupWindow(contactController, groupController);
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la modification du groupe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        this.setVisible(true);

    }

}
