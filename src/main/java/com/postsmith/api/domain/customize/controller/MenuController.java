package com.postsmith.api.domain.customize.controller;

import com.postsmith.api.domain.customize.dto.MenuDto;
import com.postsmith.api.domain.customize.service.MenuService;
import com.postsmith.api.entity.BlogsEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    BlogsEntity blog = new BlogsEntity(1);

   /* @GetMapping
    public List<MenuDto> getMenus() {
        BlogsEntity blog = getCurrentUserBlog(); // 또는 @RequestParam 사용
        return menuService.getMenus(blog);
    }

    @PutMapping
    public ResponseEntity<Void> saveMenus(@RequestBody List<MenuDto> menus) {
        BlogsEntity blog = getCurrentUserBlog(); // 또는 blogId로 조회
        menuService.replaceAllMenus(menus, blog);
        return ResponseEntity.noContent().build();
    }

    public BlogsEntity getCurrentUserBlog() {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getBlog(); // 또는 user.getBlogId() → blogService.findById(...)
    }
    */

    // Controller
    @GetMapping
    public List<MenuDto> getMenus(@RequestParam("blogId") int blogId) {
        BlogsEntity blog = new BlogsEntity(blogId);
        return menuService.getMenus(blog);
    }

    @PutMapping
    public ResponseEntity<List<MenuDto>> saveMenus(@RequestParam("blogId") int blogId, @RequestBody List<MenuDto> menus) {
        BlogsEntity blog = new BlogsEntity(blogId);
        menuService.replaceAllMenus(menus, blog);
        List<MenuDto> savedMenus = menuService.getMenus(blog);
        return ResponseEntity.ok(savedMenus);
    }




}
