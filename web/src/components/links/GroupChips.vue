<template>
  <div class="group-row" v-if="groups && groups.length">
    <button
        v-for="g in groups"
        :key="g.id"
        :class="['chip', activeGroupId===g.id && 'active']"
        @click="$emit('select', g.id)"
        :title="g.description || g.name"
    >
      <span class="color" :style="{ background: g.color || '#f59e0b' }"></span>
      <span class="name">{{ g.icon || '🗂️' }} {{ g.name }}</span>
    </button>
  </div>
</template>

<script>
export default {
  name: 'GroupChips',
  props: {
    groups: {type: Array, default: () => []},
    activeGroupId: {type: [Number, String], default: null}
  },
  emits: ['select']
}
</script>

<style scoped>
.group-row {
  display: flex;
  gap: 8px;
  padding: 0 16px 12px 16px;
  overflow: auto;
}

.chip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 999px;
  border: 1px solid #e5e7eb;
  background: #fff;
  cursor: pointer;
  transition: .2s;
}

.chip:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, .06);
}

.chip.active {
  border-color: #f59e0b;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, .15) inset;
}

.chip .color {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

.chip .name {
  white-space: nowrap;
}
</style>