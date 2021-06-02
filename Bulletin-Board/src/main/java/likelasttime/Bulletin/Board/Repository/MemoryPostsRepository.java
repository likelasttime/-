package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.Repository.PostsRepository;
import likelasttime.Bulletin.Board.domain.posts.Posts;

import java.util.*;

public class MemoryPostsRepository implements PostsRepository {

    private static Map<Long, Posts> post=new HashMap<>();
    private static long sequence=0L;

    @Override
    public Posts save(Posts posts) {
        posts.setId(++sequence);
        post.put(posts.getId(), posts);
        return posts;
    }

    @Override
    public Optional<Posts> findById(Long id) {
        return Optional.ofNullable(post.get(id));
    }

    @Override
    public Optional<Posts> findByTitle(String title) {
        return post.values().stream()
                .filter(post->post.getTitle().equals(title))
                .findAny();
    }

    @Override
    public List<Posts> findAll(){
        return new ArrayList<>(post.values());
    }

    public void clearPost(){
        post.clear();
    }

}
