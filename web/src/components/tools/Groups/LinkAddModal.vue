<template>
  <div
    v-if="modelValue"
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
                class="link-icon"
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
            <div>
              <h3 class="modal-title">
                新增链接
              </h3>
              <p class="modal-subtitle">
                添加到当前分组
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
          class="link-form"
          @submit.prevent="handleCreate"
        >
          <!-- 标题 -->
          <div class="form-group">
            <label
              for="link-title"
              class="form-label"
            >
              <span class="label-text">标题</span>
              <span class="required-indicator">*</span>
            </label>
            <div class="input-wrapper">
              <input
                id="link-title"
                v-model="newLink.title"
                type="text"
                class="form-input"
                placeholder="输入链接标题..."
                required
                :disabled="isCreating"
              >
              <div class="input-focus-border" />
            </div>
          </div>

          <!-- URL -->
          <div class="form-group">
            <label
              for="link-url"
              class="form-label"
            >
              <span class="label-text">URL 地址</span>
              <span class="required-indicator">*</span>
            </label>
            <div class="input-wrapper">
              <input
                id="link-url"
                v-model="newLink.url"
                type="url"
                class="form-input"
                placeholder="https://example.com"
                required
                :disabled="isCreating"
              >
              <div class="input-focus-border" />
            </div>
          </div>

          <!-- 描述 -->
          <div class="form-group">
            <label
              for="link-description"
              class="form-label"
            >
              <span class="label-text">描述</span>
              <span class="optional-indicator">可选</span>
            </label>
            <div class="input-wrapper">
              <textarea
                id="link-description"
                v-model="newLink.description"
                rows="3"
                class="form-textarea"
                placeholder="链接的简短描述..."
                :disabled="isCreating"
              />
              <div class="input-focus-border" />
            </div>
            <div
              v-if="newLink.description"
              class="character-count"
            >
              {{ newLink.description.length }}/200
            </div>
          </div>

          <!-- OG 图片地址 -->
          <div class="form-group">
            <label
              for="link-og-image"
              class="form-label"
            >
              <span class="label-text">图片地址</span>
              <span class="optional-indicator">可选</span>
            </label>
            <div class="input-wrapper">
              <input
                id="link-og-image"
                v-model="newLink.ogImageUrl"
                type="text"
                class="form-input"
                placeholder="输入图片的 URL"
                :disabled="isCreating"
              >
              <div class="input-focus-border" />
            </div>
          </div>

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button
              type="submit"
              class="button button-primary"
              :class="{ 'button-loading': isCreating }"
              :disabled="isCreating || !newLink.title.trim() || !newLink.url.trim()"
            >
              <span class="button-content">
                <svg
                  v-if="isCreating"
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
                    d="M12 6v6m0 0v6m0-6h6m-6 0H6"
                  />
                </svg>
                {{ isCreating ? '创建中...' : '创建链接' }}
              </span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <custom-confirm
    v-model:visible="showAlert"
    :title="alertTitle"
    :message="alertMessage"
    :type="alertType"
    @close="handleAlertClose"
  />
</template>

<script lang="ts" setup>
import {ref, watch} from 'vue';
import CustomConfirm from "@/components/tools/CustomConfirm.vue";

// 定义要创建的链接对象结构
interface NewLink {
  title: string;
  url: string;
  description?: string;
  ogImageUrl?: string;
  isFavorite: boolean;
}

const showAlert = ref(false);
const alertTitle = ref('');
const alertMessage = ref('');
const alertType = ref<'success' | 'error'>('success');

const props = defineProps({
  // v-model 控制显示
  modelValue: {
    type: Boolean,
    required: true,
  },
  // 需要知道要添加到哪个分组
  groupId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['update:modelValue', 'link-created']);

// 用于表单绑定的 ref
const initialFormState = {
  title: '',
  url: '',
  description: '',
  ogImageUrl: '',
  isFavorite: false,
};

const newLink = ref<NewLink>({...initialFormState});

const isCreating = ref(false);

function showCustomAlert(title: string, message: string, type: 'success' | 'error' = 'success') {
  alertTitle.value = title;
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
}

function handleAlertClose() {
  showAlert.value = false;
}

function close() {
  emit('update:modelValue', false);
}

// 每次打开时，重置表单
watch(() => props.modelValue, (isOpen) => {
  if (isOpen) {
    newLink.value = {...initialFormState};
  }
});

async function handleCreate() {
  if (!newLink.value.title.trim() || !newLink.value.url.trim() || isCreating.value) {
    alert('标题和 URL 是必填项！');
    return;
  }

  isCreating.value = true;
  try {
    // API 调用 POST /api/me/links
    // 你的后端文档指明了需要 groupId
    const response = await fetch('http://localhost:3000/api/me/links', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        groupId: props.groupId, // 关键！
        ...newLink.value,
      }),
    });

    const result = await response.json();
    if (!response.ok || result.status === false) {
      throw new Error(result.message || '创建链接失败');
    }

    emit('link-created');
    showCustomAlert("创建成功","链接创建成功", "success");
    close();
  } catch (error: any) {
    showCustomAlert('创建失败', `创建失败: ${error.message}`, 'error');
    console.error(error);
  } finally {
    isCreating.value = false;
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
  max-width: 520px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5),
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
  background: linear-gradient(135deg, #4299e1 0%, #667eea 100%);
  padding: 0.5rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.link-icon {
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

.modal-subtitle {
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

.link-form {
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

/* 自定义复选框样式 */
.checkbox-group {
  margin-top: 0.5rem;
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
  cursor: pointer;
  height: 0;
  width: 0;
}

.checkbox-custom {
  width: 20px;
  height: 20px;
  border: 2px solid #4a5568;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  background: rgba(26, 32, 44, 0.8);
}

.checkbox-input:checked + .checkbox-custom {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  border-color: #48bb78;
}

.checkbox-input:checked + .checkbox-custom .check-icon {
  opacity: 1;
  transform: scale(1);
}

.check-icon {
  width: 14px;
  height: 14px;
  color: white;
  opacity: 0;
  transform: scale(0.8);
  transition: all 0.2s ease;
}

.checkbox-text {
  font-size: 0.875rem;
  font-weight: 500;
  color: #e2e8f0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
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

  .header-content {
    flex-direction: column;
    gap: 1rem;
  }

  .title-section {
    width: 100%;
  }

  .close-button {
    align-self: flex-end;
  }
}
</style>