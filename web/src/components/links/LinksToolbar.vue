<template>
  <header class="toolbar">
    <div class="toolbar-content">
      <div class="left-section">
        <h2 class="title">
          <span class="title-icon">🔗</span>
          Links
          <span class="badge" v-if="totalCount > 0">{{ totalCount }}</span>
        </h2>
      </div>

      <div class="right-section">
        <!-- 搜索框 -->
        <div class="search-container">
          <Serchbox
              ref="sb"
              v-model="innerQ"
              @search="onSearch"
              placeholder="搜索链接..."
              class="search-input"
          />
          <div class="search-indicator">
            <span v-if="busy" class="spinner" aria-label="加载中"></span>
            <span v-else-if="typing" class="typing-indicator" aria-label="输入中"></span>
          </div>
          <button class="search-btn" @click="onSearch" :disabled="busy">
            <span class="search-icon">🔍</span>
            <span class="search-text">搜索</span>
          </button>
        </div>

        <div class="divider"></div>

        <div class="filter-controls">
          <div class="filter-group">
            <label class="switch" title="只看收藏">
              <input type="checkbox" :checked="onlyFav" @change="onOnlyFav($event.target.checked)" />
              <span class="slider"></span>
              <span class="switch-label">收藏</span>
            </label>
          </div>

          <div class="filter-group">
            <div class="select-wrapper">
              <select class="select" :value="sort" @change="onSort($event.target.value)" aria-label="排序">
                <option value="default">默认排序</option>
                <option value="recent">最近添加</option>
              </select>
              <span class="select-arrow">▼</span>
            </div>
          </div>
        </div>

        <div class="divider"></div>

        <button class="btn primary create-btn" @click="$emit('create')" title="新建链接 (n)">
          <span class="btn-icon">+</span>
          <span class="btn-text">添加链接</span>
        </button>
      </div>
    </div>
  </header>
</template>

<script>
import { ref, watch, onMounted, onUnmounted } from 'vue';
import Serchbox from '@/components/Serchbox.vue';

export default {
  name: 'LinksToolbar',
  components: {
    Serchbox,
  },
  props: {
    q: { type: String, default: '' },
    sort: { type: String, default: 'default' },
    onlyFav: { type: Boolean, default: false },
    busy: { type: Boolean, default: false },
    totalCount: { type: Number, default: 0 }
  },
  emits: [
    'update:q',
    'update:sort',
    'update:onlyFav',
    'search',
    'create'
  ],

  setup(props, { emit }) {
    const innerQ = ref(props.q);
    const typing = ref(false);
    const timer = ref(null);
    const sb = ref(null);

    watch(() => props.q, v => {
      if (v !== innerQ.value) innerQ.value = v || '';
    });

    const onInputDebounced = (val) => {
      typing.value = true;
      if (timer.value) clearTimeout(timer.value);

      timer.value = setTimeout(() => {
        emit('update:q', (val || '').trim());
        typing.value = false;
      }, 300);
    };

    watch(innerQ, v => onInputDebounced(v));

    const onSearch = () => {
      if (timer.value) clearTimeout(timer.value);

      emit('update:q', (innerQ.value || '').trim());
      emit('search');
    };

    const onOnlyFav = (checked) => emit('update:onlyFav', checked);
    const onSort = (val) => emit('update:sort', val);

    const onKeydown = (e) => {
      const tag = (document.activeElement && document.activeElement.tagName) || '';
      const inInput = ['INPUT', 'TEXTAREA'].includes(tag);
      if (e.key === '/' && !inInput) {
        e.preventDefault();
        sb.value && sb.value.focus();
      }
      else if ((e.key === 'n' || e.key === 'N') && !inInput) {
        e.preventDefault();
        emit('create');
      }
    };

    onMounted(() => window.addEventListener('keydown', onKeydown));
    onUnmounted(() => {
      window.removeEventListener('keydown', onKeydown);
      if (timer.value) clearTimeout(timer.value);
    });

    return { innerQ, typing, sb, onSearch, onOnlyFav, onSort };
  }
}
</script>

<style scoped>
.toolbar {
  background: #ffffff;
  border-bottom: 1px solid #e1e5e9;
  padding: 1rem 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(10px);
}

.toolbar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
}

.left-section {
  display: flex;
  align-items: center;
}

.title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.title-icon {
  font-size: 1.3rem;
}

.badge {
  background: #4a6cf7;
  color: white;
  font-size: 0.75rem;
  padding: 0.2rem 0.5rem;
  border-radius: 1rem;
  font-weight: 600;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
  background: #f8f9fa;
  border-radius: 0.5rem;
  overflow: hidden;
  border: 1px solid #e1e5e9;
  transition: all 0.2s ease;
}

.search-container:focus-within {
  border-color: #4a6cf7;
  box-shadow: 0 0 0 2px rgba(74, 108, 247, 0.1);
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 0.5rem 1rem;
  min-width: 250px;
}

.search-input:focus {
  outline: none;
}

.search-indicator {
  padding: 0 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 24px;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid #4a6cf7;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.typing-indicator {
  width: 16px;
  height: 16px;
  background: #4a6cf7;
  border-radius: 50%;
  opacity: 0.7;
  animation: pulse 1.5s infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes pulse {
  0%, 100% { opacity: 0.5; }
  50% { opacity: 1; }
}

.search-btn {
  background: #4a6cf7;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
}

.search-btn:hover:not(:disabled) {
  background: #3a5ce5;
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.search-icon {
  font-size: 0.9rem;
}

.search-text {
  font-weight: 500;
}

.divider {
  width: 1px;
  height: 24px;
  background: #e1e5e9;
}

.filter-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.filter-group {
  display: flex;
  align-items: center;
}

.switch {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 0.5rem;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: relative;
  width: 44px;
  height: 24px;
  background-color: #e1e5e9;
  transition: .4s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
  box-shadow: 0 1px 3px rgba(0,0,0,0.2);
}

input:checked + .slider {
  background-color: #4a6cf7;
}

input:checked + .slider:before {
  transform: translateX(20px);
}

.switch-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #5a6370;
}

.select-wrapper {
  position: relative;
}

.select {
  appearance: none;
  background: #f8f9fa;
  border: 1px solid #e1e5e9;
  border-radius: 0.5rem;
  padding: 0.5rem 2rem 0.5rem 1rem;
  font-size: 0.875rem;
  color: #1a1a1a;
  cursor: pointer;
  transition: all 0.2s ease;
}

.select:focus {
  outline: none;
  border-color: #4a6cf7;
  box-shadow: 0 0 0 2px rgba(74, 108, 247, 0.1);
}

.select-arrow {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  color: #5a6370;
  font-size: 0.7rem;
}

.create-btn {
  background: #4a6cf7;
  color: white;
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 1rem;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(74, 108, 247, 0.2);
}

.create-btn:hover {
  background: #3a5ce5;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(74, 108, 247, 0.3);
}

.btn-icon {
  font-size: 1.1rem;
  font-weight: 600;
}

.btn-text {
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .toolbar {
    padding: 1rem;
  }

  .toolbar-content {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .right-section {
    flex-wrap: wrap;
    gap: 0.75rem;
  }

  .search-container {
    width: 100%;
  }

  .search-input {
    min-width: auto;
  }

  .filter-controls {
    gap: 0.75rem;
  }

  .divider {
    display: none;
  }
}
</style>