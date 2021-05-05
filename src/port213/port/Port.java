package port213.port;



import port213.port.portcontent.Dock;
import port213.port.portcontent.Warehouse;
import port213.ship.Ship;
import port213.ship.shipcontent.WaybillField;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Port implements Runnable {

    private final Warehouse warehouse;
    private Exchanger<Ship> exchanger;
    private List<Dock> docks;
    private Ship waitingShip; //корабль ждущий второй корабль для разгрузки


    public Port(Exchanger<Ship> exchanger) {
        warehouse = new Warehouse();
        this.exchanger = exchanger;
        docks = new ArrayList<>();
        addDocks();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Ship ship = exchanger.exchange(null); //обмен между гаванью и портом
                System.out.println("Корабль прибыл в порт " + ship + "С грузом " + ship.getCargo().getContainers());
                if (waitingShip == null && ship.getCargo().getWaybill().getField() == WaybillField.TRANSFER) {
                    waitingShip = ship;
                    System.out.println("Корабль ожидает прибытия другого корабля для пергрузки");
                } else if (waitingShip != null && ship.getCargo().getWaybill().getField() == WaybillField.TRANSFER) {
                    System.out.println("Корабль для перегрузки прибыл");
                    transferActive(ship);//организация перегрузки
                } else {
                    checkDocks(ship); //подбираем док
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addDocks() {
        docks.add(new Dock(warehouse));
        docks.add(new Dock(warehouse));
        docks.add(new Dock(warehouse));
    }

    private void checkDocks(Ship ship) {
        external:
        while (true) {
            for (Dock dock : docks) {
                if (!dock.isLock()) {
                    dock.entrance(ship);
                    new Thread(dock).start();
                    break external;
                }
            }
        }
    }

    private void transferActive(Ship ship){
        Exchanger<Integer> exchanger = new Exchanger<>(); //буфер для двух доков
        checkDocks(waitingShip, exchanger); //подбираем док для корабля который ожидал пару
        waitingShip = null; //освобождаем место
        checkDocks(ship, exchanger); //док для другого корабля
    }

    /**
     * Для TRANSFER
     * @param ship корабль который ждал
     * @param exchanger буфер между доками
     */
    private void checkDocks(Ship ship, Exchanger<Integer> exchanger) {
        external:
        while (true) {
            for (Dock dock : docks) {
                if (!dock.isLock()) {
                    dock.setExDock(exchanger);
                    dock.entrance(ship);
                    new Thread(dock).start();
                    break external;
                }
            }
        }
    }
}