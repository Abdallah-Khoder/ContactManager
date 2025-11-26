package controler;

import java.io.Serializable;
import java.util.ArrayList;
import model.*;

public class GroupController implements Serializable {

    private ArrayList<Group> groups;

    public GroupController() {
        groups = new ArrayList<Group>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public String getGroupByName(Group g) {
        return g.getName();
    }

    public boolean ajouterGroupe(Group g) {
        if (groups.contains(g)) {
            return false;
        }
        groups.add(g);
        return true;
    }

    public boolean supprimerGroupe(Group g) {
        if (groups.contains(g)) {
            groups.remove(g);
            return true;
        }
        return false;
    }

    public boolean modifierGroupe(Group ancien, Group nouveau) {
        if (groups.contains(ancien)) {
            groups.remove(ancien);
            groups.add(nouveau);
            return true;
        }
        return false;
    }

    public boolean ajouterContactAuGroupe(Group g, Contact c) {
        ArrayList<Contact> contact = g.getContacts();
        if (contact.contains(c)) {
            return false;
        }
        contact.add(c);
        return true;

    }

    public boolean retirerContactDuGroupe(Group g, Contact c) {
        ArrayList<Contact> contact = g.getContacts();
        if (contact.contains(c)) {
            g.removeContact(c);;
            return true;
        }
        return false;
    }

    public ArrayList<Group> chercherGroupeParNom(String texte) {
        ArrayList<Group> names = new ArrayList<>();
        for (Group name : groups) {
            if (name.getName().equals(texte)) {
                names.add(name);
            }
        }
        return names;
    }

    public ArrayList<Group> chercherGroupesParContact(Contact c) {
        ArrayList<Group> groupContact = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.getContacts().contains(c)) {
                groupContact.add(g);
            }
        }
        return groupContact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> allContacts = new ArrayList<>();
        for (Group group : groups) {
            for (Contact contact : group.getContacts()) {
                if (!allContacts.contains(contact)) {
                    allContacts.add(contact);
                }
            }
        }
        return allContacts;
    }

}
