package com.anna_yanami.backend.controller.Aboutme;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.pojo.LinkGroup;
import com.anna_yanami.backend.service.AboutMe.LinkGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/me/groups")
@RequiredArgsConstructor
public class LinkGroupController {

    private final LinkGroupService linkGroupService;

    // 分组列表（未删除，置顶优先、orderIndex 升序、name 升序）
    @GetMapping
    public ResponseResult<List<LinkGroup>> list() {
        return ResponseResult.success("success", linkGroupService.list());
    }

    // 分组详情
    @GetMapping("/{id}")
    public ResponseResult<LinkGroup> get(@PathVariable Long id) {
        return ResponseResult.success("success", linkGroupService.get(id));
    }

    // 新建分组
    @PostMapping
    public ResponseResult<Long> create(@RequestBody LinkGroup group) {
        return ResponseResult.success("success", linkGroupService.create(group));
    }

    // 更新分组（局部）
    @PatchMapping("/{id}")
    public ResponseResult<String> update(@PathVariable Long id, @RequestBody LinkGroup patch) {
        linkGroupService.update(id, patch);
        return ResponseResult.success();
    }

    // 软删除分组（不级联链接）
    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(@PathVariable Long id) {
        linkGroupService.delete(id);
        return ResponseResult.success();
    }

    // 恢复分组（不级联链接）
    @PostMapping("/{id}/restore")
    public ResponseResult<String> restore(@PathVariable Long id) {
        linkGroupService.restore(id);
        return ResponseResult.success();
    }

    // 分组排序/置顶（与前端拖拽联动）
    @PatchMapping("/{id}/reorder")
    public ResponseResult<String> reorder(@PathVariable Long id,
                                          @RequestParam Integer orderIndex,
                                          @RequestParam(defaultValue = "false") boolean pinned) {
        linkGroupService.reorder(id, orderIndex, pinned);
        return ResponseResult.success();
    }
}