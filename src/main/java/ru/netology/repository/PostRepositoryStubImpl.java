package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final AtomicLong postId;
    private final Map<Long, Post> posts;

    public PostRepositoryStubImpl() {
        this.postId = new AtomicLong(0);
        this.posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return this.posts.values().stream().filter((p) -> !p.isRemoved()).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (this.posts.containsKey(id) && !this.posts.get(id).isRemoved()) {
            return Optional.ofNullable(this.posts.get(id));
        } else {
            throw new NotFoundException();
        }
    }

    public Post save(Post post) {
        var id = post.getId();
        if (id > 0 && this.posts.containsKey(id)) {
            if (!this.posts.get(id).isRemoved()) {
                this.posts.replace(id, post);
            } else {
                throw new NotFoundException();
            }
        } else {
            var newIdPost = this.postId.incrementAndGet();
            post.setId(newIdPost);
            this.posts.put(newIdPost, post);
        }

        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            Post post = posts.get(id);
            post.setRemoved(true);
        }
    }
}
