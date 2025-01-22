package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class CommentsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentsService commentsService;

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetComments() throws Exception {
        // given
        Comments comments = new Comments();
        comments.setCount(1);
        comments.setResults(List.of(new Comment()));

        // when
        when(commentsService.getComments(any(Integer.class))).thenReturn(comments);

        // then
        mockMvc.perform(get("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldAddComment() throws Exception {
        // given
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Очень дорого!");

        Comment commentResponse = new Comment();
        commentResponse.setPk(1);
        commentResponse.setText("Очень дорого!");

        // when
        when(commentsService.addComment(eq(1), any(CreateOrUpdateComment.class), eq("test@mail.ru")))
                .thenReturn(commentResponse);

        // then
        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("Очень дорого!"));
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = "ADMIN")
    public void shouldDeleteComment() throws Exception {
        // given
        doNothing().when(commentsService).deleteComment(1, 1);

        // when
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(status().isOk());

        // then
        verify(commentsService, times(1)).deleteComment(1, 1);
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = "ADMIN")
    public void shouldUpdateComment() throws Exception {
        // given
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Дорого");

        Comment commentResponse = new Comment();
        commentResponse.setPk(1);
        commentResponse.setText("Дорого");

        // when
        when(commentsService.updateComment(eq(1), eq(1), any(CreateOrUpdateComment.class)))
                .thenReturn(commentResponse);

        // then
        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("Дорого"));

        verify(commentsService, times(1)).updateComment(1, 1, createOrUpdateComment);
    }

}
