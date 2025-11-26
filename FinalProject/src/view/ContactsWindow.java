package view;

import controler.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import model.*;
import service.FileService;
import static style.style.*;

public class ContactsWindow extends JFrame implements Observer {

    private JLabel titleLabel;
    private JList<Contact> contactList;
    private DefaultListModel<Contact> listModel;
    private JTextField txtSearch;
    private JButton btnSortFirstName, btnSortLastName, btnSortCity, btnAdd, btnEdit, btnDelete, btnView;
    private JPanel leftPanel, panelBottom, centerPanel, mainPanel;
    private ContactController contactController;
    private GroupController groupController;

    public ContactsWindow(ContactController cController, GroupController gController) {
        this.contactController = cController;
        this.groupController = gController;
        this.setTitle("Projet NFA035");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);

        titleLabel = new JLabel("Gestion des contacts", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.BLUE);

        txtSearch = new JTextField();
        styletxtField(txtSearch);

        btnSortFirstName = new JButton("Sort by First name");
        styleJButton(btnSortFirstName);
        btnSortLastName = new JButton("Sort by Last name");
        styleJButton(btnSortLastName);
        btnSortCity = new JButton("Sort by City");
        styleJButton(btnSortCity);
        btnAdd = new JButton("Add new contact");
        styleJButton(btnAdd);
        btnEdit = new JButton("Update");
        styleJButton(btnEdit);
        btnDelete = new JButton("Delete");
        styleJButton(btnDelete);
        btnView = new JButton("View");
        styleJButton(btnView);

        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setFont(new Font("Arial", Font.BOLD, 20));
        contactList.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane listScrollPane = new JScrollPane(contactList);
        listScrollPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        for (Contact contact : contactController.getContacts()) {
            contact.addObserver(this);
            listModel.addElement(contact);
        }

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblContacts = new JLabel("Contacts");
        lblContacts.setFont(new Font("Arial", Font.BOLD, 22));
        lblContacts.setForeground(Color.RED);
        lblContacts.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (JButton btn : new JButton[]{btnSortFirstName, btnSortLastName, btnSortCity, btnAdd}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(lblContacts);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(btnSortFirstName);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnSortLastName);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnSortCity);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnAdd);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("Search");
        styleJLabel(searchLabel);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton btn : new JButton[]{btnView, btnEdit, btnDelete}) {
            panelBottom.add(btn);
        }

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        centerPanel.add(panelBottom, BorderLayout.SOUTH);

        JPanel colorStrip = new JPanel();
        colorStrip.setPreferredSize(new Dimension(20, 0));
        colorStrip.setBackground(Color.CYAN);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.add(colorStrip, BorderLayout.WEST);
        midPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(midPanel, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                new AddContactWindow(contactController, groupController);
            }

        });

        btnSortFirstName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ArrayList<Contact> trieContactFName = contactController.sortedFName();
                listModel.clear();
                for (Contact c : trieContactFName) {
                    listModel.addElement(c);
                }

            }

        });

        btnSortLastName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ArrayList<Contact> trieContactFName = contactController.sortedLName();
                listModel.clear();
                for (Contact c : trieContactFName) {
                    listModel.addElement(c);
                }

            }

        });

        btnSortCity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ArrayList<Contact> trieContactFName = contactController.sortedCity();
                listModel.clear();
                for (Contact c : trieContactFName) {
                    listModel.addElement(c);
                }

            }

        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Contact contactDelet = contactList.getSelectedValue();
                ArrayList<Group> groupContact = groupController.chercherGroupesParContact(contactDelet);
                boolean delete = contactController.supprimerContact(contactDelet);
                if (delete) {
                    for (Group group : groupContact) {
                        groupController.retirerContactDuGroupe(group, contactDelet);
                    }
                    FileService f = new FileService();
                    f.sauvegarderContacts(contactController);
                    f.sauvegarderGroupes(groupController);
                    listModel.clear();
                    for (Contact cc : contactController.getContacts()) {
                        listModel.addElement(cc);
                    }
                    JOptionPane.showMessageDialog(null, "delete success", "delete contact", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "delete failed", "delete contact", JOptionPane.NO_OPTION);
                }

            }

        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Contact c = (Contact) contactList.getSelectedValue();
                dispose();
                new UpdateContact(c, contactController, groupController);
            }

        });

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Contact c = (Contact) contactList.getSelectedValue();
                dispose();
                new ViewContact(c, contactController, groupController);
            }

        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = txtSearch.getText().toLowerCase();
                updateList(text);
            }

        });

        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void updateList(String filter) {
        listModel.clear();
        for (Contact name : contactController.getContacts()) {
            String fname = name.getFname();
            String lname = name.getLname();
            if (fname.toLowerCase().startsWith(filter) || lname.toLowerCase().startsWith(filter)) {
                listModel.addElement(name);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        listModel.clear();
        for (Contact c : contactController.getContacts()) {
            listModel.addElement(c);
        }
    }

}
