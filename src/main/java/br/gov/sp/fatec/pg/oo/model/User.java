package br.gov.sp.fatec.pg.oo.model;

public class User {

    private int id;
    private String username;
    private String password; 
    private String role;

    public User (){}
    public User (int id, String username, String password, String role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getUsername(){
        return username;
    }
    public void setUserngit push origin main
ame(String name){
        this.username = name;
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

