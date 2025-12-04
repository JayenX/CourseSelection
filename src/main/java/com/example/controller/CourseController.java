package com.example.controller;

import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//The API entry of the front-end request of the course module
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody Course course){
        courseService.add(course);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody Course course){
        courseService.updateById(course);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        courseService.deleteById(id);
        return Result.success();
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(Course course,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Course> pageInfo = courseService.selectPage(course,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    //QUERY ALL
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Course> list = courseService.selectAll();
        return Result.success(list);
    }
}
