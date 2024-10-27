package greencity.controller;

import greencity.ModelUtils;
import greencity.converters.UserArgumentResolver;
import greencity.dto.habit.*;
import greencity.dto.user.UserVO;
import greencity.enums.HabitAssignStatus;
import greencity.service.HabitAssignServiceImpl;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HabitAssignControllerTest {
    @Mock
    UserService userService;
    @Mock
    HabitAssignServiceImpl habitAssignService;
    @InjectMocks
    HabitAssignController habitAssignController;
    @Mock
    ModelMapper modelMapper;
    private static String ENDPOINT;
    private static long DEFAULT_HABIT_ID;
    private static UserVO DEFAULT_USER_VO;
    private static HabitAssignManagementDto DEFAULT_HABIT_ASSIGN_MANAGEMENT_DTO;
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpBeforeClass() {
        ENDPOINT = "/habit/assign";
        DEFAULT_HABIT_ID = 1L;
        DEFAULT_USER_VO = ModelUtils.getUserVO();
        DEFAULT_HABIT_ASSIGN_MANAGEMENT_DTO = HabitAssignManagementDto
                .builder()
                .id(1L)
                .status(HabitAssignStatus.INPROGRESS)
                .duration(10)
                .createDateTime(ZonedDateTime.now())
                .workingDays(20)
                .habitStreak(15)
                .lastEnrollment(ZonedDateTime.now())
                .progressNotificationHasDisplayed(true)
                .habitId(DEFAULT_HABIT_ID)
                .userId(DEFAULT_USER_VO.getId()).build();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(habitAssignController).setCustomArgumentResolvers(
                new PageableHandlerMethodArgumentResolver(),
                new UserArgumentResolver(userService, modelMapper)
        ).build();
    }

    @Test
    void assignDefaultTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.assignDefaultHabitForUser(anyLong(), any(UserVO.class)))
                .thenReturn(DEFAULT_HABIT_ASSIGN_MANAGEMENT_DTO);
        mockMvc.perform(post(ENDPOINT + "/{habitId}", DEFAULT_HABIT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("INPROGRESS"))
                .andExpect(jsonPath("$.createDateTime").exists())
                .andExpect(jsonPath("$.habitId").value(DEFAULT_HABIT_ID))
                .andExpect(jsonPath("$.userId").value(DEFAULT_USER_VO.getId()))
                .andExpect(jsonPath("$.duration").value(10))
                .andExpect(jsonPath("$.workingDays").value(20))
                .andExpect(jsonPath("$.habitStreak").value(15))
                .andExpect(jsonPath("$.lastEnrollment").exists())
                .andExpect(jsonPath("$.progressNotificationHasDisplayed").value(true));
        verify(habitAssignService, times(1)).assignDefaultHabitForUser(
                anyLong(),
                any(UserVO.class)
        );
    }

    @Test
    void updateHabitAssignDurationTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        HabitAssignUserDurationDto habitAssignUserDurationDto =
                HabitAssignUserDurationDto.builder().habitAssignId(DEFAULT_HABIT_ID)
                        .userId(DEFAULT_USER_VO.getId()).duration(10)
                        .habitAssignId(15L)
                        .status(HabitAssignStatus.REQUESTED)
                        .habitId(DEFAULT_HABIT_ID)
                        .workingDays(20)
                        .build();
        when(habitAssignService.updateUserHabitInfoDuration(
                anyLong(),
                anyLong(),
                anyInt())
        ).thenReturn(habitAssignUserDurationDto);
        mockMvc.perform(put(ENDPOINT + "/{habitAssignId}/update-habit-duration", DEFAULT_HABIT_ID)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("duration", "7")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.habitAssignId").value(15))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.habitId").value(1L))
                .andExpect(jsonPath("$.status").value("REQUESTED"))
                .andExpect(jsonPath("$.workingDays").value(20))
                .andExpect(jsonPath("$.duration").value(10));
        verify(habitAssignService, times(1)).updateUserHabitInfoDuration(anyLong(), anyLong(), anyInt());
    }

    @Test
    void getHabitAssignTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        HabitAssignDto habitAssignDto = HabitAssignDto.builder().id(1L).build();
        when(habitAssignService.getByHabitAssignIdAndUserId(
                anyLong(),
                anyLong(),
                anyString())
        ).thenReturn(habitAssignDto);
        mockMvc.perform(get(ENDPOINT + "/{habitAssignId}", 1L)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("lang", "en")
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).getByHabitAssignIdAndUserId(anyLong(), anyLong(), anyString());
    }

    @Test
    void updateAssignByHabitAssignIdTest() throws Exception {
        when(habitAssignService.updateStatusByHabitAssignId(anyLong(), any(HabitAssignStatDto.class)))
                .thenReturn(DEFAULT_HABIT_ASSIGN_MANAGEMENT_DTO);
        String json = """
                {
                  "status": "INPROGRESS"
                }
                """;
        mockMvc.perform(patch(ENDPOINT + "/{habitAssignId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .updateStatusByHabitAssignId(anyLong(), any(HabitAssignStatDto.class));
    }

    @Test
    void enrollHabitTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.enrollHabit(anyLong(), anyLong(), any(LocalDate.class), anyString()))
                .thenReturn(any(HabitAssignDto.class));
        mockMvc.perform(post(ENDPOINT + "/{habitAssignId}/enroll/{date}", 1L, "2024-10-27")
                        .param("lang", "en")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .enrollHabit(anyLong(), anyLong(), any(LocalDate.class), anyString());
    }

    @Test
    void unenrollHabitTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.unenrollHabit(anyLong(), anyLong(), any(LocalDate.class)))
                .thenReturn(any(HabitAssignDto.class));
        mockMvc.perform(post(ENDPOINT + "/{habitAssignId}/unenroll/{date}", 1L, "2024-10-27")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .unenrollHabit(anyLong(), anyLong(), any(LocalDate.class));
    }

    @Test
    void getHabitAssignBetweenDatesTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.findHabitAssignsBetweenDates(
                anyLong(),
                any(LocalDate.class),
                any(LocalDate.class),
                anyString())
        ).thenReturn(List.of(
                HabitsDateEnrollmentDto
                        .builder()
                        .enrollDate(LocalDate.now())
                        .build()
        ));
        mockMvc.perform(get(ENDPOINT + "/activity/{from}/to/{to}", "2024-10-27", "2024-10-28")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("lang", "en")
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .findHabitAssignsBetweenDates(
                        anyLong(),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        anyString()
                );
    }

    @Test
    void cancelHabitAssignTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.cancelHabitAssign(anyLong(), anyLong())).thenReturn(any(HabitAssignDto.class));
        mockMvc.perform(patch(ENDPOINT + "/cancel/{habitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .cancelHabitAssign(
                        anyLong(),
                        anyLong()
                );
    }

    @Test
    void getHabitAssignByHabitIdTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.findHabitAssignByUserIdAndHabitId(anyLong(), anyLong(), anyString()))
                .thenReturn(any(HabitAssignDto.class));
        mockMvc.perform(get(ENDPOINT + "/{habitId}/active", 1L)
                        .param("lang", "en")
                        .principal(DEFAULT_USER_VO::getEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .findHabitAssignByUserIdAndHabitId(
                        anyLong(),
                        anyLong(),
                        anyString()
                );
    }

    @Test
    void getCurrentUserHabitAssignsByIdAndAcquiredTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.getAllHabitAssignsByUserIdAndStatusNotCancelled(anyLong(), anyString()))
                .thenReturn(List.of(
                        HabitAssignDto.builder().id(1L).status(HabitAssignStatus.INPROGRESS).build(),
                        HabitAssignDto.builder().id(2L).build()
                ));

        mockMvc.perform(get(ENDPOINT + "/allForCurrentUser", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("lang", "en")
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());

        verify(habitAssignService, times(1))
                .getAllHabitAssignsByUserIdAndStatusNotCancelled(anyLong(), anyString());
    }

    @Test
    void deleteHabitAssignTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        mockMvc.perform(delete(ENDPOINT + "/delete/{habitAssignId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).deleteHabitAssign(anyLong(), anyLong());
    }

    @Test
    void updateShoppingListStatusTest() throws Exception {
        String json = """
                {
                  "habitAssignId": 0,
                  "userShoppingListItemId": 0,
                  "userShoppingListAdvanceDto": [
                    {
                      "id": 0,
                      "shoppingListItemId": 0,
                      "status": "ACTIVE",
                      "dateCompleted": "2024-10-27T11:37:48.807Z",
                      "content": "string",
                      "boolStatus": true
                    }
                  ]
                }
                """;
        mockMvc.perform(put(ENDPOINT + "/saveShoppingListForHabitAssign")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1))
                .updateUserShoppingListItem(any(UpdateUserShoppingListDto.class));
    }

    @Test
    void assignCustomTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.assignCustomHabitForUser(
                anyLong(),
                any(UserVO.class),
                any(HabitAssignCustomPropertiesDto.class))
        ).thenReturn(List.of(
                HabitAssignManagementDto.builder().id(1L).build(),
                HabitAssignManagementDto.builder().id(2L).build())
        );
        String json = """
                {
                  "habitAssignPropertiesDto": {
                    "duration": 56,
                    "defaultShoppingListItems": [
                      0
                    ]
                  },
                  "friendsIdsList": [
                    0
                  ]
                }
                """;
        mockMvc.perform(post(ENDPOINT + "/{habitId}/custom", 1L)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print()).andExpect(status().isCreated());
        verify(habitAssignService, times(1)).assignCustomHabitForUser(
                anyLong(),
                any(UserVO.class),
                any(HabitAssignCustomPropertiesDto.class)
        );
    }

    @Test
    void getAllHabitAssignsByHabitIdAndAcquiredTest() throws Exception {
        when(habitAssignService.getAllHabitAssignsByHabitIdAndStatusNotCancelled(anyLong(), anyString()))
                .thenReturn(List.of(
                                HabitAssignDto.builder().id(1L).status(HabitAssignStatus.ACQUIRED).build(),
                                HabitAssignDto.builder().id(2L).status(HabitAssignStatus.REQUESTED).build()
                        )
                );
        mockMvc.perform(get(ENDPOINT + "/{habitId}/all", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk());
        verify(habitAssignService, times(1)).getAllHabitAssignsByHabitIdAndStatusNotCancelled(
                anyLong(),
                anyString()
        );
    }

    @Test
    void getInprogressHabitAssignOnDateTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.findInprogressHabitAssignsOnDate(anyLong(), any(LocalDate.class), anyString()))
                .thenReturn(List.of(
                                HabitAssignDto.builder().id(1L).status(HabitAssignStatus.ACQUIRED).build(),
                                HabitAssignDto.builder().id(2L).status(HabitAssignStatus.REQUESTED).build()
                        )
                );
        mockMvc.perform(get(ENDPOINT + "/active/{date}", "2024-10-27")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("lang", "en")
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).findInprogressHabitAssignsOnDate(
                anyLong(),
                any(LocalDate.class),
                anyString()
        );
    }

    @Test
    void getUsersHabitByHabitAssignIdTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.findHabitByUserIdAndHabitAssignId(anyLong(), anyLong(), anyString()))
                .thenReturn(HabitDto.builder().id(1L).build());
        mockMvc.perform(get(ENDPOINT + "/{habitAssignId}/more", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("lang", "en")
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).findHabitByUserIdAndHabitAssignId(
                anyLong(),
                anyLong(),
                anyString()
        );
    }

    @Test
    void updateProgressNotificationHasDisplayedTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        mockMvc.perform(put(ENDPOINT + "/{habitAssignId}/updateProgressNotificationHasDisplayed", 1L)
                        .principal(DEFAULT_USER_VO::getEmail)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).updateProgressNotificationHasDisplayed(
                anyLong(),
                anyLong()
        );
    }

    @Test
    void getUserShoppingAndCustomShoppingListsTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService.getUserShoppingAndCustomShoppingLists(anyLong(), anyLong(), anyString()))
                .thenReturn(any(UserShoppingAndCustomShoppingListsDto.class));
        mockMvc.perform(get(ENDPOINT + "/{habitAssignId}/allUserAndCustomList", 1L)
                        .param("lang", "en")
                        .principal(DEFAULT_USER_VO::getEmail)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).getUserShoppingAndCustomShoppingLists(
                anyLong(),
                anyLong(),
                anyString()
        );
    }

    @Test
    void updateUserAndCustomShoppingLists() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        String json = """
                {
                  "userShoppingListItemDto": [
                    {
                      "id": 1,
                      "text": "string",
                      "status": "ACTIVE"
                    }
                  ],
                  "customShoppingListItemDto": [
                    {
                      "id": 1,
                      "text": "string",
                      "status": "ACTIVE"
                    }
                  ]
                }
                """;
        mockMvc.perform(put(ENDPOINT + "/{habitAssignId}/allUserAndCustomList", 1L)
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("lang", "en")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).fullUpdateUserAndCustomShoppingLists(
                anyLong(),
                anyLong(),
                any(UserShoppingAndCustomShoppingListsDto.class),
                anyString()
        );
    }

    @Test
    void getListOfUserAndCustomShoppingListsInprogress() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(DEFAULT_USER_VO);
        when(habitAssignService
                .getListOfUserAndCustomShoppingListsWithStatusInprogress(anyLong(), anyString()))
                .thenReturn(List.of(
                        new UserShoppingAndCustomShoppingListsDto(),
                        new UserShoppingAndCustomShoppingListsDto())
                );
        mockMvc.perform(get(ENDPOINT + "/allUserAndCustomShoppingListsInprogress")
                        .principal(DEFAULT_USER_VO::getEmail)
                        .param("lang", "en")
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
        verify(habitAssignService, times(1)).getListOfUserAndCustomShoppingListsWithStatusInprogress(
                anyLong(),
                anyString()
        );
    }
}
