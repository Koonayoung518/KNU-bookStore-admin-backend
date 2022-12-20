package com.knubookStore.knubookStoreadmin.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubookStore.knubookStoreadmin.core.Type.BookType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BookDocumentationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookRepository bookRepository;

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

    @Transactional
    Book saveBook(){
        Book book = Book.builder()
                .isbn("9788966262281")
                .title("이펙티브 자바")
                .publisher("인사이트")
                .author("조슈아 블로크")
                .price(32400)
                .image("url")
                .pubdate("20181101")
                .stock(3)
                .type(BookType.Registered)
                .build();
        book = bookRepository.save(book);
        return book;
    }

    @Test
    @Transactional
    void 책_등록_API_명세서_등록() throws Exception {
        RequestBook.RegisterBookDto dto = RequestBook.RegisterBookDto.builder()
                .isbn("9788966262281")
                .title("title")
                .publisher("publisher")
                .author("author")
                .price(32400)
                .image("url")
                .pubdate("20181101")
                .stock(3)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/admin/manage/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-post",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        requestFields(
                                fieldWithPath("isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("책 표지 이미지 url"),
                                fieldWithPath("pubdate").type(JsonFieldType.STRING).description("출판일"),
                                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고")
                        ),
                        responseFields(
                                ApiDocumentUtils.getDefaultResponseMessage()
                        )
                ));
    }

    @Test
    @Transactional
    void 책_전체_목록_조회_API_명세서_등록() throws Exception {
        Book book = saveBook();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/manage/book"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-get-all",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
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

    @Test
    @Transactional
    void 제목_기반_책_목록_조회_API_명세서_등록() throws Exception {
        Book book = saveBook();

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/admin/manage/book/title")
                .param("title", book.getTitle())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-get-title",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        requestParameters(parameterWithName("title").description("제목 일부")),
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

    @Test
    @Transactional
    void 책_조회_API_명세서_등록() throws Exception {
        Book book = saveBook();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/manage/book/{isbn}", book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-get",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        pathParameters(parameterWithName("isbn").description("책 isbn")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.registrationDate").type(JsonFieldType.STRING).description("책 등록일"),
                                fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("data.author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("data.image").type(JsonFieldType.STRING).description("책 표지 이미지 url"),
                                fieldWithPath("data.pubdate").type(JsonFieldType.STRING).description("출판일"),
                                fieldWithPath("data.stock").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("data.bookType").type(JsonFieldType.STRING).description("책 등록 여부")
                        )
                ));
    }

    @Test
    @Transactional
    void 책_정보_수정_API_명세서_등록() throws Exception {
        Book book = saveBook();

        RequestBook.UpdateBookDto dto = RequestBook.UpdateBookDto.builder()
                .isbn(book.getIsbn())
                .price(10000)
                .stock(13)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/admin/manage/book")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-put",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        requestFields(
                                fieldWithPath("isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고")
                        ),
                        responseFields(
                                ApiDocumentUtils.getDefaultResponseMessage()
                        )
                ));
    }

    @Test
    @Transactional
    void 책_정보_삭제_API_명세서_등록() throws Exception {
        Book book = saveBook();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/manage/book/{isbn}", book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("book-delete",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        pathParameters(parameterWithName("isbn").description("책 isbn")),
                        responseFields(
                                ApiDocumentUtils.getDefaultResponseMessage()
                        )
                ));
    }

    @Test
    @Transactional
    void 사이트용_책_전체_조회_API_명세서_등록() throws Exception {
        Book book = saveBook();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/book"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("book-get-site",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.content.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.content.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.content.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("data.content.[].author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("data.content.[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("data.content.[].image").type(JsonFieldType.STRING).description("책 표지 이미지 url"),
                                fieldWithPath("data.content.[].stock").type(JsonFieldType.NUMBER).description("재고"),

                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),


                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 개수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("한 페이지 당 보여지는 요소 개수"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("요소 개수"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요소 개수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
                        )
                ));
    }

    @Test
    @Transactional
    void 사이트용_책_제목_기반_목록_조회_API_명세서_등록() throws Exception {
        Book book = saveBook();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/book/title?title=" + book.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("book-get-site-title",
                        ApiDocumentUtils.setRequest(),
                        ApiDocumentUtils.setResponse(),
                        requestParameters(parameterWithName("title").description("제목 일부")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.content.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.content.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.content.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("data.content.[].author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("data.content.[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("data.content.[].image").type(JsonFieldType.STRING).description("책 표지 이미지 url"),
                                fieldWithPath("data.content.[].stock").type(JsonFieldType.NUMBER).description("재고"),

                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),


                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 개수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("한 페이지 당 보여지는 요소 개수"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("요소 개수"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요소 개수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
                        )
                ));
    }
}
