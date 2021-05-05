package port213;



import port213.port.Port;
import port213.port.portcontent.Harbor;
import port213.ship.Ship;

import java.util.concurrent.Exchanger;

public class Main {

    public static void main(String[] args) {

        Exchanger<Ship> exchanger = new Exchanger<>(); //буфер между queue и move
        Exchanger<Ship> exchangerQueueToPort = new Exchanger<>(); //буфер между queue и port

        Thread move = new Thread(new Move(exchanger));
        move.setName("Move");
        move.start();
        System.out.println("движение запущено");
        Thread queue = new Thread(new Harbor(exchanger, exchangerQueueToPort));
        queue.setName("Queue");
        queue.start();
        System.out.println("очередь запущена");
        Thread port = new Thread(new Port(exchangerQueueToPort));
        port.setName("Port");
        port.start();
        System.out.println("порт запущен");


    }

}