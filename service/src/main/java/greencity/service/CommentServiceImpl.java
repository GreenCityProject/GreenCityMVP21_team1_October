package greencity.service;

import greencity.converters.CommentDtoConverter;
import greencity.dto.comment.AddCommentDto;
import greencity.dto.comment.CommentDto;
import greencity.entity.Comment;
import greencity.entity.User;
import greencity.repository.CommentRepository;
import greencity.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepo userRepo;
    private final CommentDtoConverter commentDtoConverter;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepo userRepo, CommentDtoConverter commentDtoConverter) {
        this.commentRepository = commentRepository;
        this.userRepo = userRepo;
        this.commentDtoConverter = commentDtoConverter;
    }

    @Override
    @Transactional
    public CommentDto addComment(AddCommentDto addCommentDto, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Comment comment = Comment.builder()
                .text(addCommentDto.getText())
                .user(user)
                .parentComment(addCommentDto.getParentCommentId() != null
                        ? commentRepository.findById(addCommentDto.getParentCommentId())
                        .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"))
                        : null)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return commentDtoConverter.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getReplies(Long parentCommentId) {
        return commentRepository.findAllByParentCommentId(parentCommentId).stream()
                .map(commentDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUser(Long userId) {
        return commentRepository.findAllByUserId(userId).stream()
                .map(commentDtoConverter::toDto)
                .collect(Collectors.toList());
    }

//    private boolean contains
}
