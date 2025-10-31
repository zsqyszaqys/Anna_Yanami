<template>
  <div
    v-if="modelValue && link"
    class="modal-overlay"
    @click.self="close"
  >
    <div class="modal-content">
      <!-- 头部 -->
      <div class="modal-header">
        <div class="header-content">
          <div class="icon-wrapper">
            <svg
              class="header-icon"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"
              />
            </svg>
          </div>
          <div class="header-text">
            <h3 class="modal-title">
              编辑链接
            </h3>
            <p class="modal-subtitle">
              修改链接信息
            </p>
          </div>
        </div>
        <button
          class="close-button"
          @click="close"
        >
          <svg
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

      <!-- 表单区域 -->
      <div class="modal-body">
        <form @submit.prevent="handleUpdate">
          <!-- 标题 -->
          <div class="form-group">
            <label
              for="edit-link-title"
              class="form-label"
            >
              <svg
                class="label-icon"
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
              标题
            </label>
            <input
              id="edit-link-title"
              v-model="editableLink.title"
              type="text"
              class="form-input"
              placeholder="输入链接标题"
              required
            >
          </div>

          <!-- URL -->
          <div class="form-group">
            <label
              for="edit-link-url"
              class="form-label"
            >
              <svg
                class="label-icon"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"
                />
              </svg>
              URL 地址
            </label>
            <input
              id="edit-link-url"
              v-model="editableLink.url"
              type="url"
              class="form-input"
              placeholder="https://example.com"
              required
            >
          </div>

          <!-- 描述 -->
          <div class="form-group">
            <label
              for="edit-link-description"
              class="form-label"
            >
              <svg
                class="label-icon"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"
                />
              </svg>
              描述
            </label>
            <textarea
              id="edit-link-description"
              v-model="editableLink.description"
              rows="3"
              class="form-textarea"
              placeholder="输入链接描述（可选）"
            />
          </div>

          <!-- OG 图片地址 -->
          <div class="form-group">
            <label
              for="edit-link-og-image"
              class="form-label"
            >
              <svg
                class="label-icon"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
                />
              </svg>
              图片地址
            </label>
            <input
              id="edit-link-og-image"
              v-model="editableLink.ogImageUrl"
              type="text"
              class="form-input"
              placeholder="https://example.com/image.jpg"
            >
          </div>

          <!-- 收藏 -->
          <div class="form-group checkbox-group">
            <label class="checkbox-label">
              <div class="checkbox-wrapper">
                <input
                  v-model="editableLink.isFavorite"
                  type="checkbox"
                  class="checkbox-input"
                >
                <span class="checkbox-custom">
                  <svg
                    v-if="editableLink.isFavorite"
                    class="check-icon"
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
                </span>
              </div>
              <span class="checkbox-text">收藏此链接</span>
            </label>
          </div>

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button
              type="submit"
              class="button button-primary"
              :disabled="isUpdating"
            >
              <span class="button-content">
                <svg
                  v-if="isUpdating"
                  class="button-spinner"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 2v4m0 12v4m8-10h-4M6 12H2m15.364-7.364l-2.828 2.828M7.464 17.536l-2.828 2.828m12.728 0l-2.828-2.828M7.464 6.464L4.636 3.636"
                  />
                </svg>
                <span>{{ isUpdating ? '保存中...' : '保存更改' }}</span>
              </span>
            </button>
          </div>
        </form>
      </div>

      <!-- 危险区域 -->
      <div class="danger-zone">
        <div class="danger-header">
          <svg
            class="danger-icon"
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
          <h4 class="danger-title">
            危险操作
          </h4>
        </div>
        <p class="danger-description">
          删除链接后，可以从回收站恢复。
        </p>
        <button
          type="button"
          class="button button-danger"
          :disabled="isDeleting"
          @click="handleDelete"
        >
          <span class="button-content">
            <svg
              v-if="isDeleting"
              class="button-spinner"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 2v4m0 12v4m8-10h-4M6 12H2m15.364-7.364l-2.828 2.828M7.464 17.536l-2.828 2.828m12.728 0l-2.828-2.828M7.464 6.464L4.636 3.636"
              />
            </svg>
            <span>{{ isDeleting ? '删除中...' : '删除此链接' }}</span>
          </span>
        </button>
      </div>
    </div>
  </div>

  <!-- 自定义 Alert 组件 -->
  <CustomConfirm
    v-model:visible="showAlert"
    :title="alertTitle"
    :message="alertMessage"
    :type="alertType"
    @close="handleAlertClose"
  />
</template>

<script lang="ts" setup>
import {ref, watch, type PropType} from 'vue';
import CustomConfirm from "@/components/tools/CustomConfirm.vue";

interface Link {
  id: number;
  groupId: number;
  title: string;
  url: string;
  description?: string | null;
  faviconUrl?: string | null;
  ogImageUrl?: string | null;
  isFavorite: boolean;
  orderIndex: number;
  clickCount: number;
  lastClickedAt: Date | null;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}

// Alert 相关状态
const showAlert = ref(false);
const alertTitle = ref('');
const alertMessage = ref('');
const alertType = ref<'success' | 'error'>('success');

const props = defineProps({
  modelValue: {type: Boolean, required: true},
  link: {type: Object as PropType<Link | null>, default: null},
});

const emit = defineEmits(['update:modelValue', 'link-updated', 'link-deleted']);

// 创建一个可编辑的 link 副本
const editableLink = ref<Partial<Link>>({});
const isUpdating = ref(false);
const isDeleting = ref(false);

function showCustomAlert(title: string, message: string, type: 'success' | 'error' = 'success') {
  alertTitle.value = title;
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
}

function handleAlertClose() {
  showAlert.value = false;
}


// 当 prop `link` 变化时，更新我们的可编辑副本
watch(() => props.link, (newLink) => {
  if (newLink) {
    editableLink.value = {...newLink};
  }
}, {immediate: true});

function close() {
  emit('update:modelValue', false);
}

// 处理更新
async function handleUpdate() {
  if (!props.link || isUpdating.value) return;

  isUpdating.value = true;
  try {
    // 调用 API (PATCH /api/me/links/{id})
    const response = await fetch(`https://app7510.acapp.acwing.com.cn/api/me/links/${props.link.id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        title: editableLink.value.title,
        url: editableLink.value.url,
        description: editableLink.value.description,
        ogImageUrl: editableLink.value.ogImageUrl,
        isFavorite: editableLink.value.isFavorite,
      }),
    });

    const result = await response.json();
    if (!response.ok || result.status === false) {
      throw new Error(result.message || '更新失败');
    }

    emit('link-updated');
    showCustomAlert('修改成功', '分组修改成功！', 'success');
    close();
  } catch (error: any) {
    showCustomAlert('修改失败', `修改失败: ${error.message}`, 'error');
  } finally {
    isUpdating.value = false;
  }
}

// 处理删除
async function handleDelete() {
  if (!props.link || isDeleting.value) return;
  if (!window.confirm(`确定要删除链接 "${props.link.title}" 吗？`)) return;

  isDeleting.value = true;
  try {
    // 调用 API (DELETE /api/me/links/{id})
    await fetch(`https://app7510.acapp.acwing.com.cn/api/me/links/${props.link.id}`, {
      method: 'DELETE',
      headers: {'Accept': 'application/json'},
      credentials: 'include',
    });

    emit('link-deleted', props.link.id);
    showCustomAlert('删除成功', '分组删除成功！', 'success');
    close();
  } catch (error: any) {
    showCustomAlert('删除失败', '分组删除失败！', 'error');
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
  border-radius: 20px;
  padding: 0;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5),
  0 0 0 1px rgba(255, 255, 255, 0.1);
  animation: slideUp 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 2rem 2rem 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-content {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.icon-wrapper {
  width: 3rem;
  height: 3rem;
  border-radius: 12px;
  background: linear-gradient(135deg, #4299e1 0%, #667eea 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.header-icon {
  width: 1.5rem;
  height: 1.5rem;
  color: white;
}

.header-text {
  flex: 1;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.25rem 0;
  line-height: 1.2;
}

.modal-subtitle {
  color: #a0aec0;
  font-size: 0.875rem;
  margin: 0;
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
  flex-shrink: 0;
}

.close-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.close-button svg {
  width: 1.25rem;
  height: 1.25rem;
  color: #a0aec0;
}

.modal-body {
  padding: 1.5rem 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #e2e8f0;
  font-size: 0.875rem;
}

.label-icon {
  width: 1rem;
  height: 1rem;
  color: #a0aec0;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 0.875rem 1rem;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(0, 0, 0, 0.3);
  color: #e2e8f0;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
  background: rgba(0, 0, 0, 0.4);
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.checkbox-group {
  margin-top: 1.5rem;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 0.5rem;
  border-radius: 10px;
}

.checkbox-label:hover {
  background: rgba(255, 255, 255, 0.05);
}

.checkbox-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.checkbox-input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.checkbox-custom {
  width: 1.25rem;
  height: 1.25rem;
  border: 2px solid #4a5568;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  background: rgba(0, 0, 0, 0.2);
}

.checkbox-input:checked + .checkbox-custom {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  border-color: #48bb78;
}

.check-icon {
  width: 0.875rem;
  height: 0.875rem;
  color: white;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.checkbox-input:checked + .checkbox-custom .check-icon {
  opacity: 1;
}

.checkbox-text {
  font-weight: 500;
  color: #e2e8f0;
}

.form-actions {
  margin-top: 2rem;
  text-align: right;
}

.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.875rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  position: relative;
  overflow: hidden;
}

.button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.button:not(:disabled):hover {
  transform: translateY(-2px);
}

.button-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.button-spinner {
  width: 1rem;
  height: 1rem;
  animation: spin 1s linear infinite;
}

.button-primary {
  background: linear-gradient(135deg, #4299e1 0%, #667eea 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(66, 153, 225, 0.3);
}

.button-primary:not(:disabled):hover {
  box-shadow: 0 6px 20px rgba(66, 153, 225, 0.4);
}

.danger-zone {
  margin-top: 1rem;
  padding: 1.5rem 2rem;
  background: rgba(254, 215, 215, 0.05);
  border-top: 1px solid rgba(254, 215, 215, 0.2);
}

.danger-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.danger-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: #fc8181;
}

.danger-title {
  color: #fc8181;
  font-weight: 600;
  margin: 0;
  font-size: 1rem;
}

.danger-description {
  color: #a0aec0;
  font-size: 0.875rem;
  margin: 0 0 1rem 0;
  line-height: 1.5;
}

.button-danger {
  background: linear-gradient(135deg, #e53e3e 0%, #c53030 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(229, 62, 62, 0.3);
}

.button-danger:not(:disabled):hover {
  box-shadow: 0 6px 20px rgba(229, 62, 62, 0.4);
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

  .modal-header,
  .modal-body,
  .danger-zone {
    padding: 1.5rem;
  }

  .header-content {
    gap: 0.75rem;
  }

  .icon-wrapper {
    width: 2.5rem;
    height: 2.5rem;
  }

  .header-icon {
    width: 1.25rem;
    height: 1.25rem;
  }

  .modal-title {
    font-size: 1.25rem;
  }

  .form-actions {
    text-align: center;
  }

  .button {
    width: 100%;
  }
}
</style>