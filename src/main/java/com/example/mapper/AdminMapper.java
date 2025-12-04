package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operate the data interface related to admin
*/
public interface AdminMapper {

    /**
      * NEWLY ADDED
    */
    int insert(Admin admin);

    /**
      * DELETION
    */
    @Delete("delete from admin where id = #{id}")
    int deleteById(Integer id);

    /**
      * MODIFICATION
    */
    int updateById(Admin admin);

    /**
      * Query by ID
    */
    @Select("select * from admin where id = #{id}")
    Admin selectById(Integer id);

    /**
      * Query all
    */
    List<Admin> selectAll(Admin admin);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);

}