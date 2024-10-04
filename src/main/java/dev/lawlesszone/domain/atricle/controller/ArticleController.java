package dev.lawlesszone.domain.atricle.controller;

import dev.lawlesszone.domain.Member.dto.CustomUserDetail;
import dev.lawlesszone.domain.atricle.dto.ArticleRequestDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleResponseDTO;
import dev.lawlesszone.domain.atricle.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> ArticleList() {
        List<ArticleResponseDTO> articles = articleService.findAllArticles();

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ArticleResponseDTO> ArticleView(@PathVariable("id") Long id) {
        ArticleResponseDTO article = articleService.findArticleById(id);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ArticleResponseDTO> ArticleSave(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                          @RequestBody ArticleRequestDTO articleRequestDTO) {

        ArticleResponseDTO article = articleService.saveArticle(articleRequestDTO, customUserDetail.getId());
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ArticleResponseDTO> ArticleUpdate(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                            @PathVariable("id") Long id,
                                                            @RequestBody ArticleRequestDTO articleRequestDTO) {
        ArticleResponseDTO article = articleService.updateArticle(id, articleRequestDTO);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> ArticleDelete(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
