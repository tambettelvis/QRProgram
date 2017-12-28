package ee.tanilsoo.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.stage.FileChooser;

public class FileHandler {

	static final String FILE_LOCATION = "data.txt";

	public static void saveToFile(List<Pack> packs) throws IOException {
		if(packs.isEmpty())
			return;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(FILE_LOCATION)));
		
		for(Pack p : packs){
			bw.append(String.format("%d|%d|%d|%s|%s", p.getId(), p.getLength(), p.getDiameter(), p.getUniqueFile(), p.getPuu()));
			if(p.getAdditionInformation() != null)
				bw.append("|" + p.getAdditionInformation());
			bw.append(System.lineSeparator());
		}
		bw.close();
	}
	
	public static void loadFromFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(FILE_LOCATION)));
		
		String line;
		while((line = br.readLine()) != null){
			String[] data = line.split("\\|");
			if(data.length == 6){
				PackManager.addPack(new Pack(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]),
						data[3], data[4], data[5]));
			} else {
				PackManager.addPack(new Pack(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]),
						data[3], data[4]));
			}
		}
		br.close();
	}
	
	public static void exportExcel(){
		Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("new sheet");
	    
	    Map<Integer, Integer> immutatud = MysqlConnector.getLaosImmutatudPakke();
	    

	    int startRow = 2;
	    
	    Row row = sheet.createRow(startRow);
	    row.createCell(0).setCellValue(createHelper.createRichTextString("Immutatud pakid"));
	    startRow +=2;
	    sheet.createRow(startRow).createCell(0).setCellValue(createHelper.createRichTextString("Kuusk"));
	    startRow +=2;
	    Row titles = sheet.createRow(startRow);
	    titles.createCell(0).setCellValue(createHelper.createRichTextString("Pakk"));
	    titles.createCell(1).setCellValue(createHelper.createRichTextString("Lisainfo"));
	    titles.createCell(2).setCellValue(createHelper.createRichTextString("Kogus"));
	    startRow++;
	    
	    Iterator<Map.Entry<Integer, Integer>> it1= immutatud.entrySet().iterator();
	    while (it1.hasNext()) {
	    	Map.Entry<Integer, Integer> entry = it1.next();
	    	Row r = sheet.createRow(startRow);
	    	Pack pack = PackManager.getPackById(entry.getKey());
	    	if(pack.getPuu().equals("M"))
	    		continue;
	    	r.createCell(0).setCellValue(createHelper.createRichTextString(String.format("%d / %d / %s", pack.getDiameter(), pack.getLength(), pack.getPuu())));
	    	r.createCell(1).setCellValue(createHelper.createRichTextString(pack.getAdditionInformation()));
		    r.createCell(2).setCellValue(entry.getValue());
		    startRow += 1;
	    }
	    
	    startRow += 2;
	    sheet.createRow(startRow).createCell(0).setCellValue(createHelper.createRichTextString("Mänd"));
	    startRow += 2;
	    
	    it1= immutatud.entrySet().iterator();// TODO Should fix this horrible mess.
	    while (it1.hasNext()) {
	    	Map.Entry<Integer, Integer> entry = it1.next();
	    	Row r = sheet.createRow(startRow);
	    	Pack pack = PackManager.getPackById(entry.getKey());
	    	if(pack.getPuu().equals("K"))
	    		continue;
	    	r.createCell(0).setCellValue(createHelper.createRichTextString(String.format("%d / %d / %s", pack.getDiameter(), pack.getLength(), pack.getPuu())));
	    	r.createCell(1).setCellValue(createHelper.createRichTextString(pack.getAdditionInformation()));
		    r.createCell(2).setCellValue(entry.getValue());
		    startRow += 1;
	    }
	    
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Salvesta fail");
	    FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Excel failid", "*.xls", "*.xlsx", "*.xlt");
	    fileChooser.getExtensionFilters().add(fileExtensions);
	    
	    File file = fileChooser.showSaveDialog(Main.primaryStage);
	    if(file!= null){
		    try {
				FileOutputStream stream = new FileOutputStream(file);
				wb.write(stream);
				stream.close();
				wb.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
}
