package com.nico.multiservicios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String apellidos;
    private String direccion;
    @Column(nullable = false, unique = true)
    private String email;
    
    private String telefono;
    private String identificacion;
    
    @Column(name = "fecha_registro")
    private Timestamp fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getDireccion() { return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}