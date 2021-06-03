package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;

import java.util.*;

public class MemoryPostRepository implements PostRepository {

    private static Map<Long, Post> post=new HashMap<>();
    private static long sequence=0L;

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        MemoryPostRepository.post.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(post.get(id));
    }

    @Override
    public Optional<Post> findByTitle(String title) {
        return post.values().stream()
                .filter(post->post.getTitle().equals(title))
                .findAny();
    }

    @Override
    public List<Post> findAll(){
        return new ArrayList<>(post.values());
    }

    public void clearPost(){
        post.clear();
    }

}
