package me.doxxx.notion.page;

import java.util.HashMap;
import java.util.Map;

public class PageRepository {

    private final Map<Long, Page> persistence = new HashMap<>();
    private Long sequence = 0L;

    public void save(Page page) {
        page.assignId(++sequence);
        persistence.put(page.getId(), page);
    }

    public Page findById(long pageId) {
        return persistence.get(pageId);
    }
}
