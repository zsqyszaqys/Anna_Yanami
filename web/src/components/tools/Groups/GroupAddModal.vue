<template>
  <!-- 模态框容器，v-if 控制显示/隐藏 -->
  <div v-if="modelValue" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <!-- 头部 -->
      <div class="modal-header">
        <h3 class="modal-title">新建分组</h3>
        <button class="close-button" @click="close">&times;</button>
      </div>

      <!-- 表单区域 -->
      <div class="modal-body">
        <!-- 注意：这里的 @submit.prevent 调用的是 handleCreate -->
        <form @submit.prevent="handleCreate">
          <!-- 分组名称 -->
          <div class="form-group">
            <label for="new-group-name">分组名称</label>
            <input id="new-group-name" v-model="newGroup.name" type="text" placeholder="输入分组名称" required>
          </div>

          <!-- 描述信息 -->
          <div class="form-group">
            <label for="new-group-description">描述信息</label>
            <textarea id="new-group-description" v-model="newGroup.description" rows="3" placeholder="输入分组描述 (可选)"></textarea>
          </div>

          <!-- 你可以在这里添加更多字段，如 slug, color, is_pinned 等 -->

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button type="submit" class="button-primary" :disabled="isCreating">
              {{ isCreating ? '创建中...' : '创建分组' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

// 定义要创建的分组对象结构
interface NewGroup {
  name: string;
  description?: string;
  // 可以添加 order_index, is_pinned 等
}

// 这个组件只需要一个 v-model prop 来控制显示
const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['update:modelValue', 'group-created']);

// 用于绑定表单数据的 ref，并提供初始值
const newGroup = ref<NewGroup>({
  name: '',
  description: '',
});

const isCreating = ref(false);

function close() {
  emit('update:modelValue', false);
  // 关闭时重置表单，以便下次打开是干净的
  newGroup.value = { name: '', description: '' };
}

async function handleCreate() {
  if (!newGroup.value.name.trim() || isCreating.value) return;

  isCreating.value = true;
  try {
    // API 调用 POST /api/me/groups
    const response = await fetch('http://localhost:3000/api/me/groups', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        name: newGroup.value.name,
        description: newGroup.value.description,
        // 如果需要，可以在这里发送 order_index, is_pinned 等
      }),
    });

    const result = await response.json();
    if (!response.ok || result.status === false) {
      throw new Error(result.message || '创建失败');
    }

    // 通知父组件创建成功
    emit('group-created');
    alert('分组创建成功！');
    close();
  } catch (error: any) {
    alert(`创建失败: ${error.message}`);
  } finally {
    isCreating.value = false;
  }
}
</script>

<style scoped>
/*
  你可以直接从 GroupSettingsModal.vue 复制所有样式过来，
  它们是完全兼容的。
  .modal-overlay { ... }
  .modal-content { ... }
  ...等等
*/
.modal-overlay {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(0, 0, 0, 0.6); display: flex;
  justify-content: center; align-items: center; z-index: 1000;
}
.modal-content {
  background: #2d3748; color: #e2e8f0; padding: 2rem;
  border-radius: 12px; width: 90%; max-width: 500px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}
.modal-header {
  display: flex; justify-content: space-between; align-items: center;
  border-bottom: 1px solid #4a5568; padding-bottom: 1rem; margin-bottom: 1rem;
}
.modal-title { font-size: 1.5rem; font-weight: 600; }
.close-button {
  background: none; border: none; font-size: 2rem;
  color: #a0aec0; cursor: pointer;
}
.form-group { margin-bottom: 1.5rem; }
.form-group label {
  display: block; margin-bottom: 0.5rem; font-weight: 500;
  color: #a0aec0;
}
.form-group input, .form-group textarea {
  width: 100%; padding: 0.75rem; border-radius: 8px;
  border: 1px solid #4a5568; background: #1a202c;
  color: #e2e8f0; font-size: 1rem;
}
.form-actions { text-align: right; }
.button-primary {
  background-color: #4299e1; color: white; padding: 0.75rem 1.5rem;
  border: none; border-radius: 8px; cursor: pointer;
}
.button-primary:disabled { opacity: 0.5; cursor: not-allowed; }
</style>