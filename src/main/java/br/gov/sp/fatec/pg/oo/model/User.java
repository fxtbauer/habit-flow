package br.gov.sp.fatec.pg.oo.model;

public class User {

    private int id;
    private String username;
    private String email;
    private String password; 
    private String role;

    public User (){}
    public User (int id, String username, String email, String password, String role){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return username;
    }
    public void setName(String name){
        this.username = name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setParssword(String password){
        this.password = password;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }

}

