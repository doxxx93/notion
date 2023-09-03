## 목표

노션과 유사한 간단한 페이지 관리 API를 구현해주세요. 각 페이지는 제목, 컨텐츠, 그리고 서브 페이지를 가질 수 있습니다. 또한, 특정 페이지에 대한 브로드 크럼스(Breadcrumbs) 정보도 반환해야 합니다.

## 요구사항

**페이지 정보 조회 API**: 특정 페이지의 정보를 조회할 수 있는 API를 구현하세요.

- 입력: 페이지 ID
- 출력: 페이지 제목, 컨텐츠, 서브 페이지 리스트, **브로드 크럼스 ( 페이지 1 > 페이지 3 > 페이지 5)**
- 컨텐츠 내에서 서브페이지 위치 고려 X

## 제출 방법 (팀단위)

- **과제 내용을 노션 혹은 github 등에 문서화해서 제출해주세요.**
- **필수**
    - 테이블 구조
    - 비지니스 로직 (Raw 쿼리로 구현 → ORM (X))
    - 결과 정보

        ```json
        {
                "pageId" : 1,
                "title" : 1,
                "subPages" : [],
                "breadcrumbs" : ["A", "B", "C"]// 혹은 "breadcrumbs" : "A / B / C"
        }
        ```

- 제출하신 과제에 대해서 설명해주세요. (”왜 이 구조가 최선인지?” 등)


## 구현 방법

### Composite 패턴

composite 패턴을 통해 구현하는 것을 먼저 고려했다.

composite 패턴을 고려한 이유는 각각의 페이지가 트리 구조를 가지고 있기 때문이다.

그림으로 이를 표현하면 이런 모양을 띄게 된다.

![img.png](img.png)

1. Page interface는 서브 페이지가 없는 SinglePage와 서브 페이지가 있는 CompositePage가 공통으로 가지고 있는 메서드를 정의한다.
2. BasePage는 Page interface의 구현체들인 SinglePage와 CompositePage가 공통으로 가지고 있는 필드와 메서드를 정의한다.
3. SinglePage, CompositePage는 BasePage를 상속받아 필요한 메서드를 구현한다.

BasePage는 기본적으로 subPages 필드를 가지고 있는데, 기본적으로 null로 초기화되어 있다.

SinglePage는 이를 그대로 사용하지만, CompositePage는 이 필드를 ArrayList로 초기화한다.

부모가 SinglePage인지 CompositePage인지에 따라서 상황에 따른 로직이 바뀌게 된다.

- 새로운 페이지를 생성하는 경우, parentPage가 SinglePage라면 parentPage페이지를 CompositePage로 변경하고, parent 페이지의 subPages 필드에 자신을 추가한다.

- 페이지를 삭제할 때, parent의 subPages 리스트의 사이즈가 0이 된다면, parentPage를 SinglePage로 변경한다.

이렇게 되면 물론 SinglePage나 CompositePage를 구분하여 관리할 수 있게 되지만, 로직의 복잡성이 증가하게 된다.

### 현재 구조

위의 구조가 아닌, Page 단일 클래스에서 모든 로직을 처리하도록 구현하였다.

subpage의 유무 구분을 없앴다.
