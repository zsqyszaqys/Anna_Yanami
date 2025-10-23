<template>
  <!-- 自定义 Confirm 组件 -->
  <div
    v-if="visible"
    class="custom-confirm-overlay"
    @click.self="handleOverlayClick"
  >
    <div
      class="custom-confirm"
      :class="type"
    >
      <div class="confirm-header">
        <div class="confirm-icon">
          <svg
            v-if="type === 'success'"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <svg
            v-else-if="type === 'error'"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <svg
            v-else
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
        <div class="confirm-content">
          <h3 class="confirm-title">
            {{ title }}
          </h3>
          <p class="confirm-message">
            {{ message }}
          </p>
        </div>
        <button
          class="confirm-close"
          @click="handleCancel"
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
      <div class="confirm-actions">
        <button
          class="confirm-button cancel-button"
          @click="handleCancel"
        >
          取消
        </button>
        <button
          class="confirm-button confirm-button-primary"
          :class="type"
          @click="handleConfirm"
        >
          {{ confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';

interface Props {
  visible: boolean;
  title: string;
  message: string;
  type?: 'success' | 'error' | 'warning';
  confirmText?: string;
  cancelText?: string;
  closeOnOverlay?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'warning',
  confirmText: '确定',
  cancelText: '取消',
  closeOnOverlay: true
});

const emit = defineEmits(['update:visible', 'confirm', 'cancel']);

const visible = ref(props.visible);

watch(() => props.visible, (newVal) => {
  visible.value = newVal;
});

watch(visible, (newVal) => {
  emit('update:visible', newVal);
});

const handleConfirm = () => {
  visible.value = false;
  emit('confirm');
};

const handleCancel = () => {
  visible.value = false;
  emit('cancel');
};

const handleOverlayClick = () => {
  if (props.closeOnOverlay) {
    handleCancel();
  }
};
</script>

<style scoped>
.custom-confirm-overlay {
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
  z-index: 9999;
  animation: fadeIn 0.3s ease-out;
}

.custom-confirm {
  background: linear-gradient(135deg, #1a202c 0%, #2d3748 100%);
  border-radius: 20px;
  padding: 0;
  width: 90%;
  max-width: 420px;
  box-shadow:
      0 25px 50px -12px rgba(0, 0, 0, 0.5),
      0 0 0 1px rgba(255, 255, 255, 0.1);
  animation: slideUp 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  overflow: hidden;
}

.confirm-header {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 2rem 2rem 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.confirm-icon {
  flex-shrink: 0;
  width: 3rem;
  height: 3rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-confirm.success .confirm-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.custom-confirm.error .confirm-icon {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
}

.custom-confirm.warning .confirm-icon {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.confirm-icon svg {
  width: 1.5rem;
  height: 1.5rem;
  color: white;
}

.confirm-content {
  flex: 1;
  min-width: 0;
}

.confirm-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.5rem 0;
  line-height: 1.2;
}

.confirm-message {
  color: #e2e8f0;
  font-size: 0.95rem;
  line-height: 1.5;
  margin: 0;
  opacity: 0.9;
}

.confirm-close {
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

.confirm-close:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.confirm-close svg {
  width: 1.25rem;
  height: 1.25rem;
  color: #a0aec0;
}

.confirm-actions {
  padding: 1.5rem 2rem 2rem;
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
}

.confirm-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 80px;
}

.cancel-button {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.cancel-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.confirm-button-primary {
  background: linear-gradient(135deg, #4299e1 0%, #667eea 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(66, 153, 225, 0.3);
}

.confirm-button-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(66, 153, 225, 0.4);
}

.custom-confirm.success .confirm-button-primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
}

.custom-confirm.success .confirm-button-primary:hover {
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.4);
}

.custom-confirm.error .confirm-button-primary {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  box-shadow: 0 4px 15px rgba(239, 68, 68, 0.3);
}

.custom-confirm.error .confirm-button-primary:hover {
  box-shadow: 0 6px 20px rgba(239, 68, 68, 0.4);
}

.custom-confirm.warning .confirm-button-primary {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  box-shadow: 0 4px 15px rgba(245, 158, 11, 0.3);
}

.custom-confirm.warning .confirm-button-primary:hover {
  box-shadow: 0 6px 20px rgba(245, 158, 11, 0.4);
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

/* 响应式设计 */
@media (max-width: 640px) {
  .custom-confirm {
    width: 95%;
    margin: 1rem;
  }

  .confirm-header {
    padding: 1.5rem 1.5rem 1rem;
  }

  .confirm-actions {
    padding: 1.25rem 1.5rem 1.5rem;
    flex-direction: column-reverse;
  }

  .confirm-icon {
    width: 2.5rem;
    height: 2.5rem;
  }

  .confirm-icon svg {
    width: 1.25rem;
    height: 1.25rem;
  }

  .confirm-button {
    width: 100%;
  }
}
</style>