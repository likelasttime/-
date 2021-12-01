# 📌CRUD 게시판  
> SpringBoot와 Spring Data JPA를 이용하여 웹 게시판 구현

![목록](https://user-images.githubusercontent.com/46569105/144171529-4bced918-61be-48a1-9fd9-759bff1d390e.png)
</br>

## 📚 ToC
1. [기술 스택](#-1-기술-스택)
2. [핵심 기능](#-2-핵심-기능)
3. [트러블 슈팅](#-3-트러블-슈팅)
4. [프로젝트를 통해 배운 점](#-4-프로젝트를-통해-배운-점)
</br>

## [🔝 ](#-toc)1. 기술 스택

|Spring Boot|Spring Boot 2.5.2로 개발했습니다.|
|:----------:|:----------------------------------|
|**Java**|11.0.9 버전을 사용했습니다.|
|**Spring Data JPA**|메소드 이름만으로 쿼리를 생성했습니다.|
|**MySQL**|영속적인 데이터 사용을 위해 RDBMS인 MySQL을 사용했습니다.|
|**JUnit5**|Given, When, Then 형식으로 테스트 코드를 작성했습니다.|
|**Thymeleaf**|server-side Java template engine입니다.|
|**HTML**|화면을 만들기 위해 사용했습니다.|
</br>
 
 ## [🔝 ](#-1-기술-스택)2. 핵심 기능
- [작성](#-2-1-게시글-작성)
- [조회 / 상세보기](#-2-2-전체-게시글-조회-및-상세보기)
- [수정](#-2-3-게시글-수정)
- [삭제](#-2-4-글-삭제) 
- [Pagination](#-2-5-pagination)
- [검색](#-2-6-검색)
- [조회수](#-2-7-조회수)
- [인증](#-2-8-인증)
</br>

### [🔝 ](#-2-핵심-기능)2-1. 게시글 작성
![글 작성](https://user-images.githubusercontent.com/46569105/144171559-5b56d50d-b584-406b-b2ae-664ee758aa7a.gif)

제목, 작성자, 내용을 입력합니다.  
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
```
글을 작성하면 등록일자, 최근 수정한 일자가 데이터베이스에 저장됩니다.  
createdDate 속성을 updatable-false로 해서 글을 수정하더라도 변하지 않게 했습니다.  
updatable-false를 넣지 않았을 때는 게시글 수정시 null이 되는 에러가 발생했었습니다.

</br>

### [🔝 ](#-2-1-게시글-작성)2-2. 전체 게시글 조회 및 상세보기
 
![조회](https://user-images.githubusercontent.com/46569105/144171585-e8c88fb6-8d4f-418b-a64e-7263314f848f.gif)

게시글 리스트에서 게시글 번호, 제목, 작성자, 작성일자, 조회수를 출력합니다.  
상단 좌측에는 총 게시글 수를 나타냈습니다.  
```java
 // 상세 게시판 조회
    @GetMapping("/post/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Post post = postService.findOne(id);
        postService.updateView(id);     // 조회수 증가
        model.addAttribute("post", post);
        return "post/detail";
    }
```
getView 메소드로 조회수를 가져오고 1을 더한 값을 멤버변수 view에 저장합니다.

</br>

### [🔝 ](#-2-2-전체-게시글-조회-및-상세보기)2-3. 게시글 수정

![수정](https://user-images.githubusercontent.com/46569105/144171589-026eb5db-b213-4c20-9fb9-a322940ce297.gif)

get 요청으로 받은 id로 게시글을 찾습니다.  
updateForm.html을 반환해서 해당 게시글을 수정하는 페이지를 출력합니다.  
JS로 수정 버튼을 클릭했을 때 메시지를 띄웁니다.  
수정 버튼을 누르면 put 요청이 들어옵니다.  
id 값을 이용해서 해당 게시글의 조회수를 찾아 저장합니다.  
이 작업을 하지 않으면, 글 수정시 조회수가 0으로 초기화됩니다.
게시글을 수정후 게시글 리스트 화면으로 이동합니다.

</br>

### [🔝 ](#-2-3-게시글-수정)2-4. 글 삭제

![삭제](https://user-images.githubusercontent.com/46569105/144171596-cb50092d-33df-4efc-b239-e9a287bb0bfa.gif)

상세보기에서 해당 글을 삭제할 수 있습니다.  
JS로 삭제하기 전에 메시지를 띄웁니다.
id를 기준으로 게시글을 삭제합니다.  

</br>

### [🔝 ](#-2-4-글-삭제)2-5. Pagination

![페이지네이션](https://user-images.githubusercontent.com/46569105/144171606-6613baee-3cad-4398-96b4-7613580e8e49.gif)

한 페이지에 최대 5개의 게시글을 나타냅니다.  
최근에 등록한 게시글 순으로 내림차순 정렬했습니다.  
이전, 다음 버튼으로 왼쪽 또는 오른쪽 이동이 가능합니다.   
맨 처음, 맨 끝에 있을 경우 각각 이전, 다음 버튼이 비활성화됩니다.

</br>

### [🔝 ](#-2-5-pagination)2-6. 검색

![검색](https://user-images.githubusercontent.com/46569105/128582027-6ab77af5-daa3-45e1-b909-c3e7b47dfd07.gif)

제목, 작성자, 내용을 대소문자 구분 없이 검색할 수 있습니다.  
전체 게시글을 조회하는 컨트롤러에서 검색어가 파라미터로 들어옵니다.  
검색한 내용들도 페이징네이션됩니다.  
```java
Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String content, String author, Pageable pageable);
```

</br>

### [🔝 ](#-2-6-검색)2-7. 조회수 

```java
@Modifying
@Query("UPDATE Post p SET view=view+1 WHERE p.id=:id")
int updateView(@Param("id") Long id);
```  
    
다음과 같은 update 쿼리를 작성했습니다.  
게시글 상세보기를 할 때만 조회수가 1씩 증가합니다.  
postService의 updateView 메소드에서 조회수를 증가시키도록 아래와 같이 리팩토링하였습니다.  
update 쿼리를 작성할 필요가 없다는 것을 알았습니다.  

```java
// 조회수 증가
public void updateView(Long id) {
    Post post=postRepository.findById(id).get();
    post.setView(post.getView()+1);
    postRepository.save(post);
    }
```

</br>

### [🔝 ](#-2-8-인증)2-8. 인증
Spring Security를 이용해 로그인, 로그아웃을 구현했습니다.  
비밀번호는 BCryptPasswordEncoder 클래스를 이용해 암호화되어 데이터베이스에 저장됩니다.  
Thymeleaf의 sec:authorize를 이용해서 표현식이 참일 때만 로그인과 회원가입 또는 로그아웃 버튼이 나타나게 했습니다.  

![회원가입](https://user-images.githubusercontent.com/46569105/144191046-2312ec75-65b8-4f1d-a01a-a1d0ea3c5fec.png)
회원가입  

![login](https://user-images.githubusercontent.com/46569105/144176501-79830692-7e20-4a7b-bf52-8e4618247acd.gif)
</br>
로그인

![logout](https://user-images.githubusercontent.com/46569105/144176504-ba7653f3-93f2-42b4-b808-d5e46a7d9d18.gif)
</br>
로그아웃  

</br>




## [🔝 ](#-2-핵심-기능)3. 트러블 슈팅
### 3-1. 게시글 수정 후 조회수 초기화 문제
게시글 상세보기를 하면 조회수가 정상적으로 증가했지만, 게시글을 수정하고 나면 조회수가 0으로 초기화되었습니다.  
디버그해보니 put 요청을 받았을 때 조회수가 0인 것을 알았습니다.  
그 이유에 대해서 생각해보니 put 요청을 받았을 때 게시글 객체 post는 사용자가 수정한 데이터 form이었습니다.  
게시글 상세보기에서 제목, 작성자, 내용만 화면에 출력했기 때문에 post로 데이터를 받았을 때 제목, 작성자, 내용만 가지고 있었습니다. 

```java
@PutMapping("/post/edit/{id}")
    public String update(Post post, @PathVariable("id") Long id) {
        int view=postService.findOne(id).getView();
        post.setView(view);   // 조회수 저장
        postService.join(post);
        return "redirect:/post";
    }
```
데이터베이스에 영속적으로 저장하기 전에 id로 해당 객체를 찾아 가져온 조회수를 저장했습니다.  

</br>

### 3-2. 게시글 수정을 put으로 처리하기
처음에 post로 요청을 받아 처리하는 @PostMapping을 사용했습니다.  
글을 수정할 때는 post보다 멱등성이 있는 put으로 요청을 받아야 한다는 알게 되었습니다.  
@PostMapping을 @PutMapping으로 바꾸기만 하면 될 줄 알았지만, 다음과 같은 에러가 발생했습니다.  
```
DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported]
```

application.properties에 @PutMapping, @DeleteMapping과 같은 annotation을 쓸 수 있게 다음 코드를 추가했습니다.
```
spring.mvc.hiddenmethod.filter.enabled=true
```
그래도 안 돼서 html 쪽 코드를 살펴봤습니다.  
숨겨진 입력 필드를 form에 추가했습니다.
```html
<input type="hidden" name="_method" value="put"/>
```

</br>

### 3-3. 연속적인 게시글 번호
삭제 기능을 테스트하다 보니 게시글 번호가 불연속적으로 출력됐습니다.  
삭제해도 1부터 n까지 연속적으로 번호가 출력되어야 한다고 생각했습니다. 
가장 먼저 한 생각은 삭제할 때마다 데이터베이스의 id 값들을 재조정해주는 것입니다.  
실제로 이렇게 처리하는 것일까? 하고 찾아보니 데이터베이스의 id 값을 재조정할 경우 문제점이 많다는 것을 알게 되었습니다.  
두 번째로 생각한 방법은 1부터 전체 게시글 수 n까지 출력하는 것이었습니다. 
Thymeleaf 문서를 찾아보니 th:each를 쓸 때 인덱스값도 뽑아낼 수 있었습니다.  
```html
<tr th:each="post, i : ${post}">
```

</br>

## [🔝 ](#-3-트러블-슈팅)4. 프로젝트를 통해 배운 점
### 4-1. 스프링 MVC 패턴
Model, View, Controller의 의미와 흐름을 이해했습니다.
Controller는 HTTP 요청을 처리합니다. 출력할 View 이름을 반환합니다.
Model은 View에서 출력할 데이터를 담고 있습니다. 
View는 출력될 화면입니다. 
Controller, Model, View 순으로 진행됩니다.
HTTP 메소드 GET, POST, PUT, DELETE의 차이를 알았습니다.
GET은 조회, POST는 작성, PUT은 수정, DELETE는 삭제할 때 사용합니다.
POST와 PUT이 비슷한 것 아닌가 해서 찾아보니 '멱등성'을 알게 되었습니다.
멱등성은 여러 번 같은 요청을 보내더라도 한 번만 요청받은 것과 같이 처리합니다.
POST는 멱등성이 없어서 수정 작업에 적합하지 않습니다.

### 4-2. Git 
프로젝트에 변경 사항이 생길 때 마다 Git을 사용해서 버전 관리를 하면서 Git을 사용하는 방법을 익혔습니다.
로컬 저장소와 원격 저장소의 소스 코드가 다르면 pull을 해와야 합니다.
add로 파일을 추가합니다. 
status로 Staged(commit으로 저장소에 기록할 상태)에 있는 파일을 확인합니다.
commit 메시지를 개조식으로 작성합니다. 본문을 작성하고 싶을 때는 Vim을 사용합니다.
push로 원격 저장소에 올립니다.

### 4-3. Spring Data JPA
메모리 기반의 repository를 만드는 것부터 시작해서 JPA, Spring Data JPA 순으로 구현해봤습니다.
단순히 Spring Data JPA부터 사용했더라면 데이터 접근 계층에 대한 기본을 알지 못하고 썼을 것입니다.
처음에는 JPA와 Spring Data JPA의 차이를 잘 몰랐었습니다. 
Spring Data JPA는 데이터 접근 계층을 개발할 때 인터페이스만 작성해도 개발할 수 있게 해주는 이점이 있습니다. 
이러한 이점 때문에 Spring Data JPA를 사용했습니다. 
JpaRepository를 상속받는 인터페이스를 생성하고, 메소드 이름으로 쿼리가 자동으로 생성되는 점이 매우 흥미로웠습니다.  

### 4-4. 테스트
JUnit5를 이용해 Given, When, Then 형식으로 테스트 코드를 작성하는 방법을 알았습니다. 테스트는 각각 독립적으로 실행 하기 위해 @AfterEach로 테스트마다 메모리 DB를 비웠습니다. 테스트 코드를 작성하니 서버를 실행하는 시간을 절약할 수 있어서 좋았습니다.  


