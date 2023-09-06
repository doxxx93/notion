package me.doxxx.notion.page.presentation;

import me.doxxx.notion.page.application.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/pages/{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable long pageId) {
        return ResponseEntity.ok(pageService.getPageById(pageId));
    }
}

