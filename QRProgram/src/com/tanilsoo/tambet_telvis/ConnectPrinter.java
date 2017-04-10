package com.tanilsoo.tambet_telvis;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class ConnectPrinter {

	public void printToPrinter(String imageFileName) {
	    PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintable(new OutputPrinter(imageFileName));
	    
	    boolean doPrint = true;//job.printDialog();
	    
        try 
        {
            job.print();
        }
        catch (PrinterException e)
        {
            // Print job did not complete.
        }
	    
	}

	
}
