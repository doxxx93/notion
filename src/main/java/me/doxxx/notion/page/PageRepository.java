package me.doxxx.notion.page;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PageRepository {
    private final Map<Long, Page> persistence = new ConcurrentHashMap<>();
    private Long sequence = 0L;

    public void save(Page page) {
        page.assignId(++sequence);
        persistence.put(page.getId(), page);
    }

    public Page findById(long pageId) {
        return persistence.get(pageId);
    }
}
