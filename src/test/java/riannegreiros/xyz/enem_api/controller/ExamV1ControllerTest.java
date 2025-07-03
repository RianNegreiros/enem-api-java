package riannegreiros.xyz.enem_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import riannegreiros.xyz.enem_api.dto.*;
import riannegreiros.xyz.enem_api.service.ExamService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ExamV1ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExamService examService;

    @InjectMocks
    private ExamV1Controller examV1Controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(examV1Controller).build();
    }

    @Test
    void getExams_returnsOk() throws Exception {

        when(examService.getExams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/exams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getExamDetails_returnsOk() throws Exception {

        ExamDetailDto examDetail = new ExamDetailDto(2020, "title", null, null, null);
        when(examService.getExamDetails(2020)).thenReturn(examDetail);

        mockMvc.perform(get("/api/v1/exams/2020"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getExamDetails_notFound() throws Exception {

        when(examService.getExamDetails(2020)).thenThrow(new RuntimeException("Exam not found"));

        mockMvc.perform(get("/api/v1/exams/2020"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQuestions_returnsOk() throws Exception {

        QuestionsResponseDto response = new QuestionsResponseDto(null, Collections.emptyList());
        when(examService.getQuestions(eq(2020), anyInt(), anyInt(), any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/exams/2020/questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getQuestions_limitTooHigh_returnsBadRequest() throws Exception {

        mockMvc.perform(get("/api/v1/exams/2020/questions?limit=100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuestionsByIndices_returnsOk() throws Exception {

        QuestionIndicesDto indicesDto = new QuestionIndicesDto();
        indicesDto.setIndices(List.of(1, 2, 3));
        QuestionsResponseDto response = new QuestionsResponseDto(null, Collections.emptyList());
        when(examService.getQuestionsByIndices(eq(2020), eq(List.of(1, 2, 3)), anyInt(), anyInt(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/exams/2020/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(indicesDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getQuestionsByIndices_emptyIndices_returnsBadRequest() throws Exception {

        QuestionIndicesDto indicesDto = new QuestionIndicesDto();
        indicesDto.setIndices(Collections.emptyList());

        mockMvc.perform(post("/api/v1/exams/2020/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(indicesDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuestion_returnsOk() throws Exception {

        QuestionDetailDto question = new QuestionDetailDto(1, "text", Collections.emptyMap(),
                "A", "discipline", "language");
        when(examService.getQuestion(eq(2020), eq(1), any())).thenReturn(question);

        mockMvc.perform(get("/api/v1/exams/2020/questions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getQuestion_notFound() throws Exception {

        when(examService.getQuestion(eq(2020), eq(1), any())).thenThrow(new RuntimeException("Question not found"));

        mockMvc.perform(get("/api/v1/exams/2020/questions/1"))
                .andExpect(status().isNotFound());
    }
}
