package port213.port.portcontent;



import port213.ship.Ship;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;


public class Harbor implements Runnable {

    private final BlockingQueue<Ship> queue;
    private Exchanger<Ship> ex;
    private Exchanger<Ship> exToPort;
    private Thread send;

    public Harbor(Exchanger<Ship> ex, Exchanger<Ship> exToPort) {
        this.ex = ex;
        this.queue = new ArrayBlockingQueue<>(10);
        this.send = new Thread(new SendToPort(queue, exToPort));
        this.send.setName("Send");
        this.send.start();
        this.exToPort = exToPort;
    }

    @Override
    public void run() {

        while (true) {
                try {
                    queue.add(ex.exchange(null));
                    System.out.println("Корабль стал в очередь " + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}


class SendToPort implements Runnable {

    private final BlockingQueue<Ship> queue;
    private Exchanger<Ship> exchanger;

    public SendToPort(BlockingQueue<Ship> queue, Exchanger<Ship> exchanger) {
        this.queue = queue;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {

        while (true) {
            if (queue.size() > 0) {
                try {
                    exchanger.exchange(queue.take()); //передача корабля в порт
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("В гавани " + queue.size());
            }
        }
    }
}