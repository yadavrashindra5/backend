package com.lcwd.electronic.store.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  PageableResponse <T>{
    public List<T> content;
    public int pageNumber;
    public int pageSize;
    public int totalElements;
    public int totalPages;
    public boolean lastPage;
}
