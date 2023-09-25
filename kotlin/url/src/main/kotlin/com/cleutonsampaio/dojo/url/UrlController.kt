package com.cleutonsampaio.dojo.url

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

// Before running: docker run -d -p 6379:6379 --name redis redis
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