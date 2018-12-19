package App;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Server {
    private String id;
    private int num;
    private String user;
    private boolean ocupado;
    private double preco;

    private String key;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Server(int num, String id, double preco){
        this.num = num;
        this.id = id;
        this.preco = preco;
        this.ocupado = false;
    }

    public int getNum(){
        return this.num;
    }

    public boolean isFree(){
        return !ocupado;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Servidor: " + this.id + " ");
        s.append("Preço: "+ this.preco);
        return s.toString();
    }

    public String reserva(String user, LocalDateTime inicio){
        this.user = user;
        this.inicio = inicio;
        this.ocupado = true;

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String chave = new String(array, Charset.forName("UTF-8"));
        this.key = chave;
        return chave;
    }

    public double cancelareserva(String key){
        if(this.key == key){
            this.ocupado = false;
            this.user = "";
            long diff = ChronoUnit.SECONDS.between(this.fim, this.inicio);
            return this.preco*diff;
        }
        return 0.0;
    }

}
