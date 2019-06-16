package com.etu.recipemanagement;

public class User {
    private String id, name, email;

    public User() {
        this("","","");
    }

    public User(String n_id, String n_name, String n_email) {
        id = n_id;
        name = n_name;
        email = n_email;
    }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }


}
