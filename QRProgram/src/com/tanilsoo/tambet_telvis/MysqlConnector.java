package com.tanilsoo.tambet_telvis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlConnector {

	private static final String SERVER_IP = "localhost";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final String DATABASE = "qrinfo";
	
	private static boolean connected = false;
	private static Connection conn = null;
	private static Statement statment = null;
	
	public MysqlConnector(){
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
	//name(str), length(int), diameter(int), puu(char), amt_packs(int), price(int), additional_info(str)
	public static List<List<String>> getPackOrder(){
		String query = "SELECT pack_order.name, post_type.length, post_type.diameter, post_type.puu, "
				+ "pack_order.amt_packs, pack_order.price, pack_order.additional_info, pack_order.packs_done FROM pack_order, post_type "
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
	
	public static void insertPactType(int length, int diameter, String puu, String uniqueFile){
		String query = String.format("INSERT INTO post_type(id, length, diameter, puu, unique_file) VALUES(NULL, %d, %d, '%s', '%s')",
				length, diameter, puu, uniqueFile);
		insert(query);
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
	
	public static Map<String, Integer> getLaosTavaPakke(){
		return getPakke("laos_tavalisi_pakke");
	}
	
	public static Map<String, Integer> getLaosImmutatudPakke(){
		return getPakke("laos_immutatud_pakke");
	}
	
	public static Map<String, Integer> getPakke(String table){
		Map<String, Integer> result = new HashMap();
		
		try{
			String query = "SELECT paki_id, amount FROM " + table;
			Map<Integer, Integer> dataImmutatud = new HashMap();
			ResultSet rows = statment.executeQuery(query);
			while(rows.next()){
				dataImmutatud.put(rows.getInt(1), rows.getInt(2));
			}
			
			query = "SELECT id, length, diameter, puu FROM post_type";
			rows = statment.executeQuery(query);
			while(rows.next()){
				String s = rows.getInt(2) + "/" + rows.getInt(3) + "/" + rows.getString(4);
				
				Integer amount = dataImmutatud.get(rows.getInt(1));
				
				if(amount == null){
					result.put(s, 0);
				} else {
					result.put(s, amount);
				}
			}
			
		
		} catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<String> getPostTypes(){
		String query = "SELECT id, length, diameter, puu FROM post_type";
		List<String> results = new ArrayList<>();
		ResultSet rows = null;
		try {
			rows = statment.executeQuery(query);
			while(rows.next()){
				results.add(rows.getString(1) + ". " + rows.getString(2) + "/" + rows.getString(3) + "/" + rows.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static List<String> getPrintingFiles(){
		String query = "SELECT pack_id FROM print";
		ResultSet rows = null;
		try {
			rows = statment.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<String> results = new ArrayList<>();
		
		try {
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
	
	public static boolean isConnected(){
		return connected;
	}
	
}
