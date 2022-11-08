package com.knubookStore.knubookStoreadmin.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
public class BookDocumentationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookRepository bookRepository;

    @Test
    void getAllBook() throws Exception{
        Book book = Book.builder()
                .isbn("1234")
                .title("제목")
                .publisher("출판사")
                .image("url")
                .stock(3)
                .build();
        bookRepository.save(book);

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/manage/book"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("book-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("data.[].image").type(JsonFieldType.STRING).description("책 표지 이미지 url"),
                                fieldWithPath("data.[].stock").type(JsonFieldType.NUMBER).description("재고")
                            )
                        ));
    }

}
