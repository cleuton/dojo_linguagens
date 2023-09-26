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
