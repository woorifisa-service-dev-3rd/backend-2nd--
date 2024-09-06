package dev.lawlesszone.domain.atricle.controller;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

//        System.out.println("article = " + article);

        return "article/articleView";
    }
}
