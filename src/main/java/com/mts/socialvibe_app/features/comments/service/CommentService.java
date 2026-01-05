package com.mts.socialvibe_app.features.comments.service;

import com.mts.socialvibe_app.features.comments.dto.CommentDto;
import com.mts.socialvibe_app.features.comments.model.Comment;
import com.mts.socialvibe_app.features.comments.repository.CommentRepository;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
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

    private Comment mapToEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getUser() != null) {
            dto.setUsername(comment.getUser().getUsername());
            dto.setUserAvatar(comment.getUser().getAvatarUrl());
        }
        return dto;
    }

    private Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Post not found"));
        return post;
    }

    @Override
    public CommentDto createComment(Long postId, String username, CommentDto commentDto) {
        Post post = getPost(postId);

        UserEntity user = userRepository.findByUsername(username);

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return mapToDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        Post post = getPost(postId);
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream().map(this::mapToDto).toList();
    }

    @Override
    public void deleteComment(Long postId, Long commentId, UserDetails userDetails) {
        Post post = getPost(postId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException("Comment not found!"));

        if(!comment.getPost().getId().equals(post.getId())) {
            throw new RuntimeException("Comment doesn't belong to this post");
        }

        String currentUsername = userDetails.getUsername();
        if(!comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }




}
