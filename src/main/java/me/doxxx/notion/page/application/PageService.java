package me.doxxx.notion.page.application;

import jakarta.annotation.PostConstruct;
import me.doxxx.notion.common.constants.CacheConstants;
import me.doxxx.notion.page.domain.Page;
import me.doxxx.notion.page.domain.PageRepository;
import me.doxxx.notion.page.presentation.PageResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Service
public class PageService {
    private final ApplicationContext applicationContext;
    private final PageRepository pageRepository;

    public PageService(ApplicationContext applicationContext, PageRepository pageRepository) {
        this.applicationContext = applicationContext;
        this.pageRepository = pageRepository;
    }

    private PageService getInstance() {
        return applicationContext.getBean(PageService.class);
    }

    /**
     * This method is used to initialize sample data for the application. It populates
     * a list of pages with random titles and content. Each page, except for the first one,
     * is assigned a random parent page from the existing pages list. The method also saves
     * each page using the savePage() method.
     */
    @PostConstruct
    private void initializeSampleData() {
        List<Page> pages = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            Page samplePage = new Page("Title " + i, "Content " + i);
            if (i > 1) {
                int parentIndex = random.nextInt(i - 1);
                pages.get(parentIndex).addSubPage((long) i);
                samplePage.setParentPageId((long) parentIndex + 1);
            }

            savePage(samplePage);
            pages.add(samplePage);
        }
    }

    /**
     * This method retrieves a page with the specified ID from the page repository. It also
     * retrieves the breadcrumbs for the page using the getBreadcrumbs() method. The method
     * returns a PageResponse object that contains the retrieved page and its breadcrumbs.
     *
     * @param pageId The ID of the page to retrieve.
     * @return A PageResponse object containing the retrieved page and its breadcrumbs.
     */
    public PageResponse getPageById(Long pageId) {
        Page page = pageRepository.findById(pageId);
        List<Long> breadcrumbs = this.getInstance().getBreadcrumbs(pageId);
        return PageResponse.of(page, breadcrumbs);
    }

    /**
     * This method retrieves the breadcrumbs for a page with the specified ID. The method uses a
     * cache to store the breadcrumbs for faster retrieval in subsequent calls. If the breadcrumbs
     * are not found in the cache, the method retrieves them from the page repository.
     *
     * @param pageId The ID of the page for which to retrieve the breadcrumbs.
     * @return A list of Long values representing the breadcrumbs for the specified page.
     */
    @Cacheable(value = CacheConstants.BREADCRUMBS_CACHE_NAME, key = "#pageId")
    public List<Long> getBreadcrumbs(Long pageId) {
        Page page = pageRepository.findById(pageId);

        LinkedList<Long> breadcrumbsLinkedList = new LinkedList<>();
        breadcrumbsLinkedList.addFirst(pageId);
        while (page.getParentPageId() != null) {
            Long parentPageId = page.getParentPageId();
            breadcrumbsLinkedList.addFirst(parentPageId);
            page = pageRepository.findById(parentPageId);
        }

        return new ArrayList<>(breadcrumbsLinkedList);
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }
}
