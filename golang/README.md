![](./golang.jpg)

# Implementação em Go (Golang)

**Go** (**Golang**) é uma linguagem de programação poderosa, versátil, fácil de aprender e que compila em código nativo. Excelente escolha se a performance é sua preocupação. Não possui um conceito pleno de **Orientação a Objetos**, mas sua simplicidade compensa isso. 

A princípio daria sim para criar um servidor **REST** só com os recursos da linguagem, sem utilizar framework algum. Porém, o **Gin** tem se tornado muito popular e vamos utilizar aqui. 

## Executando a aplicação

Bom, você precisa instalar as dependências: 
```
go get github.com/gin-gonic/gin
go get github.com/go-redis/redis/v8
```

Suba o contêiner **REDIS**: 
```
docker run -d -p 6379:6379 --name redis redis
```

E pode rodar ou compilar a aplicação: 
```
go run main.go
```
**OU**
```
go build main.go
```

Então, pode criar URLs: 
```
curl -X POST -H "Content-Type: application/json" -d '{"url": "https://golang.network"}' http://localhost:8080/urls
```

E pode recuperar as URLs: 
```
curl http://localhost:8080/urls
```

## Criando a aplicação do zero

É muito simples criar esta aplicação. Para começar, crie a pasta do projeto: 
```
mkdir go-gin-redis-url
cd go-gin-redis-url
```

Inicie um **módulo Go** na pasta: 
```
go mod init go-gin-redis-url
```

Lembre-se de instalar as dependências: 
```
go get github.com/gin-gonic/gin
go get github.com/go-redis/redis/v8
```

Crie um arquivo "main.go" com este conteúdo: 
```
package main

import (
	"context"
	"github.com/gin-gonic/gin"
	"github.com/go-redis/redis/v8"
)

var (
	rdb     *redis.Client
	rdbCtx  = context.Background()
	urlsKey = "urls"
)

func setupDatabase() {
	rdb = redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
	})

	_, err := rdb.Ping(rdbCtx).Result()
	if err != nil {
		panic(err)
	}
}

func getUrls(c *gin.Context) {
	urls, err := rdb.SMembers(rdbCtx, urlsKey).Result()
	if err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
		return
	}

	c.JSON(200, gin.H{"urls": urls})
}

func addUrl(c *gin.Context) {
	var url struct {
		Url string `json:"url"`
	}

	if err := c.BindJSON(&url); err != nil {
		c.JSON(400, gin.H{"error": "Failed to parse request"})
		return
	}

	err := rdb.SAdd(rdbCtx, urlsKey, url.Url).Err()
	if err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
		return
	}

	c.JSON(200, gin.H{"message": "URL added successfully"})
}

func main() {
	setupDatabase()

	r := gin.Default()

	r.GET("/urls", getUrls)
	r.POST("/urls", addUrl)

	r.Run(":8080")
}
```

Pronto! Pode executar o servidor!

