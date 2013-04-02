/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author petr.hejhal
 */
public class Printer extends Thread implements Printable {

    PrinterJob job = PrinterJob.getPrinterJob();
    boolean doJob = false;
    Queue<Graphics> printStack;

    public Printer() {
        printStack = new LinkedList<>();
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        g2d.drawString("Hello world!", 100, 100);

        return PAGE_EXISTS;
    }

    @Override
    public void run() {
        while (doJob) {
            try {
                Printer.sleep(1000);
                if (printStack.isEmpty()) {
                    return;
                }
                job.print();


            } catch (InterruptedException | PrinterException ex) {
                System.out.println("CHYBA TISKU: " + ex);
            }
        }
    }
}
