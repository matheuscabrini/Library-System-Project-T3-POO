	Informações de uso:
	
	Antes do inicio do programa, pode-se escolher, no momento, entre os idiomas: inglês (default) e português.
	A interface principal é dividida em: título, o qual mostra a data do sistema; menu de opções (File, Edit, Actions); e três abas relativas a livros, usuários e empréstimos, contendo suas respectivas tabelas (listas) de registros. 
	Nas tabelas, é possível mudar a ordem das colunas, arrastando-as com o mouse; além de ordenar os registros por determinada coluna, clicando no título dela.
	Alguns registros já estão inseridos nos arquivos. Para adicionar outros, dirija-se ao menu Actions.
	Para registrar um usuário, pode-se escolher entre colocar uma identificação da universidade (algo como o numero USP) OU o número de RG e endereço de moradia. Isso serve para facilitar o cadastro de estudantes e professores; porém permitindo que qualquer tipo de usuário possa ser    eventualmente cobrado caso não devolva seus empréstimos.
	Para registrar empréstimo, digita-se os códigos do livro e do usuário (disponíveis nas suas respectivas tabelas).
	Usuários que não devolverem seus livros até a data máxima de retorno (inclusive) serão suspensos do sistema por tempo indeterminado. Quando finalmente retornarem o livro, ainda ficarão suspensos pela quantidade de dias em atraso.
	Para salvar os dados ou sair do programa (que salva automaticamente), clique no menu File.
	O programa é inicializado com a data do sistema operacional. É possível mudar a data do programa em Edit.

	Informações de implementação:

	O programa foi feito na IDE Eclipse.
	Os dados relativos aos livros, usuários e empréstimos são mantidos em 3 diferentes arquivos .csv; também é mantido um arquivo .txt com as respectivas quantidades de cada um desses 3 itens no sistema. Isso é útil, por exemplo, para gerar códigos únicos para cada item.
	 Os dados dos arquivos são passados para dentro do programa durante a inicialização do objeto SystemManager. Os dados processado são salvos nos arquivos, finalmente, por meio da função updateSystem (ou salvar, na interface).
	Cada registro possui um código único (code). No caso dos livros, ele é útil para diferenciar livros iguais. Já para usuários e empréstimos, serve mais para emular sistemas reais.
	Foram utilizadas duas APIs externas: OpenCSV e JodaTime, a fim de facilitar o trabalho com os arquivos e com datas, respectivamente. Elas encontram-se na pasta lib.
	 Os arquivos de dados, de i18n e .png de icone encontram-se em bin/resources.

	OBS: Apesar de muitas tentativas, não foi possível encapsular o programa em um Runnable JAR.