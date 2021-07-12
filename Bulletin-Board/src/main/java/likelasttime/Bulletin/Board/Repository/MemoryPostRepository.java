package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;

import java.util.*;

public class MemoryPostRepository implements PostRepository {

    private static Map<Long, Post> blog=new HashMap<>();
    private static long sequence=0L;

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        blog.put(post.getId(), post);
        return post;
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

    public void clearBlog(){
        blog.clear();
    }





}
