package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final AtomicLong postId;
    private final Map<Long, Post> posts;

    public PostRepositoryStubImpl() {
        this.postId = new AtomicLong(0);
        this.posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(this.posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(this.posts.get(id));
    }

    public Post save(Post post) {

        var id = post.getId();
        if (id > 0 && this.posts.containsKey(id)) {
            this.posts.replace(id, post);
        } else {
            var newIdPost = this.postId.incrementAndGet();
            post.setId(newIdPost);
            this.posts.put(newIdPost, post);
        }

        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            this.posts.remove(id);
        }
    }
}
