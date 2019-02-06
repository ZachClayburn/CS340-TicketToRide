package com.tickettoride.facades;

import command.Command;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;


public class FacadeThread extends Thread {

    LinkedBlockingQueue<QueueItem> commandQueue = new LinkedBlockingQueue<>();
    private boolean running = true;

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     * @see Thread(ThreadGroup, Runnable, String)
     */
    @Override
    public void run() {
        while (running) {//FIXME This could create a race condition, we may want to prevent that
            try {
                var item = commandQueue.take();

                var result = item.command.execute();

                //FIXME I'm not really sure how to correctly send this back to the caller
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void addCommandToQueue(Command command, UUID connectionID) {
        if (!commandQueue.offer(new QueueItem(command, connectionID))) {
            //TODO Handle error if item is rejected fom the queue
        }
    }

    private static class QueueItem {
        Command command;
        UUID connectionID;

        public QueueItem(Command command, UUID connectionID) {
            this.command = command;
            this.connectionID = connectionID;
        }
    }

    public void niceKill() {
        running = false;
    }
}
