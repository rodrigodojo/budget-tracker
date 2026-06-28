# Contexto do Projeto: Gestor de Gastos - Regra 50/30/20

## Resumo do Projeto
Aplicação Java desktop para gerenciar gastos mensais seguindo a regra 50/30/20, com interface gráfica Swing e persistência de dados.

## Informações do Repositório
- **GitHub:** https://github.com/rodrigodojo/budget-tracker
- **Usuário:** rodrigodojo
- **Branch:** main
- **Localização Local:** C:\Users\rodri\GestorGastos

## Estrutura do Projeto

```
GestorGastos/
├── src/
│   └── com/gestorgastos/
│       ├── model/
│       │   ├── Category.java          # Enum com categorias (50/30/20)
│       │   ├── Expense.java           # Classe de despesa
│       │   └── BudgetManager.java     # Gerenciador de orçamento
│       ├── persistence/
│       │   └── DataManager.java       # Persistência de dados
│       └── ui/
│           └── MainWindow.java        # Interface gráfica Swing
├── bin/                               # Classes compiladas (ignorado no git)
├── GestorGastos.jar                   # Executável
├── executar.bat                       # Script para executar
├── gestor_gastos_data.txt            # Dados salvos (ignorado no git)
├── MANIFEST.MF                        # Manifest do JAR
├── .gitignore
└── README.md
```

## Funcionalidades Implementadas

### 1. Regra 50/30/20
- **50%** - Gastos Fixos (aluguel, contas, alimentação essencial)
- **30%** - Gastos Não Essenciais (lazer, entretenimento, compras)
- **20%** - Investimento (poupança, investimentos)

### 2. Interface Gráfica (Swing Moderno / Estilo Tailwind)
- Campo para definir renda mensal
- Resumo financeiro flat em formato de Cards (renda, gastos, restante)
- 3 painéis de categoria com barras de progresso modernas:
  - Verde: < 80% do orçamento
  - Laranja: 80-100% do orçamento
  - Vermelho: > 100% do orçamento
- Tabela de gastos registrados estilizada de maneira responsiva
- Botões para adicionar/remover gastos com efeitos transicionais flutuantes (Hover)

### 3. Persistência de Dados
- Salvamento automático em arquivo texto
- Carregamento automático ao iniciar
- Formato simples: renda na primeira linha, gastos nas linhas seguintes
- Formato de gasto: `Descrição|Valor|Categoria|Data`

### 4. Correções Aplicadas e Estilos de Design
- Visual sofisticado Flat baseado em Tailwind CSS (Slate, Indigo, Amber, Emerald, Rose)
- Tipografia moderna e uniforme com Segoe UI em toda a aplicação
- Diálogo Sobre sofisticado com redirecionamento de autoria ao desenvolvedor rodrigodojo

### 5. Barra de Menu Superior Estilizada e Saída Graciosa
- Barra de menu com design minimalista flat e animações em hover (fundo e texto)
- Item de menu que executa o fluxo completo de salvamento inteligente antes do encerramento
- Prevenção de fechamento desordenado através de WindowAdapter e JFrame.DO_NOTHING_ON_CLOSE

### 6. Histórico de Gastos Mês a Mês Re-arquitetado
- Separação lógica de orçamentos e despesas mês a mês mantendo completa retrocompatibilidade
- Menu superior de seleção dinâmica "Histórico de Meses" gerado a partir das datas das despesas reais cadastradas
- Exibição visível em cabeçalho do período ativo ("Período Ativo: MM/AAAA" ou "Período Ativo: Todos os meses") para rastreabilidade

### 7. Inicialização Maximizado Nativo
- Janela principal configurada para inicialização direta com `setExtendedState(JFrame.MAXIMIZED_BOTH)` para que sempre inicie maximizado nativamente.

## Comandos Importantes

### Compilar
```bash
cd GestorGastos
javac -encoding UTF-8 -d bin -sourcepath src src/com/gestorgastos/ui/MainWindow.java
```

### Gerar JAR
```bash
jar cvfm GestorGastos.jar MANIFEST.MF -C bin .
```

### Executar
```bash
java -jar GestorGastos.jar
# ou
executar.bat
```

### Git
```bash
git add .
git commit -m "mensagem"
git push
```

## Histórico de Commits

1. **fa3a033** - Initial commit: Gestor de Gastos - Regra 50/30/20
   - Estrutura inicial do projeto
   - Classes de modelo
   - Interface gráfica básica
   - JAR executável

2. **fb85355** - Fix: Corrigir interface - botoes e caracteres especiais
   - Cor do texto dos botões (branco → preto)
   - Remoção de acentos
   - Propriedades setOpaque e setBorderPainted

3. **6d901b9** - Feature: Adicionar persistencia de dados
   - Classe DataManager
   - Salvamento automático
   - Carregamento automático
   - Atualização do .gitignore

4. **0695f1c** - Docs: Atualizar README com informacoes de persistencia
   - Documentação da funcionalidade de persistência

## Tecnologias Utilizadas
- **Java 15** (compatível com Java 8+)
- **Swing** - Interface gráfica
- **Git** - Controle de versão
- **GitHub** - Repositório remoto

## Arquivos de Dados
- **gestor_gastos_data.txt** - Arquivo de persistência (criado automaticamente)
- Localização: mesma pasta do JAR
- Pode ser copiado para backup

## Próximos Passos Possíveis (Não Implementados)
- Exportar relatórios em PDF
- Gráficos de gastos por categoria
- Histórico mensal
- Múltiplos usuários
- Categorias personalizadas
- Importação/exportação de dados

## Notas Técnicas
- Encoding: UTF-8
- Line endings: CRLF (Windows)
- Java version: 15.0.1
- Git version: 2.54.0.windows.1

## Contato
- Desenvolvido com assistência de Abacus.AI CLI
- Repositório: https://github.com/rodrigodojo/budget-tracker

---
**Última atualização:** 28/06/2026
**Status:** Projeto completo, moderno, maximizado por padrão e filtrável mês a mês.
