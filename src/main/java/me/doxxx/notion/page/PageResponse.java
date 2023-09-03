package me.doxxx.notion.page;

import java.util.List;
import java.util.stream.Collectors;

public record PageResponse(
        String title,
        String content,
        List<PageResponse> subpages,
        String breadcrumbs
) {
    public PageResponse(Page page) {
        this(page.getTitle(), page.getContent(), page.getSubPages()
                .stream()
                .map(PageResponse::new)
                .collect(Collectors.toList()), page.getBreadcrumbs());
    }
}
