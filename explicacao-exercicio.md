# Relatório de Uso de Inteligência Artificial

Este documento descreve como a Inteligência Artificial (IA) foi utilizada como ferramenta de apoio durante a resolução do exercício (Questão 1 - Pesquisa na Web).

## 🎯 Objetivo do Uso da IA

A IA foi empregada estritamente como um **guia e auxiliar** para facilitar a compreensão dos requisitos do problema, especialmente na interpretação de como os padrões de projeto (Observer e Strategy) deveriam interagir na arquitetura proposta. 

**Em nenhum momento a IA foi utilizada como "piloto" para escrever o código final da solução.** O desenvolvimento, a modelagem das classes e a implementação do código em Java foram feitos manualmente pelos alunos. A consulta serviu apenas para esclarecer a lógica de integração dos filtros dinâmicos no modelo de busca já existente.

---

## 1. Consulta Realizada

Abaixo está o registro da interação com a IA:

### IA e Modelo Utilizados
- **Ferramenta:** Gemini Pro

### Prompt (Pergunta enviada)
> Me explique melhor o que ele quer aqui:
>
> O pacote websearch no código inicial anexado contém código parcial para um mecanismo de busca na Web simulado. Ele vai ler um arquivo de dados e "fingir" que cada linha é uma consulta que alguém enviou a um mecanismo de pesquisa. O objetivo é extrair, em tempo real, consultas "interessantes" que atendam a determinadas condições.
>
> Observe que o código já usa objetos observadores para notificar o Snooper de cada consulta. O Snooper então imprime tudo.
>
> Altere o código para que ele use o padrão Strategy da seguinte forma:
>
> - No modelo de busca, crie uma nova interface que descreve a interface para um objeto que definirá um filtro de consulta.
> - Um objeto de filtro de consulta terá um método que receberá uma string (a consulta) e retornará `true` se o modelo de busca notificar o observador sobre essa consulta; retornará `false` se o observador não estiver interessado nesta string (a consulta).
> - Altere o modelo de busca para que, quando um observador for registrado, o método de registro também aceite um objeto de filtro de consulta.
> - Altere o modelo de busca para que, para cada consulta (string do arquivo), verifique se um observador está interessado na consulta antes de notificá-la.
> - Altere o cliente (`Snooper.java`) para criar dois observadores de consulta:
>	- Um imprime `Oh Yes! <consulta>` sempre que a consulta contém a palavra `friend` (não diferencia maiúsculas de minúsculas).
>	- Um imprime `So long <consulta>` sempre que a consulta tiver mais de 60 caracteres.
>
> ### Observações
>
> - O modelo de busca não deve saber nada sobre a implementação dos objetos de política de filtro de consulta, a não ser que implementem a interface necessária.

---

## 2. Resposta Obtida (Resumo e Guia)

A IA forneceu uma explicação clara de como os padrões interagem, ajudando a traçar o plano de implementação. Abaixo estão os principais pontos da explicação da IA que guiaram o desenvolvimento manual:

### Ideia principal
O exercício pede para combinar o padrão **Strategy** com o padrão **Observer**, que já está parcialmente implementado no código. Hoje, o mecanismo de busca lê o arquivo e avisa os observadores sobre **todas** as consultas. A proposta é usar o Strategy para criar **filtros**, de modo que cada observador só receba as consultas que realmente interessam.

### Passos Sugeridos (Usados para organizar o raciocínio)

#### 1. Criar a interface da estratégia
Criar uma interface que defina o contrato do filtro. Ela será responsável por decidir se uma consulta deve ou não ser mostrada.
- Exemplo: criar uma interface como `QueryFilter` com um único método que recebe uma `String` e retorna um `boolean`.

#### 2. Criar os filtros concretos
Em vez de colocar vários `if`s no código principal, criar classes que implementem essa interface.
- Filtro 1: retorna `true` se a string contiver a palavra `friend`, sem diferenciar maiúsculas de minúsculas.
- Filtro 2: retorna `true` se a string tiver mais de 60 caracteres.

#### 3. Alterar o mecanismo de busca
O mecanismo de busca precisa associar cada observador ao seu filtro.
- No registro: o método de cadastro do observador deve receber o observador e o filtro dele.
- Na notificação: antes de avisar um observador, o modelo deve perguntar ao filtro se aquela consulta passa ou não.
- O modelo não deve conhecer a lógica interna do filtro, apenas chamar o método da interface.

#### 4. Alterar o `Snooper.java`
No cliente, criar os dois filtros e registrar os dois observadores com comportamentos diferentes.
- Um imprime `Oh Yes! <consulta>` quando houver `friend` na consulta.
- Outro imprime `So long <consulta>` quando a consulta tiver mais de 60 caracteres.

### Conclusão do Auxílio
A explicação confirmou que o padrão Strategy serviria para injetar a regra de filtro "de fora para dentro". O modelo só precisaria perguntar: *"essa consulta passa?"*, sem saber como a resposta foi calculada. 