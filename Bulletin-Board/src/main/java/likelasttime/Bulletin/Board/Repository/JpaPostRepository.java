package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaPostRepository implements PostRepository{
    private final EntityManager em;

    public JpaPostRepository(EntityManager em){
        this.em=em;
    }

    public void save(Post post){
        em.persist(post);
    }

    public Optional<Post> findById(Long id){
        Post post=em.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    public Optional<Post> findByTitle(String title){
        List<Post> result=em.createQuery("select p from Post p where p.title=:title", Post.class)
                .setParameter("title", title)
                .getResultList();
        return result.stream().findAny();
    }

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public Optional<Post> findOne(Long id){
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public void update(Long id, Post post){
        Post find_post=em.find(Post.class, id);
        find_post.setTitle(post.getTitle());
        find_post.setContent(post.getContent());
        find_post.setAuthor(post.getAuthor());

    }

    public void delete(Long id){
        Post post=em.find(Post.class, id);
        em.remove(post);
    }

    public void deleteAll(){
        em.clear();
    }

}