package App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class ClientThread extends Thread{
    private String id;
    private boolean logged;
    private boolean running;
    BoundedBuffer b;
    Socket s;
    BufferedReader br;
    PrintWriter pw;
    Map<String, String> users ;
    Map<Integer, Server> servers ;

    public ClientThread(BoundedBuffer b, Socket s, Map<String, String> users, Map<Integer, Server> servers) throws Exception{
        this.b = b;
        this.s = s;
        this.users = users;
        this.servers = servers;
        pw = new PrintWriter(s.getOutputStream());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        running = true;
        logged = false;
    }

    public boolean login(){
        String login, password;

        try {
            pw.println("Utilizador: ");
            pw.flush();
            login = br.readLine();
            pw.println("Password: ");
            pw.flush();
            password = br.readLine();
            if (users.containsKey(login)){
                if (password.equals(users.get(login))){ this.logged = true;
                    this.id = login;
                    pw.println("Bemvindo...\n");
                    pw.flush();}
                else this.logged = false;
            }
            if(!this.logged){
                Thread.sleep(500);
                pw.println("\nCredenciais erradas!\n");
                pw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logged;

    }

    public void registo() throws IOException {
        String nome, email;
        pw.println("Escolha o username:");
        pw.flush();
        nome = br.readLine();
        if(users.containsKey(nome)){pw.println("\n Username já existe!\n"); return;}
        pw.println("Insira o seu e-mail:");
        pw.flush();
        email = br.readLine();

    }


    public void menuUtilizador () throws IOException {
        pw.println("Escolha uma opção:");
        pw.println("==================");
        pw.println("1) Ver Reservas");
        pw.println("2) Reservar Servidor");
        pw.println("3) Libertar Servidor");
        pw.println("4) Consultar Conta");
        pw.println("5) Sair");
        pw.flush();
        String option = "";
        try{
            option = br.readLine();
        }
        catch (Exception e){System.out.println("Erro na leitura");}
        switch (option) {
            case "1":
                break;
            case "2": escolherServidor();
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":   menuPrincipal();
                        this.logged = false;
                        break;
            default:
                break;
        }
        System.out.println(option);
//    switch case of (option)
    }

    public void consulta(){

    }

    public void escolherServidor() {
        //
        List<String> lista;

        lista = this.servers.values().stream().filter(entry -> entry.isFree()).map(x -> x.getNum() +" - "+ x.toString()).collect(Collectors.toList());
        Collections.sort(lista);
        if(lista.size() == 0) pw.println("Não existem servidores disponiveis!");
        for(int i = 0; i < lista.size();i++){
            pw.println(lista.get(i));
        }
        pw.flush();
        String option = "";
        try {
            option = br.readLine();

        } catch (Exception e) {
            System.out.println("Erro na leitura");
        }
        try {
            if(this.servers.containsKey(Integer.parseInt(option))){
                Server s = servers.get(Integer.parseInt(option));
                s.reserva(this.id, LocalDateTime.now());
            }
            else{pw.println("Servidor ocupado/inexistente");pw.flush();}

        } catch (Exception e) {
            System.out.println("Erro na leitura");
        }
    }
    public void menuPrincipal () throws IOException {
        pw.println("Escolha uma opção:");
        pw.println("==================");
        pw.println("1) Registo");
        pw.println("2) Login");
        pw.println("3) Sair");
        pw.flush();
        String option = "";
        try{
            option = br.readLine();
        }
        catch (Exception e){System.out.println("Erro na leituuuuuuuuuuuuura");}
        switch (option) {
            case "1":   registo();
                        break;
            case "2":   login();
                        break;
            case "3":   running= false;
                        break;
            default:
                break;
        }
        System.out.println(option);

    }


    public void run(){
        while(this.running){
            if(!logged) try {
                menuPrincipal();
            } catch (IOException e) {
                e.printStackTrace();
            }

            else try {
                menuUtilizador();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        try {
            this.logged = false;
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
