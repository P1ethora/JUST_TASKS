package port213.ship;



import port213.ship.shipcontent.Cargo;
import port213.ship.shipcontent.WaybillField;

import java.util.concurrent.Exchanger;

public class Ship implements Runnable {

    Exchanger<Integer> ex;
    private Cargo cargo;


    @Override
    public void run() {
        try {
           cargo.setContainers(ex.exchange(cargo.getContainers())); //изменение
           if(cargo.getWaybill().getField() == WaybillField.TRANSFER) {
               cargo.setContainers(ex.exchange(cargo.getContainers()));
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void setExchanger(Exchanger<Integer> exchanger) {
        this.ex = exchanger;
    }

}
