package com.example.demo.SocialNetwork.Model;

import com.example.demo.SocialNetwork.Helper.Entity;

import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //gettere pentru fiecare atribut a unui user
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    //settere pentru fiecare proprietate a unui user
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [firstName=" + firstName + ", lastName=" + lastName + ", email="+ email + ", password=" + password + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
