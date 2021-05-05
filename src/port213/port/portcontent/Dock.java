package port213.port.portcontent;



import port213.ship.Ship;
import port213.ship.shipcontent.WaybillField;

import java.util.concurrent.Exchanger;

public class Dock implements Runnable {

    private Exchanger<Integer> exDock;
    private Exchanger<Integer> exShip;
    private Warehouse warehouse;
    private Ship ship;
    private boolean lock;

    public Dock(Warehouse warehouse) {
        this.warehouse = warehouse;
        exShip = new Exchanger<>();
    }

    @Override
    public void run() {

        if (ship != null) {
            ship.setExchanger(exShip);//добавляем буфер кораблю
            new Thread(ship).start();
            if (ship.getCargo().getWaybill().getField() == WaybillField.TRANSFER) {
                try {
                    load(transfer(unload()));  //обмен между кораблем и доком
                    System.out.println("Док загрузил корабль " + ship);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (ship.getCargo().getWaybill().getField() == WaybillField.LOAD) {
                try {
                    System.out.println("Началась загрузка");
                    load(warehouse.remove(50));
                    System.out.println("Корабль загружен");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (ship.getCargo().getWaybill().getField() == WaybillField.UNLOAD) {
                try {
                    System.out.println("Началась разгрузка");
                    warehouse.add(unload());
                    System.out.println("Корабль разгружен");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ship = null;
            lock = false;

        } else {
            throw new IllegalArgumentException("The ship can't be null");
        }

    }


    public void entrance(Ship ship) {
        this.ship = ship;
        System.out.println("Корабль зашел в док " + ship.getCargo().getContainers());
        lock = true;
    }

    private void load(int containers) throws InterruptedException {
       exShip.exchange(containers);
    }

    private int unload() throws InterruptedException {
        return exShip.exchange(0);
    }

    private int transfer(int count) throws InterruptedException {
        return exDock.exchange(count);
    }


    public boolean isLock() {
        return lock;
    }

    public void setExDock(Exchanger<Integer> ex) {
        this.exDock = ex;
    }
}
