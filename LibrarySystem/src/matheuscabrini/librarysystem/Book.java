package matheuscabrini.librarysystem;

import java.util.ArrayList;

//Representação de um livro
public class Book extends Record {	
	public static final String TYPE_TEXTBOOK = "textbook";
	public static final String TYPE_BOOK = "book";
	public static final String STATUS_AVAILABLE = "available";
	public static final String STATUS_RENTED = "rented";
	
	// Campos presentes nos arquivos:
	private String title = "";
	private String author = "";
	private String type = ""; // "textbook" ou "book"
	private String publisher = "";
	private String language = "";
	private String ISBN = "";
	private int pages = 0;
	private int year = 0;
	private String status = STATUS_AVAILABLE; // "available" ou "rented"
	
	@Override
	String[] getData() {
		ArrayList<String> dataList = new ArrayList<>();
		
		dataList.add(""+code);
		dataList.add(title);
		dataList.add(author);
		dataList.add(type);
		dataList.add(publisher);
		dataList.add(language);
		dataList.add(ISBN);
		dataList.add((pages == 0) ? "" : ""+pages);
		dataList.add((year == 0) ? "" : ""+year);
		dataList.add(status);
		
		String[] retDataList = dataList.toArray(new String[dataList.size()]);
		return retDataList;
	}

	@Override
	void setData(String[] dataList) {
		int i = 0;
		code = Integer.parseInt(dataList[i++]);
		title = dataList[i++];
		author = dataList[i++];
		type = dataList[i++];
		publisher = dataList[i++];
		language = dataList[i++];
		ISBN = dataList[i++];
		pages = (dataList[i].isEmpty()) ? 0 : Integer.parseInt(dataList[i]); i++;
		year = (dataList[i].isEmpty()) ? 0 : Integer.parseInt(dataList[i]); i++;
		status = dataList[i];
	}
	
	// Diz se o book está emprestado ou não
	public boolean isAvailable() {
		return (status.equals(STATUS_AVAILABLE)) ? true : false;
	}
	
	// Setters e getters:
	
	public int getCode() {
		return code;
	}
	void setCode(int code) { // utilizado somente por SystemManager
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
