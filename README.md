Trabalho Prático de LPOO I — Locadora de Veículos
Roteiro do grupo: escopo, divisão em 4 partes e orientações de cada integrante
Grupo: Cezar Anthonio (GRR20256527) · Christian Santana (GRR20251118) · Gabriel de Almeida (GRR20252934) · Osmar Nunes Júnior (GRR20250999)
Fonte: enunciado «Trabalho – Disciplina – 2026SEM2». As datas de entrega e de defesa estão no Moodle: alguém do grupo deve conferir e avisar todos.
1. Resumo do trabalho
Uma locadora quer um sistema em Java, com interface Swing e orientado a objetos, para controlar o ciclo de vida dos veículos: compra, locações e venda. Os veículos são automóveis, vans e motocicletas. Os dados ficam só em memória (não há banco de dados).
O sistema tem cinco telas:
1. Clientes: incluir, atualizar, excluir e listar (tabela com AbstractTableModel).
2. Incluir veículo: formulário com combos e máscaras que instancia o veículo e o guarda em memória.
3. Locar veículo: escolher o cliente, filtrar veículos, informar dias e data e clicar em Locar.
4. Devolver veículo: tabela dos veículos locados e botão Devolver veículo.
5. Vender veículo: filtrar, escolher o veículo na tabela e clicar em Vender.
Tecnologias exigidas: herança (Veiculo, Automovel, Motocicleta e Van, com classe abstrata), polimorfismo para manipular os veículos e Java Swing.
Itens a entregar: (1) diagrama de classes, (2) projeto no Eclipse ou NetBeans com o código-fonte e (3) arquivo .jar executável.
Avaliação: material entregue, qualidade do software (bugs encontrados na defesa) e defesa do código. A nota é individual e considera a defesa. Por isso cada integrante precisa entender o sistema inteiro, não só a própria parte (seção 8).
2. Pontos de atenção do enunciado
• A classe Veiculo é abstrata, implementa a interface VeiculoI e não tem get/set para cada atributo: só os métodos da interface. Cada subclasse tem o seu getModelo() e a sua tabela de diárias.
• Placa no formato XXX-0000 (três letras de A a Z, hífen e quatro dígitos). A máscara da tela ajuda, mas o construtor também valida.
• Diária por categoria (Popular / Intermediário / Luxo): Automóvel R$100 / R$300 / R$450; Motocicleta R$70 / R$200 / R$350; Van R$200 / R$400 / R$600.
• Valor para venda = valorDeCompra − idade × 0,15 × valorDeCompra. Se o resultado ficar abaixo de 10% do valor de compra (ou negativo), vale 10% do valor de compra.
• A Locacao só existe quando o estado é LOCADO. Locar cria a Locacao; devolver a descarta (null) e o estado volta a DISPONIVEL; vender muda para VENDIDO e o veículo nunca mais é alugado.
• Um cliente pode locar um veículo por vez.
• Cliente com veículo locado não pode ser excluído: a tela deve mostrar uma mensagem informando.
• As tabelas devem usar AbstractTableModel (não DefaultTableModel).
• Nas tabelas, o ano aparece com 4 dígitos e o preço no formato R$XXX,XX.
• As telas de locar e de vender listam só veículos DISPONIVEL: veículo locado ou vendido sai da lista.
• Na tela de incluir, o combo Modelo muda conforme o tipo (automóvel, moto ou van). Logo a tela também precisa de um seletor de tipo, que o enunciado não lista na tabela de atributos.
3. Arquitetura e contratos entre as partes
Para as quatro partes andarem em paralelo, o núcleo (modelo, repositório e formatação) já está pronto e testado: é a Parte 1. As outras partes só constroem telas em cima dele.
Pacotes do projeto Eclipse «Locadora» (pasta src):
• locadora.modelo: classes do domínio (pronto).
• locadora.dados: Repositorio (pronto).
• locadora.util: Formatador (pronto).
• locadora.ui: as telas, uma classe por tela, cada uma um JPanel que implementa a interface Atualizavel (Partes 2, 3 e 4).
• locadora.teste: TesteModelo, com 81 testes do núcleo (pronto).
Como as telas se encaixam: a Parte 2 cria a JanelaPrincipal com um JTabbedPane (uma aba por tela). Ao trocar de aba, a janela chama o método atualizar() do painel mostrado. Assim a tabela de Locar se atualiza sozinha depois de uma venda feita em outra aba. É o mesmo padrão da Tarefa 08 de LPOO.
Nomes obrigatórios das classes de tela (para a integração não quebrar):
• Parte 2: JanelaPrincipal, PainelClientes, ClienteTableModel.
• Parte 3: FiltroVeiculosPanel, PainelIncluirVeiculo, PainelVenderVeiculo, VeiculoVendaTableModel.
• Parte 4: PainelLocarVeiculo, PainelDevolverVeiculo, VeiculoLocacaoTableModel, VeiculoLocadoTableModel.
O que já existe e como usar:
• Veículos: new Automovel(marca, estado, locacao, categoria, valorDeCompra, placa, ano, modelo). Motocicleta e Van são iguais, com ModeloMotocicleta e ModeloVan. A locacao é null para estados NOVO e DISPONIVEL. A placa é validada no construtor (IllegalArgumentException).
• Enums: Marca, Estado, Categoria, TipoVeiculo (AUTOMOVEL, VAN, MOTOCICLETA), ModeloAutomovel, ModeloMotocicleta e ModeloVan. Todos têm toString() com o texto bonito, então podem ser colocados direto em um JComboBox.
• Operações do veículo: veiculo.locar(dias, data, cliente), veiculo.vender() e veiculo.devolver(). Se o estado não permitir, lançam IllegalStateException com mensagem em português: a tela deve capturar e mostrar a mensagem.
• Repositorio.getInstance(): único banco em memória, compartilhado por todas as telas. Métodos:
    ◦ Clientes: listarClientes(), adicionarCliente(c) (devolve false se o CPF já existe), cpfEmUso(cpf, clienteIgnorado), buscarClientes(texto) (por nome, sobrenome ou CPF), clientePossuiVeiculoLocado(c) e removerCliente(c) (lança IllegalStateException se o cliente tiver veículo locado).
    ◦ Veículos: adicionarVeiculo(v) (lança IllegalArgumentException se a placa já existe), placaJaCadastrada(placa), listarVeiculos(), listarDisponiveis(tipo, marca, categoria) (null em um filtro significa sem filtro) e listarLocados().
    ◦ Locação: locar(veiculo, dias, data, cliente) aplica a regra de um veículo por cliente e depois chama veiculo.locar.
• VeiculoUtil: tipoDe(v), descricaoModelo(v) (texto da coluna Modelo, para qualquer tipo de veículo) e modelosDoTipo(tipo) (valores do combo Modelo).
• Formatador: moeda(double) gera R$100,00; ano(int) gera 2021; data(Calendar) gera 05/11/2026; converterData(texto) devolve um Calendar, ou null se a data for inválida ou não existir (como 31/02/2026).
Contrato do FiltroVeiculosPanel (a Parte 3 entrega cedo; as Partes 3 e 4 usam): um JPanel com três combos (Tipo, Marca e Categoria), cada um com a opção «Todos» como primeiro item. Construtor FiltroVeiculosPanel(Runnable aoMudar): o aoMudar.run() é chamado sempre que algum combo mudar. Métodos getTipo(), getMarca() e getCategoria() devolvem null quando «Todos» está selecionado, exatamente o que o Repositorio.listarDisponiveis espera.
4. Divisão em 4 partes
Os responsáveis abaixo são uma sugestão (o Osmar assumiu a Parte 1, que já está pronta). Podem trocar entre si, mas mantenham os nomes das classes e o contrato da seção 3.
Parte 1 — Núcleo do sistema (Osmar) — PRONTA
Entregue no projeto-base:
• Todas as classes de locadora.modelo: Veiculo, VeiculoI, Automovel, Motocicleta, Van, Locacao, Cliente, os enums e VeiculoUtil.
• Repositorio (dados em memória com as regras de CPF único, placa única, exclusão de cliente e um veículo por cliente), Formatador e a interface Atualizavel.
• TesteModelo, com 81 testes (diárias, valor de venda, estados, placa, repositório e formatação).
• Diagramas de classes do modelo e da camada de dados (PNG, SVG e fonte .dot), arquivos de projeto do Eclipse e scripts build.bat e build.sh que geram o .jar.
Ainda cabe ao Osmar: montar o diagrama de classes final, com as telas, quando as outras partes forem entregues; apoiar as integrações; corrigir bugs do núcleo.
Parte 2 — Clientes, janela principal e .jar (Cezar)
Requisitos do enunciado: item 1 (a a d), a janela que reúne as telas e a geração do .jar.
Classes (pacote locadora.ui):
• ClienteTableModel, que estende AbstractTableModel. Colunas: Nome, Sobrenome, RG, CPF e Endereço. Implemente getRowCount, getColumnCount, getColumnName e getValueAt; inclua um método para trocar a lista de clientes (chamando fireTableDataChanged) e um getCliente(linha).
• PainelClientes (JPanel que implementa Atualizavel): campos Nome, Sobrenome, RG, CPF e Endereço; botões Incluir, Atualizar, Excluir e Limpar; tabela com todos os clientes. Clicar em uma linha preenche os campos.
• JanelaPrincipal (JFrame): JTabbedPane com as abas Clientes, Incluir veículo, Locar, Devolver e Vender, nessa ordem; um ChangeListener que chama atualizar() do painel mostrado; método main com SwingUtilities.invokeLater. As telas das outras partes ainda não existem no começo: crie a janela com a aba Clientes e abas provisórias (JPanel vazio) e troque pelas telas reais conforme forem entregues. Entregue essa casca no primeiro ou segundo dia, para todos poderem plugar a sua tela.
Regras e mensagens:
• Todos os campos são obrigatórios. O CPF deve ser único: ao incluir, o adicionarCliente devolve false se já existe; ao atualizar, use cpfEmUso(cpf, clienteSelecionado). Sugestão: validar também o CPF (11 dígitos e dígitos verificadores); no mínimo, exigir 11 dígitos.
• Atualizar: altere o cliente selecionado pelos setters e atualize a tabela.
• Excluir: peça confirmação. Antes, chame clientePossuiVeiculoLocado(cliente). Se for true, mostre uma mensagem (JOptionPane) dizendo que o cliente não pode ser excluído porque possui veículo locado, e não exclua. Esse é um requisito explícito do enunciado.
.jar executável:
• Rode build.bat (Windows) ou build.sh. O manifesto usa a classe principal locadora.ui.JanelaPrincipal.
• Teste o Locadora.jar com um duplo clique e com java -jar Locadora.jar, de preferência em um computador sem IDE.
• Exporte o projeto final (zip) para a entrega e confira se abre no Eclipse e no NetBeans.
Pronto quando: incluir, atualizar, excluir e listar clientes funcionam; a exclusão de cliente locado é recusada com mensagem; a janela principal abre as cinco abas; o .jar roda.
Parte 3 — Incluir veículo e vender veículo (Christian)
Requisitos do enunciado: item 2 (a a h) e item 5 (a a d).
Classes (pacote locadora.ui):
• FiltroVeiculosPanel: entregue primeiro (dia 1 ou 2), porque a Parte 4 usa. Contrato na seção 3.
• PainelIncluirVeiculo, PainelVenderVeiculo e VeiculoVendaTableModel (AbstractTableModel).
Tela Incluir veículo:
• Componentes: combo Tipo (TipoVeiculo.values()), combos Marca, Estado, Categoria e Modelo, campo Ano (4 dígitos), campo valorDeCompra com máscara monetária, campo Placa com máscara e botão Incluir.
• O combo Modelo é recarregado sempre que o tipo muda, com VeiculoUtil.modelosDoTipo(tipo).
• Estado: mostre só NOVO e DISPONIVEL, porque LOCADO exige uma locação e VENDIDO não faz sentido na compra (ver decisão D2).
• Placa: JFormattedTextField com MaskFormatter «UUU-####» (U é letra, convertida para maiúscula; # é dígito).
• Valor monetário: JFormattedTextField com NumberFormatter para pt-BR, ou um DocumentFilter que só aceita dígitos e vírgula. Em qualquer caso, converta para double antes de criar o veículo.
• Botão Incluir: valide todos os campos; crie um Automovel, uma Motocicleta ou uma Van conforme o tipo, sempre em uma variável do tipo Veiculo (polimorfismo); chame Repositorio.adicionarVeiculo(v); capture IllegalArgumentException (placa repetida ou inválida, valor inválido) e mostre a mensagem; ao concluir, avise e limpe o formulário.
Tela Vender veículo:
• FiltroVeiculosPanel no topo e uma tabela VeiculoVendaTableModel com as colunas Placa, Marca, Modelo, Ano e Preço para venda. Os dados vêm de Repositorio.listarDisponiveis(tipo, marca, categoria).
• Formatação: Formatador.ano(v.getAno()), Formatador.moeda(v.getValorParaVenda()) e VeiculoUtil.descricaoModelo(v).
• Botão Vender: exija uma linha selecionada, chame veiculo.vender() e recarregue a tabela (o veículo some da lista).
Pronto quando: é possível cadastrar um veículo de cada tipo; placa e valor têm máscara; a tabela de venda mostra só DISPONIVEL; vender tira o veículo da lista.
Parte 4 — Locar e devolver veículo (Gabriel)
Requisitos do enunciado: item 3 (a a g) e item 4 (a e b).
Classes (pacote locadora.ui):
• VeiculoLocacaoTableModel, com as colunas Placa, Marca, Modelo, Ano e Preço da diária.
• VeiculoLocadoTableModel, com as colunas Nome do Cliente, Placa, Marca, Modelo, Ano, Data Locação, Preço Diária, Quantidade de dias locado e Valor Locação.
• PainelLocarVeiculo e PainelDevolverVeiculo.
Tela Locar veículo:
• Cliente: um campo de busca (nome, sobrenome ou CPF) e uma lista ou combo preenchida com Repositorio.buscarClientes(texto), atualizada a cada alteração do texto ou ao clicar em Buscar. O cliente escolhido é o selecionado na lista.
• Filtro: FiltroVeiculosPanel (da Parte 3) com tipo, marca e categoria. Ao mudar, recarregue a tabela com Repositorio.listarDisponiveis(...). Se a Parte 3 ainda não entregou, use um stub com a mesma API.
• Tabela com o VeiculoLocacaoTableModel (ano com 4 dígitos e preço no formato R$XXX,XX).
• Campo Dias (inteiro maior ou igual a 1) e campo Data (dd/MM/aaaa), validada com Formatador.converterData. Sugestão: já vir preenchida com a data de hoje.
• Botão Locar: exija cliente, veículo na tabela, dias e data válidos; chame Repositorio.locar(veiculo, dias, data, cliente), que aplica a regra de um veículo por cliente e aciona veiculo.locar. Capture IllegalStateException (cliente que já tem veículo, veículo indisponível) e IllegalArgumentException e mostre a mensagem. No sucesso, mostre o valor da locação e recarregue a tabela (o veículo sai da lista).
Tela Devolver veículo:
• Tabela com o VeiculoLocadoTableModel, a partir de Repositorio.listarLocados(). Valores: v.getLocacao().getCliente().getNomeCompleto(), Formatador.data(locacao.getData()), Formatador.moeda(v.getValorDiariaLocacao()), locacao.getDias() e Formatador.moeda(locacao.getValor()).
• Botão Devolver veículo: exija uma linha selecionada, chame veiculo.devolver() (a Locacao vira null e o estado volta a DISPONIVEL) e recarregue a tabela.
Pronto quando: locar respeita todas as validações; o veículo locado some das tabelas de Locar e de Vender e aparece em Devolver; devolver o faz voltar às duas outras listas.
5. Cronograma sugerido
Ajustem as datas ao prazo do Moodle e deixem pelo menos 3 dias de folga no fim.
• Dia 0: o Osmar entrega o projeto-base e este roteiro. Todos importam o projeto no Eclipse e rodam o TesteModelo (resultado esperado: 81 OK, 0 ERRO).
• Dias 1 e 2: o Cezar entrega a JanelaPrincipal (casca com abas) e o Christian entrega o FiltroVeiculosPanel e a tela de incluir veículo, que serve para cadastrar dados de teste para todos.
• Dias 3 a 5: cada um termina as suas telas.
• Dia 6: integração: todas as telas dentro da JanelaPrincipal.
• Dia 7: testes cruzados com o roteiro da seção 8; cada pessoa testa a tela de outra.
• Dia 8: o Osmar fecha o diagrama final; o Cezar gera o .jar e o zip do projeto.
• Dia 9: ensaio da defesa.
6. Convenções do grupo
• Cada arquivo .java começa com um comentário com o GRR e o nome do autor (modelo nos arquivos do núcleo).
• Um arquivo, um dono. Não editem arquivos de outra parte sem avisar. Mudanças nas classes de locadora.modelo e locadora.dados passam pelo Osmar, porque todas as telas dependem delas.
• Compartilhem o projeto em um repositório (GitHub, por exemplo) ou em uma pasta do Drive, e avisem no grupo a cada entrega.
• Código compatível com Java 8, arquivos em UTF-8, nomes de classes e métodos em português como no enunciado, JavaDoc nos métodos públicos.
• Telas em locadora.ui, cada uma um JPanel que implementa Atualizavel. Não usem DefaultTableModel. Mensagens ao usuário com JOptionPane, sem System.out.
7. Integração e entrega
• Diagrama de classes: o Osmar parte dos diagramas já feitos (modelo e dados) e acrescenta as classes de locadora.ui (JanelaPrincipal, os painéis e os TableModels, ligados ao Repositorio). Os arquivos .dot estão na pasta docs do projeto.
• Projeto: pasta Locadora inteira, sem a pasta bin, em um zip, abrindo sem erro no Eclipse (File > Import > Existing Projects into Workspace) e, se possível, também no NetBeans.
• Executável: Locadora.jar gerado pelo build.bat e testado em outro computador com java -jar.
• Antes de entregar: compilar do zero, rodar o roteiro de testes da seção 8 e conferir os três itens exigidos (diagrama, projeto e .jar).
8. Preparação para a defesa
A nota é individual e considera a defesa; bugs encontrados na defesa pesam na qualidade do software. Todos devem ser capazes de explicar o sistema inteiro.
Perguntas prováveis:
• Por que Veiculo é abstrata e onde está o polimorfismo? (Veiculo v = new Van(...), getValorDiariaLocacao abstrato, as telas manipulam Veiculo sem saber o tipo.)
• Como funciona o AbstractTableModel? Quais métodos implementar e quando chamar fireTableDataChanged?
• Como é calculado o valor da locação e o valor para venda?
• O que acontece com a Locacao ao devolver? E ao vender um veículo locado?
• Onde ficam os dados e por que o Repositorio é compartilhado (singleton)?
• Como a placa é validada (máscara na tela e regex no construtor)?
• Como uma tela fica atualizada depois de uma ação em outra aba?
Roteiro de testes manuais (os cenários em que costuma aparecer bug):
1. Incluir cliente com CPF repetido (com e sem pontos): deve recusar.
2. Atualizar um cliente e conferir a tabela.
3. Excluir cliente sem locação (ok) e com veículo locado (mensagem).
4. Incluir um veículo de cada tipo, nas três categorias.
5. Placa inválida e placa repetida.
6. Valor de compra com vírgula, zero e texto.
7. Locar sem cliente, sem veículo, com dias 0, negativo ou texto, e com data inexistente (31/02).
8. Locar e conferir que o veículo sai das tabelas de Locar e Vender e aparece em Devolver.
9. Cliente com veículo locado tentando locar outro: deve recusar.
10. Conferir o valor da locação (diária vezes dias) para automóvel, moto e van.
11. Devolver e conferir que o veículo volta às tabelas de Locar e Vender.
12. Vender e conferir que o veículo não aparece mais em nenhuma tabela.
13. Valor para venda com veículo do ano atual, de 3 anos e de 8 anos (nunca abaixo de 10% do valor de compra).
14. Trocar de aba e voltar: as tabelas devem estar atualizadas.
15. Fechar e abrir o .jar fora da IDE.
9. Decisões tomadas e pontos a confirmar
• D1 (confirmar com o professor): o enunciado lista o estado NOVO, mas as telas só mostram veículos DISPONIVEL. Do jeito atual, só um veículo DISPONIVEL pode ser locado ou vendido; um veículo NOVO não aparece nas telas. Por isso a tela de incluir deve mostrar DISPONIVEL como valor padrão do estado.
• D2: o combo Estado da tela de incluir mostra só NOVO e DISPONIVEL. LOCADO exigiria uma locação e VENDIDO não faz sentido numa compra.
• D3: a classe Locacao tem um getDias(), além dos getters do enunciado, porque a tabela de devolução precisa da coluna «Quantidade de dias locado».
• D4 (confirmar com o professor): o preço aparece como R$45.000,00 (com ponto de milhar). Se o professor exigir R$45000,00, basta trocar Formatador.USAR_SEPARADOR_MILHAR para false.
• D5: a data da locação é digitada em um TextField no formato dd/MM/aaaa, validado pelo Formatador.
• D6: a regra «um veículo por cliente» está no Repositorio.locar; por isso as telas devem usar esse método (e não veiculo.locar direto).
• D7: só veículo DISPONIVEL pode ser locado ou vendido; veículo locado não pode ser vendido nem locado de novo.
