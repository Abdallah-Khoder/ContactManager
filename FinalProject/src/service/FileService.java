package service;

import controler.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class FileService {

    public void sauvegarderContacts(ContactController contacts) {
        try {
            FileOutputStream fos = new FileOutputStream("contacts.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(contacts);
            oos.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
    }

    public ContactController chargerContacts() {
        ContactController contacts = new ContactController();
        try {
            FileInputStream fis = new FileInputStream("contacts.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            contacts = (ContactController) ois.readObject();
            ois.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Class Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
        return contacts;
    }

    public void sauvegarderGroupes(GroupController groupes) {
        try {
            FileOutputStream fos = new FileOutputStream("groups.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(groupes);
            oos.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
    }

    public GroupController chargerGroupes() {
        GroupController groups = new GroupController();
        try {
            FileInputStream fis = new FileInputStream("groups.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            groups = (GroupController) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Class Not Found ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
        return groups;
    }

    public boolean isTableEmpty(DefaultTableModel model) {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                if (value != null && !value.toString().trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

}
