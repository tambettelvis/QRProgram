package com.tanilsoo.tambet_telvis;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ConnectPrinter {
	
	public void printToPrinter(String imageFileName){
		printToPrinter(imageFileName, true, 1);
	}

	public void printToPrinter(String imageFileName, boolean defaultPrinter, int amount) {
	    PrinterJob job = PrinterJob.getPrinterJob();
	    
	    boolean doPrint = true;
	    
	    if(!defaultPrinter){
	    	doPrint = job.printDialog();
	    }
	    
	    if(doPrint){
	    	Pack pack = PackManager.getPackByUniqueFile(imageFileName);
	    	String label = pack.getDiameter() + " / " + pack.getLength() + " / " + pack.getPuu();
		    PageFormat pf = job.defaultPage();
		    
		    
		    Paper paper = pf.getPaper();
		    System.out.println("paper info...");
		    System.out.println(paper.getWidth());
		    System.out.println(paper.getHeight());
		    System.out.println(paper.getImageableWidth());
		    System.out.println(paper.getImageableHeight());
		    
		    double width = fromCMToPPI(6.0D);
		    double height = fromCMToPPI(8.8D);
		    paper.setImageableArea(0, 0, width, height);
		    paper.setImageableArea(0, 0, width, height);
		    pf.setPaper(paper);
		    
		    job.setPrintable(new OutputPrinter(imageFileName, label, pack.getAdditionInformation()), pf);
		    job.setCopies(amount);
	        try 
	        {
	            job.print();
	        }
	        catch (PrinterException e)
	        {
	        	Alert alert = new Alert(AlertType.ERROR, "Printing error: " + e.getMessage(), ButtonType.OK);
				alert.show();
	        	System.out.println("PRINTING ERROR");
	        	e.printStackTrace();
	            // Print job did not complete.
	        }
	    }
	    
	}
	
	protected static double fromCMToPPI(double cm) {            
        return toPPI(cm * 0.393700787);            
    }

	protected static double toPPI(double inch) {            
        return inch * 72d;            
    }
	
}
