package me.doxxx.notion.page;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Page class represents a web page and its properties.
 */
public class Page {
    @Getter
    private long id;
    @Getter
    private final String title;
    @Getter
    private final String content;
    @Getter
    private final List<Page> subPages;
    private Page parentPage;

    public Page(String title, String content) {
        this.title = title;
        this.content = content;
        this.subPages = new ArrayList<>();
    }

    /**
     * Assigns the given ID to the object.
     *
     * @param id The ID to be assigned
     */
    public void assignId(final Long id) {
        this.id = id;
    }

    /**
     * Retrieves the breadcrumbs of the object.
     *
     * @return The breadcrumbs of the object as a String
     */
    public String getBreadcrumbs() {
        if (parentPage == null) {
            return String.valueOf(id);
        }
        return parentPage.getBreadcrumbs() + "/" + id;
    }

    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    public boolean isComposite() {
        return !subPages.isEmpty();
    }

    public void addSubPage(Page page) {
        subPages.add(page);
        page.setParentPage(this);
    }

    public void removeSubPage(Page page) {
        subPages.remove(page);
    }
}
