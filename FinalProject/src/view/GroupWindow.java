package view;

import controler.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import model.Contact;
import model.Group;
import service.FileService;
import static style.style.*;

public class GroupWindow extends JFrame implements Observer {

    private JLabel titleLabel, groupLabel, listLabel;
    private JButton addGroupBtn, updateGroupBtn, deleteGroupBtn;
    private JTable groupTable, contactTable;
    private DefaultTableModel groupModel, contactModel;
    private JPanel topPanel, leftPanel, colorPanel, rightPanel, buttonsPanel;
    private JScrollPane groupScroll, contactScroll;
    private ContactController contactController;
    private GroupController groupController;
    private ArrayList<Group> groups;

    public GroupWindow(ContactController c, GroupController g) {
        contactController = c;
        groupController = g;
        setTitle("Projet NFA035");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Gestion des contacts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLUE);

        topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        groupLabel = new JLabel("Groups");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 23));
        groupLabel.setForeground(Color.RED);

        addGroupBtn = new JButton("Add new Group");
        styleJButton(addGroupBtn);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.add(groupLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(addGroupBtn);
        add(leftPanel, BorderLayout.WEST);

        addGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                new AddGroupWindow(groupController, contactController);
            }
        });

        colorPanel = new JPanel();
        colorPanel.setBackground(Color.CYAN);
        colorPanel.setPreferredSize(new Dimension(80, 400));
        add(colorPanel, BorderLayout.CENTER);

        groups = groupController.getGroups();

        String[] groupColumns = {"Group name", "Nb. of contacts"};
        groupModel = new DefaultTableModel(groupColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        groupTable = new JTable(groupModel);
        groupTable.setFillsViewportHeight(true);
        groupScroll = new JScrollPane(groupTable);
        groupScroll.setPreferredSize(new Dimension(100, 100));
        for (Group group : groupController.getGroups()) {
            group.addObserver(this);
            groupModel.addRow(new Object[]{group.getName(), group.getContacts().size()});
        }
        groupTable.setFont(new Font("Arial", Font.PLAIN, 18));
        groupTable.setRowHeight(28);

        String[] contactColumns = {"Contact Name", "Contact City"};
        contactModel = new DefaultTableModel(contactColumns, 0);
        contactTable = new JTable(contactModel);
        contactTable.setFillsViewportHeight(true);
        contactScroll = new JScrollPane(contactTable);
        contactScroll.setPreferredSize(new Dimension(400, 300));

        groupTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = groupTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedGroupName = (String) groupModel.getValueAt(selectedRow, 0);
                    for (Group g : groupController.getGroups()) {
                        if (g.getName().equals(selectedGroupName)) {
                            contactModel.setRowCount(0);
                            for (Contact c : g.getContacts()) {
                                contactModel.addRow(new Object[]{
                                    c.getFname() + " " + c.getLname(),
                                    c.getCity()
                                });
                            }
                            break;
                        }
                    }
                }
            }
        });
        contactTable.setFont(new Font("Arial", Font.PLAIN, 18));
        contactTable.setRowHeight(28);

        contactTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = contactTable.getSelectedRow();
                if (selectedRow != -1) {
                    String fullName = (String) contactModel.getValueAt(selectedRow, 0);
                    String[] parts = fullName.split(" ");
                    String firstName = parts[0];
                    String lastName = parts[1];

                    String city = (String) contactModel.getValueAt(selectedRow, 1);

                    Contact c = new Contact(firstName, lastName, city);
                    Contact contactView = contactController.getContact(c);
                    new ViewContact(contactView, contactController, groupController);
                }
            }
        });

        updateGroupBtn = new JButton("Update Group");
        deleteGroupBtn = new JButton("Delete");
        styleJButton(updateGroupBtn);
        styleJButton(deleteGroupBtn);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(updateGroupBtn);
        buttonsPanel.add(deleteGroupBtn);

        deleteGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedGroupName = (String) groupModel.getValueAt(selectedRow, 0);
                    int response = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer le groupe \"" + selectedGroupName + "\" ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        Group groupToDelete = null;
                        for (Group g : groupController.getGroups()) {
                            if (g.getName().equals(selectedGroupName)) {
                                groupToDelete = g;
                                break;
                            }
                        }
                        if (groupToDelete != null) {
                            ArrayList<Contact> contactGroupSupprimer = groupToDelete.getContacts();
                            groups.remove(groupToDelete);
                            groupModel.removeRow(selectedRow);
                            for (Contact con : contactGroupSupprimer) {
                                groupController.retirerContactDuGroupe(groupToDelete, con);
                            }
                            groupController.supprimerGroupe(groupToDelete);
                            contactModel.setRowCount(0);
                            new FileService().sauvegarderGroupes(groupController);
                            new FileService().sauvegarderContacts(contactController);
                            JOptionPane.showMessageDialog(null, "Le groupe a été supprimé avec succès.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un groupe à supprimer.");
                }
            }
        });

        updateGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedGroupName = (String) groupModel.getValueAt(selectedRow, 0);
                    for (Group g : groupController.getGroups()) {
                        if (g.getName().equals(selectedGroupName)) {
                            new UpdateGroupWindow(g, groupController, contactController);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un groupe à modifier.");
                }
            }
        });

        listLabel = new JLabel("List of groups");
        styleJLabel(listLabel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(listLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(groupScroll);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(contactScroll);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(buttonsPanel);

        add(rightPanel, BorderLayout.EAST);

        this.setResizable(false);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        groupModel.setRowCount(0);
        for (Group g : groupController.getGroups()) {
            groupModel.addRow(new Object[]{g.getName(), g.getContacts().size()});
        }
        contactModel.setRowCount(0);
    }

}
