package me.doxxx.notion.page;

import java.util.List;

public record PageResponse(
        String title,
        String content,

        List<Page> subpages,
        String breadcrumbs
) {
    public PageResponse(Page page) {
        this(page.getTitle(), page.getContent(), page.getSubPages(), page.getBreadcrumbs());
    }
}
