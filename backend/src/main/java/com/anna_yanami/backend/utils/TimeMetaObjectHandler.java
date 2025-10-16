package com.anna_yanami.backend.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);

        // 默认值兜底（如果实体没赋值）
        strictInsertFill(metaObject, "isPinned", Boolean.class, false);
        strictInsertFill(metaObject, "orderIndex", Integer.class, 0);
        strictInsertFill(metaObject, "isFavorite", Boolean.class, false);
        strictInsertFill(metaObject, "clickCount", Long.class, 0L);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}