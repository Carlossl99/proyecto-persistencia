package persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Jugador {
    @Id
    @GeneratedValue
    private int nij;
    private String nombre;
    private int puntuacion;
    @ManyToOne
    private Equipo equipo; //equipo al que pertenece

    public Jugador() {
    }

    public Jugador(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getNij() {
        return nij;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    @Override
    public String toString() {
        return "persistence.Jugador{" +
                "nij=" + nij +
                ", nombre='" + nombre + '\'' +
                ", puntuacion=" + puntuacion + (equipo != null ? ", equipo=" + equipo.getNombre() + '}': '}');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return getNij() == jugador.getNij();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNij());
    }
}
