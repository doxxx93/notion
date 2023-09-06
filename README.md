## 목표

노션과 유사한 간단한 페이지 관리 API를 구현해주세요. 각 페이지는 제목, 컨텐츠, 그리고 서브 페이지를 가질 수 있습니다. 또한, 특정 페이지에 대한 브로드 크럼스(Breadcrumbs) 정보도 반환해야 합니다.

## 요구사항

**페이지 정보 조회 API**: 특정 페이지의 정보를 조회할 수 있는 API를 구현하세요.

- 입력: 페이지 ID
- 출력: 페이지 제목, 컨텐츠, 서브 페이지 리스트, **브로드 크럼스 ( 페이지 1 > 페이지 3 > 페이지 5)**
- 컨텐츠 내에서 서브페이지 위치 고려 X

## 구현 내용

### 클래스 다이어그램

![image.png](image.png)

단순한 MVC 패턴으로 구현하였다.

### 비즈니스 로직

- 페이지 생성과 조회

### Page 객체

1. **Composite 패턴**

각각의 페이지는 트리 구조를 가지고 있기 때문에 composite 패턴을 통해 구현하는 것을 먼저 고려했다.

그림으로 이를 표현하면 이런 모양을 띄게 된다.

![img.png](img.png)

1. Page interface는 서브 페이지가 없는 SinglePage와 서브 페이지가 있는 CompositePage가 공통으로 가지고 있는 메서드를 정의한다.
2. BasePage는 Page interface의 구현체들인 SinglePage와 CompositePage가 공통으로 가지고 있는 필드와 메서드를 정의한다.
3. SinglePage, CompositePage는 BasePage를 상속받아 필요한 메서드를 구현한다.

- BasePage는 기본적으로 subPages 필드를 가지고 있는데, 기본적으로 null로 초기화되어 있다.
- SinglePage는 이를 그대로 사용하지만, CompositePage는 이 필드를 ArrayList로 초기화한다.

이렇게 구현하게 되면, 부모가 SinglePage인지 CompositePage인지에 따라서 상황에 따른 로직이 바뀌게 된다.

- 새로운 페이지를 생성하는 경우, parentPage가 SinglePage라면 parentPage페이지를 CompositePage로 변경하고, parent 페이지의 subPages 필드에 자신을 추가한다.

- 페이지를 삭제할 때, parent의 subPages 리스트의 사이즈가 0이 된다면, parentPage를 SinglePage로 변경한다.

SinglePage나 CompositePage를 구분하여 관리할 수 있게 되지만, 로직의 복잡성이 증가하게 된다.

2. **현재 구조**

위의 구조가 아닌, Page 단일 클래스에서 모든 로직을 처리하도록 구현하였다.

```java

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
```

### 부모와 자식간 관계

기존에는 서브 페이지와 부모 페이지를 객체로 갖고 있게 구현했지만, 현재는 서브 페이지와 부모 페이지의 ID를 갖고 있게 구현했다.

기존 방식은 시스템의 규모가 커질수록 객체간 관계가 복잡해질 가능성이 있고 또한 어플리케이션 내에서 객체간의 참조가 많아질 수록 메모리 사용량이 증가할 것이라고 생각했다.

메모리의 효율성과 확장성을 고려하여 현재 방식으로 구현하게 되었다.

브레드 크럼스나 서브 페이지의 조회에는 ID만 필요로 하고, 상황에 따라 다른 로직이 필요한 경우엔 결국 ID를 통해 조회를 해야하기 때문에 이렇게 구현하게 되었다.

삭제, 수정 로직이 추가가 되더라도 ID를 통해 조회를 하고, 해당 객체를 수정나 삭제하면 되기 때문에 큰 문제가 되지 않을 것이라고 생각했다.

### 캐싱

페이지를 조회할 때, 페이지의 ID를 통해 페이지를 조회하고, 브레드 크럼스를 생성하는 로직을 가지고 있다.

페이지를 조회할 때마다, 브레드 크럼스를 생성하는 것은 비효율적이고 캐싱을 통해 이를 개선할 수 있다고 생각했다.

페이지의 이동이나 삭제가 발생할 때는 캐싱된 데이터를 서브페이지에 있는 ID를 재귀적으로 조회하여 캐싱된 데이터를 삭제하고, 새로운 데이터를 캐싱하면 된다.

현재 캐싱의 대상은 해당 페이지의 브레드 크럼스이다.

```java
    public PageResponse getPageById(Long pageId) {
        Page page = pageRepository.findById(pageId);
        List<Long> breadcrumbs = this.getInstance().getBreadcrumbs(pageId);
        return PageResponse.of(page, breadcrumbs);
    }

    // public final class CacheConstants 
    // public static final String BREADCRUMBS_CACHE_NAME = "breadcrumbs";
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
```

pageId를 키로, 브레드 크럼스를 값으로 가지는 캐시를 생성한다. 향후 과제로 캐시의 유효기간을 설정하는 것을 고려해볼 수 있다.

따로 캐시 구성을 위한 설정을 하지 않았기 때문에, 기본적으로 캐시는 메모리에 저장된다.

참고한 자료에는 https://junior-datalist.tistory.com/329 과 https://livenow14.tistory.com/56 가 있다.

### self-invocation 이슈

현재 PageService의 getPageById 메서드에서는 getBreadcrumbs 메서드를 호출하고 있다.

getPageById의 리턴은 PageResponse인데, 캐싱의 대상은 브래드 크럼스로 고려했기 때문에, 이와 같이 구현했다.

내부에서 호출되는 메서드를 캐싱하려고 하면, self-invocation 이슈가 발생하게 된다.

self-invocation의 원인은 지금과 같이 한 클래스의 메서드에서 같은 클래스의 다른 메서드를 직접 호출할 경우, 이 호출은 Spring AOP 프록시를 통해 실행되지 않고, 직접 실행된다.

이를 위해서 2가지 해결 방법을 찾을 수 있었다.

1. @Resource 어노테이션
2. ApplicationContext 객체를 통한 Bean 주입

1의 경우 PageService 클래스의 순환 참조 발생으로 인해 빈 생성에 실패하였다.

결국 2의 방법으로 이를 해결할 수 있었다.

`getPageById`메서드에서 확인할 수 있듯이 `getBreadcrumbs`메서드를 호출할 때, `PageService`의 프록시 인스턴스를 통해 호출하도록 구현 된 것을 알 수 있다.

`List<Long> breadcrumbs = this.getInstance().getBreadcrumbs(pageId);`

### 향후 과제

- 페이징 처리

현재 로직은 모든 페이지를 조회하는 것이기 때문에, 규모가 커질 경우에는 페이징 처리가 필요하다고 생각한다.

- 트랜잭션 처리

현재는 트랜잭션 처리를 하지 않고 있다. 트랜잭션 처리를 통해 데이터의 일관성을 보장할 수 있도록 구현하는 것이 필요하다고 생각한다.

- 데이터베이스

현재는 단순하게 ConcurrentHashMap을 사용하고 있다.

이러한 트리 구조의 데이터를 관리에 용이한 NoSQL이나 그래프 데이터베이스를 사용하는 것도 좋을 것 같다.

[Notion Interview Guide - Tech Roles](https://www.notion.so/Interview-Guides-Technical-Roles-50e339f3fa8a4c8a8a4c1cd7a7565110)

[Notion Data Model](https://www.notion.so/blog/data-model-behind-notion)

둘을 참고해보면, 유저 정보에는 RDBMS를 사용하고, 페이지 정보에는 NoSQL을 사용하고 있는 것 같다.

### 결과 정보

- 가장 상위 페이지

```json
{
  "title": "Title 1",
  "content": "Content 1",
  "subpages": [
    2,
    3
  ],
  "breadcrumbs": [
    1
  ]
}
```

- 하위 페이지들

```json
{
  "title": "Title 3",
  "content": "Content 3",
  "subpages": [
    4
  ],
  "breadcrumbs": [
    1,
    3
  ]
}
```

```json
{
  "title": "Title 5",
  "content": "Content 5",
  "subpages": [
    6,
    7,
    8,
    9
  ],
  "breadcrumbs": [
    1,
    3,
    4,
    5
  ]
}
```

```json
{
  "title": "Title 9",
  "content": "Content 9",
  "subpages": [
  ],
  "breadcrumbs": [
    1,
    3,
    4,
    5,
    9
  ]
}
```

