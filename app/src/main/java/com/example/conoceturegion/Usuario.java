package com.example.conoceturegion;

public class Usuario {
    private int id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String region;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String nombre, String apellido, String correo, String contrasena, String region) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.region = region;
    }

    public int getId() {return id;}
    public String getUsuario() {return usuario;}
    public String getNombre() {return nombre;}
    public String getApellido() {return apellido;}
    public String getCorreo() {return correo;}
    public String getContrasena() {return contrasena;}
    public String getRegion() {return region;}


    public void setId(int id) {this.id = id;}
    public void setUsuario(String usuario) {this.usuario = usuario;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public void setCorreo(String correo) {this.correo = correo;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}
    public void setRegion(String region) {this.region = region;}
}
