package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//Business-level processing of student information
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;
    public void add(Student student) {
        // Do some logical processing
        Student dbStudent=studentMapper.selectByUsername(student.getUsername());
        if (ObjectUtil.isNotEmpty(dbStudent)){
            throw new CustomException("The username already exists");
        }
        if (ObjectUtil.isEmpty(student.getPassword())){
            student.setPassword("123456");
        }
        if (ObjectUtil.isEmpty(student.getName())){
            student.setName(student.getUsername());
        }
        student.setRole("STUDENT");
        student.setScore(0);
        studentMapper.insert(student);
    }

    public PageInfo<Student> selectPage(Student student,Integer pageNum, Integer pageSize) {
        List<Student> list;
        PageHelper.startPage(pageNum,pageSize);
        if (ObjectUtil.isNotEmpty(student.getName())){
            list = studentMapper.selectByName(student.getName());
        }else{
            list = studentMapper.selectAll();
        }
        return PageInfo.of(list);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * LOGIN
     */
    public Student login(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException("The user does not exist");
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException("The account number or password is incorrect");
        }
        return dbStudent;
    }

    /**
     * Register
     */
    public void register(Account account) {
        Student student = new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        add(student);
    }

    /**
     * Change your password
     */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException("The user does not exist");
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException("The original password is incorrect");
        }
        dbStudent.setPassword(account.getNewPassword());
        studentMapper.updateById(dbStudent);
    }

    /**
     * Query by id
     */
    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }
}
