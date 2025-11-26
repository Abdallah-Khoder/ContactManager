package view;

import controler.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.FileService;

public class MainWindow extends JFrame {

    private JLabel titleLabel;
    private JButton buttonContact;
    private JButton buttonGroup;
    private JPanel leftPanel, centerPanel, mainPanel;
    private ContactController contactController;
    private GroupController groupController;

    public MainWindow() {
        FileService fileService = new FileService();
        contactController = fileService.chargerContacts();
        groupController = fileService.chargerGroupes();

        this.setTitle("Projet NFA035");
        mainPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Gestion des contacts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        buttonContact = new JButton("Contacts");
        buttonGroup = new JButton("Groups");

        buttonContact.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(buttonContact);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(buttonGroup);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        centerPanel = new JPanel();
        centerPanel.setBackground(Color.cyan);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        buttonContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ContactsWindow(contactController, groupController);
            }
        });

        buttonGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GroupWindow(contactController, groupController);
            }
        });

        this.add(mainPanel);
        this.setVisible(true);
        this.setResizable(false);
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

    }
}
