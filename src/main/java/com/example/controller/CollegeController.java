package com.example.controller;

import com.example.common.Result;
import com.example.entity.College;
import com.example.service.CollegeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//The API entry of the front-end request of the college module
@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    private CollegeService collegeService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody College college){
        collegeService.add(college);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody College college){
        collegeService.updateById(college);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collegeService.deleteById(id);
        return Result.success();
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(College college,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<College> pageInfo = collegeService.selectPage(college,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    //QUERY ALL
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<College> list = collegeService.selectAll();
        return Result.success(list);
    }
}
