package App;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class ClientThread extends Thread{
    private String user;
    private boolean logged;
    private boolean running;
    Socket s;
    BufferedReader br;
    PrintWriter pw;
    Map<String, User> users ;
    Map<Integer, ServerBuffer> servers ;

    public ClientThread(Socket s, Map<String, User> users, Map<Integer, ServerBuffer> servers) throws Exception{
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
            pw.print("\nUtilizador: ");
            pw.flush();
            login = br.readLine();
            pw.print("Password: ");
            pw.flush();
            password = br.readLine();
            if (users.containsKey(login)){
                User u = users.get(login);
                if (u.login(password)){
                    this.logged = true;
                    this.user = login;
                    pw.println("\nBemvindo...\n");
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
        String nome, password;
        pw.println("\nEscolha o username:");
        pw.flush();
        nome = br.readLine();
        if(users.containsKey(nome)) {
            pw.println("\n Username já existe!\n");
            return;
        }
        pw.println("Insira a password:");
        pw.flush();
        password = br.readLine();
        User u = new User (nome, password);
        users.put(nome, u);
    }

    public void menuUtilizador () throws IOException {
        pw.println("==================\n");
        pw.println("1) Ver Reservas");
        pw.println("2) Reservar Servidor");
        pw.println("3) Libertar Servidor");
        pw.println("4) Consultar Conta");
        pw.println("5) Sair");
        pw.print("Escolha uma opção: ");
        pw.flush();
        String option = "";
        try{
            option = br.readLine();
        }
        catch (Exception e){System.out.println("Erro na leitura");}
        switch (option) {
            case "1":   verReservas();
                        break;
            case "2":   escolherServidor();
                        break;
            case "3":   libertarServidor();
                        break;
            case "4":   consulta();
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
        User u = users.get(this.user);
        pw.println("\nSaldo para liquidar: " + u.getValor());
    }

    public void verReservas(){
        User u = users.get(this.user);
        List <Server>  lista = u.getReservas();
        if(lista!=null) {
            for (Server s : lista) {
                pw.println(s.toString());
            }
        }
        else pw.println("Não existem reservas!");
        pw.flush();
    }

    public void libertarServidor(){
        User u = this.users.get(this.user);
        pw.println("\nIndique a chave do servidor que deseja libertar:");
        pw.flush();
        String k = "";
        try{
            k = br.readLine();
        }catch (Exception e) {
            System.out.println("\nErro na leitura");
        }
        List<Server> servers = u.getReservas();
        Iterator<Server> iter = servers.iterator();

        while(iter.hasNext()){
            Server s = iter.next();
            if(s.getKey().equals(k)){
                u.charge(s.getPrice(k));
                ServerBuffer sb = this.servers.get(s.getType());
                sb.putServer(s);
                iter.remove();
            }
        }
    }


    public void escolherServidor() {

        List<String> lista ;
        lista =  servers.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(x -> x.getKey() +" - "+ x.getValue().getType()).collect(Collectors.toList());

        for(int i = 0; i < lista.size();i++){
            pw.println(lista.get(i));
        }
        int n = 0;

        pw.flush();
        String option = "";
        try {
            option = br.readLine();
            n = Integer.parseInt(option);

        } catch (Exception e) {
            System.out.println("\nErro na leitura");
        }
        try {
            if(this.servers.containsKey(n)){
                Server s = this.servers.get(n).getServer();
                System.out.println(s.toString());
                User u = this.users.get(this.user);
                pw.println(s.reserva(u,LocalDateTime.now()));
                pw.flush();
            }
            else{pw.println("\nServidor inexistente");pw.flush();}

        } catch (Exception e) {
            System.out.println("\nErro na leituuuuuuuuuura");
        }
    }

    public void menuPrincipal () throws IOException {
        pw.println("\n==================\n");
        pw.println("1) Registo");
        pw.println("2) Login");
        pw.println("3) Sair");
        pw.print("Escolha uma opção: ");
        pw.flush();
        String option = "";
        try{
            option = br.readLine();
        }
        catch (Exception e){System.out.println("\nErro na leitura");}
        switch (option) {
            case "1":   registo();
                        break;
            case "2":   login();
                        break;
            case "3":   System.out.println("Prima 'Enter' novamente");
                        running = false;
                        //System.exit(0);
                        break;
            default:    System.out.println("\nOpção Inválida");
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
