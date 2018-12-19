package App;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        Map<String, String> users = new HashMap<>();
        users.put("pedro","pedro");

        Map<Integer, Server> servers = new HashMap<>();
        Server s1 = new Server(1,"s1.small", 2.00);
        Server s2 = new Server(2,"s2.small", 2.00);
        Server s3 = new Server(3,"s3.small", 2.00);
        Server s4 = new Server(4,"l1.large", 4.00);
        Server s5 = new Server(5,"l2.large", 4.00);
        Server s6 = new Server(6,"l3.large", 4.00);
        servers.put(1,s1);
        servers.put(2,s2);
        servers.put(3,s3);
        servers.put(4,s4);
        servers.put(5,s5);
        servers.put(6,s6);


        int port = Integer.parseInt(args[0]);
        ServerSocket ss = new ServerSocket(port);


        int soma = 0;
        String line ;
        BoundedBuffer b = new BoundedBuffer(100);

        while(true){
            Socket cs = ss.accept();
            ClientThread t = new ClientThread(b, cs, users,servers);
            t.start();
        }
    }
}

