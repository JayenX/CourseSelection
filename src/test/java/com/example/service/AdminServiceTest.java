package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminService adminService;

    public AdminServiceTest() {
        MockitoAnnotations.openMocks(this); // 初始化 mock
    }

    @Test
    public void testLoginSuccess() {
        // 模拟数据库中的 Admin 用户
        Admin mockAdmin = new Admin();
        mockAdmin.setUsername("admin");
        mockAdmin.setPassword("admin");
        mockAdmin.setRole("ADMIN");

        // 输入账号信息
        Account input = new Account();
        input.setUsername("admin");
        input.setPassword("admin");
        input.setRole("ADMIN");

        // 当调用 selectByUsername 时返回 mockAdmin
        when(adminMapper.selectByUsername("admin")).thenReturn(mockAdmin);

        // 调用 login 方法
        Account result = adminService.login(input);

        // 验证返回值
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("admin", result.getPassword());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    public void testLogin_userNotFound() {
        // 模拟数据库中查不到该用户
        when(adminMapper.selectByUsername("notExist")).thenReturn(null);

        Account input = new Account();
        input.setUsername("notExist");
        input.setPassword("admin");

        // 验证是否抛出 CustomException 类型
        assertThrows(CustomException.class, () -> {
            adminService.login(input);
        });
    }

    @Test
    public void testLogin_wrongPassword() {
        // 模拟用户存在但密码错误
        Admin mockAdmin = new Admin();
        mockAdmin.setUsername("admin");
        mockAdmin.setPassword("admin");

        Account input = new Account();
        input.setUsername("admin");
        input.setPassword("wrong");

        when(adminMapper.selectByUsername("admin")).thenReturn(mockAdmin);

        // 验证是否抛出 CustomException 类型
        assertThrows(CustomException.class, () -> {
            adminService.login(input);
        });
    }

}
