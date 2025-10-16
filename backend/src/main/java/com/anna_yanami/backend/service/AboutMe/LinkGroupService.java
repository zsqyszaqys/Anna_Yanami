package com.anna_yanami.backend.service.AboutMe;

import com.anna_yanami.backend.pojo.LinkGroup;

import java.util.List;

public interface LinkGroupService {
    // 列出分组（未删除，按置顶+排序+名称）
    List<LinkGroup> list();

    // 获取分组详情
    LinkGroup get(Long id);

    // 新建分组
    Long create(LinkGroup group);

    // 更新（非空字段覆盖）
    void update(Long id, LinkGroup patch);

    // 软删除
    void delete(Long id);

    //恢复分组本身
    void restore(Long id);

    // 排序/置顶
    void reorder(Long id, Integer orderIndex, Boolean pinned);
}
