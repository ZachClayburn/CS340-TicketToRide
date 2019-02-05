package com.tickettoride.facades;

import command.Command;

import java.util.concurrent.LinkedBlockingQueue;


public class FacadeThread extends Thread {

    LinkedBlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

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
        while (true) {
            try {
                var command = commandQueue.take();

                var result = command.execute();

                //FIXME I'm not really sure how to correctly send this back to the caller
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
