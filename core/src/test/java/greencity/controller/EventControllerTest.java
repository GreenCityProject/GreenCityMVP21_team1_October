package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.ModelUtils;
import greencity.dto.event.*;
import greencity.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import static greencity.ModelUtils.getPrincipal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    private static final String BASE_LINK = "/events";
    private final Principal principal = getPrincipal();
    private MockMvc mockMvc;
    @Mock
    private EventService eventService;
    @InjectMocks
    private EventController eventController;
    private EventDetailsUpdate eventDetailsUpdate;
    private EventResponseDto eventResponseDto;
    private EventRequestDto eventRequestDto;

    @BeforeEach
    void setUp() {
        eventResponseDto = ModelUtils.getEventResponseDto();
        eventDetailsUpdate = ModelUtils.getEventDetailsUpdate();
        eventRequestDto = ModelUtils.getEventRequestDto();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

    }

    @Test
    void update() throws Exception {
        MockMultipartFile jsonFile = getMockMultipartFile();

        when(eventService.update(any(EventDetailsUpdate.class), anyLong(), eq(principal.getName()), any()))
                .thenReturn(eventResponseDto);

        mockMvc.perform(multipart(BASE_LINK + "/{eventId}", 1L)
                        .file(jsonFile)
                        .principal(principal)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Lectures on garbage segregation"))
                .andExpect(jsonPath("$.dayList[0].id").value(1L))
                .andExpect(jsonPath("$.dayList[0].eventDate").value("2024-12-16"))
                .andExpect(jsonPath("$.dayList[0].eventStartTime").value("09:00:00"))
                .andExpect(jsonPath("$.dayList[0].eventEndTime").value("20:00:00"));

        verify(eventService).update(any(EventDetailsUpdate.class), anyLong(), eq(principal.getName()), any());
    }

    private static MockMultipartFile getMockMultipartFile() {
        String jsonRequest = """
                {
                    "id": 1,
                    "title": "Lectures on garbage segregation",
                    "description": "An event focused on promoting environmental awareness and sustainability practices within the community",
                    "eventDays": [
                        {
                            "id": 1,
                            "eventDate": "2024-12-16",
                            "eventStartTime": "09:00:00",
                            "eventEndTime": "20:00:00",
                            "isOnline": true,
                            "onlineLink": "https://example.com/event-link"
                        }
                    ],
                    "additionalImages": []
                }
                """;

        return new MockMultipartFile("requestDto", "",
                "application/json", jsonRequest.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void saveAndGetCreated() throws Exception {
        MockMultipartFile jsonFile = getMockMultipartFile();

        when(eventService.save(eq(eventRequestDto), eq(principal.getName()), any()))
                .thenReturn(eventResponseDto);

        mockMvc.perform(multipart(BASE_LINK)
                        .file(jsonFile)
                        .principal(principal)
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Lectures on garbage segregation"))
                .andExpect(jsonPath("$.dayList[0].id").value(1L))
                .andExpect(jsonPath("$.dayList[0].eventDate").value("2024-12-16"))
                .andExpect(jsonPath("$.dayList[0].eventStartTime").value("09:00:00"))
                .andExpect(jsonPath("$.dayList[0].eventEndTime").value("20:00:00"));

        verify(eventService).save(eq(eventRequestDto), eq(principal.getName()), any());
    }

    @Test
    void getAllEventsWithoutFilters() throws Exception {

        List<EventDto> eventDtos = List.of(
                EventDto.builder()
                        .id(1L)
                        .title("Online Tech Meetup")
                        .description("Tech event for enthusiasts")
                        .isOpen(true)
                        .build(),
                EventDto.builder()
                        .id(2L)
                        .title("Local Art Fair")
                        .description("An offline event showcasing art")
                        .isOpen(false)
                        .build()
        );

        PageableAdvancedDtoOfEventDto response = PageableAdvancedDtoOfEventDto.builder()
                .currentPage(0)
                .page(eventDtos)
                .totalElements(2L)
                .totalPages(1)
                .build();

        when(eventService.getAllEvents(any())).thenReturn(response);

        mockMvc.perform(get(BASE_LINK)
                        .param("page", "0")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.page[0].title").value("Online Tech Meetup"))
                .andExpect(jsonPath("$.page[1].title").value("Local Art Fair"));

        verify(eventService).getAllEvents(any());
    }

    @Test
    void getFilteredEvents() throws Exception {

        List<EventDto> eventDtos = List.of(
                EventDto.builder()
                        .id(1L)
                        .title("Online Tech Meetup")
                        .description("Tech event for enthusiasts")
                        .isOpen(true)
                        .build()
        );

        PageableAdvancedDtoOfEventDto response = PageableAdvancedDtoOfEventDto.builder()
                .currentPage(0)
                .page(eventDtos)
                .totalElements(1L)
                .totalPages(1)
                .build();

        when(eventService.getFilteredEvents(any(), eq("upcoming"), eq("Kyiv"), eq(List.of("social")), eq("open"), any()))
                .thenReturn(response);

        mockMvc.perform(get(BASE_LINK)
                        .param("page", "0")
                        .param("size", "5")
                        .param("eventTime", "upcoming")
                        .param("location", "Kyiv")
                        .param("tags", "social")
                        .param("status", "open")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page[0].title").value("Online Tech Meetup"));

        verify(eventService).getFilteredEvents(any(), eq("upcoming"), eq("Kyiv"), eq(List.of("social")), eq("open"), any());
    }

    @Test
    void getAllEventsWithPagination() throws Exception {

        List<EventDto> eventDtos = List.of(
                EventDto.builder()
                        .id(1L)
                        .title("Online Tech Meetup")
                        .description("Tech event for enthusiasts")
                        .isOpen(true)
                        .build(),
                EventDto.builder()
                        .id(2L)
                        .title("Local Art Fair")
                        .description("An offline event showcasing art")
                        .isOpen(false)
                        .build()
        );

        PageableAdvancedDtoOfEventDto response = PageableAdvancedDtoOfEventDto.builder()
                .currentPage(1)
                .page(eventDtos)
                .totalElements(10L)
                .totalPages(5)
                .build();

        when(eventService.getAllEvents(any())).thenReturn(response);

        mockMvc.perform(get(BASE_LINK)
                        .param("page", "1")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.page[0].title").value("Online Tech Meetup"))
                .andExpect(jsonPath("$.page[1].title").value("Local Art Fair"));

        verify(eventService).getAllEvents(any());
    }
}