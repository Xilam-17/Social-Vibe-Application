package com.mts.socialvibe_app.features.comments.service;

import com.mts.socialvibe_app.features.comments.dto.CommentRequest;
import com.mts.socialvibe_app.features.comments.dto.CommentResponse;
import com.mts.socialvibe_app.features.comments.model.Comment;
import com.mts.socialvibe_app.features.comments.repository.CommentRepository;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements ICommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Post not found"));
        return post;
    }

    @Override
    public CommentResponse createComment(Long postId, String username, CommentRequest commentRequest) {
        Post post = getPost(postId);

        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw  new RuntimeException("User not found!");
        }

        Comment comment = Comment.mapToEntity(commentRequest);
        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.mapToDto(savedComment);
    }

    @Override
    public List<CommentResponse> getAllCommentsByPostId(Long postId) {
        Post post = getPost(postId);
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream().map(CommentResponse::mapToDto).toList();
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String username) {
        Post post = getPost(postId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new RuntimeException("Comment doesn't belong to this post");
        }

        String commentAuthor = comment.getUser().getUsername();
        String postOwner = post.getUser().getUsername();

        boolean isCommentAuthor = commentAuthor.equals(username);
        boolean isPostOwner = postOwner.equals(username);

        if (!isCommentAuthor && !isPostOwner) {
            throw new RuntimeException("You are not authorized to delete this comment. " +
                    "Only the comment author or post owner can perform this action.");
        }

        commentRepository.delete(comment);
    }




}
