package dev.lawlesszone.domain.atricle.controller;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(path = "/list")
    public String ArticleList(Model model) {
        List<Article> articles = articleService.findAllArticles();
        model.addAttribute("articles", articles);

        return "article/articleList";
    }

    @GetMapping(path = "/view/{id}")
    public String ArticleView(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findArticleById(id);
        model.addAttribute("article", article);

        return "article/articleView";
    }

    @GetMapping(path = "/write")
    public String ArticleWrite() {
        return  "article/articleWrite";
    }

    @PostMapping(path = "/write")
    public String ArticleSave(Article article) {
        articleService.saveArticle(article);
        System.out.println("write post()");
        return "redirect:/articles/list";
    }

    @GetMapping(path = "/edit/{id}")
    public String ArticleEdit(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findArticleById(id);
        model.addAttribute("article", article);
        System.out.println("article = " + article);
        return  "article/articleEdit";
    }

    @PostMapping(path = "/edit/{id}")
    public String ArticleEdit(@PathVariable("id") Long id, Article article, Model model) {
        articleService.updateArticle(article, id);
        return "redirect:/articles/list";
    }

    @GetMapping(path = "/delete/{id}")
    public ResponseEntity<Void> ArticleDelete(@PathVariable("id") Long id) {
        System.out.println("삭제 컨트롤러");
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
