package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;

public class Contact extends Observable implements Serializable {

    private String fname;
    private String lname;
    private String city;
    private ArrayList<String> phoneNumbers;

    public Contact(String f, String l, String c) {
        fname = f;
        lname = l;
        city = c;
        phoneNumbers = new ArrayList<String>();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
        setChanged();
        notifyObservers();
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
        setChanged();
        notifyObservers();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        setChanged();
        notifyObservers();
    }

    public boolean addPhoneNumber(String number) {
        if (!phoneNumbers.contains(number)) {
            boolean succ = phoneNumbers.add(number);
            if (succ) {
                setChanged();
                notifyObservers();
            }
            return succ;
        }
        return false;
    }

    public void removeNumber(String number) {
        phoneNumbers.remove(number);
    }

    @Override
    public String toString() {
        return fname + " " + lname + " - " + city;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contact contact = (Contact) obj;
        return fname.equals(contact.fname)
                && lname.equals(contact.lname)
                && city.equals(contact.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fname, lname, city);
    }

    public void notifyModification() {
        setChanged();
        notifyObservers();
    }

}
