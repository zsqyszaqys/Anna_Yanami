<template>
  <header class="toolbar">
    <h2 class="title">Links</h2>
    <div class="tools">
      <div class="search">
        <input
            :value="q"
            @input="$emit('update:q', $event.target.value)"
            @keyup.enter="$emit('search')"
            type="text"
            placeholder="搜索链接（回车确认）"
        />
        <button class="btn ghost" @click="$emit('search')">搜索</button>
      </div>

      <label class="switch">
        <input type="checkbox" :checked="onlyFav" @change="$emit('update:onlyFav', $event.target.checked)"/>
        <span>只看收藏</span>
      </label>

      <select class="select" :value="sort" @change="$emit('update:sort', $event.target.value)">
        <option value="default">默认排序</option>
        <option value="recent">最近创建</option>
      </select>

      <button class="btn primary" @click="$emit('create')">+ 添加链接</button>
    </div>
  </header>
</template>

<script>
export default {
  name: 'LinksToolbar',
  props: {
    q: {type: String, default: ''},
    sort: {type: String, default: 'default'},
    onlyFav: {type: Boolean, default: false}
  },
  emits: ['update:q', 'update:sort', 'update:onlyFav', 'search', 'create']
}
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  gap: 12px;
}

.title {
  font-size: 18px;
  font-weight: 600;
}

.tools {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.search {
  display: flex;
  gap: 8px;
  align-items: center;
}

.search input {
  width: 280px;
  padding: 8px 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  outline: none;
  background: #fff;
}

.switch {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #555;
}

.select {
  padding: 8px 10px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  background: #fff;
}

.btn {
  padding: 8px 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  background: #fff;
  cursor: pointer;
}

.btn.primary {
  background: #111827;
  color: #fff;
  border-color: #111827;
}

.btn.ghost {
  background: #fff;
}
</style>