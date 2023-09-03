package me.doxxx.notion.page;

import java.util.List;
import java.util.stream.Collectors;

public record PageResponse(
        String title,
        String content,
        List<Long> subpages,
        String breadcrumbs
) {
    public PageResponse(Page page) {
        this(page.getTitle(), page.getContent(), page.getSubPages()
                        .stream()
                        .map(Page::getId)
                        .collect(Collectors.toList())
                , page.getBreadcrumbs());
    }
}
