package matheuscabrini;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import matheuscabrini.librarysystem.Book;
import matheuscabrini.librarysystem.Rental;
import matheuscabrini.librarysystem.SystemManager;
import matheuscabrini.librarysystem.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Main extends Application {

	SystemManager sysMan; // realiza operações nos dados do sistema
    ResourceBundle resBun; // para i18n
    Stage primaryStage; // Stage principal do programa
    MenuBar menuBar; // menu do canto esquerdo superior, com File, Edit...
    TableView tableB = new TableView(); // tabela de books
    TableView tableU = new TableView(); // tabela de users
    TableView tableR = new TableView(); // tabela de rentals
    Tab tabB; // aba com tabela de books
    Tab tabU; // aba com tabela de users
    Tab tabR; // aba com tabela de rentals
    InputStream iconStream; // stream do icone do programa
    String resBunPath; // caminho do arquivo de idioma default
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Aqui, nesta tela inicial, é requisitada do usuário
	// seu idioma desejado para o programa
	@Override
	public void start(Stage primaryStage) {	
		this.primaryStage = primaryStage;

		iconStream = this.getClass().getClassLoader().
				getResourceAsStream("resources/icon.png");
		resBunPath = "resources/MsgBundle";
		
        Stage langStage = new Stage();
        langStage.setTitle("Library System");

		try {
			sysMan = new SystemManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Label msg = new Label("Select a language:");
        
        ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("English");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("Português");
        rb2.setToggleGroup(group);
        
        // Com este botão, é setado o idioma escolhido
        // e parte-se para a tela principal
        Button bStart = new Button("Start");
        bStart.setOnAction( (ev) -> {
        	Locale currentLocale;
        	if (rb1.isSelected()) currentLocale = new Locale("en", "us");
        	else currentLocale = new Locale("pt", "br");
        	
            resBun = ResourceBundle.getBundle(resBunPath, currentLocale);
            
            langStage.hide();
            initProgram();	
        });
        
        VBox vbox = new VBox(30, msg, rb1, rb2, bStart);
        vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
        langStage.setScene(new Scene(vbox));
        langStage.show();
	}
	
	// Aqui são iniciados os elementos da tela principal
	void initProgram() {
        primaryStage.getIcons().add(new Image(iconStream));
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        updateStageTitle();
        Scene scene = new Scene(new Group());
        
        initMenus(); // Inicializando menus File, Edit, Actions
        
        // Inicializando tabelas e seus containers (tabs\abas)
        
        initBookTable();
		tabB = new Tab(resBun.getString("tab.b"));
        tabB.setContent(tableB);
        tabB.setClosable(false);
        
        initUserTable();
		tabU = new Tab(resBun.getString("tab.u"));
        tabU.setContent(tableU);
        tabU.setClosable(false);
        
        initRentalTable();
		tabR = new Tab(resBun.getString("tab.r"));
        tabR.setContent(tableR);
        tabR.setClosable(false);
        
        TabPane tabPane = new TabPane(tabB, tabU, tabR);
        tabPane.setMinWidth(1200);
        
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(menuBar, tabPane);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	// Atualização do titulo da tela principal, quando
	// uma nova data é setada
	void updateStageTitle() {
        primaryStage.setTitle(resBun.getString("stage.title") + " - " + 
        	SystemManager.getDay() + "/" + SystemManager.getMonth() + 
        	"/" + SystemManager.getYear());
	}
	
	// Aqui inicializa-se o menu do canto esquero-superior da tela principal
	// que possibilita as as diferentes operações do usuário sobre o sistema 
	void initMenus() {
        Menu menuFile = new Menu(resBun.getString("menu.file"));
        Menu menuEdit = new Menu(resBun.getString("menu.edit"));    
        Menu menuActions = new Menu(resBun.getString("menu.actions"));
        
		Menu menuFileSave = new Menu(resBun.getString("menu.file.save"));
        menuFileSave.setOnAction(ev -> {
        	saveChanges();
        	menuFile.hide();
        });
        Menu menuFileExit = new Menu(resBun.getString("menu.file.exit"));
        menuFileExit.setOnAction(ev -> {
        	exitProgram();
        });
        
        Menu menuEditDate = new Menu(resBun.getString("menu.edit.date"));
        menuEditDate.setOnAction(ev -> {
        	changeSysDate();
        });
        	
        Menu menuAddBook = new Menu(resBun.getString("menu.addb"));
        menuAddBook.setOnAction(ev -> {
        	createBook();
        });
        Menu menuAddUser = new Menu(resBun.getString("menu.addu"));
        menuAddUser.setOnAction(ev -> {
        	createUser();
        });
        Menu menuAddRental = new Menu(resBun.getString("menu.addr"));
        menuAddRental.setOnAction(ev -> {
        	createRental();
        });
        Menu menuReturnBook = new Menu(resBun.getString("menu.returnb"));
        menuReturnBook.setOnAction(ev -> {
        	returnBook();
        });
        
        menuFile.getItems().addAll(menuFileSave, menuFileExit);
        menuEdit.getItems().addAll(menuEditDate);    
        menuActions.getItems().addAll(menuAddBook, menuAddUser, menuAddRental, menuReturnBook);

        menuBar = new MenuBar(menuFile, menuEdit, menuActions);
	}
	
	// Inicialização (e atualização, quando necessário) das tabelas
	
	void initBookTable() {
		tableB.setPlaceholder(new Label(resBun.getString("no.elements")));
		
		// Os procedimentos abaixo tornarão possível que a tabela
		// obtenha os dados de cada livro na lista do sistema 
		// por meio de seus getters.
		
        TableColumn codeCol = new TableColumn(resBun.getString("code"));
        codeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("code")
        );
        
        TableColumn titleCol = new TableColumn(resBun.getString("title"));
        titleCol.setCellValueFactory(   
        		new PropertyValueFactory<>("title")
        );
        
        TableColumn authorCol = new TableColumn(resBun.getString("author"));
        authorCol.setCellValueFactory(   
        		new PropertyValueFactory<>("author")
        );
        
        TableColumn typeCol = new TableColumn(resBun.getString("type"));
        typeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("type")
        );
        
        TableColumn pubCol = new TableColumn(resBun.getString("publisher"));
        pubCol.setCellValueFactory(   
        		new PropertyValueFactory<>("publisher")
        );
        
        TableColumn langCol = new TableColumn(resBun.getString("lang"));        
        langCol.setCellValueFactory(   
        		new PropertyValueFactory<>("language")
        );
        
        TableColumn isbnCol = new TableColumn("ISBN");
        isbnCol.setCellValueFactory(   
        		new PropertyValueFactory<>("ISBN")
        );
        
        TableColumn pagesCol = new TableColumn(resBun.getString("pages"));
        pagesCol.setCellValueFactory(   
        		new PropertyValueFactory<>("pages")
        );
        
        TableColumn yearCol = new TableColumn(resBun.getString("year"));
        yearCol.setCellValueFactory(   
        		new PropertyValueFactory<>("year")
        );
        
        TableColumn statusCol = new TableColumn(resBun.getString("status"));
        statusCol.setCellValueFactory(   
        		new PropertyValueFactory<>("status")
        );
        
        // Associando a lista de livros à tabela:
		ObservableList<Book> bList = FXCollections.observableArrayList(sysMan.getBooks());
        tableB.setItems(bList);
        tableB.setEditable(false);
        if (tableB.getColumns().isEmpty())
        	tableB.getColumns().addAll(codeCol, titleCol, authorCol, typeCol, pubCol, langCol,
        		isbnCol, pagesCol, yearCol, statusCol);
        ((TableColumn) tableB.getColumns().get(0)).setVisible(false); // update na tabela
        ((TableColumn) tableB.getColumns().get(0)).setVisible(true);
	}

	void initUserTable() {
		tableU.setPlaceholder(new Label(resBun.getString("no.elements")));
		
		TableColumn codeCol = new TableColumn(resBun.getString("code"));
        codeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("code")
        );
        
        TableColumn nameCol = new TableColumn(resBun.getString("name"));
        nameCol.setCellValueFactory(   
        		new PropertyValueFactory<>("name")
        );
        
        TableColumn uniIDCol = new TableColumn(resBun.getString("uni.id"));
        uniIDCol.setCellValueFactory(   
        		new PropertyValueFactory<>("uniID")
        );
        
        TableColumn IDCol = new TableColumn(resBun.getString("id"));
        IDCol.setCellValueFactory(   
        		new PropertyValueFactory<>("ID")
        );
        
        TableColumn addressCol = new TableColumn(resBun.getString("address"));
        addressCol.setCellValueFactory(   
        		new PropertyValueFactory<>("address")
        );
        
        TableColumn emailCol = new TableColumn("Email");        
        emailCol.setCellValueFactory(   
        		new PropertyValueFactory<>("email")
        );
        
        TableColumn typeCol = new TableColumn(resBun.getString("type"));
        typeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("type")
        );
        
        TableColumn rentCountCol = new TableColumn(resBun.getString("rental.count"));
        rentCountCol.setCellValueFactory(   
        		new PropertyValueFactory<>("rentalCount")
        );
        
        TableColumn suspEndDateCol = new TableColumn(resBun.getString("susp.end.date"));
        suspEndDateCol.setCellValueFactory(   
        		new PropertyValueFactory<>("suspensionEndDateAsString")
        );
        
        TableColumn suspStartDateCol = new TableColumn(resBun.getString("susp.start.date"));
        suspStartDateCol.setCellValueFactory(   
        		new PropertyValueFactory<>("suspensionStartDateAsString")
        );
        
        ObservableList<User> uList = FXCollections.observableArrayList(sysMan.getUsers());
        tableU.setItems(uList);
        tableU.setEditable(false);
        if (tableU.getColumns().isEmpty())
        	tableU.getColumns().addAll(codeCol, nameCol, uniIDCol, IDCol, addressCol, emailCol,
        		typeCol, rentCountCol, suspEndDateCol, suspStartDateCol);
        ((TableColumn) tableU.getColumns().get(0)).setVisible(false); // update
        ((TableColumn) tableU.getColumns().get(0)).setVisible(true);
	}
	
	void initRentalTable() {
		tableR.setPlaceholder(new Label(resBun.getString("no.elements")));

		TableColumn codeCol = new TableColumn(resBun.getString("code"));
        codeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("code")
        );
        
        TableColumn bCodeCol = new TableColumn(resBun.getString("b.code"));
        bCodeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("bookCode")
        );
        
        TableColumn uCodeCol = new TableColumn(resBun.getString("u.code"));
        uCodeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("userCode")
        );
        
        TableColumn typeCol = new TableColumn(resBun.getString("u.type"));
        typeCol.setCellValueFactory(   
        		new PropertyValueFactory<>("userType")
        );
        
        TableColumn rentDateCol = new TableColumn(resBun.getString("rent.date"));
        rentDateCol.setCellValueFactory(   
        		new PropertyValueFactory<>("rentalDateAsString")
        );
        
        TableColumn returnDateCol = new TableColumn(resBun.getString("return.date"));        
        returnDateCol.setCellValueFactory(   
        		new PropertyValueFactory<>("returnDateAsString")
        );
        
        TableColumn statusCol = new TableColumn(resBun.getString("status"));        
        statusCol.setCellValueFactory(   
        		new PropertyValueFactory<>("statusAsString")
        );
        
        ObservableList<Rental> rList = FXCollections.observableArrayList(sysMan.getRentals());
        tableR.setItems(rList);
        tableR.setEditable(false);
        if (tableR.getColumns().isEmpty())
        	tableR.getColumns().addAll(codeCol, bCodeCol, uCodeCol, 
        			typeCol, rentDateCol, returnDateCol, statusCol);
        ((TableColumn) tableR.getColumns().get(0)).setVisible(false); // update
        ((TableColumn) tableR.getColumns().get(0)).setVisible(true);
	}
	
	// Este método cria uma tela nova, na qual podem ser digitados
	// os diferentes campos possíveis para o registro de um livro
	// Pode-se omitir os dados de quaisquer campos
	void createBook() {
		Stage bookStage = new Stage();
		bookStage.setTitle(resBun.getString("menu.addb"));
		
		Book book = new Book();
		
		TextField title = new TextField();
		title.setPromptText(resBun.getString("title"));
		TextField author = new TextField();
		author.setPromptText(resBun.getString("author"));
		TextField publisher = new TextField();
		publisher.setPromptText(resBun.getString("publisher"));
		TextField lang = new TextField();
		lang.setPromptText(resBun.getString("lang"));
		TextField ISBN = new TextField();
		ISBN.setPromptText("ISBN");
		TextField pages = new TextField();
		pages.setPromptText(resBun.getString("pages"));
		TextField year = new TextField();
		year.setPromptText(resBun.getString("year"));

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton bookButton = new RadioButton(resBun.getString("type.book"));
        bookButton.setToggleGroup(typeGroup);
        bookButton.setSelected(true);
        RadioButton txtBookButton = new RadioButton(resBun.getString("type.textbook"));
        txtBookButton.setToggleGroup(typeGroup);
		
        Button bOK = new Button(resBun.getString("ok"));
        bOK.setOnAction( (ev) -> {
        	book.setTitle(title.getText());
        	book.setAuthor(author.getText());
        	book.setPublisher(publisher.getText());
        	book.setLanguage(lang.getText());
        	book.setISBN(ISBN.getText());
        	book.setPages(Integer.parseInt(pages.getText()));
        	book.setYear(Integer.parseInt(year.getText()));  
        	if (txtBookButton.isSelected()) book.setType(Book.TYPE_TEXTBOOK);
        	else book.setType(Book.TYPE_BOOK);
        	
        	// Adicionamos o livro no sistema,
        	// atualizamos a tabela, e fechamos esta janela
        	sysMan.addBook(book);
        	initBookTable();
        	bookStage.hide();
        });
        
		VBox vbox = new VBox(5, title, author, publisher, lang, ISBN, 
				pages, year, bookButton, txtBookButton, bOK);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		
		bookStage.setScene(new Scene(vbox));
		bookStage.show();
		vbox.requestFocus();
	}
	
	// Nova janela para entrada dos dados do novo usuário
	void createUser() {
		Stage userStage = new Stage();
		userStage.setTitle(resBun.getString("menu.addu"));
		
		User user = new User();
		
		TextField name = new TextField();
		name.setPromptText(resBun.getString("name"));
		TextField uniID = new TextField();
		uniID.setPromptText(resBun.getString("uni.id")+" *");
		TextField ID = new TextField();
		ID.setPromptText(resBun.getString("id")+" *");
		TextField address = new TextField();
		address.setPromptText(resBun.getString("address")+" *");
		TextField email = new TextField();
		email.setPromptText(resBun.getString("email"));

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton studButton = new RadioButton(resBun.getString("type.stud"));
        studButton.setToggleGroup(typeGroup);
        studButton.setSelected(true);
        RadioButton profButton = new RadioButton(resBun.getString("type.prof"));
        profButton.setToggleGroup(typeGroup);
        RadioButton commButton = new RadioButton(resBun.getString("type.comm"));
        commButton.setToggleGroup(typeGroup);
        
        // Mensagem de erro caso não seja digitado os campos obrigatorios
        Label errorMsg = new Label(resBun.getString("user.errormsg")); 
        errorMsg.setVisible(false);
        
        Button bOK = new Button(resBun.getString("ok"));
        bOK.setOnAction( (ev) -> {
        	if (uniID.getText().isEmpty() &&
        	   (address.getText().isEmpty() || ID.getText().isEmpty())) {
        		errorMsg.setVisible(true);
        		return;
        	}
        	
        	user.setName(name.getText());
        	user.setAddress(address.getText());
        	user.setEmail(email.getText());
        	if (!uniID.getText().isEmpty()) user.setUniID(Integer.parseInt(uniID.getText()));
        	if (!ID.getText().isEmpty()) user.setID(Integer.parseInt(ID.getText()));  
        	
        	if (studButton.isSelected()) user.setType(User.TYPE_STUDENT);
        	else if (profButton.isSelected()) user.setType(User.TYPE_PROFESSOR);
        	else user.setType(User.TYPE_COMMUNITY);
        	
    		// Adicionando user no sistema; 
        	// atualizando tabela; fechando janela
        	sysMan.addUser(user);
        	initUserTable();
        	userStage.hide();
        });
        
		VBox vbox = new VBox(5, name, uniID, ID, address, email, 
				studButton, profButton, commButton, errorMsg, bOK);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		
		userStage.setScene(new Scene(vbox));
		userStage.show();
		vbox.requestFocus();
	} 
	
	// Nova janela para digitação dos códigos de usuário e de livro,
	// que formam um novo empréstimo
	void createRental() {
		Stage rentalStage = new Stage();
		rentalStage.setTitle(resBun.getString("menu.addr"));
		
		TextField bCode = new TextField(); // campo obrigatorio
		bCode.setPromptText(resBun.getString("b.code")+" *");
		TextField uCode = new TextField(); // campo obrigatorio
		uCode.setPromptText(resBun.getString("u.code")+" *");
        
        // Mensagem de erro caso não seja digitado os campos obrigatorios
		// ou caso os códigos formem um emprestimo invalido
        Label errorMsg = new Label(); 
        errorMsg.setVisible(false);
        
        Button bOK = new Button(resBun.getString("ok"));
        bOK.setOnAction( (ev) -> {
        	if (bCode.getText().isEmpty() || uCode.getText().isEmpty()) {
        		errorMsg.setText(resBun.getString("rental.error.empty"));
        		errorMsg.setVisible(true);
        		return;
        	}
        	
        	// Verificando validade do empréstimo
        	Rental rental;
        	try {
        		rental = sysMan.newRental(sysMan.getBookByCode(Integer.parseInt(bCode.getText()), true), 
    				 sysMan.getUserByCode(Integer.parseInt(uCode.getText())));
        	} catch(Exception e) {
        		e.printStackTrace();
        		errorMsg.setText(resBun.getString("rental.error.invalid"));
        		errorMsg.setVisible(true);
        		return;
        	}
    		if (rental == null) {
        		errorMsg.setText(resBun.getString("rental.error.invalid"));
        		errorMsg.setVisible(true);
        		return;
        	}
        	
    		// Atualizando tabelas, fechando janela
    		initBookTable();
    		initUserTable();
        	initRentalTable();
        	rentalStage.hide();
        });
        
		VBox vbox = new VBox(5, bCode, uCode, errorMsg, bOK);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		
		rentalStage.setScene(new Scene(vbox));
		rentalStage.show();
		vbox.requestFocus();
	}
	
	// Nova janela para digitar-se código do livro o qual
	// será devolvido a biblioteca
	void returnBook() {
		Stage retBookStage = new Stage();
		retBookStage.setTitle(resBun.getString("menu.returnb"));
		
		TextField bCode = new TextField();
		bCode.setPromptText(resBun.getString("b.code")+" *");
        
        // Mensagem de erro caso o codigo seja invalido (livro não emprestado, etc.)
        Label errorMsg = new Label(resBun.getString("code.invalid")); 
        errorMsg.setVisible(false);
        
        Button bOK = new Button(resBun.getString("ok"));
        bOK.setOnAction( (ev) -> {
        	if (bCode.getText().isEmpty()) {
        		errorMsg.setVisible(true);
        		return;
        	}
        	
        	// verificando validade da devolução
        	try {
        		sysMan.returnBook(sysMan.getBookByCode(
        				Integer.parseInt(bCode.getText()), false));
        	} catch (Exception e) {
        		e.printStackTrace();
        		errorMsg.setVisible(true);
        		return;
        	}
        	
    		initBookTable();
    		initUserTable();
        	initRentalTable();
        	retBookStage.hide();
        });
        
		VBox vbox = new VBox(5, bCode, errorMsg, bOK);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		
		retBookStage.setScene(new Scene(vbox));
		retBookStage.show();
		vbox.requestFocus();
	}
	
	// Nova janela em que possibilita-se a escolha de nova
	// data no sistema, a partir de um calendário interativo (date picker) 
	void changeSysDate() {
		Stage dateStage = new Stage();
		dateStage.setTitle(resBun.getString("menu.edit.date"));
		
		DatePicker picker = new DatePicker();
        
        Button bOK = new Button(resBun.getString("ok"));
        bOK.setOnAction( (ev) -> {
        	if (picker.getValue() == null) return;
        	LocalDate date = picker.getValue();
        	sysMan.setDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        	
        	initBookTable();
        	initUserTable();
        	initRentalTable();
        	dateStage.hide();
    		updateStageTitle();
        });
        
		VBox vbox = new VBox(5, picker, bOK);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		
		dateStage.setScene(new Scene(vbox));
		dateStage.show();
		vbox.requestFocus();
	}
    
    void saveChanges() {
		try {
			sysMan.saveChangesToFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    void exitProgram() {
    	saveChanges();
    	primaryStage.hide();
    }
    
	// Este método é chamado pela Application quando o usuário
	// sai do programa pelo X no canto superior.
	@Override
	public void stop() {
		saveChanges();
	}
}
