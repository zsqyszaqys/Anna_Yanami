<template>
  <!-- 模态框容器，v-if 控制显示/隐藏 -->
  <div
    v-if="modelValue && group"
    class="modal-overlay"
    @click.self="close"
  >
    <div class="modal-content">
      <!-- 头部 -->
      <div class="modal-header">
        <div class="header-content">
          <div class="title-section">
            <div class="icon-wrapper">
              <svg
                class="edit-icon"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                />
              </svg>
            </div>
            <div>
              <h3 class="modal-title">
                编辑分组
              </h3>
              <p class="group-subtitle">
                {{ group.name }}
              </p>
            </div>
          </div>
          <button
            class="close-button"
            @click="close"
          >
            <svg
              class="close-icon"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
      </div>

      <!-- 表单区域 -->
      <div class="modal-body">
        <form
          class="edit-form"
          @submit.prevent="handleUpdate"
        >
          <!-- 分组名称 -->
          <div class="form-group">
            <label
              for="group-name"
              class="form-label"
            >
              <span class="label-text">分组名称</span>
              <span class="required-indicator">*</span>
            </label>
            <div class="input-wrapper">
              <input
                id="group-name"
                v-model="editableGroup.name"
                type="text"
                class="form-input"
                placeholder="为分组起一个好听的名字..."
                required
                :disabled="isUpdating"
              >
              <div class="input-focus-border" />
            </div>
          </div>

          <!-- 描述信息 -->
          <div class="form-group">
            <label
              for="group-description"
              class="form-label"
            >
              <span class="label-text">描述信息</span>
              <span class="optional-indicator">可选</span>
            </label>
            <div class="input-wrapper">
              <textarea
                id="group-description"
                v-model="editableGroup.description"
                rows="3"
                class="form-textarea"
                placeholder="描述这个分组的用途或特点..."
                :disabled="isUpdating"
              />
              <div class="input-focus-border" />
            </div>
            <div
              v-if="editableGroup.description"
              class="character-count"
            >
              {{ editableGroup.description.length }}/200
            </div>
          </div>

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button
              type="submit"
              class="button button-primary"
              :class="{ 'button-loading': isUpdating }"
              :disabled="isUpdating"
            >
              <span class="button-content">
                <svg
                  v-if="isUpdating"
                  class="button-spinner"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    class="spinner-circle"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    stroke-width="4"
                  />
                  <path
                    class="spinner-path"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                  />
                </svg>
                <svg
                  v-else
                  class="button-icon"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M5 13l4 4L19 7"
                  />
                </svg>
                {{ isUpdating ? '保存中...' : '保存更改' }}
              </span>
            </button>
          </div>
        </form>
      </div>

      <!-- 危险区域 -->
      <div class="danger-zone">
        <div class="danger-header">
          <div class="danger-icon">
            <svg
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.35 16.5c-.77.833.192 2.5 1.732 2.5z"
              />
            </svg>
          </div>
          <div class="danger-text">
            <h4 class="danger-title">
              危险操作
            </h4>
            <p class="danger-description">
              删除分组后，所有相关数据将永久丢失且无法恢复
            </p>
          </div>
        </div>
        <button
          class="button button-danger"
          :class="{ 'button-loading': isDeleting }"
          :disabled="isDeleting"
          @click="showDeleteConfirm"
        >
          <span class="button-content">
            <svg
              v-if="isDeleting"
              class="button-spinner"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                class="spinner-circle"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                stroke-width="4"
              />
              <path
                class="spinner-path"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
              />
            </svg>
            <svg
              v-else
              class="button-icon"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
            {{ isDeleting ? '删除中...' : '删除此分组' }}
          </span>
        </button>
      </div>
    </div>
  </div>

  <!-- 自定义 Confirm 组件 -->
  <CustomConfirm
    v-model:visible="showConfirm"
    :title="confirmTitle"
    :message="confirmMessage"
    :type="confirmType"
    :confirm-text="confirmButtonText"
    @confirm="handleConfirm"
    @cancel="handleConfirmCancel"
  />
</template>

<script lang="ts" setup>
import { ref, watch, type PropType } from 'vue';
import CustomConfirm from '@/components/tools/CustomConfirm.vue';

// 前端使用的 Group 类型，全部用驼峰式
interface Group {
  id: number;
  name: string;
  slug?: string | null;
  description?: string | null;
  color?: string | null;
  icon?: string | null;
  orderIndex?: number;
  isPinned?: boolean | null;
  updatedAt?: Date;
  deletedAt?: Date;
}

const emit = defineEmits(['update:modelValue', 'group-updated', 'group-deleted']);

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  group: {
    type: Object as PropType<Group | null>,
    default: null,
  },
});

const editableGroup = ref<Partial<Group>>({});
const isUpdating = ref(false);
const isDeleting = ref(false);

// Confirm 相关状态
const showConfirm = ref(false);
const confirmTitle = ref('');
const confirmMessage = ref('');
const confirmType = ref<'success' | 'error' | 'warning'>('warning');
const confirmButtonText = ref('确定');

// 操作类型标识
let currentAction = '';

watch(() => props.group, (newGroup) => {
  if (newGroup) {
    editableGroup.value = { ...newGroup };
  }
}, { immediate: true });

function close() {
  emit('update:modelValue', false);
}

function showCustomConfirm(title: string, message: string, type: 'success' | 'error' | 'warning' = 'warning', action = '', confirmText = '确定') {
  confirmTitle.value = title;
  confirmMessage.value = message;
  confirmType.value = type;
  confirmButtonText.value = confirmText;
  currentAction = action;
  showConfirm.value = true;
}

function handleConfirm() {
  if (currentAction === 'delete') {
    performDelete();
  }
}

function handleConfirmCancel() {
  // 取消操作，不需要做任何事情
  currentAction = '';
}

function showDeleteConfirm() {
  if (!props.group) return;

  showCustomConfirm(
      '确认删除',
      `确定要删除分组 "${props.group.name}" 吗？此操作不可撤销，所有相关数据将永久丢失。`,
      'warning',
      'delete',
      '确认删除'
  );
}

const API_BASE_URL = 'https://app7510.acapp.acwing.com.cn/api/me';

async function handleUpdate() {
  if (!props.group || isUpdating.value) return;

  isUpdating.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/groups/${props.group.id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        name: editableGroup.value.name,
        description: editableGroup.value.description,
        slug: editableGroup.value.slug,
        color: editableGroup.value.color,
        icon: editableGroup.value.icon,
        order_index: editableGroup.value.orderIndex,
        is_pinned: editableGroup.value.isPinned,
      })
    });

    const result = await response.json();

    if (!response.ok || result.status === false) {
      throw new Error(result.message || '更新失败');
    }

    emit('group-updated');
    showCustomConfirm('操作成功', '分组更新成功！', 'success');
    close();
  } catch (error: any) {
    showCustomConfirm('操作失败', `更新失败: ${error.message}`, 'error');
  } finally {
    isUpdating.value = false;
  }
}

async function performDelete() {
  if (!props.group || isDeleting.value) return;

  isDeleting.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/groups/${props.group.id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
      },
      credentials: 'include',
    });

    const result = await response.json();

    if (!response.ok || result.status === false) {
      throw new Error(result.message || '删除失败');
    }

    emit('group-deleted', props.group.id);
    showCustomConfirm('操作成功', '分组删除成功！', 'success');
    close();
  } catch (error: any) {
    showCustomConfirm('操作失败', `删除失败: ${error.message}`, 'error');
  } finally {
    isDeleting.value = false;
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(8px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease-out;
}

.modal-content {
  background: linear-gradient(135deg, #1a202c 0%, #2d3748 100%);
  color: #e2e8f0;
  padding: 0;
  border-radius: 20px;
  width: 90%;
  max-width: 480px;
  box-shadow:
      0 25px 50px -12px rgba(0, 0, 0, 0.5),
      0 0 0 1px rgba(255, 255, 255, 0.1);
  animation: slideUp 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  overflow: hidden;
}

.modal-header {
  background: linear-gradient(135deg, #2d3748 0%, #4a5568 100%);
  padding: 1.5rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.title-section {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
}

.icon-wrapper {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0.5rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-icon {
  width: 1.5rem;
  height: 1.5rem;
  color: white;
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.25rem 0;
  line-height: 1.2;
}

.group-subtitle {
  font-size: 0.875rem;
  color: #a0aec0;
  margin: 0;
  font-weight: 500;
}

.close-button {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  padding: 0.5rem;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.close-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: #a0aec0;
}

.modal-body {
  padding: 2rem;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #e2e8f0;
  font-size: 0.875rem;
}

.label-text {
  font-size: 0.875rem;
  font-weight: 600;
  color: #e2e8f0;
}

.required-indicator {
  color: #fc8181;
  font-size: 0.75rem;
}

.optional-indicator {
  color: #a0aec0;
  font-size: 0.75rem;
  font-weight: 400;
}

.input-wrapper {
  position: relative;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 0.875rem 1rem;
  border-radius: 12px;
  border: 2px solid #4a5568;
  background: rgba(26, 32, 44, 0.8);
  color: #e2e8f0;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  resize: vertical;
}

.form-input:focus, .form-textarea:focus {
  outline: none;
  border-color: #4299e1;
  background: rgba(26, 32, 44, 0.9);
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.1);
}

.form-input::placeholder, .form-textarea::placeholder {
  color: #718096;
}

.input-focus-border {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #4299e1, #667eea);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.form-input:focus ~ .input-focus-border,
.form-textarea:focus ~ .input-focus-border {
  width: 100%;
}

.character-count {
  text-align: right;
  font-size: 0.75rem;
  color: #a0aec0;
  margin-top: 0.25rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 0.5rem;
}

.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.875rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.button-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.button-primary {
  background: linear-gradient(135deg, #4299e1 0%, #667eea 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(66, 153, 225, 0.3);
}

.button-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(66, 153, 225, 0.4);
}

.button-danger {
  background: linear-gradient(135deg, #e53e3e 0%, #c53030 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(229, 62, 62, 0.3);
  width: 100%;
}

.button-danger:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(229, 62, 62, 0.4);
}

.button-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.button-spinner {
  width: 1.25rem;
  height: 1.25rem;
  animation: spin 1s linear infinite;
}

.spinner-circle {
  opacity: 0.25;
}

.spinner-path {
  opacity: 0.75;
}

.danger-zone {
  margin-top: 2rem;
  padding: 1.5rem;
  background: rgba(254, 215, 215, 0.05);
  border-radius: 16px;
  border: 1px solid rgba(254, 178, 178, 0.2);
}

.danger-header {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.danger-icon {
  background: rgba(254, 178, 178, 0.1);
  padding: 0.5rem;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.danger-icon svg {
  width: 1.25rem;
  height: 1.25rem;
  color: #fc8181;
}

.danger-text {
  flex: 1;
}

.danger-title {
  color: #fc8181;
  font-size: 1rem;
  font-weight: 700;
  margin: 0 0 0.25rem 0;
}

.danger-description {
  color: #e2e8f0;
  font-size: 0.875rem;
  margin: 0;
  opacity: 0.8;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 640px) {
  .modal-content {
    width: 95%;
    margin: 1rem;
  }

  .modal-header {
    padding: 1.25rem 1.5rem;
  }

  .modal-body {
    padding: 1.5rem;
  }

  .danger-zone {
    padding: 1.25rem;
  }
}
</style>