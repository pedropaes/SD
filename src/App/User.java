package App;

import java.util.ArrayList;
import java.util.List;

class Reserva{
    private String server;
    private int slot;
/*
    public Reserva(String s, int slot){
        this.server = s;
        this.slot = slot;
    }*/

}

public class User {
    private String nome;
    private String email;
    private double quantia;
    private List<Server> reservas;

    public User(String n, String e, double q){
        this.nome = n;
        this.email = e;
        this.quantia = q;
        this.reservas = new ArrayList<>();
    }

    public void cobrarUser(double q){
        this.quantia += q;
    }
}
