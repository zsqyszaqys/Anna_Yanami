package com.anna_yanami.backend.service.impl.Aboutme;

import com.anna_yanami.backend.mapper.LinkGroupMapper;
import com.anna_yanami.backend.pojo.LinkGroup;
import com.anna_yanami.backend.service.AboutMe.LinkGroupService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkGroupServiceImpl implements LinkGroupService {

    private final LinkGroupMapper linkGroupMapper;

    @Override
    public List<LinkGroup> list() {
        return linkGroupMapper.selectList(
                Wrappers.lambdaQuery(LinkGroup.class)
                        .isNull(LinkGroup::getDeletedAt)
                        .orderByDesc(LinkGroup::getIsPinned)
                        .orderByAsc(LinkGroup::getOrderIndex)
                        .orderByAsc(LinkGroup::getName)
        );
    }

    @Override
    public LinkGroup get(Long id) {
        LinkGroup g = linkGroupMapper.selectOne(
                Wrappers.lambdaQuery(LinkGroup.class)
                        .eq(LinkGroup::getId, id)
                        .isNull(LinkGroup::getDeletedAt));
        if (g == null) throw new IllegalArgumentException("分组不存在或已删除");
        return g;
    }

    @Override
    @Transactional
    public Long create(LinkGroup group) {
        if (group.getIsPinned() == null) group.setIsPinned(false);
        if (group.getOrderIndex() == null) group.setOrderIndex(0);

        linkGroupMapper.insert(group);
        return group.getId();
    }

    @Override
    @Transactional
    public void update(Long id, LinkGroup patch) {
        LinkGroup old = linkGroupMapper.selectOne(
                Wrappers.lambdaQuery(LinkGroup.class)
                        .eq(LinkGroup::getId, id)
                        .isNull(LinkGroup::getDeletedAt));
        if (old == null) throw new IllegalArgumentException("分组不存在或已删除");

        var uw = Wrappers.lambdaUpdate(LinkGroup.class).eq(LinkGroup::getId, id);
        if (patch.getName() != null) uw.set(LinkGroup::getName, patch.getName());
        if (patch.getSlug() != null) uw.set(LinkGroup::getSlug, patch.getSlug());
        if (patch.getDescription() != null) uw.set(LinkGroup::getDescription, patch.getDescription());
        if (patch.getColor() != null) uw.set(LinkGroup::getColor, patch.getColor());
        if (patch.getIcon() != null) uw.set(LinkGroup::getIcon, patch.getIcon());
        if (patch.getOrderIndex() != null) uw.set(LinkGroup::getOrderIndex, patch.getOrderIndex());
        if (patch.getIsPinned() != null) uw.set(LinkGroup::getIsPinned, patch.getIsPinned());

        linkGroupMapper.update(null, uw);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int n = linkGroupMapper.update(null,
                Wrappers.lambdaUpdate(LinkGroup.class)
                        .eq(LinkGroup::getId, id)
                        .isNull(LinkGroup::getDeletedAt)
                        .set(LinkGroup::getDeletedAt, LocalDateTime.now()));
        if (n == 0) throw new IllegalArgumentException("分组不存在或已删除");
        // 不级联：不修改该分组下的链接
    }

    @Override
    @Transactional
    public void restore(Long id) {
        int n = linkGroupMapper.update(null,
                Wrappers.lambdaUpdate(LinkGroup.class)
                        .eq(LinkGroup::getId, id)
                        .isNotNull(LinkGroup::getDeletedAt)
                        .set(LinkGroup::getDeletedAt, null));
        if (n == 0) throw new IllegalArgumentException("分组未被删除或不存在");
        // 不级联：不修改该分组下的链接
    }

    @Override
    @Transactional
    public void reorder(Long id, Integer orderIndex, Boolean pinned) {
        var uw = Wrappers.lambdaUpdate(LinkGroup.class).eq(LinkGroup::getId, id);
        if (orderIndex != null) uw.set(LinkGroup::getOrderIndex, orderIndex);
        if (pinned != null) uw.set(LinkGroup::getIsPinned, pinned);
        int n = linkGroupMapper.update(null, uw);
        if (n == 0) throw new IllegalArgumentException("分组不存在或已删除");
    }
}