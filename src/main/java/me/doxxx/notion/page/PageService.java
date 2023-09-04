package me.doxxx.notion.page;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService() {
        pageRepository = new PageRepository();
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
                pages.get(parentIndex).addSubPage(samplePage);
            }

            savePage(samplePage);
            pages.add(samplePage);
        }
    }

    public PageResponse getPageById(long pageId) {
        return new PageResponse(pageRepository.findById(pageId));
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }
}
