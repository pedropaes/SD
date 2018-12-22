package App;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String password;
    private double valor;
    private List<Server> reservas;

    public User(String u, String p, String e, double v){
        this.username = u;
        this.password = p;
        this.email = e;
        this.valor = v;
        this.reservas = new ArrayList<>();
    }

    public String getUserName(){
        return this.username;
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
