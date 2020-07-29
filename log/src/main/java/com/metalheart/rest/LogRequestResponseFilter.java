package com.metalheart.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.RestRequestInfo;
import com.metalheart.model.RestResponseInfo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogRequestResponseFilter implements Filter {

    @Autowired
    private ObjectMapper mapper;

    private int maxPayloadLength = 10000;
    private boolean includeRequest = true;
    private boolean includeRequestHeaders = true;
    private boolean includeResponse = false;
    private boolean includeResponseHeaders = false;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        var requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        var responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(requestWrapper, responseWrapper);

        String requestURI = requestWrapper.getRequestURI();
        String method = requestWrapper.getMethod();

        RestRequestInfo requestInfo = includeRequest ? getRestRequestInfo(requestWrapper) : null;
        RestResponseInfo responseInfo = getRestResponseInfo(responseWrapper);

        try {

            responseWrapper.copyBodyToResponse();

            log.info(String.format("%s %s", method, requestURI), requestInfo, responseInfo);

        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }


    @Override
    public void destroy() {

    }

    private RestResponseInfo getRestResponseInfo(ContentCachingResponseWrapper response) {

        RestResponseInfo responseInfo = RestResponseInfo.builder()
            .status(response.getStatus())
            .build();

        if (includeResponse) {
            responseInfo.setPayload(getMessagePayload(response));
        }

        if (includeResponseHeaders) {
            Map<String, String> headers = new HashMap<>();
            for (String header : response.getHeaderNames()) {
                headers.put(header, response.getHeader(header));
            }
            responseInfo.setHeaders(headers);
        }

        return responseInfo;
    }

    private RestRequestInfo getRestRequestInfo(ContentCachingRequestWrapper request) {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RestRequestInfo requestInfo = RestRequestInfo.builder()
            .method(method)
            .requestURI(requestURI)
            .payload(getMessagePayload(request))
            .build();

        if (includeRequestHeaders) {
            Map<String, String> headers = new HashMap<>();
            for (String header : Collections.list(request.getHeaderNames())) {
                headers.put(header, request.getHeader(header));
            }
            requestInfo.setHeaders(headers);
        }
        return requestInfo;
    }

    private String getMessagePayload(HttpServletRequest request) {
        var wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            String length = getMessage(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            if (length != null) {
                return length;
            }
        }
        return null;
    }

    private String getMessagePayload(HttpServletResponse response) {

        var wrapper = (ContentCachingResponseWrapper) response;

        if (wrapper != null) {
            String length = getMessage(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            if (length != null) {
                return length;
            }
        }
        return null;
    }

    private String getMessage(byte[] buf, String characterEncoding) {
        if (buf.length > 0) {
            int length = Math.min(buf.length, maxPayloadLength);
            try {

                String payload = new String(buf, 0, length, characterEncoding);
                return escapePassword(payload);

            } catch (Exception ex) {
                return "[unknown]";
            }
        }
        return null;
    }

    private String escapePassword(String payload) throws Exception {
        Map<String, Object> map = mapper.readValue(payload, Map.class);
        map.forEach((key, value) -> {

            if ("password".equals(key) || "confirmPassword".equals(key)) {
                map.put(key, (isNull(value) || isEmpty(value.toString()) ? "<null>" : "<non-null>"));
            }
        });
        return mapper.writeValueAsString(map);
    }
}
