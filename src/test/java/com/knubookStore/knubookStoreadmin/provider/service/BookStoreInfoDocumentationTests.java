package com.knubookStore.knubookStoreadmin.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubookStore.knubookStoreadmin.core.Type.BookType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.BookStoreInfo;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.BookStoreInfoRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBookStoreInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BookStoreInfoDocumentationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookStoreInfoRepository bookStoreInfoRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @Transactional
    void 서점_정보_등록_API_명세서_등록() throws Exception{
        RequestBookStoreInfo.RegisterBookStoreInfoDto dto = RequestBookStoreInfo.RegisterBookStoreInfoDto.builder()
                .operatingTime("9:00 - 17:00")
                .phone("031-280-2222")
                .location("인사관 1층")
                .notice("수요일에 책 입고됩니다.")
                .build();
//        String requestBody = "{" +
//                "        \"operatingTime\": \"9:00 - 17:00\"," +
//                "        \"phone\": \"031-111-2222\"," +
//                "        \"location\": \"인사관 1층\"," +
//                "        \"notice\": \"수요일에 책 입고됩니다.\" " +
//                "    }";
//
//        Map<String, Object> requestDto = new HashMap<>();
//        requestDto.put("operatingTime", "9:00 - 17:00");
//        requestDto.put("phone", "031-280-2222");
//        requestDto.put("location", "인사관 1층");
//        requestDto.put("notice", "수요일에 책 입고됩니다.");

        ResultActions result =  this.mockMvc.perform(post("/admin/bookStoreInfo")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        );

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("bookStoreInfo-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("operatingTime").description("운영시간"),
                                fieldWithPath("phone").description("연락처"),
                                fieldWithPath("location").description("위치"),
                                fieldWithPath("notice").description("공지사항")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @Transactional
    void 서점_정보_조회_API_명세서_등록() throws Exception{
        BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                .operatingTime("9:00 - 17:00")
                .phone("031-280-2222")
                .location("인사관 1층")
                .notice("수요일에 책 입고됩니다.")
                .build();
        bookStoreInfoRepository.save(bookStoreInfo);

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/bookStoreInfo"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("bookStoreInfo-get",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.operatingTime").type(JsonFieldType.STRING).description("운영시간"),
                                fieldWithPath("data.phone").type(JsonFieldType.STRING).description("연락처"),
                                fieldWithPath("data.location").type(JsonFieldType.STRING).description("위치"),
                                fieldWithPath("data.notice").type(JsonFieldType.STRING).description("공지사항")
                        )
                ));
    }

    @Test
    @Transactional
    void 서점_정보_삭제_API_명세서_등록() throws Exception{
        BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                .operatingTime("9:00 - 17:00")
                .phone("031-280-2222")
                .location("인사관 1층")
                .notice("수요일에 책 입고됩니다.")
                .build();
        bookStoreInfoRepository.save(bookStoreInfo);

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/bookStoreInfo"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("bookStoreInfo-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
}
