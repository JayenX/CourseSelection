package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;


    /**
     * The default request interface
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * LOGIN
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            return Result.success(adminService.login(account));
        }
        if ("TEACHER".equals(account.getRole())) {
            return Result.success(teacherService.login(account));
        }
        if ("STUDENT".equals(account.getRole())) {
            return Result.success(studentService.login(account));
        }
        return Result.success();
    }

    /**
     * REGISTRATION
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        studentService.register(account);
        return Result.success();
    }

    /**
     * Change your password
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("TEACHER".equals(account.getRole())) {
            teacherService.updatePassword(account);
        }
        if ("STUDENT".equals(account.getRole())) {
            studentService.updatePassword(account);
        }
        return Result.success();
    }

}
