package com.sbtest.mapper;

import com.sbtest.dto.User;
import org.mapstruct.Mapper;

/**
 * Created by zhangyaping on 17/8/7.
 */
@Mapper
public interface UserMapper {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findById(int id);
}
