package me.doxxx.notion.page;

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
    private List<Long> subpages;
    private Long parentPageId;

    public Page(String title, String content) {
        this.title = title;
        this.content = content;
        this.subpages = new ArrayList<>();
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void addSubPage(Long subPageID) {
        subpages.add(subPageID);
    }

    public void setParentPageId(Long parentPageId) {
        this.parentPageId = parentPageId;
    }
}
