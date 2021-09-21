package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;

import java.util.*;

public class MemoryPostRepository implements PostRepository {

    private static Map<Long, Post> blog=new HashMap<>();
    private static long sequence=0L;

    @Override
    public void save(Post post) {
        post.setId(++sequence);
        blog.put(post.getId(), post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(blog.get(id));
    }

    @Override
    public Optional<Post> findByTitle(String title) {
        return blog.values().stream()
                .filter(post->post.getTitle().equals(title))
                .findAny();
    }

    @Override
    public List<Post> findAll(){
        return new ArrayList<>(blog.values());
    }

    @Override
    public void update(Long id, Post post){
        blog.put(id, blog.replace(id, post));
    }

    @Override
    public void delete(Long id){ blog.remove(id); }

    // 모두 삭제
    public void clearBlog(){
        blog.clear();
    }

}
