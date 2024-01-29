# 📅 Projeto Agenda de Contatos 📇

Bem-vindo(a) ao **Projeto Agenda de Contatos**! Esta aplicação oferece uma maneira simples e organizada de gerenciar seus contatos, utilizando arquivos de texto para simular uma base de dados.

Uma classe chamada `Agenda` é fornecida para gerenciar contatos. A classe contém diversos métodos para várias funcionalidades.

## Métodos

### `leitor_arquivo`

Lê contatos de um arquivo e popula a lista `contatos`.

### `escritor_arquivo`

Escreve os contatos da lista `contatos` em um arquivo txt.

### `criar_contato`

Permite que o usuário crie um novo contato e o adicione à agenda.

### `remover_contato`

Permite que o usuário remova um contato da agenda.

### `editar_contato`

Permite que o usuário edite um contato existente na agenda.

### `listar_contatos`

Imprime a lista de contatos na agenda.

### `imprimir_menu`

Exibe o menu de comandos para adicionar, remover ou editar contatos.

### `main`

O ponto de entrada da aplicação. Inicializa a `Agenda`, lê contatos de um arquivo e entra em um loop para interagir com o usuário para adicionar, remover ou editar contatos.

## Uso

Para utilizar a classe `Agenda` e seus métodos, siga estas etapas:

1. nicialize uma instância da classe `Agenda`.
2. Utilize o método `leitor_arquivo` para ler contatos de um arquivo.
3. Interaja com o usuário usando os métodos fornecidos (`criar_contato`, `remover_contato`, `editar_contato`, `listar_contatos`, `imprimir_menu`) dentro do loop `main`.
4. Utilize o método `escritor_arquivo` para salvar os contatos atualizados em um arquivo.

Sinta-se à vontade para personalizar os métodos e integrá-los em sua aplicação para gerenciar contatos de maneira eficaz.
