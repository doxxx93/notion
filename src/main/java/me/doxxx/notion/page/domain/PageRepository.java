package me.doxxx.notion.page.domain;

public interface PageRepository {
    void save(Page page);
    Page findById(long pageId);
}
