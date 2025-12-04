package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Choice;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.ChoiceMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//Business-level processing of choice information
@Service
public class ChoiceService {

    @Resource
    private ChoiceMapper choiceMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private StudentMapper studentMapper;

    public void add(Course course) {
        //1.Is the course full?
        if (course.getNum().equals(course.getAlreadyNum())){
            throw new CustomException("The course is full, please select another course");
        }
        //2.Whether the student has already taken the course？
        List<Choice> list = choiceMapper.selectByCourseIdAndStudentId(course.getId(), course.getStudentId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("This course has already been selected, please choose another course");
        }
        //3.Make course selections
        Choice choice = new Choice();
        choice.setName(course.getName());
        choice.setTeacherId(course.getTeacherId());
        choice.setStudentId(course.getStudentId());
        choice.setCourseId(course.getId());
        choiceMapper.insert(choice);
        //4.The number of students selected for this course is increased by one
        course.setAlreadyNum(course.getAlreadyNum() + 1);
        courseMapper.updateById(course);
        //5.Students are given additional credits for the corresponding courses
        Student student = studentMapper.selectById(course.getStudentId());
        student.setScore(student.getScore() + course.getScore());
        studentMapper.updateById(student);
    }

    public PageInfo<Choice> selectPage(Choice choice,Integer pageNum, Integer pageSize) {
        List<Choice> list;
        PageHelper.startPage(pageNum,pageSize);
        if (ObjectUtil.isNotEmpty(choice.getStudentId())){
            //The logged-in role is student
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndStudentId(choice.getName(), choice.getStudentId());
            } else {
                list = choiceMapper.selectAllByStudentId(choice.getStudentId());
            }
        } else if (ObjectUtil.isNotEmpty(choice.getTeacherId())) {
            //The logged-in role is teacher
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndTeacherId(choice.getName(), choice.getTeacherId());
            } else {
                list = choiceMapper.selectAllByTeacherId(choice.getTeacherId());
            }
        } else {
            //The login role is administrator
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByName(choice.getName());
            } else {
                list = choiceMapper.selectAll();
            }
        }
        return PageInfo.of(list);
     }


    public void updateById(Choice choice) {
        choiceMapper.updateById(choice);
    }

    public void deleteById(Integer id) {
        Choice choice = choiceMapper.selectById(id);
        choiceMapper.deleteById(id);
        //After canceling the course registration, the number of selected courses will be reduced by one
        Course course = courseMapper.selectById(choice.getCourseId());
        course.setAlreadyNum(course.getAlreadyNum() - 1 );
        courseMapper.updateById(course);
        //The credits of the corresponding course will also be subtracted accordingly
        Course dbCourse = courseMapper.selectById(choice.getCourseId());
        Student student = studentMapper.selectById(choice.getStudentId());
        student.setScore(student.getScore() - dbCourse.getScore());
        studentMapper.updateById(student);
    }


    public List<Choice> selectAll() {
        return choiceMapper.selectAll();
    }
}
