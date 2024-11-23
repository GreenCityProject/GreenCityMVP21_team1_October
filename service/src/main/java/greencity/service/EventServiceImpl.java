package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.event.EventRequestDto;
import greencity.dto.event.EventResponseDto;
import greencity.entity.*;
import greencity.enums.Role;
import greencity.enums.TagType;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EventImagesRepository;
import greencity.repository.EventRepository;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepo;
    private final EventImagesRepository eventImagesRepo;
    private final ModelMapper modelMapper;
    private final RestClient restClient;
    private final FileService fileService;
    private final TagsService tagsService;
    private final UserRepo userRepo;
//    private final NotificationService notificationService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EventResponseDto update(Long eventId, EventRequestDto requestDto, String email, MultipartFile[] files) {
        Event eventToUpdate = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        User organizer = modelMapper.map(restClient.findByEmail(email), User.class);

        if (!organizer.getId().equals(eventToUpdate.getOrganizer().getId())
                && organizer.getRole() != Role.ROLE_ADMIN) {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }

        updateEvent(requestDto, eventToUpdate);
        var eventDaysToUpdate = requestDto.getEventDays()
                .stream()
                .map(dto -> modelMapper.map(dto, EventDay.class))
                .map(eventDay -> eventDay.setEvent(eventToUpdate)).collect(Collectors.toList());
        eventToUpdate.setEventDays(eventDaysToUpdate);
//        updateEventDay(requestDto, eventToUpdate);
        if (files != null) updateAdditionalImages(files, eventToUpdate);

        Event saved = eventRepo.save(eventToUpdate);
        return modelMapper.map(saved, EventResponseDto.class);
    }

    private void updateEvent(EventRequestDto requestDto, Event eventToUpdate) {
        if (requestDto.getTitle() != null) {
            eventToUpdate.setTitle(requestDto.getTitle());
        }

        if (requestDto.getDescription() != null) {
            eventToUpdate.setDescription(requestDto.getDescription());
        }

        if (requestDto.getTags() != null) {
            eventToUpdate.setTags(modelMapper.map(tagsService.findTagsWithAllTranslationsByNamesAndType(
                    requestDto.getTags(), TagType.EVENT), new TypeToken<List<Tag>>() {
            }.getType()));
        }
    }

//    private void updateEventDay(EventRequestDto requestDto, Event eventToUpdate) {
//        if (requestDto.getEventDays() != null) {
//            List<EventDay> daysToUpdate = requestDto.getEventDays().stream()
//                    .map(eventDayDto -> {
//                        EventDay eventDay = modelMapper.map(eventDayDto, EventDay.class);
//                        eventDay.setEvent(eventToUpdate);
//                        return eventDay;
//                    })
//                    .collect(Collectors.toList());
//
//            List<EventDay> toRemove = eventToUpdate.getEventDays().stream()
//                    .filter(day -> daysToUpdate.stream()
//                            .noneMatch(newDay -> newDay.getId().equals(day.getId())))
//                    .toList();
//            toRemove.forEach(day -> eventRepo.deleteEventDayByEventId(day.getId()));
//
//            eventToUpdate.setEventDays(daysToUpdate);
//        }
//    }

    private void updateAdditionalImages(MultipartFile[] files, Event eventToUpdate) {
        String existingImage = eventToUpdate.getImage();
        String requestImage = fileService.upload(files[0]);
        if (existingImage != null && !requestImage.equals(existingImage)) {
            fileService.delete(existingImage);
            eventToUpdate.setImage(requestImage);
        }
        List<String> requestImageLinks = Arrays.stream(files).map(fileService::upload).toList();
        List<String> existingImageLinks = eventToUpdate.getAdditionalImages().stream().map(EventImages::getLink).toList();
        List<String> imagesToDeleteFromAzure = existingImageLinks.stream()
                .filter(image -> !requestImageLinks.contains(image)).toList();
        imagesToDeleteFromAzure.forEach(fileService::delete);
        eventToUpdate.setAdditionalImages(requestImageLinks.stream()
                .map(link -> EventImages.builder().event(eventToUpdate).link(link).build()).toList());
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with ID: " + eventId));
        if (!event.getOrganizer().getId().equals(userId) && !isAdmin(userId)) {
            throw new UserHasNoPermissionToAccessException("You do not have permission to delete this event.");
        }

        eventImagesRepo.deleteEventImagesByEvent_Id(eventId);
        eventRepo.deleteEventDayByEventId(eventId);

//        notificationSrvice.notifyAttendees(event.getAttendants(), "The event has been deleted");
        eventRepo.delete(event);
    }

    @Override
    public EventResponseDto save(EventRequestDto eventRequestDto, String email, MultipartFile[] files) {
        Event eventToSave = modelMapper.map(eventRequestDto, Event.class);
        User organizer = modelMapper.map(restClient.findByEmail(email), User.class);
        if (eventRequestDto.getTags() != null) {
            eventToSave.setTags(modelMapper.map(tagsService.findTagsWithAllTranslationsByNamesAndType(
                    eventRequestDto.getTags(), TagType.EVENT), new TypeToken<List<Tag>>() {
            }.getType()));
        }

        var eventDaysRequest = eventRequestDto.getEventDays();
        var eventDaysToSave = eventDaysRequest.stream()
                .map(eventDayDto -> modelMapper.map(eventDayDto, EventDay.class))
                .map(eventDay -> eventDay.setEvent(eventToSave)).toList();
        eventToSave.setEventDays(eventDaysToSave);

        eventToSave.setOrganizer(organizer);
        if (files != null) {
            eventToSave.setImage(fileService.upload(files[0]));
            eventToSave.setAdditionalImages(Arrays.stream(files)
                    .map(file -> EventImages.builder().event(eventToSave).link(fileService.upload(file)).build())
                    .collect(Collectors.toList()));
        }
        return modelMapper.map(eventRepo.save(eventToSave), EventResponseDto.class);
    }

    private boolean isAdmin(Long userId) {
        return userRepo.findById(userId)
                .map(User::getRole)
                .orElse(Role.ROLE_USER) == Role.ROLE_ADMIN;
    }
}
