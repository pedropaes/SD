package App;

class BoundedBuffer{
    private String[] array;
    private int elements;

    public BoundedBuffer(int size){
        this.array = new String[size];
        this.elements = 0;
    }

    public void put(String s){
        synchronized(this){
            while(this.elements >= this.array.length){
                try{ this.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            this.array[this.elements++] = s;
            this.notifyAll();
        }
    }

    public String get(){
        String res;
        synchronized(this){
            while(this.elements <= 0){
                try{ this.wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            //this.elements--;
            res = this.array[this.elements-1];
            this.notifyAll();
        }
        return res;
    }

    public synchronized int getSize(){
        return this.elements;
    }
}
