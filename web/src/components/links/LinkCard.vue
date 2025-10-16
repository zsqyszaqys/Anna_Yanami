<template>
  <article class="card" @mousemove="onMouseMove" @mouseleave="onMouseLeave">
    <div class="card-top">
      <div class="left">
        <img v-if="item.faviconUrl" :src="item.faviconUrl" class="fav"/>
        <div v-else class="fav fallback">🔗</div>
        <a class="title" :href="item.url" target="_blank" rel="noreferrer" @click.prevent="$emit('open', item)">
          {{ item.title }}
        </a>
      </div>
      <div class="right" @mouseleave="menuOpen=false">
        <button class="star" :class="{ on: item.isFavorite }" @click="$emit('favorite', item)">
          {{ item.isFavorite ? '★' : '☆' }}
        </button>
        <div class="more">
          <button class="more-btn" @click="menuOpen = !menuOpen">···</button>
          <ul v-if="menuOpen" class="menu">
            <li @click="$emit('copy', item)">复制链接</li>
            <li @click="$emit('edit', item)">编辑</li>
            <li @click="$emit('move', item)">移动分组</li>
            <li class="danger" @click="$emit('delete', item)">删除</li>
          </ul>
        </div>
      </div>
    </div>
    <p v-if="item.description" class="desc">{{ item.description }}</p>
  </article>
</template>

<script>
export default {
  name: 'LinkCard',
  props: {
    item: {type: Object, required: true}
  },
  emits: ['open', 'favorite', 'copy', 'edit', 'move', 'delete'],
  data() {
    return {menuOpen: false};
  },
  methods: {
    onMouseMove(e) {
      const el = e.currentTarget;
      const rect = el.getBoundingClientRect();
      el.style.setProperty('--mx', `${e.clientX - rect.left}px`);
      el.style.setProperty('--my', `${e.clientY - rect.top}px`);
    },
    onMouseLeave(e) {
      const el = e.currentTarget;
      el.style.removeProperty('--mx');
      el.style.removeProperty('--my');
      this.menuOpen = false;
    }
  }
}
</script>

<style scoped>
.card {
  position: relative;
  overflow: hidden;
  border-radius: 14px;
  border: 1px solid #e5e7eb;
  background: #fff;
  padding: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, .05);
  transition: transform .15s ease, box-shadow .15s ease;
}

.card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, .08);
}

.card::after {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  opacity: 0;
  transition: opacity .25s;
  background: radial-gradient(600px circle at var(--mx, -10%) var(--my, -10%), rgba(255, 255, 255, .3), transparent 40%);
}

.card:hover::after {
  opacity: 1;
}

.card-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.fav {
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

.fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.title {
  font-weight: 600;
  color: #111827;
  text-decoration: none;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.right {
  display: flex;
  align-items: center;
  gap: 6px;
  position: relative;
}

.star {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 18px;
  color: #d1d5db;
  transition: .15s;
}

.star.on {
  color: #f59e0b;
  transform: scale(1.06);
}

.more-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  color: #6b7280;
  font-weight: 700;
  padding: 2px 6px;
}

.menu {
  position: absolute;
  top: 24px;
  right: 0;
  z-index: 10;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 6px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, .12);
}

.menu li {
  list-style: none;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  white-space: nowrap;
}

.menu li:hover {
  background: #f3f4f6;
}

.menu li.danger {
  color: #ef4444;
}

.desc {
  color: #6b7280;
  font-size: 13px;
  margin: 8px 4px 2px 26px;
  line-height: 1.45;
}
</style>