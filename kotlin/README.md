![](./kotlin.png)

# Implementação Kotlin

Certamente, você ouviu dizer que **Kotlin** é utilizada para aplicações **Android**. Sim, é. Mas é uma linguagem de programação baseada na **JVM** e pode ser utilizada para **backend**. 

Você não conseguirá criar um RESTful service utilizando apenas Kotlin. Ao contrário de outras linguagens de programação, Kotlin é extremamente verbosa para isto. 

Para ser justo, usei o pacote **Spring**, muito popular entre os desenvolvedores **backend**.

O **Spring** é o framework mais utilizado para aplicações Kotlin/REST. Ele tem uma ferramenta de **scaffolding** genial chamada: [**Spring Initializr**](https://start.spring.io/) que gera uma aplicação Java utilizando o **Spring Boot**, que é uma camada de bibliotecas para facilitar a criação de aplicações Java/Spring.

É possível criar essa aplicação apenas com **JAX-RS**, a implementação padrão, porém não é o mais usual. 

Nem vamos entrar em muitos detalhes sobre Spring aqui. Vamos apenas criar o **backend** da nossa app que guarda e retorna URLs.

O projeto Kotlin já está criado aqui neste repositório para você. Se quiser repetir os passos criando você mesmo uma aplicação, o que eu recomendo muito para que faça um "test-drive" da linguagem, siga estes passos. 

Se quiser apenas executar esta aplicação: 

1. Suba uma instância de REDIS no Docker: 

```
docker run -d -p 6379:6379 --name redis redis
```

2. Abra a pasta raiz do projeto e execute: 

```
mvn spring-boot:run
```

3. No terminal, poste uma URL: 
```
curl -X POST -H "Content-Type: text/plain" -d "https://www.example.com" http://localhost:8080/urls
```

4. Agora obtenha as URLs cadastradas: 

```
curl http://localhost:8080/urls
```

Parabéns! Você tem um RESTful service feito em Kotlin com Spring acessando um database REDIS. 

## Construindo do zero

1. Comece inicializando um novo projeto **Spring Boot**: 

Acesse [Spring Initializr](https://start.spring.io/), selecionando `Web`, `JPA`, `Lombok` e `Redis` como dependências. Gere e baixe o projeto. Aqui você vai escolher inclusive o nome do pacote da sua aplicação. Selecione **maven**: 

![](./spring-initializr.png)

2. No pacote principal do seu projeto (src/main/kotlin/{pacote}/), crie o código abaixo (cuidado para alterar o nome do pacote conforme a sua escolha): **UrlResource.kt**

```
package com.cleutonsampaio.dojo.url

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/urls")
class UrlController {
    @Autowired
    private val redisTemplate: RedisTemplate<String, String>? = null

    @get:GetMapping
    val urls: Set<String>?
        get() = redisTemplate!!.opsForSet().members("urls")

    @PostMapping
    fun addUrl(@RequestBody url: String) {
        redisTemplate!!.opsForSet().add("urls", url)
    }
}

```

3. Altere a linha abaixo na classe **UrlApplication.kt**:

**de:**
```
@SpringBootApplication
```
**para:**
```
@SpringBootApplication(exclude = arrayOf(DataSourceAutoConfiguration::class))
```

4. Adicione as propriedades do **REDIS** no "src/main/resources/application.properties": 
```
spring.redis.host=localhost
spring.redis.port=6379
```

5. Inicie o **REDIS** no **Docker**:
```
docker run -d -p 6379:6379 --name redis redis
```

6. Inicie a sua aplicação **Java/SpringBoot**: 
```
mvn spring-boot:run
```
