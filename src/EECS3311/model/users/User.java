package EECS3311.model.users;

import EECS3311.model.PBKDF2Hasher;

public class User {
    public static PBKDF2Hasher hasher = new PBKDF2Hasher();

    protected String name, email ,encryptedPassword;
    protected int id;

    public User(int id, String name, String email, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return encryptedPassword;
    }

    public boolean mathPassword(String password) {
        return hasher.checkPassword(password.toCharArray(), encryptedPassword);
    }

    public void setPassword(String password) {
        this.encryptedPassword = hasher.hash(password.toCharArray());
    }
}
