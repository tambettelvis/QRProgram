package com.tanilsoo.tambet_telvis;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class OutputPrinter implements Printable {

	private static final int QR_MARGIN_X = -20;
	private static final int QR_MARGIN_Y = 5;
	private static final int LABEL_MARGIN_X = 30;
	private static final int LABEL_MARGIN_Y = 30;
	private static final String FILE_TYPE = ".jpeg";
	private static final String PATH = "C://woodmasters/images/";
	
	String imageFileName;
	String label;
	String addtionalInfo;

    public OutputPrinter(String imageFileName, String label, String additonalInfo){
    	this.imageFileName = imageFileName;
    	this.label = label;
    	this.addtionalInfo = additonalInfo;
    }

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		// Should only have one page, and page # is zero-based.
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		// Adding the "Imageable" to the x and y puts the margins on the page.
		// To make it safe for printing.
		Graphics2D g2d = (Graphics2D) g;
        
		
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(PATH + imageFileName  + FILE_TYPE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR, "Image not found at " + PATH + imageFileName  + FILE_TYPE, ButtonType.OK);
			alert.show();
			e.printStackTrace();
			System.out.println("----IMG NOT FOUND");
		}		
		
		
		// Draw the page:
	
		//g2d.scale(0.5, 0.5);
		g2d.translate(LABEL_MARGIN_X, LABEL_MARGIN_Y);
		System.out.println(label);
		g2d.setFont(new Font("Calibri", Font.PLAIN, 20));
		g2d.setColor(Color.BLACK);
		g2d.drawString(label, 0, 0);
		
		g2d.translate(QR_MARGIN_X, QR_MARGIN_Y);
		g2d.drawImage(bi, 0, 0, null);
		System.out.println(addtionalInfo);
		if(addtionalInfo != null){
			g2d.translate(40, bi.getHeight() + 7);
			g2d.setFont(new Font("Calibri", Font.PLAIN, 16));
			g2d.drawString(addtionalInfo, 0, 0);
		}
		
		//g2d.draw(new Rectangle(0, 0, 100, 100));
		//g2d.draw(new Rectangle2D.Double(1, 1, width - 1, height - 1));     
		

		return PAGE_EXISTS;
	}
	
}
