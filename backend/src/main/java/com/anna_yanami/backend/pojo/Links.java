package com.anna_yanami.backend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("links")
public class Links {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 外键：分组ID（不做对象关联，MP 推荐按ID关联）
    @TableField(value = "group_id")
    private Long groupId;

    @TableField(value = "title")
    private String title;

    // URL 长度 512，和 group_id 组合唯一（DB层约束）
    @TableField(value = "url")
    private String url;

    @TableField(value = "description")
    private String description;

    @TableField(value = "favicon_url")
    private String faviconUrl;

    @TableField(value = "og_image_url")
    private String ogImageUrl;

    @TableField(value = "is_favorite")
    private Boolean isFavorite;

    @TableField(value = "order_index")
    private Integer orderIndex;

    @TableField(value = "click_count")
    private Long clickCount;

    @TableField(value = "last_clicked_at")
    private LocalDateTime lastClickedAt;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
}