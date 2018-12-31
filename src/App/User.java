package App;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private double valor;
    private List<Server> reservas;

    public User (String e, String p) {
        this.password = p;
        this.email = e;
        this.valor = 0;
        this.reservas = new ArrayList<>();
    }

    public User (String u, String p, double v){
        this.password = p;
        this.email = u;
        this.valor = v;
        this.reservas = new ArrayList<>();
    }

    public String getUserName(){
        return this.email;
    }

    public boolean login(String password){
        return this.password.equals(password);
    }

    public List<Server> getReservas(){
        return this.reservas;
    }

    public void charge(double v){
        this.valor += v;
    }

    public double getValor(){
        return this.valor;
    }
}
