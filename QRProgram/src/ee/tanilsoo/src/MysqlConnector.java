package ee.tanilsoo.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class MysqlConnector {

	private static final String SERVER_IP = "medesteetika.ee";
	private static final String USER = "client";
	private static final String PASSWORD = "banaan2";
	private static final String DATABASE = "qrinfo";
	
	private static boolean connected = false;
	private static Connection conn = null;
	private static Statement statment = null;
	
	public MysqlConnector() {
		connectDatabase();
	}
	
	public static void connectDatabase(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection("jdbc:mysql://" + SERVER_IP + "/" + DATABASE, USER, PASSWORD);
			statment = conn.createStatement();
			connected = true;
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileNameById(int id){
		String query = "SELECT unique_file FROM post_type WHERE id=" + id;
		
		try {
			ResultSet result = statment.executeQuery(query);
			if(result.next()){
				return result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Number> getPackOperationsByAmount(int operation, int days, int startClock, int endClock){
		Map<String, Number> map = new LinkedHashMap<>();
		String query;
		if(startClock == -1 || endClock == -1){
			query = String.format("SELECT time, COUNT(time) FROM pack_operations WHERE operation=%d AND "
					+ "time > DATE_SUB(NOW(), INTERVAL %d DAY) GROUP BY DAY(TIME)", operation, days);
		} else {
			query = String.format("SELECT time, COUNT(time) FROM pack_operations WHERE operation=%d AND "
					+ "time > DATE_SUB(NOW(), INTERVAL %d DAY) AND HOUR(time) >= %d AND HOUR(time) < %d GROUP BY DAY(TIME)", operation, days, startClock, endClock);
		}
		try {
			LocalDate startDate = LocalDate.now().minusDays(days);
			
			ResultSet results = statment.executeQuery(query);
			while(results.next()){
				LocalDate resultDate = results.getTimestamp(1).toLocalDateTime().toLocalDate();
				while(startDate.isBefore(resultDate)){
					map.put(startDate.toString(), 0);
					startDate = startDate.plusDays(1);
				}
				map.put(resultDate.toString(), results.getInt(2));
				startDate = startDate.plusDays(1);
				
				if(startDate.isAfter(LocalDate.now().plusDays(1))){
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, Number> getPackOperationsByAmount(int operation, int days){
		return getPackOperationsByAmount(operation, days, -1, -1);
	}
	
	public static Map<String, Integer> getJobData(){
		Map<String, Integer> map = new HashMap<>();
		String query = "SELECT job_desc, priority FROM jobs";
		try {
			ResultSet results = statment.executeQuery(query);
			while(results.next()){
				map.put(results.getString(1), results.getInt(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static int getHighestPriority(){
		int priority = 1;
		String query = "SELECT MAX(priority) FROM jobs";
		try {
			ResultSet result = statment.executeQuery(query);
			if(result.next()){
				priority = result.getInt(1);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return priority;
	}
	
	public static void deleteOrder(int id){
		String query = "DELETE FROM pack_order WHERE id=" + id;
		insert(query);
	}
	
	public static void deleteJob(String description, int priority){
		String query = String.format("DELETE FROM jobs WHERE job_desc='%s' AND priority=%d", description, priority);
		String query2 = String.format("UPDATE jobs SET priority=priority - 1 WHERE priority >= %d", priority);
		insert(query);
		insert(query2);
	}
	
	public static void setJobPriority(String description, int priority){
		String query = String.format("UPDATE jobs SET priority=%d WHERE job_desc='%s'", priority, description);
		insert(query);
	}
	
	public static void insertNewJob(String description, int priority){
		String query1 = "UPDATE jobs SET priority=priority+1 WHERE priority>=" + priority;
		String query2 = String.format("INSERT INTO jobs(job_desc, priority) VALUES('%s', %d)", description, priority);
		insert(query1);
		insert(query2);
	}
	
	public static void updateTavalisi(int packId, int amount){
		String query = String.format("UPDATE laos_tavalisi_pakke SET amount=%d WHERE pack_id=%d", packId, amount);
		insert(query);
	}
	
	public static void updateImmutatud(int packId, int amount){
		String query = String.format("UPDATE laos_immutatud_pakke SET amount=%d WHERE pack_id=%d", packId, amount);
		insert(query);
	}
	
	//name(str), length(int), diameter(int), puu(char), amt_packs(int), price(int), additional_info(str)
	public static List<List<String>> getPackOrder(){
		String query = "SELECT pack_order.name, post_type.length, post_type.diameter, post_type.puu, "
				+ "pack_order.amt_packs, pack_order.price, pack_order.additional_info, pack_order.packs_done, pack_order.id FROM pack_order, post_type "
				+ "WHERE pack_order.post_type = post_type.id";
		List<List<String>> data = new ArrayList<List<String>>();
		try {
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				List<String> row = new ArrayList<String>();
				row.add(result.getString(1));
				row.add(String.valueOf(result.getInt(2)));
				row.add(String.valueOf(result.getInt(3)));
				row.add(result.getString(4));
				row.add(String.valueOf(result.getInt(5)));
				row.add(String.valueOf(result.getInt(6)));
				row.add(result.getString(7));
				row.add(String.valueOf(result.getInt(8)));
				row.add(String.valueOf(result.getInt(9)));
				data.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void insertOrder(String name, int postType, int amtOfPacks, int price, String additionalInfo){
		String query = String.format("INSERT INTO pack_order(name, post_type, amt_packs, price, additional_info) "
				+ "VALUES('%s', %d, %d, %d, '%s')", name, postType, amtOfPacks, price, additionalInfo);
		insert(query);
	}
	
	public static int getPackImmutamatAmountByEmployee(String employeeName, int days){
		return getPackAmountByEmployee(employeeName, 2, days);
	}
	
	public static int getPackImmutatudAmountByEmployee(String employeeName, int days){
		return getPackAmountByEmployee(employeeName, 3, days);
	}
	
	public static int getPackLaaditudAmountByEmployee(String employeeName, int days){
		return getPackAmountByEmployee(employeeName, 4, days);
	}
	
	public static int getPackAmountByEmployee(String employeeName, int operation, int days){
		String query = "SELECT COUNT(*) FROM pack_operations, employee WHERE pack_operations.employee_id=employee.id AND "
				+ "employee.name='" + employeeName + "' AND pack_operations.operation=" + operation + " AND "
				+ "time > DATE_SUB(NOW(), INTERVAL " + days + " DAY) ORDER BY time DESC";
		try{
			ResultSet result = statment.executeQuery(query);
			if(result.next()){
				return result.getInt(1);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		/* Error handling... */
		return -1;
	}
	
	public static List<String> getUniqueFiles(){
		String query = "SELECT unique_file FROM post_type";
		List<String> data = new ArrayList<String>();
		try {
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				data.add(result.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void insertPactType(int length, int diameter, String puu, String uniqueFile, String additionalInfo){
		String query;
		if(additionalInfo != null){
			query = String.format("INSERT INTO post_type(id, length, diameter, puu, unique_file, additional_info) VALUES(NULL, %d, %d, '%s', '%s', '%s')",
					length, diameter, puu, uniqueFile, additionalInfo);
		} else {
			query = String.format("INSERT INTO post_type(id, length, diameter, puu, unique_file, additional_info) VALUES(NULL, %d, %d, '%s', '%s', NULL)",
					length, diameter, puu, uniqueFile);
		}
		insert(query);
		PackManager.refreshPacksList();
	}
	
	public static List<String> getEmployeeNames(){
		String query = "SELECT name FROM employee";
		List<String> data = new ArrayList<String>();
		try {
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				data.add(result.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void insertEmployee(String employeeName){
		String query = "INSERT INTO employee(id, name) VALUES(NULL, '" + employeeName + "')";
		insert(query);
	}
	
	public static void insert(String query){
		try {
			statment.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> getPackOperationDataByEmployee(String employeeName){
		String query = "SELECT employee.name, post_type.length, post_type.diameter, post_type.puu, pack_operations.operation, pack_operations.time "
				+ "FROM employee,post_type, pack_operations WHERE pack_operations.employee_id=employee.id AND post_type.id=pack_operations.pack_id "
				+ "AND employee.name='" + employeeName + "' ORDER BY time DESC";
		return lastOperations(query);
	}
	
	public static List<String> getLastOperationsData(){
		String query = "SELECT employee.name, post_type.length, post_type.diameter, post_type.puu, pack_operations.operation, pack_operations.time "
				+ "FROM employee,post_type, pack_operations WHERE pack_operations.employee_id=employee.id AND post_type.id=pack_operations.pack_id ORDER BY time DESC";
		return lastOperations(query);
	}
	
	public static List<String> lastOperations(String query){
		List<String> data = new ArrayList<String>();
		try{
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				Timestamp time = result.getTimestamp(6);
				
				String operation = "";
				if(result.getInt(5) == 2){
					operation = "ladu";
				} else if(result.getInt(5) == 3){
					operation = "immutus";
				} else if(result.getInt(5) == 4) {
					operation = "laadimine";
				}
				String row = String.format("%s %d/%d/%s operatsioon:%s aeg:%s", result.getString(1), result.getInt(2), result.getInt(3), result.getString(4),
						operation, time.toString());
				data.add(row);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return data;
	}
	
	public static Map<String, Integer> getEmployeeOperationsAmount(int operation, int days){
		String query = " SELECT employee.name, COUNT(pack_operations.employee_id) FROM pack_operations, employee "
				+ "WHERE pack_operations.employee_id=employee.id AND pack_operations.operation=" + operation
				+ " AND time>DATE_SUB(NOW(), INTERVAL " + days + " DAY) GROUP BY employee_id;";
		Map<String, Integer> packsDone = new HashMap();
		try{
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				packsDone.put(result.getString(1), result.getInt(2));
			} 
			return packsDone;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;// Handle error...
	}
	
	public static String getEmployeeNameById(int id){
		try{
			String query = "SELECT name FROM employee WHERE id=" + id + " LIMIT 1";
			ResultSet result = statment.executeQuery(query);
			if(result.next()){
				return result.getString(1);
			} 
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;// Handle error...
	}
	
	
	public static Map<Integer, Integer> getLaosImmutatudPakke(){ 
		//ID, AMOUNT
		Map<Integer, Integer> result = new LinkedHashMap<>();
		
		try{
			String query = "SELECT paki_id, amount FROM laos_immutatud_pakke, post_type WHERE post_type.id=paki_id ORDER BY diameter, length";
			ResultSet rows = statment.executeQuery(query);
			while(rows.next()){
				result.put(rows.getInt(1), rows.getInt(2));
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<Pack> getPostTypes(){
		String query = "SELECT id, length, diameter, puu, unique_file, additional_info FROM post_type ORDER BY diameter, length";
		List<Pack> results = new ArrayList<>();
		ResultSet rows = null;
		try {
			rows = statment.executeQuery(query);
			while(rows.next()){
				int id = rows.getInt(1);
				int length = rows.getInt(2);
				int diameter = rows.getInt(3);
				String puu = rows.getString(4);
				String uniqueFile = rows.getString(5);
				String additionalInfo = rows.getString(6);
				
				if(additionalInfo != null){
					results.add(new Pack(id, length, diameter, uniqueFile, puu, additionalInfo));
				} else {
					results.add(new Pack(id, length, diameter, uniqueFile, puu));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static List<String> getPostDataByFileName(String fileName){
		String query = String.format("SELECT diameter, length, puu, additional_info FROM post_type WHERE unique_file='%s'", fileName);
		List<String> data = new ArrayList<String>();
		try{
			ResultSet rows = statment.executeQuery(query);
			if(rows.next()){
				data.add(String.valueOf(rows.getInt(1)));
				data.add(String.valueOf(rows.getInt(2)));
				data.add(rows.getString(3));
				data.add(rows.getString(4));
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return data;
	}
	
	public static List<String> getPrintingFiles(){
		String query = "SELECT pack_id FROM print";
		ResultSet rows = null;
		List<String> results = new ArrayList<>();
		try {
			rows = statment.executeQuery(query);
			
			while(rows.next()){
				results.add(rows.getString("pack_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static void clearPrintTable(){
		String query = "TRUNCATE TABLE print";
		try {
			statment.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPackInToLaosImmutatud(int id, int amt){
		insert("INSERT INTO laos_immutatud_pakke(id,paki_id,amount) VALUES(NULL," + id + "," + amt + ")");
	}
	
	public static void incrementLaosImmutatudPack(int id, int amt){
		insert("UPDATE laos_immutatud_pakke SET amount=amount+" + amt + " WHERE paki_id=" + id);
	}
	
	public static void decrementLaosImmutatudPack(int id, int amt){
		insert("UPDATE laos_immutatud_pakke SET amount=amount-" + amt + " WHERE paki_id=" + id);
	}
	
	public static boolean isPackInImmutatud(int id){
		String query = "SELECT id FROM laos_immutatud_pakke WHERE paki_id=" + id;
		ResultSet rows = null;
		try {
			rows = statment.executeQuery(query);
			if(rows.next()){
				System.out.println("returned true");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("returned false");
		return false;
	}
	
	public static boolean isConnected(){
		return connected;
	}
	
	public static int getPackAmount(int id){
		String query = "SELECT amount FROM laos_immutatud_pakke WHERE paki_id=" + id;
		try{
			ResultSet result = statment.executeQuery(query);
			while(result.next()){
				return result.getInt(1);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void setPackAmount(int id, int amount){
		String query1 = "SELECT amount FROM laos_immutatud_pakke WHERE paki_id=" + id;
		try{
			ResultSet result = statment.executeQuery(query1);
			String query2 = null;
			if(result.next()){
				System.out.println("RESULT");
				query2 = "UPDATE laos_immutatud_pakke SET amount=" + amount + " WHERE paki_id=" + id;
			} else {
				query2 = String.format("INSERT INTO laos_immutatud_pakke(paki_id, amount) VALUES(%d, %d)", id, amount);
			}
			insert(query2);
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
}
