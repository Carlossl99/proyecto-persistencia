package persistence;

import jakarta.persistence.*;
import java.util.*;
import java.io.*;

import java.util.Objects;

@Entity
public class Equipo implements Serializable{
    @Id
    @GeneratedValue
    private int nie;
    private String nombre;
    @OneToMany(mappedBy = "equipo")
    private List<Jugador> jugadores; //Jugadores del equipo

    public Equipo() {
    }

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    public int getNie() {
        return nie;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
    //Método para añadir un jugador al equipo
    public void setJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    @Override
    public String toString() {
        return "persistence.Equipo{" +
                "nie=" + nie +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return getNie() == equipo.getNie();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNie());
    }
}
