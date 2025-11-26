package controler;

import java.io.Serializable;
import java.util.*;
import javax.swing.JOptionPane;
import model.Contact;

public class ContactController implements Serializable {

    private ArrayList<Contact> contacts;

    public ContactController() {
        contacts = new ArrayList<Contact>();
    }

    public Contact getContact(Contact c) {
        if (contacts.contains(c)) {
            int index = contacts.indexOf(c);
            Contact con = contacts.get(index);
            return con;
        } else {
            return new Contact("", "", "");
        }
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public boolean ajouterContact(Contact c) {
        if (contacts.contains(c)) {
            return false;
        }
        contacts.add(c);
        return true;
    }

    public boolean supprimerContact(Contact c) {
        if (contacts.contains(c)) {
            contacts.remove(c);
            return true;
        }
        return false;
    }

    public boolean modifierContact(Contact ancien, Contact nouveau) {
        if (contacts.contains(ancien)) {
            contacts.remove(ancien);
            contacts.add(nouveau);
            return true;
        }
        return false;
    }

    public ArrayList<Contact> chercherParNom(String texte) {
        ArrayList<Contact> names = new ArrayList<>();
        for (Contact name : contacts) {
            if (name.getFname().equals(texte) || name.getLname().equals(texte)) {
                names.add(name);
            }
        }
        return names;
    }

    public ArrayList<Contact> sortedFName() {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getFname().compareTo(c2.getFname());
            }
        });

        return contacts;
    }

    public ArrayList<Contact> sortedLName() {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getLname().compareTo(c2.getLname());
            }
        });
        return contacts;
    }

    public ArrayList<Contact> sortedCity() {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getCity().compareTo(c2.getCity());
            }
        });

        return contacts;
    }

    @Override
    public String toString() {
        return contacts + " ";
    }

    public void printList(ArrayList<Contact> list) {
        Iterator<Contact> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
