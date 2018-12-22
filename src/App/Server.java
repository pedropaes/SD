package App;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Server {
    private String id;
    private int type;
    private String user;
    private double preco;

    private String key;

    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Server(int t, String id, double preco){
        this.type = t;
        this.id = id;
        this.preco = preco;
        this.user = "";
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.key + " ");
        sb.append("Servidor: " + this.id + " ");
        sb.append("Preço: "+ this.preco);
        return sb.toString();
    }

    public String reserva(User user, LocalDateTime inicio){
        this.user = user.getUserName();
        user.getReservas().add(this);
        this.inicio = inicio;

        String chave = Long.toHexString(Double.doubleToLongBits(Math.random()));

        this.key = chave;
        return chave;
    }

    public String getKey(){
        return this.key;
    }

    public double getPrice(String key){
            this.fim = LocalDateTime.now();
            long diff = ChronoUnit.SECONDS.between(this.inicio, this.fim);
            return this.preco*diff;

    }

}
