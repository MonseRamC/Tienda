package company.project;

import java.util.function.BiPredicate;

public class Producto {

    public String nombre;
    public String descripcion;
    public double precio;
    public double cantidad;
    public double peso;

    public double totalAPagar;
    public Boolean existencias;

    public Producto() {

    }

    public Producto(String role, String descripcion, double precio, double cantidad, double peso, double totalAPagar, Boolean existencias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.peso = peso;
        this.precio = precio;

        this.totalAPagar = totalAPagar;
        this.existencias = existencias;
    }

}