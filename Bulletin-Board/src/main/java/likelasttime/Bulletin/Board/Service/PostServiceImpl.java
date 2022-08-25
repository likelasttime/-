package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate redisTemplate;

    // 게시글 작성
    public PostResponseDto create(PostRequestDto postRequestDto) {
        //validateDuplicatePost(post);  // 중복 게시글
        Post post_entity=modelMapper.map(postRequestDto, Post.class);
        Post savedPost=postRepository.save(post_entity);
        String id=savedPost.getId().toString();
        PostResponseDto postResponseDto=modelMapper.map(savedPost, PostResponseDto.class);
        redisTemplate.opsForHash().put("findAll", id, postResponseDto);

        return postResponseDto;
    }

    private void validateDuplicatePost(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    // 게시글 수정
    public PostResponseDto update(Long id, PostRequestDto postDto){
        Post post_entity=postRepository.findById(id).get();
        post_entity.update(postDto.getTitle(), postDto.getContent(), post_entity.getView(), post_entity.getComment_cnt());
        PostResponseDto postResponseDto= PostResponseDto.builder()
                .post(post_entity)
                .build();
        String stringId=id.toString();
        redisTemplate.opsForHash().put("rankByHash", stringId, postResponseDto);        // 고려
        redisTemplate.opsForHash().put("findAll", stringId, postResponseDto);

        return postResponseDto;
    }

    //전체 게시글 조회(캐시)
    public List<PostResponseDto> findAllByCache(){
        TreeMap<Long, PostResponseDto> treeMap=new TreeMap(Collections.reverseOrder());
        Map<String, PostResponseDto> map1=redisTemplate.opsForHash().entries("findAll");
        Map<Long, PostResponseDto> map2=map1.entrySet().stream().collect(Collectors.toMap(i -> (Long)Long.valueOf(i.getKey()), i -> (PostResponseDto)i.getValue()));
        treeMap.putAll(map2);
        List<PostResponseDto> postResponseDtoList=treeMap.values().stream().collect(Collectors.toList());

        return postResponseDtoList;
    }

    // 전체 게시글 조회(캐시에 없어서 DB에서 조회 후 캐시에 저장)
    public List<PostResponseDto> findAll() {
        List<PostResponseDto> postResponseDtoList=postRepository.findAll(Sort.by("id").descending()).stream().map(PostResponseDto::new).collect(Collectors.toList());
        Map<String, PostResponseDto> map=postResponseDtoList.stream()
                .collect(Collectors.toMap(P -> P.getId().toString(), Function.identity()));
        redisTemplate.opsForHash().putAll("findAll", map);            // 캐시에 저장

        return postResponseDtoList;
    }

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

        List<PostResponseDto> collect=new ArrayList<>();
        for(ZSetOperations.TypedTuple<String> t : typedTuples){
            String id=t.getValue();
            PostResponseDto postResponseDto=(PostResponseDto) redisTemplate.opsForHash().get("rankByHash", id);
            collect.add(postResponseDto);
        }

        return collect;
    }

    // 특정 게시글 조회
    public PostResponseDto findById(Long postId) {
        String key="findByRank";
        String id=postId.toString();
        Post post=postRepository.findById(postId).get();
        post.update(post.getTitle(), post.getContent(), post.getView()+1, post.getComment_cnt());
        PostResponseDto dto = PostResponseDto.builder()
                .post(post)
                .build();
        redisTemplate.opsForZSet().add(key, id, dto.getView());
        redisTemplate.opsForHash().put("rankByHash", id, dto);
        redisTemplate.opsForHash().put("findAll", id, dto);
        return dto;
    }

    // 삭제
    public void deletePost(Long id){
        String postId=id.toString();
        redisTemplate.opsForZSet().remove("findByRank", postId);
        redisTemplate.opsForHash().delete("rankByHash", postId);
        redisTemplate.opsForHash().delete("findAll", postId);
        postRepository.deleteById(id);
    }

    // 검색
    public Page<PostResponseDto> search(String title, String content, String author, Pageable pageable){
        Page<Post> post=postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, content, author, pageable);
        Page<PostResponseDto> postResponseDto=post.map(p -> modelMapper.map(p, PostResponseDto.class));

        return postResponseDto;
    }

    public void deleteAll(){
        postRepository.deleteAll();
        redisTemplate.opsForZSet().getOperations().delete("findByRank");
        redisTemplate.opsForHash().getOperations().delete("rankByHash");
        redisTemplate.opsForList().getOperations().delete("findAll");
    }
}
