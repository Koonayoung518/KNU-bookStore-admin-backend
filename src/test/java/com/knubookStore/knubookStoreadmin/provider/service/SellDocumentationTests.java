package com.knubookStore.knubookStoreadmin.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubookStore.knubookStoreadmin.core.Type.BookType;
import com.knubookStore.knubookStoreadmin.core.Type.PaymentType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.BookStoreInfo;
import com.knubookStore.knubookStoreadmin.entity.History;
import com.knubookStore.knubookStoreadmin.entity.Sell;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.BookStoreInfoRepository;
import com.knubookStore.knubookStoreadmin.repository.HistoryRepository;
import com.knubookStore.knubookStoreadmin.repository.SellRepository;
import com.knubookStore.knubookStoreadmin.web.dto.BookInfo;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBookStoreInfo;
import com.knubookStore.knubookStoreadmin.web.dto.RequestSell;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SellDocumentationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    SellRepository sellRepository;
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void before(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @Transactional
    void 판매할_책_정보_조회_API_명세서_등록() throws Exception{
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
        bookRepository.save(book);
        String isbn = "9788966262281";

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/sell/book/{isbn}", isbn));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("sell-get-book",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("isbn").description("책 isbn")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.unitPrice").type(JsonFieldType.NUMBER).description("단가")
                                )
                ));
    }

    @Test
    @Transactional
    void 판매내역_상세_조회_API_명세서_등록() throws Exception{
        History history = History.builder()
                .sellDate(LocalDateTime.now())
                .totalPrice(64800)
                .payment(PaymentType.CASH)
                .money(70000)
                .change(5200)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("9788966262281")
                .title("이펙티브 자바")
                .unitPrice(32400)
                .amount(2)
                .total(64800)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);

        Long id = history.getId();

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/history/{id}", id));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("sell-get-history-detail",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("판매내역 번호")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.sellDate").type(JsonFieldType.STRING).description("판매 날짜"),
                                fieldWithPath("data.bookList.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("data.bookList.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.bookList.[].unitPrice").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.bookList.[].amount").type(JsonFieldType.NUMBER).description("수량"),
                                fieldWithPath("data.bookList.[].total").type(JsonFieldType.NUMBER).description("금액"),

                                fieldWithPath("data.payment").type(JsonFieldType.STRING).description("결제수단"),
                                fieldWithPath("data.money").type(JsonFieldType.NUMBER).description("받은 금액"),
                                fieldWithPath("data.change").type(JsonFieldType.NUMBER).description("거스름돈"),
                                fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER).description("총금액")
                                )
                ));
    }

    @Test
    @Transactional
    void 판매내역_저장_API_명세서_등록() throws Exception{
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
        bookRepository.save(book);

        List<BookInfo> list = new ArrayList<>();
        BookInfo bookInfo = BookInfo.builder()
                .isbn("9788966262281")
                .title("이펙티브 자바")
                .amount(2)
                .unitPrice(32400)
                .total(64800)
                .build();
        list.add(bookInfo);

        RequestSell.RegisterSellBookHistoryDto dto = RequestSell.RegisterSellBookHistoryDto.builder()
                .bookList(list)
                .totalPrice(64800)
                .payment("CASH")
                .money(70000)
                .change(5200)
                .build();

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders
                .post("/admin/sell/book")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("sell-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("bookList.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                fieldWithPath("bookList.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("bookList.[].unitPrice").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("bookList.[].amount").type(JsonFieldType.NUMBER).description("수량"),
                                fieldWithPath("bookList.[].total").type(JsonFieldType.NUMBER).description("금액"),

                                fieldWithPath("payment").type(JsonFieldType.STRING).description("결제수단"),
                                fieldWithPath("money").type(JsonFieldType.NUMBER).description("받은 금액"),
                                fieldWithPath("change").type(JsonFieldType.NUMBER).description("거스름돈"),
                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총금액")
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
    void 판매내역_목록_조회_API_명세서_등록() throws Exception{
        History history = History.builder()
                .sellDate(LocalDateTime.now())
                .totalPrice(64800)
                .payment(PaymentType.CASH)
                .money(70000)
                .change(5200)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("9788966262281")
                .title("이펙티브 자바")
                .unitPrice(32400)
                .amount(2)
                .total(64800)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/history"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("sell-get-history",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.content.[].id").type(JsonFieldType.NUMBER).description("판매 번호"),
                                fieldWithPath("data.content.[].sellDate").type(JsonFieldType.STRING).description("판매 날짜"),
                                fieldWithPath("data.content.[].totalPrice").type(JsonFieldType.NUMBER).description("총 금액"),

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
    void 판매내역_목록_조건_조회_API_명세서_등록() throws Exception{
        History history = History.builder()
                .sellDate(LocalDateTime.now())
                .totalPrice(64800)
                .payment(PaymentType.CASH)
                .money(70000)
                .change(5200)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("9788966262281")
                .title("이펙티브 자바")
                .unitPrice(32400)
                .amount(2)
                .total(64800)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);

        String type = "DEFAULT";
        String startDate = "2022-10-15T01:49:43.560073";
        String endDate ="2022-10-17T01:49:43.560073";
        Integer price = 10000;
        Integer page = 0;

        ResultActions result =  mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/history/type"+
                "?type="+type+"&startDate="+startDate+"&endDate="+ endDate +"&price=" +price+"&page="+page));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("sell-get-history-condition",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("type").description("검색 타입"),
                                parameterWithName("price").description("검색할 금액"),
                                parameterWithName("startDate").description("검색할 시작 날짜"),
                                parameterWithName("endDate").description("검색할 종료 날짜"),
                                parameterWithName("page").description("page 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.content.[].id").type(JsonFieldType.NUMBER).description("판매 번호"),
                                fieldWithPath("data.content.[].sellDate").type(JsonFieldType.STRING).description("판매 날짜"),
                                fieldWithPath("data.content.[].totalPrice").type(JsonFieldType.NUMBER).description("총 금액"),

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
