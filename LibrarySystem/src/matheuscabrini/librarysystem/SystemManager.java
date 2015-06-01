package matheuscabrini.librarysystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.joda.time.DateTimeComparator;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

public class SystemManager {
	
	// Caminhos para os arquivos:
    private static URL bookFileName;
    private static URL userFileName;
    private static URL rentalFileName;
    private static URL counterFileName;
    
    // Definições do sistema para a quantia máxima de dias em empréstimo 
    // e de livros emprestados possíveis para cada tipo de usuário
	public static final int MAX_STUDENT_DAYS = 15;
	public static final int MAX_PROFESSOR_DAYS = 60;
	public static final int MAX_COMMUNITY_DAYS = 15;
	public static final int MAX_STUDENT_BOOKS = 4;
	public static final int MAX_PROFESSOR_BOOKS = 6;
	public static final int MAX_COMMUNITY_BOOKS = 2;
	
	// Quantias de items registrados no sistema:
	private int nOfBooks = 0;
	private int nOfUsers = 0;
	private int nOfRentals = 0;
	
	// Listas de itens registrados no sistema:
	private ArrayList<Book> bookList;
	private ArrayList<User> userList;
	private ArrayList<Rental> rentalList;
	
	static MutableDateTime sysDate; // data atual do sistema
	
	/*
	 * 
	 * ***** MÉTODOS PÚBLICOS ********
	 * 
	 */
	
	public SystemManager() throws IOException {
		sysDate = new MutableDateTime();
		bookList = new ArrayList<Book>();
		userList = new ArrayList<User>();
		rentalList = new ArrayList<Rental>();
		bookFileName = this.getClass().getResource("/resources/bookData.csv");
	    userFileName = this.getClass().getResource("/resources/userData.csv");
	    rentalFileName = this.getClass().getResource("/resources/rentalData.csv");
	    counterFileName = this.getClass().getResource("/resources/recordCounter.txt");
		
		// Se o arquivo com as quantidades de registros existir, tais valores 
		// são trazidos ao programa. Também é esperado que exista os arquivos de
		// registros, cujos dados logo serão copiados ao programa.	
		BufferedReader br = new BufferedReader(
				new FileReader(counterFileName.getPath()));
	
		nOfBooks = Integer.parseInt(br.readLine());
		nOfUsers = Integer.parseInt(br.readLine());
		nOfRentals = Integer.parseInt(br.readLine());
		br.close();
		
		bookList.ensureCapacity(nOfBooks);
		for (int i = 0; i < nOfBooks; i++) 
			bookList.add(new Book());
		getRecordsFromFile(bookFileName, bookList);
		
		userList.ensureCapacity(nOfUsers);
		for (int i = 0; i < nOfUsers; i++) 
			userList.add(new User());
		getRecordsFromFile(userFileName, userList);

		rentalList.ensureCapacity(nOfRentals);
		for (int i = 0; i < nOfRentals; i++) 
			rentalList.add(new Rental());
		getRecordsFromFile(rentalFileName, rentalList);
		
		updateSystem();
	}

	// Métodos para obter lista de registros contidos no programa
	public ArrayList<Book> getBooks() {
		return bookList;
	}
	public ArrayList<User> getUsers() {
		return userList;
	}
	public ArrayList<Rental> getRentals() {
		return rentalList;
	}
	
	// Métodos para obtenção de livros, usuarios ou empréstimos, por algum campo: 
	
	// Aqui, a flag serve para dizer se deve ser retornado 
	// necessariamente um livro que esteja disponível para empréstimo
	public Book getBookByCode(int code, boolean needAvailable) throws IllegalArgumentException {
		for (Book b : bookList) {
			if (b.getCode() == code) {
				if (needAvailable) {
					if (b.isAvailable()) return b; 
				} 
				else return b;
			}
		}
		throw new IllegalArgumentException("Book not found");
	}
	public Book getBookByTitle(String title, boolean needAvailable) throws IllegalArgumentException {
		for (Book b : bookList) {
			if (b.getTitle().equals(title)) {
				if (needAvailable) {
					if (b.isAvailable()) return b; 
				} 
				else return b;
			}
		}
		throw new IllegalArgumentException("Book not found");
	}
	public Book getBookByISBN(String ISBN, boolean needAvailable) throws IllegalArgumentException {
		for (Book b : bookList) {
			if (b.getISBN().equals(ISBN)) {
				if (needAvailable) {
					if (b.isAvailable()) return b; 
				} 
				else return b;
			}
		}
		throw new IllegalArgumentException("Book not found");
	}
	public User getUserByCode(int code) throws IllegalArgumentException {
		for (User u : userList) {
			if (u.getCode() == code) return u;
		}
		throw new IllegalArgumentException("User not found");
	}
	public User getUserByUniID(int uniID) throws IllegalArgumentException {
		for (User u : userList) {
			if (u.getUniID() == u.getUniID()) return u;
		}
		throw new IllegalArgumentException("User not found");
	}
	public User getUserByID(int ID) throws IllegalArgumentException {
		for (User u : userList) {
			if (u.getID() == ID) return u;
		}
		throw new IllegalArgumentException("User not found");
	}
	public Rental getRentalByBookCode(int bCode) {
		for (Rental r : rentalList) {
			if (r.getBookCode() == bCode) return r;
		}
		throw new IllegalArgumentException("Rental not found");
	}
	
	// Métodos relacionados ao tempo do sistema:
	public void setDate(int d, int m, int y) {
		sysDate.setDate(y, m, d);
		updateSystem();
	}
	public static int getDay() {
		return sysDate.getDayOfMonth();
	}
	public static int getMonth() {
		return sysDate.getMonthOfYear();
	}
	public static int getYear() {
		return sysDate.getYear();
	}
	
	// Métodos para adicionar, no programa, um registro.
	public boolean addBook(Book b) {
		if (b == null) return false;
		b.setCode(nOfBooks);
		bookList.add(b);
		nOfBooks++;
		return true;
	}
	public boolean addUser(User u) {
		if (u == null) return false;
		for (User existingUser : userList) { // Checando se user já existe
			if (u.getType().equals(User.TYPE_COMMUNITY)) {
				if (existingUser.getID() == u.getID()) 
					return false;
			}
			else if (existingUser.getUniID() == u.getUniID())
				return false;
		}
		u.setCode(nOfUsers);
		userList.add(u);
		nOfUsers++;
		return true;
	}
	public Rental newRental(Book bookToBeRent, User rentingUser) {
		Rental r = validateRental(bookToBeRent, rentingUser);
		if (r == null) return null;
		
		// Como o empréstimo foi validado, atualizamos campos 
		// necessários em user e book e completamos os do rental:
		
		rentingUser.incrementRentalCount();
		bookToBeRent.setStatus(Book.STATUS_RENTED);
		
		r.setCode(nOfRentals);
		r.setBookCode(bookToBeRent.getCode());
		r.setUserCode(rentingUser.getCode());
		r.setUserType(rentingUser.getType());
		
		// rental date = sysdate; return date = sysdate + max dias do user
		MutableDateTime retDate = sysDate.copy();
		retDate.addDays(rentingUser.getMaxRentalDays()); 
		r.setReturnDate(retDate.getDayOfMonth(), retDate.getMonthOfYear(), retDate.getYear());
		r.setRentalDate(sysDate.getDayOfMonth(), sysDate.getMonthOfYear(), sysDate.getYear());
		
		rentalList.add(r);
		nOfRentals++;
		return r;
	}
	
	// Para registrar devoluções de livros:
	public void returnBook(Book b) throws IllegalArgumentException {
		if (b == null) throw new IllegalArgumentException("Book not found");
		
		// Removendo o empréstimo da lista, se ele existir:
		Rental rental = null;
		for (int i = 0; i < rentalList.size(); i++) {
			if (rentalList.get(i).getBookCode() == b.getCode()) {
				rental = rentalList.remove(i);
				nOfRentals--;
				break; 
			}
		}
		if (rental == null) throw new IllegalArgumentException("Rental not found");
		
		// Alterando os campos relacionados ao empréstimo em book e user:
		b.setStatus(Book.STATUS_AVAILABLE);
		User u = getUserByCode(rental.getUserCode());
		u.decrementRentalCount();
		
		// Se houve atraso na devolução: suspensionEndDate := sysDate + (delay+1)dias + 1dia
		if (rental.isOverdue()) {
			MutableDateTime suspEndDate = sysDate.copy();
			Days delay = Days.daysBetween(sysDate, rental.getReturnDate()).plus(1); 
			suspEndDate.addDays(Math.abs(delay.getDays() + 1));
			
			u.setSuspended(true);
			u.setSuspensionEndDate(suspEndDate.getDayOfMonth(), 
				suspEndDate.getMonthOfYear(), suspEndDate.getYear());
		}
	}	
	
	// Esta função atualiza, nos respectivos arquivos, os dados 
	// contidos no programa sobre livros, usuários, empréstimos
	// e suas respectivas quantidades.
	public void saveChangesToFiles() throws IOException {
		writeRecordsToFile(bookFileName, bookList);
		writeRecordsToFile(userFileName, userList);
		writeRecordsToFile(rentalFileName, rentalList);
		
		PrintWriter pw = new PrintWriter(
				new FileWriter(counterFileName.getPath()));
		pw.println(nOfBooks);
		pw.println(nOfUsers);
		pw.println(nOfRentals);
		pw.close();	
	}
	
	/*
	 * 
	 * ***** MÉTODOS PRIVADOS ******** 
	 * 
	 */
	
	// Passa os dados do programa os arquivo
	private void writeRecordsToFile(URL fileName, ArrayList<? extends Record> list) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(fileName.getPath(), false),
			',', CSVWriter.NO_QUOTE_CHARACTER, 
			CSVWriter.NO_ESCAPE_CHARACTER, 
			System.getProperty("line.separator"));

		// Montando o vetor de dados sobre o registro, e
		// escrevendo-o no arquivo:
		for (Record rec : list) {
			writer.writeNext(rec.getData());
		}			
		writer.close();
	}
	
	// Passa os dados do arquivo ao programa
	private void getRecordsFromFile(URL bookFileName, ArrayList<? extends Record> list) {
		CSVReader reader;
		String[] data;
		try {
			reader = new CSVReader(new FileReader(bookFileName.getPath()), ',', 
				CSVWriter.NO_QUOTE_CHARACTER, 
				CSVWriter.NO_ESCAPE_CHARACTER);
			for (Record rec : list) {
				data = reader.readNext();
				rec.setData(data);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Se o par livro/usuário, dados por seus códigos, for
	// válido, este método retorna um Rental só construido.
	// Campos e outros procedimentos são completados em newRental()
	private Rental validateRental(Book bookToBeRent, User rentingUser) {
		Rental rental = new Rental();
		
		// User deve existir, não estar suspenso, nem
		// estourar a cota máxima de empréstimos
		if (rentingUser == null || 
			rentingUser.isSuspended() ||
			rentingUser.exceedsRentalsLimit())
			return null;
		
		// Livro deve existir e estar disponível, e 
		// seu tipo deve concordar com o tipo do user
		if (bookToBeRent == null || 
			!bookToBeRent.isAvailable() ||
		    (rentingUser.getType().equals(User.TYPE_COMMUNITY) &&
			bookToBeRent.getType().equals(Book.TYPE_TEXTBOOK))) {
			return null;
		}
			
		return rental;
	}
	
	// Este método, com base numa nova data de sistema,
	// atualiza o status dos empréstimos, de users e de suspensões.
	void updateSystem() {
		// Para comparar somente datas, sem horas, minutos etc.:
		DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();

		// Suspendemos usuários com algum empréstimo em atraso
		// Também checamos se ocorreu o fato do programa ter voltado no tempo antes 
		// de algum empréstimo ter sido feito; caso sim, é registrada sua "devolução".
		for (int i = 0; i < rentalList.size(); i++) {
			Rental r = rentalList.get(i);
			
			if (r.isOverdue()) {
				User delayedUser = getUserByCode(r.getUserCode());
				delayedUser.setSuspended(true);
				
				if (delayedUser.getSuspensionStartDate() == null) {
					MutableDateTime suspStart = r.getReturnDate().copy();
					suspStart.addDays(1);
					delayedUser.setSuspensionStartDate(suspStart.getDayOfMonth(), 
							suspStart.getMonthOfYear(), suspStart.getYear());	
				}
			}
			else if (cmp.compare(sysDate, r.getRentalDate()) < 0) {
				returnBook(getBookByCode(r.getBookCode(), false)); 
				i--; // remoção em rentalList shifta elementos para a esquerda
			}
		}
		
		// Devemos checar se o período de suspensão de algum dos 
		// usuários suspensos expirou; e também se o sistema voltou
		// ao período de tempo antes do user ficar inicialmente suspenso.
		for (User u : userList) {
			if (u.isSuspended() && u.getSuspensionEndDate() != null) {
				if (cmp.compare(sysDate, u.getSuspensionEndDate()) >= 0) 
					u.setSuspended(false);	
			}
			if (u.isSuspended() && u.getSuspensionStartDate() != null) {
				if (cmp.compare(sysDate, u.getSuspensionStartDate()) < 0) 
					u.setSuspended(false);	
			}
		}
	}
}
