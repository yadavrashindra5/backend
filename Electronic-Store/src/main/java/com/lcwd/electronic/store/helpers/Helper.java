package com.lcwd.electronic.store.helpers;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.response.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
        // U is entity type and V is dto type
        List<U> userList = page.getContent();

        List<V> list = userList.stream().map(user -> new ModelMapper().map(user,type)).toList();

        PageableResponse<V>pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(list);
        pageableResponse.setPageNumber(page.getNumber()+1);
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setLastPage(page.isLast());
        return pageableResponse;
    }
}
