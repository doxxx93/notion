package me.doxxx.notion.page.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Page class represents a web page and its properties.
 */
@Getter
public class Page {
    private Long id;
    private String title;
    private String content;
    private List<Long> subPageIds;
    private Long parentPageId;

    public Page(String title, String content) {
        this.title = title;
        this.content = content;
        this.subPageIds = new ArrayList<>();
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void addSubPage(Long subPageID) {
        subPageIds.add(subPageID);
    }

    public void assignParentPageId(Long parentPageId) {
        this.parentPageId = parentPageId;
    }
}
