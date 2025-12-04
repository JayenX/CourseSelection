package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Teacher;
import com.example.exception.CustomException;
import com.example.mapper.TeacherMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//Business-level processing of teacher information
@Service
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;
    public void add(Teacher teacher) {
        // Do some logical processing
        Teacher dbTeacher=teacherMapper.selectByUsername(teacher.getUsername());
        if (ObjectUtil.isNotEmpty(dbTeacher)){
            throw new CustomException("The username already exists");
        }
        if (ObjectUtil.isEmpty(teacher.getPassword())){
            teacher.setPassword("123456");
        }
        teacher.setRole("TEACHER");
        teacherMapper.insert(teacher);
    }

    public PageInfo<Teacher> selectPage(Teacher teacher,Integer pageNum, Integer pageSize) {
        List<Teacher> list;
        PageHelper.startPage(pageNum,pageSize);
        if (ObjectUtil.isNotEmpty(teacher.getName())){
            list = teacherMapper.selectByName(teacher.getName());
        }else{
            list = teacherMapper.selectAll();
        }
        return PageInfo.of(list);
    }

    public void updateById(Teacher teacher) {
        teacherMapper.updateById(teacher);
    }

    public void deleteById(Integer id) {
        teacherMapper.deleteById(id);
    }

    /**
     * LOGIN
     */
    public Teacher login(Account account) {
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher)) {
            throw new CustomException("The user does not exist");
        }
        if (!account.getPassword().equals(dbTeacher.getPassword())) {
            throw new CustomException("The account number or password is incorrect");
        }
        return dbTeacher;
    }

    /**
     * Change your password
     */
    public void updatePassword(Account account) {
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher)) {
            throw new CustomException("The user does not exist");
        }
        if (!account.getPassword().equals(dbTeacher.getPassword())) {
            throw new CustomException("The original password is incorrect");
        }
        dbTeacher.setPassword(account.getNewPassword());
        teacherMapper.updateById(dbTeacher);
    }

    public List<Teacher> selectAll() {
        return teacherMapper.selectAll();
    }
}
