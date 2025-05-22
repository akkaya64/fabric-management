package com.fabric.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@SuperBuilder
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> extends ApiResponse<List<T>> {
    private PageInfo pageInfo;
    
    public static <T> PagedResponse<T> of(List<T> data, PageInfo pageInfo) {
        return PagedResponse.<T>builder()
                .success(true)
                .data(data)
                .pageInfo(pageInfo)
                .build();
    }
    
    public static <T> PagedResponse<T> of(List<T> data, PageInfo pageInfo, String message) {
        return PagedResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .pageInfo(pageInfo)
                .build();
    }

    @Data
    @Builder
    @Jacksonized
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
        private boolean isFirst;
        private boolean isLast;
        private int numberOfElements;
        
        public static PageInfo from(org.springframework.data.domain.Page<?> page) {
            return PageInfo.builder()
                    .page(page.getNumber())
                    .size(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .hasNext(page.hasNext())
                    .hasPrevious(page.hasPrevious())
                    .isFirst(page.isFirst())
                    .isLast(page.isLast())
                    .numberOfElements(page.getNumberOfElements())
                    .build();
        }
    }
}