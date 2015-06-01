package matheuscabrini.librarysystem;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.MutableDateTime;

// Representa��o de um empr�stimo de livro
public class Rental extends Record {	
	
	// Todos os campos da classe est�o presentes nos arquivos.
	private int bookCode = 0; // code do livro emprestado
	private int userCode = 0; // code do user que emprestou
	private String userType = ""; // "student", ou ...etc
	
	// Datas: setadas por SystemManager em newRental()
	private MutableDateTime rentalDate; // Data em que foi efetuado o empr�stimo
	private MutableDateTime returnDate; // Data m�xima de devolu��o em que ainda 
										// pode-se entregar o livro sem atraso
	
	// Construtor � tilizado somente por SystemManager.
	// Outras classes devem chamar newRental() de SystemManager
	// para criar um Rental asseguradamente v�lido.
	Rental() { 
		rentalDate = new MutableDateTime();
		returnDate = new MutableDateTime();
	}
	
	@Override
	String[] getData() {
		ArrayList<String> dataList = new ArrayList<>();
		dataList.add(""+code);
		dataList.add(""+bookCode);
		dataList.add(""+userCode);
		dataList.add(userType);
		dataList.add(""+rentalDate.getDayOfMonth());
		dataList.add(""+rentalDate.getMonthOfYear());
		dataList.add(""+rentalDate.getYear());
		dataList.add(""+returnDate.getDayOfMonth());
		dataList.add(""+returnDate.getMonthOfYear());
		dataList.add(""+returnDate.getYear());
		
		String[] retDataList = dataList.toArray(new String[dataList.size()]);
		return retDataList;
	}

	@Override
	void setData(String[] dataList) {
		int i = 0;
		code = Integer.parseInt(dataList[i++]);
		bookCode = Integer.parseInt(dataList[i++]);
		userCode = Integer.parseInt(dataList[i++]);
		userType = dataList[i++];
		rentalDate.setDayOfMonth(Integer.parseInt(dataList[i++]));
		rentalDate.setMonthOfYear(Integer.parseInt(dataList[i++]));
		rentalDate.setYear(Integer.parseInt(dataList[i++]));
		returnDate.setDayOfMonth(Integer.parseInt(dataList[i++]));
		returnDate.setMonthOfYear(Integer.parseInt(dataList[i++]));
		returnDate.setYear(Integer.parseInt(dataList[i++]));
	}
	
	// M�todo para checar se um empr�stimo est� atrasado,
	// com rela��o a uma determinada data (d/m/y)
	public boolean isOverdue() {
		// Para comparar somente datas, sem horas, minutos etc.:
		DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();
		int d = SystemManager.getDay();
		int m = SystemManager.getMonth();
		int y = SystemManager.getYear();
		
		DateTime today = new DateTime(y, m, d, 0, 0);
		if (cmp.compare(today, returnDate) > 0) return true;
		else return false;
	}
	
	// Getters e Setters: p�blicos e utilizados somente 
	// por SystemManager, respectivamente.
	void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	void setBookCode(int code) {
		this.bookCode = code;
	}
	public int getBookCode() {
		return bookCode;
	}
	void setUserCode(int code) {
		this.userCode = code;
	}
	public int getUserCode() {
		return userCode;
	}
	void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserType() {
		return userType;
	}
	void setRentalDate(int d, int m, int y) {
		rentalDate.setDate(y, m, d);
	}
	MutableDateTime getRentalDate() {
		return rentalDate.copy();
	}
	public int getRentalDay() {
		return rentalDate.getDayOfMonth();
	}
	public int getRentalMonth() {
		return rentalDate.getMonthOfYear();
	}
	public int getRentalYear() {
		return rentalDate.getYear();
	}
	void setReturnDate(int d, int m, int y) {
		returnDate.setDate(y, m, d);
	}
	MutableDateTime getReturnDate() {
		return returnDate.copy();
	}
	public int getReturnDay() {
		return returnDate.getDayOfMonth();
	}
	public int getReturnMonth() {
		return returnDate.getMonthOfYear();
	}
	public int getReturnYear() {
		return returnDate.getYear();
	}
	
	// M�todos para facilitar a apresenta��o dos dados
	// na tabela da GUI:
	public String getRentalDateAsString() {
		String date = rentalDate.getDayOfMonth() + "/" +
				rentalDate.getMonthOfYear() + "/" +
				rentalDate.getYear();
		return date;
	}
	public String getReturnDateAsString() {
		String date = returnDate.getDayOfMonth() + "/" +
				returnDate.getMonthOfYear() + "/" +
				returnDate.getYear();
		return date;
	}
	public String getStatusAsString() {
		if (this.isOverdue()) return "OVERDUE!";
		else return "normal";
	}
}
