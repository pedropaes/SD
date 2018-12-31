package App;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        Map<String, User> users = new HashMap<>();
        User u1 = new User("1", "1");
        users.put(u1.getUserName(),u1);
        Map<Integer, ServerBuffer> servers = new HashMap<>();
        ServerBuffer type1 = new ServerBuffer(3, "s1.small");
        ServerBuffer type2 = new ServerBuffer(3, "l1.large");

        Server s1 = new Server(1,"s1.small", 2.00);
        Server s2 = new Server(1,"s1.small", 2.00);
        Server s3 = new Server(1,"s1.small", 2.00);
        Server s4 = new Server(2,"l1.large", 4.00);
        Server s5 = new Server(2,"l1.large", 4.00);
        Server s6 = new Server(2,"l1.large", 4.00);

        type1.putServer(s1);
        type1.putServer(s2);
        type1.putServer(s3);

        type2.putServer(s4);
        type2.putServer(s5);
        type2.putServer(s6);

        servers.put(1,type1);
        servers.put(2,type2);

        int port = Integer.parseInt(args[0]);
        ServerSocket ss = new ServerSocket(port);

        while(true){
            Socket cs = ss.accept();
            ClientThread t = new ClientThread(cs, users,servers);
            t.start();
        }
    }
}

