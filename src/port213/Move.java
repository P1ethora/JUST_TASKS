package port213;


import port213.ship.Ship;
import port213.ship.shipcontent.Cargo;
import port213.ship.shipcontent.Waybill;
import port213.ship.shipcontent.WaybillField;

import java.util.concurrent.Exchanger;

public class Move implements Runnable {

    private Exchanger<Ship> exchanger;

    public Move(Exchanger<Ship> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {

        while (true) {

            Ship ship = createShip();

            System.out.println("Корабль создан");
            try {
                System.out.println();
                exchanger.exchange(ship);
                System.out.println("Получен " + ship);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Ship createShip() {
        Ship ship = new Ship();
        Cargo cargo = new Cargo();
        Waybill waybill = new Waybill();
        waybill.setField(WaybillField.getRandom());

        if (waybill.getField() == WaybillField.UNLOAD)
            cargo.setContainers(50);
        else if (waybill.getField() == WaybillField.LOAD)
            cargo.setContainers(0);
        else
            cargo.setContainers(50);

        cargo.setWaybill(waybill);
        ship.setCargo(cargo);
        return ship;
    }
}
