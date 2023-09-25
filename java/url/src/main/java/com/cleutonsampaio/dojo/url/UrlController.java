package com.cleutonsampaio.dojo.url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

// Before running: docker run -d -p 6379:6379 --name redis redis

@RestController
@RequestMapping("/urls")
public class UrlController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping
    public Set<String> getUrls() {
        return redisTemplate.opsForSet().members("urls");
    }

    @PostMapping
    public void addUrl(@RequestBody String url) {
        redisTemplate.opsForSet().add("urls", url);
    }
}

