package com.anna_yanami.backend.controller.Aboutme;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.pojo.Links;
import com.anna_yanami.backend.service.AboutMe.LinksService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class LinksController {

    private final LinksService linksService;

    // 分页查询：某分组下的链接（支持搜索与排序）
    // 示例：GET /api/me/groups/1/links?q=docs&pageNo=1&pageSize=24&sort=default|recent
    @GetMapping("/groups/{groupId}/links")
    public ResponseResult<Page<Links>> page(@PathVariable Long groupId,
                                            @RequestParam(required = false) String q,
                                            @RequestParam(defaultValue = "1") int pageNo,
                                            @RequestParam(defaultValue = "24") int pageSize,
                                            @RequestParam(defaultValue = "default") String sort) {
        return ResponseResult.success("success",
                linksService.PageBYGroup(groupId, q, pageNo, pageSize, sort));
    }

    // 新建链接（组内 URL 唯一，软删后可重复）
    @PostMapping("/links")
    public ResponseResult<Long> create(@RequestBody Links link) {
        return ResponseResult.success("success", linksService.create(link));
    }

    // 局部更新（title/url/description/isFavorite/orderIndex 等）
    @PatchMapping("/links/{id}")
    public ResponseResult<String> update(@PathVariable Long id, @RequestBody Links patch) {
        linksService.update(id, patch);
        return ResponseResult.success();
    }

    // 软删除
    @DeleteMapping("/links/{id}")
    public ResponseResult<String> delete(@PathVariable Long id) {
        linksService.delete(id);
        return ResponseResult.success();
    }

    // 恢复已软删链接（若与现有有效 URL 冲突则报错）
    @PostMapping("/links/{id}/restore")
    public ResponseResult<String> restore(@PathVariable Long id) {
        linksService.restore(id);
        return ResponseResult.success();
    }

    // 移动到其他分组（校验目标分组存在与组内唯一）
    @PostMapping("/links/{id}/move")
    public ResponseResult<String> move(@PathVariable Long id, @RequestParam Long toGroupId) {
        linksService.move(id, toGroupId);
        return ResponseResult.success();
    }

    // 收藏/取消收藏
    @PostMapping("/links/{id}/favorite")
    public ResponseResult<String> favorite(@PathVariable Long id, @RequestParam boolean fav) {
        linksService.toggleFavorite(id, fav);
        return ResponseResult.success();
    }

    // 点击计数 +1
    @PostMapping("/links/{id}/click")
    public ResponseResult<String> click(@PathVariable Long id) {
        linksService.click(id);
        return ResponseResult.success();
    }
}