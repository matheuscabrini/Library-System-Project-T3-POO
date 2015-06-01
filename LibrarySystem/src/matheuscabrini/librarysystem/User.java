package matheuscabrini.librarysystem;

import java.util.ArrayList;

import org.joda.time.MutableDateTime;

//Representação de um usuário
public class User extends Record {	
	
	// Tipos possíveis de usuários
	public static final String TYPE_STUDENT = "student";
	public static final String TYPE_PROFESSOR = "professor";
	public static final String TYPE_COMMUNITY = "community";

	private Boolean suspended; // auxiliar para informar o status do usuário
	
	// Campos presentes nos arquivos:
	private String name = "";
	private int uniID = 0; // nUSP, por exemplo
	private int ID = 0; // RG; necessario caso nao tenha nUsp
	private String address = ""; // Endereço de moradia; necessario caso nao tenha nUsp
	private String email = "";
	private String type = ""; // "student" ou "professor" ou "community"
	private int rentalCount = 0; // Atual número de empréstimos que este user tem.
	private MutableDateTime suspensionEndDate; // Data final de suspensão imposta a este user;
	private MutableDateTime suspensionStartDate; // Data em que este user começou a estar suspenso;
								     		     // Se o user estiver nos conformes com seus 
												 // empréstimos, essas datas serão null.
	
	@Override
	String[] getData() {
		ArrayList<String> dataList = new ArrayList<>();
		
		dataList.add(""+code);
		dataList.add(name);
		dataList.add((uniID == 0) ? "" : ""+uniID);
		dataList.add((ID == 0) ? "" : ""+ID);;
		dataList.add(address);
		dataList.add(email);
		dataList.add(type);
		dataList.add(""+rentalCount);
		if (suspensionEndDate == null) {
			dataList.add(""); dataList.add(""); dataList.add(""); 
		}
		else {
			dataList.add(""+suspensionEndDate.getDayOfMonth());
			dataList.add(""+suspensionEndDate.getMonthOfYear());
			dataList.add(""+suspensionEndDate.getYear());
		}
		if (suspensionStartDate == null) {
			dataList.add(""); dataList.add(""); dataList.add(""); 
		}
		else {
			dataList.add(""+suspensionStartDate.getDayOfMonth());
			dataList.add(""+suspensionStartDate.getMonthOfYear());
			dataList.add(""+suspensionStartDate.getYear());
		}
		
		String[] retDataList = dataList.toArray(new String[dataList.size()]);
		return retDataList;
	}

	@Override
	void setData(String[] dataList) {
		int i = 0;
		code = Integer.parseInt(dataList[i++]);
		name = dataList[i++];
		uniID = (dataList[i].isEmpty()) ? 0 : Integer.parseInt(dataList[i]); i++;
		ID = (dataList[i].isEmpty()) ? 0 : Integer.parseInt(dataList[i]); i++;
		address = dataList[i++];
		email = dataList[i++];
		type = dataList[i++];
		rentalCount = Integer.parseInt(dataList[i++]);
		if (!dataList[i].isEmpty()) { // se o campo <dia> existir, então há data para se obter
			if (suspensionEndDate == null) suspensionEndDate = new MutableDateTime();
			suspensionEndDate.setDayOfMonth(Integer.parseInt(dataList[i++]));
			suspensionEndDate.setMonthOfYear(Integer.parseInt(dataList[i++]));
			suspensionEndDate.setYear(Integer.parseInt(dataList[i++]));
		}
		if (!dataList[i].isEmpty()) { // se o campo <dia> existir, então há data para se obter
			if (suspensionStartDate == null) suspensionStartDate = new MutableDateTime();
			suspensionStartDate.setDayOfMonth(Integer.parseInt(dataList[i++]));
			suspensionStartDate.setMonthOfYear(Integer.parseInt(dataList[i++]));
			suspensionStartDate.setYear(Integer.parseInt(dataList[i]));
		}
	}
	
	// Método que checa se a cota de livros do user estourou
	public boolean exceedsRentalsLimit() {
		int maxRentalCount;
		if (type.equals(User.TYPE_STUDENT)) 
			maxRentalCount = SystemManager.MAX_STUDENT_BOOKS;
		else if (type.equals(User.TYPE_PROFESSOR)) 
			maxRentalCount = SystemManager.MAX_PROFESSOR_BOOKS;
		else 
			maxRentalCount = SystemManager.MAX_COMMUNITY_BOOKS;	
				
		return (rentalCount >= maxRentalCount) ? true : false;
	}
	
	// Retorna a quantia máxima de dias que que este user
	// pode ficar com um livro
	public int getMaxRentalDays() {
		if (type.equals(User.TYPE_STUDENT))
			return SystemManager.MAX_STUDENT_DAYS;
		else if (type.equals(User.TYPE_PROFESSOR)) 
			return SystemManager.MAX_PROFESSOR_DAYS;
		else 
			return SystemManager.MAX_COMMUNITY_DAYS;		
	}
	
	// Setters e getters:

	void setCode(int code) { // utilizado somente por SystemManager
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setUniID(int uniID) {
		this.uniID = uniID;
	}
	public int getUniID() {
		return uniID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public int getID() {
		return ID;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	void incrementRentalCount() { // utilizado somente por SystemManager
		rentalCount++;
	}
	void decrementRentalCount() { // utilizado somente por SystemManager
		rentalCount--;
	}
	public int getRentalCount() {
		return rentalCount;
	}
	void setSuspended(boolean value) {
		suspended = value;
		if (value == false) { 
			suspensionEndDate = null;
			suspensionStartDate = null;
		}
	}
	public boolean isSuspended() {
		if (suspended != null) 
			return suspended.booleanValue(); 
		else if (suspensionEndDate != null || suspensionStartDate != null)
			return true;
		else 
			return false;
	}
	void setSuspensionEndDate(int d, int m, int y) { // utilizado somente por SystemManager
		if (suspensionEndDate == null) suspensionEndDate = new MutableDateTime();
		suspensionEndDate.setDayOfMonth(d);
		suspensionEndDate.setMonthOfYear(m);
		suspensionEndDate.setYear(y);
	}
	MutableDateTime getSuspensionEndDate() {
		return (suspensionEndDate == null) ? null : suspensionEndDate.copy();
	}
	public int getSuspensionEndDay() {
		return (suspensionEndDate == null) ? 0 : suspensionEndDate.getDayOfMonth();
	}
	public int getSuspensionEndMonth() {
		return (suspensionEndDate == null) ? 0 : suspensionEndDate.getMonthOfYear();
	}
	public int getSuspensionEndYear() {
		return (suspensionEndDate == null) ? 0 : suspensionEndDate.getYear();
	}	
	void setSuspensionStartDate(int d, int m, int y) { // utilizado somente por SystemManager
		if (suspensionStartDate == null) suspensionStartDate = new MutableDateTime();
		suspensionStartDate.setDayOfMonth(d);
		suspensionStartDate.setMonthOfYear(m);
		suspensionStartDate.setYear(y);
	}
	MutableDateTime getSuspensionStartDate() {
		return (suspensionStartDate == null) ? null : suspensionStartDate.copy();
	}
	public int getSuspensionStartDay() {
		return (suspensionStartDate == null) ? 0 : suspensionStartDate.getDayOfMonth();
	}
	public int getSuspensionStartMonth() {
		return (suspensionStartDate == null) ? 0 : suspensionStartDate.getMonthOfYear();
	}
	public int getSuspensionStartYear() {
		return (suspensionStartDate == null) ? 0 : suspensionStartDate.getYear();
	}
	
	// Métodos para facilitar a apresentação dos dados
	// na tabela da GUI:
	public String getSuspensionEndDateAsString() {
		if (suspensionEndDate == null)
			return "-";
		else {
			String date = suspensionEndDate.getDayOfMonth() + "/" +
					suspensionEndDate.getMonthOfYear() + "/" +
					suspensionEndDate.getYear();
			return date;
		}
	}
	public String getSuspensionStartDateAsString() {
		if (suspensionStartDate == null)
			return "-";
		else {
			String date = suspensionStartDate.getDayOfMonth() + "/" +
					suspensionStartDate.getMonthOfYear() + "/" +
					suspensionStartDate.getYear();
			return date;
		}
	}
}
