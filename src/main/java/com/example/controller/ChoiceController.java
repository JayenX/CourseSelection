package com.example.controller;

import com.example.common.Result;
import com.example.entity.Choice;
import com.example.entity.Course;
import com.example.service.ChoiceService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//The API entry of the front-end request of the choice module
@RestController
@RequestMapping("/choice")
public class ChoiceController {

    @Resource
    private ChoiceService choiceService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody Course course){
        choiceService.add(course);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody Choice choice){
        choiceService.updateById(choice);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        choiceService.deleteById(id);
        return Result.success();
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(Choice choice,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Choice> pageInfo = choiceService.selectPage(choice,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    //QUERY ALL
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Choice> list = choiceService.selectAll();
        return Result.success(list);
    }
}
