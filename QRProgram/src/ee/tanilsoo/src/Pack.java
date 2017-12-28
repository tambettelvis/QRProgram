package ee.tanilsoo.src;

public class Pack {

	private int id;
	private int length;
	private int diameter;
	private int amtInPack;
	private String additionInformation;
	private String uniqueFile;
	private String puu;
	
	private int amount = 1;
	
	public Pack(int id, int length, int diameter, String uniqueFile, String puu) {
		this.id = id;
		this.length = length;
		this.diameter = diameter;
		this.uniqueFile = uniqueFile;
		this.puu = puu;
	}
	
	public Pack(int id, int length, int diameter, String uniqueFile, String puu, String additionInformation) {
		this.id = id;
		this.length = length;
		this.diameter = diameter;
		this.additionInformation = additionInformation;
		this.uniqueFile = uniqueFile;
		this.puu = puu;
	}
	
	@Override
	public String toString() {
		if(additionInformation != null)
			return String.format("%d / %d / %s - %s", diameter, length, puu, additionInformation);
		return String.format("%d / %d / %s", diameter, length, puu); 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public int getAmtInPack() {
		return amtInPack;
	}

	public void setAmtInPack(int amtInPack) {
		this.amtInPack = amtInPack;
	}

	public String getAdditionInformation() {
		return additionInformation;
	}

	public void setAdditionInformation(String additionInformation) {
		this.additionInformation = additionInformation;
	}

	public String getUniqueFile() {
		return uniqueFile;
	}

	public void setUniqueFile(String uniqueFile) {
		this.uniqueFile = uniqueFile;
	}

	public String getPuu() {
		return puu;
	}

	public void setPuu(String puu) {
		this.puu = puu;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
