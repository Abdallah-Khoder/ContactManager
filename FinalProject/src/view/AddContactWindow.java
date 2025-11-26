package view;

import controler.ContactController;
import controler.GroupController;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.FileService;
import static style.style.*;

public class AddContactWindow extends JFrame {

    private JLabel label1, label2, labelFName, labelLName, labelCity, labelPhone, labelGroup;
    private JTextField firstName, lastName, city;
    private JButton save, cancel;
    private DefaultTableModel phoneTableModel;
    private JTable phoneTable;
    private JCheckBox noGroup;
    private JPanel leftPanel, panelCheck, buttonPanel, centerPanel, mainPanel, FNamePanel, LNamePanel, cityPanel, phoneNumberPanel, tablePanel, rowPanel;
    private ContactController controller;
    private GroupController groupController;

    public AddContactWindow(ContactController Ccontroller, GroupController g) {
        this.controller = Ccontroller;
        this.groupController = g;
        setTitle("Projet NFA035");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        label1 = new JLabel("Gestion des contacts", JLabel.CENTER);
        label1.setFont(new Font("Arial", Font.BOLD, 36));
        label1.setForeground(Color.BLUE);
        mainPanel.add(label1, BorderLayout.NORTH);

        leftPanel = new JPanel(new BorderLayout());
        label2 = new JLabel("New Contact", JLabel.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 36));
        label2.setForeground(Color.RED);
        leftPanel.add(label2, BorderLayout.WEST);

        JPanel blueColumn = new JPanel();
        blueColumn.setBackground(Color.CYAN);
        blueColumn.setPreferredSize(new Dimension(50, 1000));
        leftPanel.add(blueColumn, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        FNamePanel = new JPanel();
        labelFName = new JLabel("First name ");
        styleJLabel(labelFName);
        firstName = new JTextField();
        styletxtField(firstName);
        FNamePanel.add(labelFName);
        FNamePanel.add(firstName);

        LNamePanel = new JPanel();
        labelLName = new JLabel("Last name ");
        styleJLabel(labelLName);
        lastName = new JTextField();
        styletxtField(lastName);
        LNamePanel.add(labelLName);
        LNamePanel.add(lastName);

        cityPanel = new JPanel();
        labelCity = new JLabel("City ");
        styleJLabel(labelCity);
        city = new JTextField();
        styletxtField(city);
        cityPanel.add(labelCity);
        cityPanel.add(city);

        labelPhone = new JLabel("Phone numbers");
        styleJLabel(labelPhone);
        phoneNumberPanel = new JPanel();
        phoneNumberPanel.add(labelPhone);

        String[] columnNames = {"Region Code", "Phone number"};

        phoneTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        phoneTable = new JTable(phoneTableModel);

        JScrollPane tableScrollPane = new JScrollPane(phoneTable);
        styleTable(phoneTable, tableScrollPane, phoneTableModel);
        tablePanel = new JPanel();
        tablePanel.add(tableScrollPane);

        rowPanel = new JPanel();
        labelGroup = new JLabel("Add the contacts to groups ");
        styleJLabel(labelGroup);
        rowPanel.add(labelGroup);

        panelCheck = new JPanel();
        panelCheck.setLayout(new BoxLayout(panelCheck, BoxLayout.Y_AXIS));
        panelCheck.setBorder(BorderFactory.createEmptyBorder(10, 300, 10, 0));
        ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        HashMap<JCheckBox, Group> checkBoxGroupMap = new HashMap<>();

        noGroup = new JCheckBox("No Groups", true);
        styleCheckBox(noGroup);
        panelCheck.add(noGroup);
        checkBoxList.add(noGroup);

        FileService f = new FileService();
        for (Group group : groupController.getGroups()) {
            JCheckBox groupCheckBox = new JCheckBox(group.getName());
            styleCheckBox(groupCheckBox);
            panelCheck.add(groupCheckBox);
            checkBoxList.add(groupCheckBox);
            checkBoxGroupMap.put(groupCheckBox, group);
        }
        JScrollPane scrollPane = new JScrollPane(panelCheck);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        buttonPanel = new JPanel();
        save = new JButton("save");
        styleJButton(save);
        cancel = new JButton("cancel");
        styleJButton(cancel);
        buttonPanel.add(save);
        buttonPanel.add(cancel);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(FNamePanel);
        centerPanel.add(LNamePanel);
        centerPanel.add(cityPanel);
        centerPanel.add(phoneNumberPanel);
        centerPanel.add(tablePanel);
        centerPanel.add(rowPanel);
        centerPanel.add(scrollPane);
        centerPanel.add(buttonPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Vous voulez quitter cette fenêtre ?", "Confirm message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    new ContactsWindow(controller, groupController);
                } else {

                }
            }

        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = firstName.getText().trim();
                String lname = lastName.getText().trim();
                String cityName = city.getText().trim();

                if (fname.isEmpty() || lname.isEmpty() || f.isTableEmpty(phoneTableModel)) {
                    JOptionPane.showMessageDialog(null, "Un contact doit avoir un nom , un prenom , et un numero de telephone", "Error message", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Contact c = new Contact(fname, lname, cityName);
                    int j = 0;
                    for (int i = 0; i < phoneTableModel.getRowCount(); i++) {
                        String region = (String) phoneTableModel.getValueAt(i, 0);
                        String number = (String) phoneTableModel.getValueAt(i, 1);
                        if (region != null && number != null && !number.trim().isEmpty()) {
                            boolean succ = c.addPhoneNumber(region + number);
                        } else {
                        }
                    }
                    ArrayList<Group> selectedGroups = new ArrayList<Group>();

                    for (JCheckBox cb : checkBoxList) {
                        if (cb.isSelected() && checkBoxGroupMap.containsKey(cb)) {
                            selectedGroups.add(checkBoxGroupMap.get(cb));
                        }
                    }

                    if (selectedGroups.contains(noGroup)) {
                    } else {
                        for (Group g : selectedGroups) {
                            groupController.ajouterContactAuGroupe(g, c);
                            g.addContact(c);
                        }
                    }

                    boolean b = controller.ajouterContact(c);
                    if (b) {
                        JOptionPane.showMessageDialog(null, "contact added");
                        f.sauvegarderContacts(controller);
                        f.sauvegarderGroupes(groupController);
                        dispose();
                        new AddContactWindow(controller, groupController);
                    } else {
                        JOptionPane.showMessageDialog(null, "Contact mawjoud");
                    }

                }
            }
        });

        this.setResizable(false);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        this.setVisible(true);
    }

}
