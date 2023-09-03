package me.doxxx.notion.page;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class PageService {
    private Map<Long, Page> pageMap;

    public PageService() {
        // 초기화
        pageMap = new HashMap<>();
    }

    public PageResponse getPageById(long pageId) {
        Page page = pageMap.get(pageId);
        return new PageResponse(page);
    }

    // 페이지를 저장하는 메서드 (예시 데이터로 초기화)
    public void savePage(Page page) {
        pageMap.put(page.getPageId(), page);
    }

    // 예시 데이터 생성 메서드
    @PostConstruct
    private void initializeSampleData() {
        Page samplePage1 = new Page(1L, "Title 1", "Content 1");
        Page samplePage2 = new Page(2L, "Title 2", "Content 2");
        Page samplePage3 = new Page(3L, "Title 3", "Content 3");

        // 부모 페이지 설정
        samplePage2.setParentPage(samplePage1);
        samplePage3.setParentPage(samplePage2);

        // 하위 페이지 추가
        samplePage1.getSubPages().add(samplePage2);
        samplePage2.getSubPages().add(samplePage3);

        // Map 데이터 저장
        savePage(samplePage1);
        savePage(samplePage2);
        savePage(samplePage3);
    }
}
