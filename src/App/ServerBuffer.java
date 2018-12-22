package App;

public class ServerBuffer {
    private Server[] servidores;
    private int elements;
    private String type;

    public ServerBuffer(int size, String type){
        this.servidores = new Server[size];
        this.elements = 0;
        this.type = type;
    }

    public void putServer(Server s){
        synchronized(this){
            while(this.elements >= this.servidores.length){
                try{ this.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            this.servidores[this.elements++] = s;
            this.notifyAll();
        }
        System.out.println(this.elements + "     " + this.servidores.length);
    }

    public Server getServer(){
        Server s;
        synchronized(this){
            while(this.elements <= 0){
                try{ this.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            this.elements--;
            s = this.servidores[this.elements];
            this.notifyAll();
        }
        System.out.println(this.elements + "     " + this.servidores.length);
        return s;
    }

    public String getType(){
        return this.type;
    }
}
