package me.doxxx.notion.page;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService() {
        pageRepository = new PageRepository();
    }

    // 예시 데이터 생성 메서드
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
    }

    public PageResponse getPageById(long pageId) {
        return new PageResponse(pageRepository.findById(pageId));
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }
}
