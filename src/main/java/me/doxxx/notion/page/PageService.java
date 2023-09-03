package me.doxxx.notion.page;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService() {
        pageRepository = new PageRepository();
    }

    /**
     * Initializes sample data for the application.
     */
    @PostConstruct
    private void initializeSampleData() {
        Page samplePage1 = new Page("Title 1", "Content 1");
        savePage(samplePage1);

        Page samplePage2 = new Page("Title 2", "Content 2");
        samplePage1.addSubPage(samplePage2);
        savePage(samplePage2);

        Page samplePage3 = new Page("Title 3", "Content 3");
        samplePage2.addSubPage(samplePage3);
        savePage(samplePage3);

        Page samplePage4 = new Page("Title 4", "Content 4");
        samplePage3.addSubPage(samplePage4);
        savePage(samplePage4);

        Page samplePage5 = new Page("Title 5", "Content 5");
        samplePage4.addSubPage(samplePage5);
        savePage(samplePage5);

        Page samplePage6 = new Page("Title 6", "Content 6");
        samplePage4.addSubPage(samplePage6);
        savePage(samplePage6);

        Page samplePage7 = new Page("Title 7", "Content 7");
        samplePage3.addSubPage(samplePage7);
        savePage(samplePage7);

        Page samplePage8 = new Page("Title 8", "Content 8");
        samplePage7.addSubPage(samplePage8);
        savePage(samplePage8);

        Page samplePage9 = new Page("Title 9", "Content 9");
        samplePage8.addSubPage(samplePage9);
        savePage(samplePage9);

        Page samplePage10 = new Page("Title 10", "Content 10");
        samplePage2.addSubPage(samplePage10);
        savePage(samplePage10);

        Page samplePage11 = new Page("Title 11", "Content 11");
        samplePage2.addSubPage(samplePage11);
        savePage(samplePage11);
    }

    public PageResponse getPageById(long pageId) {
        return new PageResponse(pageRepository.findById(pageId));
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }
}
