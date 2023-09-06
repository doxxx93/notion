package me.doxxx.notion.page.presentation;

import me.doxxx.notion.page.domain.Page;

import java.util.List;

public record PageResponse(
        String title,
        String content,
        List<Long> subpages,
        List<Long> breadcrumbs
) {
    public static PageResponse of(Page page, List<Long> breadcrumbs) {
        return new PageResponse(
                page.getTitle(),
                page.getContent(),
                page.getSubpages(),
                breadcrumbs
        );
    }
}
