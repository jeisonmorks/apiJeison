Instruções para rodar a API:

- O java 11 ou mais recente deve estar instalado para execução da API
- Instalar o postman para realizar as requisições para a API
- Executar o arquivo Filme.bat para iniciar a aplicação. OBS: o arquivo Filme-0.0.1-SNAPSHOT.jar deve estar no mesmo diretório do bat
- A API foi desenvolvida utilizando-se spring boot, inclusive os testes de integração, então para se executar os testes, basta importar o projeto filme no eclipse e fazer um maven build no projeto, clicando com o botão direito no projeto -> run as -> maven build.
Outra opção, é abrir a classe FilmeApplicationTests e executar ela individualmente atráves do run as -> JUnit test
- Para testar a API, realizar requisições com os seguintes verbos:

POST usando a url localhost:8080/filme para inserir um filme, passando o seguinte json:
{
    "ano": 2000,
    "titulo": "Nome do filme",
    "estudios": "Estúdio",
    "produtores": "Nome(s) do(s) produtor(es)",
    "vencedor": "yes" - yes para indicar que ganhou
}
GET usando a url localhost:8080/filme/vencedor para obter os produtores com menor e maior intervalo entre cada prêmio
GET usando a url localhost:8080/filme/todos para obter todos os filmes cadastrados
GET usando a url localhost:8080/filme/{id} para obter um filme específico
DELETE usando a url localhost:8080/filme?id={id} para deletar um filme
PUT usando a url localhost:8080/filme para atualizar totalmente um filme, passando um json também, como no método POST
PATCH usando a url localhost:8080/filme para atualizar parcialmente um filme, passando um json também, como no método POST
