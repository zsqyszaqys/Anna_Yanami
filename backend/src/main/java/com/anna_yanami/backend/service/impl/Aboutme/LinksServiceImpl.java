package com.anna_yanami.backend.service.impl.Aboutme;

import com.anna_yanami.backend.mapper.LinkGroupMapper;
import com.anna_yanami.backend.mapper.LinkMapper;
import com.anna_yanami.backend.pojo.LinkGroup;
import com.anna_yanami.backend.pojo.Links;
import com.anna_yanami.backend.service.AboutMe.LinksService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LinksServiceImpl implements LinksService {

    private final LinkMapper linksMapper;
    private final LinkGroupMapper linkGroupMapper;


    @Override
    public Page<Links> PageBYGroup(long groupId, String q, int pageNo, int pageSize, String sort) {
        Page<Links> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Links> qw = Wrappers.lambdaQuery(Links.class)
                .eq(Links::getGroupId, groupId)
                .isNull(Links::getDeletedAt)
                .like(StringUtils.hasText(q), Links::getTitle, q);

        if ("recent".equalsIgnoreCase(sort)) {
            qw.orderByDesc(Links::getCreatedAt);
        } else {
            qw.orderByDesc(Links::getIsFavorite)
                    .orderByAsc(Links::getOrderIndex)
                    .orderByDesc(Links::getCreatedAt);
        }
        return linksMapper.selectPage(page, qw);
    }

    @Override
    @Transactional
    public Long create(Links link) {
        // 目标分组必须存在且未删除
        LinkGroup g = linkGroupMapper.selectOne(
                Wrappers.lambdaQuery(LinkGroup.class)
                        .eq(LinkGroup::getId, link.getGroupId())
                        .isNull(LinkGroup::getDeletedAt));
        if (g == null) throw new IllegalArgumentException("分组不存在或已删除");

        // 组内 URL 未被占用（仅检查未删除记录）
        long dup = linksMapper.selectCount(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getGroupId, link.getGroupId())
                        .eq(Links::getUrl, link.getUrl())
                        .isNull(Links::getDeletedAt));
        if (dup > 0) throw new IllegalArgumentException("该分组下 URL 已存在");

        // 兜底默认值（也可通过 MetaObjectHandler 自动填充）
        if (link.getIsFavorite() == null) link.setIsFavorite(false);
        if (link.getOrderIndex() == null) link.setOrderIndex(0);
        if (link.getClickCount() == null) link.setClickCount(0L);

        linksMapper.insert(link);
        return link.getId();
    }

    @Override
    @Transactional
    public void update(Long id, Links patch) {
        Links old = linksMapper.selectOne(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getId, id)
                        .isNull(Links::getDeletedAt));
        if (old == null) throw new IllegalArgumentException("链接不存在或已删除");

        Long newGroupId = patch.getGroupId() != null ? patch.getGroupId() : old.getGroupId();
        String newUrl = patch.getUrl() != null ? patch.getUrl() : old.getUrl();

        if (patch.getGroupId() != null || patch.getUrl() != null) {
            // 目标分组必须存在且未删除
            LinkGroup g = linkGroupMapper.selectOne(
                    Wrappers.lambdaQuery(LinkGroup.class)
                            .eq(LinkGroup::getId, newGroupId)
                            .isNull(LinkGroup::getDeletedAt));
            if (g == null) throw new IllegalArgumentException("目标分组不存在或已删除");

            long dup = linksMapper.selectCount(
                    Wrappers.lambdaQuery(Links.class)
                            .eq(Links::getGroupId, newGroupId)
                            .eq(Links::getUrl, newUrl)
                            .ne(Links::getId, id)
                            .isNull(Links::getDeletedAt));
            if (dup > 0) throw new IllegalArgumentException("该分组下 URL 已存在");
        }

        LambdaUpdateWrapper<Links> uw = Wrappers.lambdaUpdate(Links.class).eq(Links::getId, id);
        if (patch.getGroupId() != null) uw.set(Links::getGroupId, patch.getGroupId());
        if (patch.getTitle() != null) uw.set(Links::getTitle, patch.getTitle());
        if (patch.getUrl() != null) uw.set(Links::getUrl, patch.getUrl());
        if (patch.getDescription() != null) uw.set(Links::getDescription, patch.getDescription());
        if (patch.getFaviconUrl() != null) uw.set(Links::getFaviconUrl, patch.getFaviconUrl());
        if (patch.getOgImageUrl() != null) uw.set(Links::getOgImageUrl, patch.getOgImageUrl());
        if (patch.getIsFavorite() != null) uw.set(Links::getIsFavorite, patch.getIsFavorite());
        if (patch.getOrderIndex() != null) uw.set(Links::getOrderIndex, patch.getOrderIndex());
        linksMapper.update(null, uw);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int n = linksMapper.update(null, Wrappers.lambdaUpdate(Links.class)
                .eq(Links::getId, id)
                .isNull(Links::getDeletedAt)
                .set(Links::getDeletedAt, LocalDateTime.now()));
        if (n == 0) throw new IllegalArgumentException("链接不存在或已删除");
    }

    @Override
    @Transactional
    public void restore(Long id) {
        // 只能恢复“已软删”的记录
        Links deleted = linksMapper.selectOne(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getId, id)
                        .isNotNull(Links::getDeletedAt));
        if (deleted == null) throw new IllegalArgumentException("链接未被删除或不存在");

        // 恢复前检查是否有同组同URL的有效记录
        long activeDup = linksMapper.selectCount(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getGroupId, deleted.getGroupId())
                        .eq(Links::getUrl, deleted.getUrl())
                        .isNull(Links::getDeletedAt));
        if (activeDup > 0) throw new IllegalStateException("该分组下已存在相同 URL 的有效记录，无法恢复");

        linksMapper.update(null, Wrappers.lambdaUpdate(Links.class)
                .eq(Links::getId, id)
                .set(Links::getDeletedAt, null));
    }

    @Override
    @Transactional
    public void move(Long id, Long toGroupId) {
        // 目标分组必须存在且未删除
        LinkGroup g = linkGroupMapper.selectOne(
                Wrappers.lambdaQuery(LinkGroup.class)
                        .eq(LinkGroup::getId, toGroupId)
                        .isNull(LinkGroup::getDeletedAt));
        if (g == null) throw new IllegalArgumentException("目标分组不存在或已删除");

        Links link = linksMapper.selectOne(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getId, id)
                        .isNull(Links::getDeletedAt));
        if (link == null) throw new IllegalArgumentException("链接不存在或已删除");

        long dup = linksMapper.selectCount(
                Wrappers.lambdaQuery(Links.class)
                        .eq(Links::getGroupId, toGroupId)
                        .eq(Links::getUrl, link.getUrl())
                        .ne(Links::getId, id)
                        .isNull(Links::getDeletedAt));
        if (dup > 0) throw new IllegalArgumentException("目标分组下 URL 已存在");

        linksMapper.update(null, Wrappers.lambdaUpdate(Links.class)
                .eq(Links::getId, id)
                .set(Links::getGroupId, toGroupId));
    }

    @Override
    @Transactional
    public void toggleFavorite(Long id, boolean fav) {
        int n = linksMapper.update(null, Wrappers.lambdaUpdate(Links.class)
                .eq(Links::getId, id)
                .isNull(Links::getDeletedAt)
                .set(Links::getIsFavorite, fav));
        if (n == 0) throw new IllegalArgumentException("链接不存在或已删除");
    }

    @Override
    @Transactional
    public void click(Long id) {
        int n = linksMapper.update(null, Wrappers.lambdaUpdate(Links.class)
                .eq(Links::getId, id)
                .isNull(Links::getDeletedAt)
                .setSql("click_count = click_count + 1")
                .set(Links::getLastClickedAt, LocalDateTime.now()));
        if (n == 0) throw new IllegalArgumentException("链接不存在或已删除");
    }
}