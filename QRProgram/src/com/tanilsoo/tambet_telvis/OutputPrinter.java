package com.tanilsoo.tambet_telvis;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

public class OutputPrinter implements Printable {

	
	String imageFileName;

    public OutputPrinter(String imageFileName){
    	this.imageFileName = imageFileName;
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
		int x = (int) pf.getImageableX();
		int y = (int) pf.getImageableY();
		//g2d.setColor(Color.BLACK);
		//g2d.translate(x, y);
		
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(imageFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("----IMG NOT FOUND");
		}		
		

		// Draw the page:
	
		// Just a safety net in case no margin was added.
		
		g2d.drawImage(bi, x, y, null);
		

		return PAGE_EXISTS;
	}
	
}
