package me.doxxx.notion.page;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private final long pageId;
    private final String title;
    private final String content;
    private final List<Page> subPages;
    private Page parentPage;

    public Page(long pageId, String title, String content) {
        this.pageId = pageId;
        this.title = title;
        this.content = content;
        this.subPages = new ArrayList<>();
    }

    public long getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Page> getSubPages() {
        return subPages;
    }

    public String getBreadcrumbs() {
        if (parentPage == null) {
            return title;
        } else {
            return parentPage.getBreadcrumbs() + " > " + title;
        }
    }

    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    public boolean isComposite() {
        return !subPages.isEmpty();
    }

    public void addSubPage(Page page) {
        subPages.add(page);
    }

    public void removeSubPage(Page page) {
        subPages.remove(page);
    }
}
