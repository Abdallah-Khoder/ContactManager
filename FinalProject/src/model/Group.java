package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class Group extends Observable implements Serializable {

    private String name;
    private String description;
    private ArrayList<Contact> contacts;

    public Group(String name, String des) {
        this.name = name;
        description = des;
        contacts = new ArrayList<Contact>();
    }

    public String getName() {
        return name;
    }

    public int getCountContactOfGroup() {
        return contacts.size();
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        setChanged();
        notifyObservers();
    }

    public void addContact(Contact contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact);
            setChanged();
            notifyObservers();
        }
    }

    public void removeContact(Contact contact) {
        for (Contact s : contacts) {
            if ((s.getFname().equals(contact.getFname())) && (s.getLname().equals(contact.getLname()))
                    && (s.getCity().equals(contact.getCity()))) {
                contacts.remove(s);
                setChanged();
                notifyObservers();
                return;
            }
        }
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    @Override
    public String toString() {
        return name + " " + description + " " + contacts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Group group = (Group) obj;
        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void notifyModification() {
        setChanged();
        notifyObservers();
    }

}
