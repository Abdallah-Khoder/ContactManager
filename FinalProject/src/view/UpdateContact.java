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
import java.util.Observable;


public class UpdateContact extends JFrame{
    private Contact contact;
    private JLabel label1, label2, labelFName, labelLName, labelCity, labelPhone, labelGroup;
    private JTextField firstName, lastName, city;
    private JButton save, cancel;
    private DefaultTableModel phoneTableModel;
    private JTable phoneTable;
    private JCheckBox noGroup;
    private ContactController contactController;
    private GroupController groupController;
    private JPanel leftPanel, panelCheck, buttonPanel, centerPanel, mainPanel, FNamePanel, LNamePanel, cityPanel, phoneNumberPanel, tablePanel, rowPanel;

    public UpdateContact(Contact c , ContactController contactControl , GroupController groupControl) {
        contact = c;
        contactController = contactControl;
        groupController = groupControl ; 
        setTitle("Projet NFA035");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        label1 = new JLabel("Gestion des contacts", JLabel.CENTER);
        label1.setFont(new Font("Arial", Font.BOLD, 36));
        label1.setForeground(Color.BLUE);
        mainPanel.add(label1, BorderLayout.NORTH);

        leftPanel = new JPanel(new BorderLayout());
        label2 = new JLabel("Update Contact", JLabel.CENTER);
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
        firstName.setText(contact.getFname());
        FNamePanel.add(labelFName);
        FNamePanel.add(firstName);

        LNamePanel = new JPanel();
        labelLName = new JLabel("Last name ");
        styleJLabel(labelLName);
        lastName = new JTextField();
        styletxtField(lastName);
        lastName.setText(contact.getLname());
        LNamePanel.add(labelLName);
        LNamePanel.add(lastName);

        cityPanel = new JPanel();
        labelCity = new JLabel("City ");
        styleJLabel(labelCity);
        city = new JTextField();
        styletxtField(city);
        city.setText(contact.getCity());
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

        FileService f = new FileService() ; 
        for (String number : contact.getPhoneNumbers()) {
            if (number.length() >= 2) {
                String regionCode = number.substring(0, 2); 
                String actualNumber = number.substring(2);  
                phoneTableModel.addRow(new Object[]{regionCode, actualNumber});
            } else {
                phoneTableModel.addRow(new Object[]{"", number});
            }
        }

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

        for (Group group : groupController.getGroups()) {
            if(group.getContacts().contains(contact)){
                noGroup.setSelected(false) ; 
                JCheckBox groupCheckBox = new JCheckBox(group.getName() , true);
                styleCheckBox(groupCheckBox);
                panelCheck.add(groupCheckBox);
                checkBoxList.add(groupCheckBox);
                checkBoxGroupMap.put(groupCheckBox, group);
            }
            else{
                JCheckBox groupCheckBox = new JCheckBox(group.getName());
                styleCheckBox(groupCheckBox);
                panelCheck.add(groupCheckBox);
                checkBoxList.add(groupCheckBox);
                checkBoxGroupMap.put(groupCheckBox, group);
            }
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
                    new ContactsWindow(contactController , groupController);
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
                }

                Contact contactEdited = new Contact(fname, lname, cityName);

                for (int i = 0; i < phoneTableModel.getRowCount(); i++) {
                    String region = (String) phoneTableModel.getValueAt(i, 0);
                    String number = (String) phoneTableModel.getValueAt(i, 1);
                    if (region != null && number != null && !number.trim().isEmpty()) {
                        contactEdited.addPhoneNumber(region + number);
                    }
                }
                for(Group g : groupController.getGroups()){
                    groupController.retirerContactDuGroupe(g, contact) ; 
                    g.removeContact(contact);
                }
                ArrayList<Group> selectedGroups = new ArrayList<>();

                for (JCheckBox cb : checkBoxList) {
                    if (cb.isSelected() && checkBoxGroupMap.containsKey(cb)) {
                        selectedGroups.add(checkBoxGroupMap.get(cb));
                    }
                }

                if (selectedGroups.contains(noGroup)) {
                    
                } else {
                    for (Group g : selectedGroups) {
                        groupController.ajouterContactAuGroupe(g, contactEdited) ; 
                        g.addContact(contactEdited);
                    }
                }

                boolean b = contactController.modifierContact(contact ,contactEdited) ; 
                contactEdited.notifyModification();

                if (b) {
                    JOptionPane.showMessageDialog(null, "contact edited");
                    f.sauvegarderContacts(contactController);
                    f.sauvegarderGroupes(groupController);
                    dispose() ; 
                    new ContactsWindow(contactController , groupController) ; 
                }
            }
        });

        setResizable(false);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        add(mainPanel);
        setVisible(true);
    }

}
