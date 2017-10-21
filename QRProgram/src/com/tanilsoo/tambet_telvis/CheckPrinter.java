package com.tanilsoo.tambet_telvis;

import java.net.SocketException;
import java.util.List;

public class CheckPrinter extends Thread {

	ConnectPrinter printer;
	
	public CheckPrinter(){
		printer = new ConnectPrinter();
	}
	
	@Override
	public void run(){
		List<String> fileNames = MysqlConnector.getPrintingFiles();
		
		if(fileNames.size() > 0){
			System.out.println("Printing...");
			for(String fileName : fileNames){
				printer.printToPrinter(fileName);
			}
			MysqlConnector.clearPrintTable();
		}
		
		System.out.println("CHECKED!");
	}
	
}
