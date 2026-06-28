# Gestor de Gastos - Regra 50/30/20

## Descrição
Aplicação Java para gerenciar seus gastos mensais seguindo a regra 50/30/20:
- **50%** - Gastos Fixos (aluguel, contas, alimentação essencial)
- **30%** - Gastos Não Essenciais (lazer, entretenimento, compras)
- **20%** - Investimento (poupança, investimentos)

## Como Usar

### Executar a Aplicação
1. Certifique-se de ter o Java instalado (versão 8 ou superior)
2. Execute o arquivo JAR:
   ```
   java -jar GestorGastos.jar
   ```
   Ou use o script fornecido:
   ```
   executar.bat
   ```

### Funcionalidades
1. **Definir Renda Mensal**: Insira sua renda mensal no campo superior
2. **Adicionar Gastos**: Clique em "Adicionar Gasto" para registrar um novo gasto
3. **Remover Gastos**: Selecione um gasto na tabela e clique em "Remover Gasto"
4. **Acompanhamento Visual**: Barras de progresso mostram quanto você gastou em cada categoria
   - Verde: Dentro do orçamento
   - Laranja: Próximo do limite (>80%)
   - Vermelho: Acima do orçamento
5. **Persistência de Dados**: Seus dados são salvos automaticamente e recuperados ao reabrir a aplicação
   - Os dados são salvos no arquivo `gestor_gastos_data.txt`
   - Salvamento automático ao definir renda, adicionar ou remover gastos
   - Carregamento automático ao iniciar a aplicação

### Categorias
- **Gastos Fixos (50%)**: Despesas essenciais e recorrentes
- **Gastos Não Essenciais (30%)**: Despesas opcionais e lazer
- **Investimento (20%)**: Poupança e investimentos

## Estrutura do Projeto
```
GestorGastos/
├── src/
│   └── com/gestorgastos/
│       ├── model/
│       │   ├── Category.java
│       │   ├── Expense.java
│       │   └── BudgetManager.java
│       ├── persistence/
│       │   └── DataManager.java
│       └── ui/
│           └── MainWindow.java
├── bin/
├── GestorGastos.jar
├── gestor_gastos_data.txt (gerado automaticamente)
└── README.md
```

## Requisitos
- Java Runtime Environment (JRE) 8 ou superior
