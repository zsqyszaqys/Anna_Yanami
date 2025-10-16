package com.anna_yanami.backend.service.AboutMe;

import com.anna_yanami.backend.pojo.Links;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface LinksService {
    // 分页查询分组下的链接（支持按标题搜索、简单排序）
    Page<Links> PageBYGroup(long groupId, String q, int pageNo, int pageSize, String sort);

    // 新建链接（包含重复校验）
    Long create(Links link);

    // 局部更新（非空字段覆盖）
    void update(Long id, Links patch);

    // 软删除
    void delete(Long id);

    //恢复
    void restore(Long id);

    // 移动到其他分组（含重复校验）
    void move(Long id, Long toGroupId);

    // 收藏/取消收藏
    void toggleFavorite(Long id, boolean fav);

    // 点击计数 +1
    void click(Long id);
}
