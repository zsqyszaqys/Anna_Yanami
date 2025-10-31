<template>
  <!-- 模态框容器，v-if 控制显示/隐藏 -->
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
                class="create-icon"
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
            </div>
            <div>
              <h3 class="modal-title">
                新建分组
              </h3>
              <p class="create-subtitle">
                创建一个新的内容分组
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
          class="create-form"
          @submit.prevent="handleCreate"
        >
          <!-- 分组名称 -->
          <div class="form-group">
            <label
              for="new-group-name"
              class="form-label"
            >
              <span class="label-text">分组名称</span>
              <span class="required-indicator">*</span>
            </label>
            <div class="input-wrapper">
              <input
                id="new-group-name"
                v-model="newGroup.name"
                type="text"
                class="form-input"
                placeholder="为分组起一个好听的名字..."
                required
                :disabled="isCreating"
              >
              <div class="input-focus-border" />
            </div>
          </div>

          <!-- 描述信息 -->
          <div class="form-group">
            <label
              for="new-group-description"
              class="form-label"
            >
              <span class="label-text">描述信息</span>
              <span class="optional-indicator">可选</span>
            </label>
            <div class="input-wrapper">
              <textarea
                id="new-group-description"
                v-model="newGroup.description"
                rows="3"
                class="form-textarea"
                placeholder="描述这个分组的用途或特点..."
                :disabled="isCreating"
              />
              <div class="input-focus-border" />
            </div>
            <div
              v-if="newGroup.description"
              class="character-count"
            >
              {{ newGroup.description.length }}/200
            </div>
          </div>

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button
              type="submit"
              class="button button-primary"
              :class="{ 'button-loading': isCreating }"
              :disabled="isCreating || !newGroup.name.trim()"
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
                {{ isCreating ? '创建中...' : '创建分组' }}
              </span>
            </button>
          </div>
        </form>
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
import { ref } from 'vue';
import CustomConfirm from "@/components/tools/CustomConfirm.vue";

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

// Alert 相关状态
const showAlert = ref(false);
const alertTitle = ref('');
const alertMessage = ref('');
const alertType = ref<'success' | 'error'>('success');

function close() {
  emit('update:modelValue', false);
  // 关闭时重置表单，以便下次打开是干净的
  newGroup.value = { name: '', description: '' };
}

function showCustomAlert(title: string, message: string, type: 'success' | 'error' = 'success') {
  alertTitle.value = title;
  alertMessage.value = message;
  alertType.value = type;
  showAlert.value = true;
}

function handleAlertClose() {
  showAlert.value = false;
}

async function handleCreate() {
  if (!newGroup.value.name.trim() || isCreating.value) return;

  isCreating.value = true;
  try {
    // API 调用 POST /api/me/groups
    const response = await fetch('https://app7510.acapp.acwing.com.cn/api/me/groups', {
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
    showCustomAlert('创建成功', '分组创建成功！', 'success');
    close();
  } catch (error: any) {
    showCustomAlert('创建失败', `创建失败: ${error.message}`, 'error');
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
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  padding: 0.5rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.create-icon {
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

.create-subtitle {
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

.create-form {
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
  border-color: #48bb78;
  background: rgba(26, 32, 44, 0.9);
  box-shadow: 0 0 0 3px rgba(72, 187, 120, 0.1);
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
  background: linear-gradient(90deg, #48bb78, #4299e1);
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
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(72, 187, 120, 0.3);
}

.button-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(72, 187, 120, 0.4);
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
}
</style>