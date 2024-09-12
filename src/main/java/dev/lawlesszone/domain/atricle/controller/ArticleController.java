package dev.lawlesszone.domain.atricle.controller;

import dev.lawlesszone.domain.atricle.dto.ArticleWriteRequestDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleViewResponseDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleWrtieResponseDTO;
import dev.lawlesszone.domain.atricle.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleViewResponseDTO>> ArticleList() {
        List<ArticleViewResponseDTO> articles = articleService.findAllArticles();

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ArticleViewResponseDTO> ArticleView(@PathVariable("id") Long id) {
        ArticleViewResponseDTO article = articleService.findArticleById(id);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    // @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ArticleWrtieResponseDTO> ArticleSave(@RequestBody ArticleWriteRequestDTO articleWriteRequestDTO) {
        ArticleWrtieResponseDTO article = articleService.saveArticle(articleWriteRequestDTO);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    //@PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ArticleWrtieResponseDTO> ArticleUpdate(@PathVariable("id") Long id, @RequestBody ArticleWriteRequestDTO articleWriteRequestDTO) {
        ArticleWrtieResponseDTO article = articleService.updateArticle(id, articleWriteRequestDTO);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    // @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> ArticleDelete(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
