package port213.port.portcontent;

public class Warehouse {

    private int containers;
    private boolean lock;

    public Warehouse() {
        containers = 10000;
    }

    public void add(int containers) {

        if (!lock) {
            lock = true;
            this.containers+=containers;
            lock = false;
            System.out.println("Склад получил " + containers + " контейнеров. " + "Store = " + this.containers);
        } else {
            add(containers);
        }

    }

    public int remove(int containers) {

        if (!lock) {
            lock = true;
            this.containers-=containers;
            System.out.println("Склад отдал " + containers + " контейнеров. " + "Store = " + this.containers);
            lock = false;
        } else {
           return remove(containers);
        }
return containers;
    }

}
