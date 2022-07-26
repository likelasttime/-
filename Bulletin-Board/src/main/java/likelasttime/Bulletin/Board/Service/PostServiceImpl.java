package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate redisTemplate;

    // 게시글 작성
    @CacheEvict(value={"findByRank", "findAll"}, allEntries = true)
    public PostResponseDto create(PostRequestDto post){
        //validateDuplicatePost(post);  // 중복 게시글
        Post post_entity=modelMapper.map(post, Post.class);
        return modelMapper.map(postRepository.save(post_entity), PostResponseDto.class);
    }

    private void validateDuplicatePost(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    // 게시글 수정
    @CacheEvict(value={"findByRank", "findAll"}, allEntries = true)
    public Post update(Long id, PostRequestDto post){
        Post post_entity=postRepository.findById(id).get();
        post_entity.update(post.getTitle(), post.getContent(), post_entity.getView(), post_entity.getComment_cnt());
        return post_entity;
    }

    //전체 게시글 조회
    //@Cacheable(value="findAll")
    public List<PostResponseDto> findAll(){
        List<PostResponseDto> postResponsedtoList=postRepository.findAll(Sort.by("id").descending()).stream().map(PostResponseDto::new).collect(Collectors.toList());
        redisTemplate.opsForList().leftPushAll("findAll", postResponsedtoList);
        return postResponsedtoList;
    }

    //@Cacheable(value="findByRank")
    public List<PostResponseDto> findByRank(){
        String key="findByRank";
        ZSetOperations<String, String> zSetOperations=redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.reverseRangeWithScores(key, 0, 9);

        if(typedTuples.isEmpty()){
            // 데이터베이스에서 조회
            List<Post> postList=postRepository.findTop10ByOrderByViewDesc();
            List<PostResponseDto> postDto=postList.stream().map(PostResponseDto::new).collect(Collectors.toList());
            return postDto;
        }
        //HashOperations<String, String, Object> hashOperations= redisTemplate.opsForHash();
        List<PostResponseDto> collect=new ArrayList<>();
        for(ZSetOperations.TypedTuple<String> t : typedTuples){
            String id=t.getValue();
            PostResponseDto postResponseDto=(PostResponseDto) redisTemplate.opsForHash().get("rankByHash", id);
            collect.add(postResponseDto);
        }
        return collect;
    }

    // 특정 게시글 조회
    public Optional<Post> findById(Long postId) throws IOException {
        String key="findByRank";
        String id=postId.toString();
        Optional<Post> post=postRepository.findById(postId);
        PostResponseDto dto=modelMapper.map(post.get(), PostResponseDto.class);
        dto.setView(dto.getView() + 1);
        redisTemplate.opsForZSet().add(key, id, dto.getView());
        redisTemplate.opsForHash().put("rankByHash", id, dto);
        return post;
    }

    // 삭제
    @CacheEvict(value="findAll", allEntries = true)
    public void deletePost(Long id){
        String postId=id.toString();
        redisTemplate.opsForZSet().remove("findByRank", postId);
        redisTemplate.opsForHash().delete("rankByHash", postId);
        postRepository.deleteById(id);
    }

    // 조회수 증가
    public void updateView(Long id) {
        Post post=postRepository.findById(id).get();
        post.update(post.getTitle(), post.getContent(), post.getView()+1, post.getComment_cnt());
    }

    // 검색
    public Page<PostResponseDto> search(String title, String content, String author, Pageable pageable){
        Page<Post> post=postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, content, author, pageable);
        Page<PostResponseDto> postResponseDtos=post.map(p -> modelMapper.map(p, PostResponseDto.class));
        return postResponseDtos;
    }

    @CacheEvict(value={"findByRank", "findAll"}, allEntries=true)
    public void deleteAll(){
        postRepository.deleteAll();
    }
}
