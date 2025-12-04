package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//The API entry of the front-end request of the notice module
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    //NEWLY ADDED
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice){
        noticeService.add(notice);
        return Result.success();
    }

    //UPDATE
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return Result.success();
    }

    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        noticeService.deleteById(id);
        return Result.success();
    }

    //PAGINATED QUERIES
    @GetMapping("/selectPage")
    public Result selectPage(Notice notice,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Notice> pageInfo = noticeService.selectPage(notice,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    //QUERY ALL
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> list = noticeService.selectAll();
        return Result.success(list);
    }
}
