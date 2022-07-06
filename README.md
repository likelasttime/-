# 📌CRUD 게시판  
> SpringBoot와 Spring Data JPA를 이용하여 웹 게시판 구현

![first](https://user-images.githubusercontent.com/46569105/144240175-0aab13f9-3425-44c8-ad7c-06e66dc6e69e.png)
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
- [댓글](#-2-9-댓글)
</br>

### [🔝 ](#-2-핵심-기능)2-1. 게시글 작성
![작성](https://user-images.githubusercontent.com/46569105/144240182-355bbc64-c882-4f93-9a9d-788bb4b1ec43.gif)

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
 
![게시글상세보기](https://user-images.githubusercontent.com/46569105/173226386-3a29a74c-c081-4e2e-a35b-d0329a5c14fb.gif)  

게시글 리스트에서 게시글 번호, 제목, 작성자, 작성일자, 조회수를 출력합니다.  
상단 좌측에는 총 게시글 수를 나타냈습니다.  

</br>

### [🔝 ](#-2-2-전체-게시글-조회-및-상세보기)2-3. 게시글 수정

![게시글 수정](https://user-images.githubusercontent.com/46569105/173226389-ab0ef6b7-5f06-4e2a-bb47-628b9b48b86b.gif)  

JS로 수정 버튼을 클릭했을 때 메시지를 띄웁니다.  
게시글을 수정후 게시글 리스트 화면으로 이동합니다.

</br>

### [🔝 ](#-2-3-게시글-수정)2-4. 글 삭제

![게시글 삭제](https://user-images.githubusercontent.com/46569105/173226392-05329c72-aa1d-489c-a51a-ea16e81109ee.gif)  

상세보기에서 해당 글을 삭제할 수 있습니다.  
id를 기준으로 게시글을 삭제합니다.  

</br>

### [🔝 ](#-2-4-글-삭제)2-5. Pagination

![페이징네이션](https://user-images.githubusercontent.com/46569105/144242014-c0a69817-b38e-426a-ae9c-eddec62d250c.gif)

한 페이지에 최대 5개의 게시글을 나타냅니다.  
최근에 등록한 게시글 순으로 내림차순 정렬했습니다.  
이전, 다음 버튼으로 왼쪽 또는 오른쪽 이동이 가능합니다.   
맨 처음, 맨 끝에 있을 경우 각각 이전, 다음 버튼이 비활성화됩니다.

</br>

### [🔝 ](#-2-5-pagination)2-6. 검색

![검색](https://user-images.githubusercontent.com/46569105/144238073-0f3656e4-c749-4fea-a4ab-e5e02270b55a.gif)

제목, 작성자, 내용을 대소문자 구분 없이 검색할 수 있습니다.  
전체 게시글을 조회하는 컨트롤러에서 검색어가 파라미터로 들어옵니다.  
검색한 내용들도 페이징네이션됩니다.  
```java
Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String content, String author, Pageable pageable);
```

</br>

### [🔝 ](#-2-6-검색)2-7. 조회수 

게시글 상세보기를 할 때만 조회수가 1씩 증가합니다.  

```java
// 조회수 증가
    public void updateView(Long id) {
        Post post=postRepository.findById(id).get();
        post.update(post.getTitle(), post.getContent(), post.getView()+1);
    }

```

</br>

### [🔝 ](#-2-7-조회수)2-8. 인증
Spring Security를 이용해 로그인, 로그아웃을 구현했습니다.  
비밀번호는 PasswordEncoder 클래스를 이용해 암호화되어 데이터베이스에 저장됩니다.  
Thymeleaf의 sec:authorize를 이용해서 표현식이 참일 때만 로그인과 회원가입 또는 로그아웃 버튼이 나타나게 했습니다. 
</br>
![login](https://user-images.githubusercontent.com/46569105/159108736-221c1824-2e31-4630-9eb1-a1770a864b5e.gif)
</br>
로그인

![logout](https://user-images.githubusercontent.com/46569105/159108738-5d82dfb3-f8cf-4ba6-87f3-c99cd159ad20.gif)
</br>
로그아웃  


![id](https://user-images.githubusercontent.com/46569105/159108778-a2576ace-412f-48ad-96cc-6b16ab364631.gif)
</br>
아이디 찾기  
```java
Optional<User> findByNameAndEmailAndPhone(String name, String email, String phone);
```
이름, 메일, 연락처가 일치하는 사용자를 찾습니다.  
아이디를 로그인 페이지에 자동으로 채워줍니다.  
찾는 사용자가 없으면 "가입된 아이디가 없습니다" 메시지를 출력합니다.  

![비번form](https://user-images.githubusercontent.com/46569105/159108977-5a2d78b5-6c66-422b-8f39-b7bfc7898b1b.png)
비밀번호 찾기  
```java
boolean existsByUsernameAndEmail(String username, String mail);
```
아이디, 메일을 이용해 사용자를 찾습니다.  
가입된 메일로 임시 비밀번호를 발송합니다.  
데이터베이스에 저장된 비밀번호는 임시 비밀번호로 변경됩니다.  
![메일](https://user-images.githubusercontent.com/46569105/159108979-85f7941c-184d-49d5-aad8-9ed3093559cd.png)


![회원가입](https://user-images.githubusercontent.com/46569105/149871638-80fc7ad3-18e4-479b-8bbf-dafcf76fda68.png)
form 태그의 onsubmit 속성을 이용해 모든 유효성 검사를 통과해야 서버에 전송합니다.  
프런트엔드에서만 유효성 검사를 하면 개발자 도구로 쉽게 뚫을 수 있습니다.  
그리고, 프런트엔드에서의 유효성 검사는 UI/UX 역할이기 때문에 백엔드에서도 유효성 검사를 합니다.  
아이디, 이메일, 연락처는 ajax를 이용해 서버에 전송하고 중복 검사를 합니다.  
```java
@PostMapping("/user/idCheck")       // 아이디 중복 체크
    @ResponseBody
    public boolean idCheck(@RequestParam("id") String id){
        LOG.info("userIdCheck 진입");
        LOG.info("전달받은 id:"+id);
        boolean flag=userService.userIdCheck(id);
        LOG.info("확인 결과:" + flag);
        return flag;
    }

    @PostMapping("/user/emailCheck")        // 이메일 중복 체크
    @ResponseBody
    public boolean emailCheck(@RequestParam("email") String email){
        LOG.info("userEmailCheck 진입");
        LOG.info("전달받은 email:"+email);
        boolean flag=userService.userEmailCheck(email);
        LOG.info("확인 결과:"+flag);
        return flag;
    }

    @PostMapping("/user/phoneCheck")    // 전화번호 중복 검사
    @ResponseBody
    public boolean phoneCheck(@RequestParam("phone") String phone){
        LOG.info("userPhoneCheck 진입");
        LOG.info("전달받은 번호:"+phone);
        boolean flag=userService.userPhoneCheck(phone);
        LOG.info("확인 결과:"+flag);
        return flag;
    }
```
```java
    // 중복 아이디 체크
    public boolean userIdCheck(String user_id) {return userRepository.existsByUsername(user_id);}
    
    // 중복 이메일 체크
    public boolean userEmailCheck(String email){return userRepository.existsByEmail(email);}

    // 중복 휴대폰 번호 체크
    public boolean userPhoneCheck(String phone){return userRepository.existsByPhone(phone);}
```

서버에서는 두 가지 방법으로 유효성 검사를 합니다.  
1. UserRequestDto에서 validation annotation을 이용해 아래와 같이 유효성 검사를 합니다.  
도메인의 복잡성을 줄이기 위해 entity에서 validation을 하지 않고, UserRequestDto에서 했습니다.   
데이터베이스에 저장할 때 toEntity 메소드를 이용해 UserRequestDto를 User로 변환합니다.  
```java
@NotBlank(message="이름을 작성해주세요.")
    @Pattern(regexp="^[가-힣]{2,5}$", message="2~5자의 한글만 입력가능합니다.")
    private String name;

    @NotBlank(message="전화번호를 작성해주세요.")
    @Pattern(regexp="^[0-9]{10,11}$", message="10~11자리의 숫자만 입력가능합니다.")
    private String phone;

    @NotBlank(message="메일을 작성해주세요.")
    @Email(message="메일의 양식을 지켜주세요.")
    @Pattern(regexp="^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$"
    , message="예시) 메일명@gmail.com")
    private String email;

    @NotBlank(message="아이디를 입력해주세요.")
    @Pattern(regexp="^(?=.*[0-9]+)[a-zA-Z][a-zA-Z0-9]{5,10}$",
            message="소문자/대문자, 숫자가 포함된 5~10자의 아이디여야 합니다.")
    private String username;

    @NotBlank(message="비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}",
    message="소문자, 숫자, 특수문자가 모두 포함된 8~20자의 비밀번호여야 합니다.")
    private String password;
  ```

2. Validator를 구현한 UserValidator에서 이메일/아이디/연락처 중복 검사, 아이디의 첫 문자 검사를 합니다.  
```java
@Override
    public void validate(Object object, Errors errors){
        UserRequestDto userRequestDto=(UserRequestDto) object;
        if(userRepository.existsByEmail(userRequestDto.getEmail())){    // 이메일 중복
            errors.rejectValue("email", "invalid.email"
                    , new Object[]{userRequestDto.getEmail()}
                    , "이미 사용중인 이메일입니다.");
        }
        if(!Pattern.matches(pattern, userRequestDto.getUsername())){     // 첫 문자가 영문자가 아니면
            errors.rejectValue("username", "invalid.username"
                    , new Object[]{userRequestDto.getUsername()}
                    , "첫 문자는 영문자야 합니다.");
        }
        if(userRepository.existsByUsername(userRequestDto.getUsername())){    // 아이디 중복
            errors.rejectValue("username", "invalid.username"
            , new Object[]{userRequestDto.getUsername()}
            , "이미 사용중인 아이디입니다.");
        }
        if(userRepository.existsByPhone(userRequestDto.getPhone())){  // 연략처 중복
            errors.rejectValue("phone", "invalid.phone"
            , new Object[]{userRequestDto.getPhone()}
            , "이미 가입된 번호입니다.");
        }
    }
```

</br>

## [🔝 ](#-2-8댓글)2-9. 댓글  
댓글 등록, 수정은 @ResponseBody를 사용해 메소드가 리턴하는 오브젝트가 메시지 컨버터를 통해 HTTP 응답의 메시지 본문으로 전환됩니다.  
#### 댓글 등록
![댓글 등록](https://user-images.githubusercontent.com/46569105/173214260-321a00d4-1cd4-42c4-b969-119289d9f557.gif)  
Ajax를 이용해 비동기식으로 댓글을 등록합니다.  
댓글이 Blank하거나 최대 길이 10,000자를 넘었는지 백엔드와 프런트엔드에서 유효성 검사를 합니다.  
<br/>

댓글 수정과 삭제는 Modal을 띄워서 합니다.  
댓글을 쓴 회원에게만 Modal을 띄우는 아이콘이 보입니다.  
<br/>

#### 댓글 수정  
![댓글 수정](https://user-images.githubusercontent.com/46569105/173214263-2999b79b-655f-4334-9bbb-6596fbf5b20c.gif)  
Entity의 변경사항을 데이터베이스에 자동으로 반영하는 기능 Dirty Checking을 이용했습니다.  
댓글을 수정하기 전 알림창을 띄우고 true를 리턴받으면 form을 submit합니다.  
<br/>

#### 댓글 삭제  
![댓글 삭제](https://user-images.githubusercontent.com/46569105/173214266-27b8e5db-1463-4432-bcea-027b05ae2fe5.gif)  
댓글을 삭제하기 전 알림창을 띄우고 true를 리턴받으면 POST 방식으로 요청을 보냅니다.  
</br>

## [🔝 ](#-2-핵심-기능)3. 트러블 슈팅
### 3-1. Spring Security 때문에 컨트롤러가 동작하지 않음
ajax를 이용해서 아이디 중복 확인을 하는 기능을 만들다가 문제가 생겼습니다.  
중복되지 않은 아이디를 입력해도 컨트롤러가 동작하지 않는 것 같다는 생각을 했습니다.  
서버에서 받은 값을 프런트에서 출력하면 엉뚱하게 전체 회원가입 HTML 소스 코드가 나왔습니다.  
아무리 자바스크립트 문법, 컨트롤러를 봐도 문제가 없었습니다.  
문제의 원인은 Spring Security에 있었습니다.  
Spring Security 때문에 post 방식으로 요청한 '/user/idCheck'에 못 가고, 회원가입 페이지를 리턴하는 컨트롤러로 갔습니다.  
WebSecurityConfigurerAdapter를 상속받은 WebSecurityConfig에서 인증 없이도 접근하도록 '/user/idCheck'를 추가해 해결했습니다. 
이메일, 연락처 중복 검사도 같은 문제가 발생해서 같은 방법으로 해결했습니다.  

```java
@Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/user/joinForm", "/user/idCheck", "/user/join", "/user/emailCheck", "/user/phoneCheck").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/user/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }
```

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

### 3-4. 깃허브 커밋 reset 후 변경
한번은 파일을 실수로 빼먹고 원격 repository에 커밋을 올린 적이 있습니다.  
```
git reset --hard HEAD~1
git push -f origin main
```
위의 코드로 커밋을 하기 전으로 돌아갔습니다.  
하지만, 로컬에서도 작업한 코드가 사라졌습니다.  
IntelliJ의 Local History를 통해 커밋할 파일들을 복원했습니다.  

```
git reflog
git reset --hard commit_id
```
삭제된 커밋 아이디를 알아내고, 다시 삭제했던 커밋으로 돌아왔습니다.  
파일을 추가 후 정상적으로 커밋을 할 수 있었습니다.  
reset을 해도 완전히 삭제되는 게 아니라 HEAD가 가리키는 커밋이 변하는 것을 배웠습니다.  

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

### 4-2. 스프링 DI
필드 주입을 생성자 주입으로 변경했습니다.  
불편, 필수 의존 관계에 생성자 주입을 주로 쓴다는 것을 알았습니다.  
필드 주입은 외부에서 변경이 불가능해서 테스트 하기 힘들다는 단점이 있습니다.  
private final 멤버변수를 lombok의 @RequiredArgsConstructor로 생성자 주입을 했습니다.  

### 4-3. Git 
프로젝트에 변경 사항이 생길 때 마다 Git을 사용해서 버전 관리를 하면서 Git을 사용하는 방법을 익혔습니다.  
로컬 저장소와 원격 저장소의 소스 코드가 다르면 pull을 해와야 합니다.  
add로 파일을 추가합니다. 
status로 Staged(commit으로 저장소에 기록할 상태)에 있는 파일을 확인합니다.  
commit 메시지를 개조식으로 작성합니다. 본문을 작성하고 싶을 때는 Vim을 사용합니다.  
push로 원격 저장소에 올립니다.  
reset으로 HEAD가 가리키는 커밋을 지정해서 커밋을 삭제하는 방법을 알았습니다. 

### 4-4. Spring Data JPA
메모리 기반의 repository를 만드는 것부터 시작해서 JPA, Spring Data JPA 순으로 구현해봤습니다.  
단순히 Spring Data JPA부터 사용했더라면 데이터 접근 계층에 대한 기본을 알지 못하고 썼을 것입니다.  
처음에는 JPA와 Spring Data JPA의 차이를 잘 몰랐었습니다.  
Spring Data JPA는 데이터 접근 계층을 개발할 때 인터페이스만 작성해도 개발할 수 있게 해주는 이점이 있습니다.   
이러한 이점 때문에 Spring Data JPA를 사용했습니다.  
JpaRepository를 상속받는 인터페이스를 생성하고, 메소드 이름으로 쿼리가 자동으로 생성되는 점이 매우 흥미로웠습니다.   

### 4-5. 테스트
JUnit5를 이용해 Given, When, Then 형식으로 테스트 코드를 작성하는 방법을 알았습니다.  
테스트는 각각 독립적으로 실행 하기 위해 @AfterEach로 테스트마다 메모리 DB를 비웠습니다.  
테스트 코드를 작성하니 서버를 실행하는 시간을 절약할 수 있어서 좋았습니다.  

### 4-6. 정규 표현식
정규 표현식을 알고 있다고 생각했는데, 회원가입 유효성 검사를 구현하면서 몰랐던 점을 발견했습니다.  
소문자/대문자, 숫자 조합의 아이디를 만들려고 했습니다.  
검색을 통해 아이디 정규 표현식을 찾아보니 ^[A-za-z0-9]{4,12}$가 있었습니다.  
하지만, 이렇게 하면 소문자/대문자, 숫자 조합이 안 됩니다.  
숫자만 4자 이상 12자 이하 입력해도 통과됩니다.  
저는 소문자/대문자로 시작하고 영문자+숫자 조합인 정규 표현식을 썼습니다.  
```java
^(?=.*[0-9]+)[a-zA-Z][a-zA-Z0-9]{5,10}$
```
정규 표현식을 테스트할 수 있는 사이트에서(https://regexr.com/) 여러가지 케이스를 넣어가면서 정규 표현식을 제대로 익히기 위해 공부했습니다.  
맞게 작성한 것 같다고 생각이 들더라도 다시 확인해 보는 게 중요하다고 느꼈습니다.
