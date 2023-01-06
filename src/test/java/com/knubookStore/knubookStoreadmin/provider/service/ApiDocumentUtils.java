package com.knubookStore.knubookStoreadmin.provider.service;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface ApiDocumentUtils {
    static OperationRequestPreprocessor setRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("http")
                        .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                        .removePort(), prettyPrint());
    }

    static OperationResponsePreprocessor setResponse() {
        return preprocessResponse(prettyPrint());
    }

    static FieldDescriptor[] getDefaultResponseMessage() {
        FieldDescriptor[] responseMessage = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
        };


        return responseMessage;
    }

}
