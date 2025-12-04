package com.example.controller;

import com.example.common.Result;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

//The API entry of the front-end request of the student module
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody Student student){
        studentService.add(student);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody Student student){
        studentService.updateById(student);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        studentService.deleteById(id);
        return Result.success();
    }

    //QUERY BY ID
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Student student = studentService.selectById(id);
        return Result.success(student);
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(Student student,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Student> pageInfo = studentService.selectPage(student,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
