package com.anna_yanami.backend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("link_groups")
public class LinkGroup {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    // 路由友好标识（唯一，DB层约束）
    @TableField(value = "slug")
    private String slug;

    @TableField(value = "description")
    private String description;

    @TableField(value = "color")
    private String color;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "order_index")
    private Integer orderIndex;

    @TableField(value = "is_pinned")
    private Boolean isPinned;

    // 时间字段（自动填充）
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 软删除时间（继续采用时间戳方案 → 手动控制）
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
}