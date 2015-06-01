	Informa��es de uso:
	
	Antes do inicio do programa, pode-se escolher, no momento, entre os idiomas: ingl�s (default) e portugu�s.
	A interface principal � dividida em: t�tulo, o qual mostra a data do sistema; menu de op��es (File, Edit, Actions); e tr�s abas relativas a livros, usu�rios e empr�stimos, contendo suas respectivas tabelas (listas) de registros. 
	Nas tabelas, � poss�vel mudar a ordem das colunas, arrastando-as com o mouse; al�m de ordenar os registros por determinada coluna, clicando no t�tulo dela.
	Alguns registros j� est�o inseridos nos arquivos. Para adicionar outros, dirija-se ao menu Actions.
	Para registrar um usu�rio, pode-se escolher entre colocar uma identifica��o da universidade (algo como o numero USP) OU o n�mero de RG e endere�o de moradia. Isso serve para facilitar o cadastro de estudantes e professores; por�m permitindo que qualquer tipo de usu�rio possa ser    eventualmente cobrado caso n�o devolva seus empr�stimos.
	Para registrar empr�stimo, digita-se os c�digos do livro e do usu�rio (dispon�veis nas suas respectivas tabelas).
	Usu�rios que n�o devolverem seus livros at� a data m�xima de retorno (inclusive) ser�o suspensos do sistema por tempo indeterminado. Quando finalmente retornarem o livro, ainda ficar�o suspensos pela quantidade de dias em atraso.
	Para salvar os dados ou sair do programa (que salva automaticamente), clique no menu File.
	O programa � inicializado com a data do sistema operacional. � poss�vel mudar a data do programa em Edit.

	Informa��es de implementa��o:

	O programa foi feito na IDE Eclipse.
	Os dados relativos aos livros, usu�rios e empr�stimos s�o mantidos em 3 diferentes arquivos .csv; tamb�m � mantido um arquivo .txt com as respectivas quantidades de cada um desses 3 itens no sistema. Isso � �til, por exemplo, para gerar c�digos �nicos para cada item.
	 Os dados dos arquivos s�o passados para dentro do programa durante a inicializa��o do objeto SystemManager. Os dados processado s�o salvos nos arquivos, finalmente, por meio da fun��o updateSystem (ou salvar, na interface).
	Cada registro possui um c�digo �nico (code). No caso dos livros, ele � �til para diferenciar livros iguais. J� para usu�rios e empr�stimos, serve mais para emular sistemas reais.
	Foram utilizadas duas APIs externas: OpenCSV e JodaTime, a fim de facilitar o trabalho com os arquivos e com datas, respectivamente. Elas encontram-se na pasta lib.
	 Os arquivos de dados, de i18n e .png de icone encontram-se em bin/resources.

	OBS: Apesar de muitas tentativas, n�o foi poss�vel encapsular o programa em um Runnable JAR.