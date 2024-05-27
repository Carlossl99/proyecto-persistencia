import jakarta.persistence.*;
import java.util.*;

/*
* CREACIÓN DE LAS TABLAS
*create table equipo(
    nie int primary key,
    nombre varchar(15)
);
* create table jugador(
    nij int primary key,
    nombre varchar(30),
    puntuacion int,
    equipo_nie int references equipo(nie)
);
*
* La asociación que he elegido entre la entidad Equipo y la entidad Jugador es de uno a muchos, ya que un equipo puede
* tener varios jugadores y un jugador solo puede estar en un equipo. Como éste puede cambiar de equipo, he hecho la asociación
* bidireccional para saber el equipo al que pertenece un jugador, usando las anotaciones y los tipos de datos
* apropiados en cada una de las entidades, así como la estructura de tablas en la base de datos correspondiente.
* */

public class Main {
    public static void main(String[] args) {
        //Creo el EntityManager al principio del programa para tener la conexión establecida con la base de datos
        //y lo paso como parametro al resto del programa
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        int op;

        do{
            muestraMenu();
            op = capturaOpcion();
            filtraOpcion(op, em);
        }while(op != 8);
        //Cierro el EntityManager al finalizar el programa
        em.close();
        emf.close();
    }

    static void muestraMenu(){
        System.out.println("\nGestión de equipos y sus jugadores\n-------------------------------");
        System.out.println("1) Nuevo equipo");
        System.out.println("2) Baja de equipo");
        System.out.println("3) Nuevo jugador");
        System.out.println("4) Asignación de un jugador a un equipo");
        System.out.println("5) Mostrar los datos de un equipo");
        System.out.println("6) Mostrar todos los equipos");
        System.out.println("7) Mostrar todos los jugadores");
        System.out.println("8) Salir");
    }

    static int capturaOpcion(){
        Scanner sc = new Scanner(System.in);
        int c;

        do{
            System.out.print("\n¿Qué operación quieres hacer? (1-8): ");
            c = sc.nextInt();
            if(c < 1 || c > 8)
                System.out.println("Elige una opción entre 1 y 8.");
        }while(c < 1 || c > 8);

        return c;
    }

    static void filtraOpcion(int op, EntityManager em){
        switch (op){
            case 1:
                altaEquipo(em);
                break;
            case 2:
                bajaEquipo(em);
                break;
            case 3:
                altaJugador(em);
                break;
            case 4:
                asignarJugador(em);
                break;
            case 5:
                mostrarEquiposDetallados(em);
                break;
            case 6:
                mostrarEquipos(em);
                break;
            case 7:
                mostrarJugadores(em);
                break;
            default:
        }
    }
    //Funcion para dar de alta un equipo
    private static void altaEquipo(EntityManager em) {
        Scanner sc = new Scanner(System.in);
        String nombre;

        System.out.println("\nNUEVO EQUIPO\n=======================");
        System.out.print("Introduce el nombre del equipo: ");
        nombre = sc.nextLine();
        Equipo equipo = new Equipo(nombre);
        //Bloque de persistenia
        try {
            em.getTransaction().begin(); //comienzo la transaccion
            em.persist(equipo); //persisto el equipo
            em.getTransaction().commit(); //termino la transaccion
            System.out.println("EQUIPO REGISTRADO");
        }catch (Exception e){
            System.out.println("Error al registrar el equipo");
        }
    }
    //Funcion para mostrar un listado de los equipos registrados
    private static List<Equipo> mostrarEquipos(EntityManager em) {
        //Obtengo la lista ejeutando una consulta jpql
        List<Equipo> equipos = em.createQuery("SELECT e FROM Equipo e", Equipo.class).getResultList();

        //Si la lista no está vacia, muestro su contenido
        if(!equipos.isEmpty()){
            System.out.println("\nLista de equipos\n------------------------------");
            for(int i = 0;i < equipos.size();i++)
                System.out.println(i+1 + ") " + equipos.get(i).toString());
        }else
            System.out.println("No hay equipos registrados");
        //Devuelvo la lista para futuras aplicaciones
        return equipos;
    }
    //Funcion para dar de baja un equipo
    private static void bajaEquipo(EntityManager em) {
        Scanner sc = new Scanner(System.in);
        int nie;

        System.out.println("\nBAJA DE EQUIPO\n=======================");
        //Muestro los equipos registrados
        List<Equipo> equipos = mostrarEquipos(em);
        //Si la lista no está vacia, sigo con las operaciones
        if(!equipos.isEmpty()) {
            System.out.print("\nIntroduce el número de identificación del equipo: ");
            nie = sc.nextInt();
            Equipo equipo = em.find(Equipo.class, nie); //busco el equipo indicado
            //Si el equipo existe, lo elimino
            if (equipo != null) {
                em.getTransaction().begin();
                em.remove(equipo);
                em.getTransaction().commit();
                System.out.println("EQUIPO ELIMINADO");
            } else
                System.out.println("Este equipo no está registrado");
        }
    }
    //Funcion para dar de alta un jugador
    private static void altaJugador(EntityManager em) {
        Scanner sc = new Scanner(System.in);
        String nombre;
        int puntuacion;

        System.out.println("\nNUEVO JUGADOR\n==========================");
        System.out.print("Introduce el nombre del jugador: ");
        nombre = sc.nextLine();
        System.out.print("Introduce la puntuación máxima: ");
        puntuacion = sc.nextInt();
        Jugador jugador = new Jugador(nombre, puntuacion);
        //Bloque de persistencia
        try {
            em.getTransaction().begin();
            em.persist(jugador);
            em.getTransaction().commit();
            System.out.println("JUGADOR REGISTRADO");
        }catch (Exception e){
            System.out.println("Error al registrar el jugador");
        }
    }
    //Funcion para mostrar los jugadores registrados
    private static List<Jugador> mostrarJugadores(EntityManager em){
        //Obtengo la lista ejecutando una consulta jpql
        List<Jugador> jugadores = em.createQuery("SELECT j FROM Jugador j", Jugador.class).getResultList();
        //Si la lista no esta vacia, imprimo su contenido
        if(!jugadores.isEmpty()){
            System.out.println("\nLista de jugadores\n------------------------------");
            for(int i = 0;i < jugadores.size();i++)
                System.out.println(i+1 + ") " + jugadores.get(i).toString());
        }else
            System.out.println("No hay jugadores registrados");
        //Devuelvo la lista para futuras aplicaciones
        return jugadores;
    }
    //Funcion para asignar un jugador a un equipo
    private static void asignarJugador(EntityManager em) {
        Scanner sc = new Scanner(System.in);
        int nij, nie;

        System.out.println("\nASIGNACIÓN DE UN JUGADOR A UN EQUIPO\n=======================");
        List<Jugador> jugadores = mostrarJugadores(em);
        //Si hay jugadores registrados, sigo con las operaciones
        if(!jugadores.isEmpty()) {
            System.out.print("\nIntroduce el número de identificación del jugador: ");
            nij = sc.nextInt();
            List<Equipo> equipos = mostrarEquipos(em);
            //Si hay equipos registrados, sigo con las operaciones
            if(!equipos.isEmpty()) {
                System.out.print("\nIntroduce el número de identificación del equipo: ");
                nie = sc.nextInt();
                Equipo equipo = em.find(Equipo.class, nie);
                Jugador jugador = em.find(Jugador.class, nij);
                //Si el equipo y el jugador indicados existen, sigo con las operaciones
                if (equipo != null && jugador != null) {
                    //Si el equipo tiene menos de 5 jugadores en su plantilla, asigno el jugador al equipo y viceversa
                    if (equipo.getJugadores().size() < 5) {
                        em.getTransaction().begin();
                        jugador.setEquipo(equipo);
                        equipo.setJugador(jugador);
                        em.getTransaction().commit();
                        System.out.println("JUGADOR ASIGNADO");
                    } else
                        System.out.println("El equipo ya tiene 5 jugadores");
                } else
                    System.out.println("El equipo o el jugador no están registrados");
            }
        }
    }
    //Funcion para mostrar los jugadores del equipo indicado
    private static void mostrarEquiposDetallados(EntityManager em){
        Scanner sc = new Scanner(System.in);
        int nie;

        System.out.println("\nEQUIPO Y SUS JUGADORES\n=======================");
        List<Equipo> equipos = mostrarEquipos(em);
        //Si hay equipos registrados, sigo con las operaciones
        if(!equipos.isEmpty()) {
            System.out.print("\nIntroduce el número de identificación del equipo: ");
            nie = sc.nextInt();
            Equipo equipo = em.find(Equipo.class, nie);
            //Si el equipo indicado existe y tiene jugadores en su plantilla, sigo con las operaciones
            if (equipo != null && !equipo.getJugadores().isEmpty()) {
                System.out.println(equipo.toString()); //imprimo los datos del equipo
                //Ejecuto una consulta jpql para obtener los jugadores del equipo indicado
                Query jpql = em.createQuery("SELECT j FROM Jugador j WHERE j.equipo = :equipo", Jugador.class);
                jpql.setParameter("equipo", equipo);
                List<Jugador> jugadores = jpql.getResultList();
                //Imprimo los jugadores
                System.out.println("\nJugadores del equipo: ");
                for (int i = 0; i < jugadores.size(); i++)
                    System.out.println(i+1 + ") " + jugadores.get(i).toString());
            } else
                System.out.println("Este equipo no está registrado o no tiene jugadores en su plantilla");
        }
    }
}
