package greencity.service;


import greencity.ModelUtils;
import greencity.constant.ErrorMessage;
import greencity.dto.event.EventVO;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.AddEventCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentVO;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventComment;
import greencity.entity.User;
import greencity.enums.Role;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.EventCommentRepo;
import greencity.repository.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static greencity.ModelUtils.getUser;
import static greencity.ModelUtils.getUserVO;
import static greencity.constant.AppConstant.AUTHORIZATION;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EventCommentServiceImplTest {
    @Mock
    private EventCommentRepo eventCommentRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventCommentServiceImpl eventCommentService;
    @Mock
    private HttpServletRequest request;

    @Test
    void saveTest() {
        UserVO userVO = getUserVO();
        User user = getUser();
        Event event = ModelUtils.getEvent();
        EventVO eventVO = ModelUtils.getEventVO();
        AddEventCommentDtoRequest addEventCommentDtoRequest = ModelUtils.getEventCommentDtoRequest();
        EventComment eventComment = ModelUtils.getEventComment();
        AddEventCommentDtoResponse addEventCommentDtoResponse = new AddEventCommentDtoResponse();
        when(modelMapper.map(eventRepository.findById(anyLong()), EventVO.class)).thenReturn(eventVO);
        when(modelMapper.map(addEventCommentDtoRequest, EventComment.class)).thenReturn(eventComment);
        when(request.getHeader(AUTHORIZATION)).thenReturn("token");
        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(modelMapper.map(eventVO, Event.class)).thenReturn(event);
        when(eventCommentRepo.save(eventComment)).thenReturn(eventComment);
        when(modelMapper.map(eventComment, AddEventCommentDtoResponse.class)).thenReturn(addEventCommentDtoResponse);
        eventCommentService.save(1L, addEventCommentDtoRequest, userVO);
        verify(eventCommentRepo).save(any(EventComment.class));
    }

    @Test
    void saveTest_NoParentComment() {
        UserVO userVO = getUserVO();
        User user = getUser();
        Event event = ModelUtils.getEvent();
        EventVO eventVO = ModelUtils.getEventVO();
        AddEventCommentDtoRequest addEventCommentDtoRequest = ModelUtils.getEventCommentDtoRequest();
        EventComment eventComment = ModelUtils.getEventComment();
        AddEventCommentDtoResponse addEventCommentDtoResponse = new AddEventCommentDtoResponse();

        when(modelMapper.map(eventRepository.findById(anyLong()), EventVO.class)).thenReturn(eventVO);
        when(modelMapper.map(addEventCommentDtoRequest, EventComment.class)).thenReturn(eventComment);
        when(request.getHeader(AUTHORIZATION)).thenReturn("token");
        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(modelMapper.map(eventVO, Event.class)).thenReturn(event);
        when(eventCommentRepo.save(eventComment)).thenReturn(eventComment);
        when(modelMapper.map(eventComment, AddEventCommentDtoResponse.class)).thenReturn(addEventCommentDtoResponse);

        AddEventCommentDtoResponse response = eventCommentService.save(1L, addEventCommentDtoRequest, userVO);

        verify(eventCommentRepo).save(any(EventComment.class));
        assertNotNull(response);
    }

    @Test
    void saveTest_WithParentComment() {
        UserVO userVO = getUserVO();
        User user = getUser();
        Event event = ModelUtils.getEvent();
        EventVO eventVO = ModelUtils.getEventVO();
        AddEventCommentDtoRequest addEventCommentDtoRequest = ModelUtils.getEventCommentDtoRequest();
        addEventCommentDtoRequest.setParentCommentId(1L); // Додаємо батьківський коментар
        EventComment eventComment = ModelUtils.getEventComment();
        EventComment parentComment = ModelUtils.getEventComment();
        AddEventCommentDtoResponse addEventCommentDtoResponse = new AddEventCommentDtoResponse();

        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.of(parentComment));
        when(modelMapper.map(eventRepository.findById(anyLong()), EventVO.class)).thenReturn(eventVO);
        when(modelMapper.map(addEventCommentDtoRequest, EventComment.class)).thenReturn(eventComment);
        when(request.getHeader(AUTHORIZATION)).thenReturn("token");
        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(modelMapper.map(eventVO, Event.class)).thenReturn(event);
        when(eventCommentRepo.save(eventComment)).thenReturn(eventComment);
        when(modelMapper.map(eventComment, AddEventCommentDtoResponse.class)).thenReturn(addEventCommentDtoResponse);

        AddEventCommentDtoResponse response = eventCommentService.save(1L, addEventCommentDtoRequest, userVO);

        verify(eventCommentRepo).save(any(EventComment.class));
        assertNotNull(response);
    }

    @Test
    void updateTest() {
        EventCommentVO eventCommentVO = ModelUtils.getEventCommentVO();
        EventComment eventComment = ModelUtils.getEventComment();
        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.of(eventComment));
        when(modelMapper.map(eventComment, EventCommentVO.class)).thenReturn(eventCommentVO);
        when(modelMapper.map(eventCommentVO, EventComment.class)).thenReturn(eventComment);
        eventCommentService.update("Updated comment text", 1L, eventCommentVO.getUser());
        verify(eventCommentRepo).findById(anyLong());
        verify(eventCommentRepo).save(any(EventComment.class));
    }

    @Test
    void deleteTest() {
        UserVO userVO = getUserVO();
        User user = getUser();
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.setUser(user);
        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.of(eventComment));
        when(request.getHeader(AUTHORIZATION)).thenReturn("token");

        eventCommentService.delete(1L, userVO);

        verify(eventCommentRepo).findById(anyLong());
        verify(eventCommentRepo).delete(any(EventComment.class));
    }

    @Test
    void deleteTest_NotFoundException() {
        UserVO userVO = getUserVO();
        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventCommentService.delete(1L, userVO));
        assertEquals(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION, thrown.getMessage());
    }

    @Test
    void deleteTest_NotCurrentUserException() {
        UserVO userVO = getUserVO();
        User otherUser = getUser();
        otherUser.setId(999L);
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.setUser(otherUser);
        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.of(eventComment));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> eventCommentService.delete(1L, userVO));
        assertEquals(ErrorMessage.NOT_A_CURRENT_USER, thrown.getMessage());
    }

    @Test
    void deleteTest_Admin() {
        UserVO adminVO = getUserVO();
        adminVO.setRole(Role.ROLE_ADMIN);
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.setUser(getUser());
        when(eventCommentRepo.findById(anyLong())).thenReturn(Optional.of(eventComment));
        when(request.getHeader(AUTHORIZATION)).thenReturn("token");

        eventCommentService.delete(1L, adminVO);

        verify(eventCommentRepo).findById(anyLong());
        verify(eventCommentRepo).delete(any(EventComment.class));
    }

    @Test
    void findByIdTest() {
        long commentId = 1L;
        EventComment eventComment = ModelUtils.getEventComment();
        when(eventCommentRepo.findById(commentId)).thenReturn(Optional.of(eventComment));
        when(modelMapper.map(eventComment, EventCommentVO.class)).thenReturn(new EventCommentVO());

        EventCommentVO foundComment = eventCommentService.findById(commentId);

        assertNotNull(foundComment);
        verify(eventCommentRepo).findById(commentId);
        verify(modelMapper).map(eventComment, EventCommentVO.class);
    }

    @Test
    void findByIdTest_NotFoundException() {
        long commentId = 1L;
        when(eventCommentRepo.findById(commentId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventCommentService.findById(commentId));
        assertEquals(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION, thrown.getMessage());
    }
}
