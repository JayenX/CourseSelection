package com.example.controller;

import com.example.common.Result;
import com.example.entity.Teacher;
import com.example.service.TeacherService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//The API entry of the front-end request of the teacher module
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody Teacher teacher){
        teacherService.add(teacher);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody Teacher teacher){
        teacherService.updateById(teacher);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        teacherService.deleteById(id);
        return Result.success();
    }

    //QUERY ALL
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Teacher> list = teacherService.selectAll();
        return Result.success(list);
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(Teacher teacher,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Teacher> pageInfo = teacherService.selectPage(teacher,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
