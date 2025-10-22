<template>
  <div
      :key="name"
      :class="
      cn(
        'group relative col-span-1 xl:col-span-2 flex flex-col justify-end overflow-hidden rounded-xl',
        // 黑色主题样式
        'bg-black [box-shadow:0_0_0_1px_rgba(255,255,255,.1),0_2px_4px_rgba(255,255,255,.05),0_12px_24px_rgba(255,255,255,.05)]',
        // 移除原来的浅色/深色模式区分
        props.class,
      )
    "
  >
    <slot name="background"/>

    <!-- 优化后的分组修改按钮 -->
    <button
        class="settings-button"
        title="编辑分组"
        @click.stop="$emit('settings-click')"
        aria-label="编辑分组设置"
    >
      <div class="button-content">
        <svg class="settings-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
        <div class="button-glow"></div>
      </div>
      <div class="tooltip">编辑分组</div>
    </button>

    <div
        class="pointer-events-none z-10 flex transform-gpu flex-col gap-1 p-6 transition-all duration-300 group-hover:-translate-y-10"
    >
      <component
          :is="icon"
          v-if="icon"
          class="size-12 origin-left transform-gpu text-white transition-all duration-300 ease-in-out group-hover:scale-75"
      />
      <div
          v-else
          class="size-12 origin-left transform-gpu text-white transition-all duration-300 ease-in-out group-hover:scale-75"
      />
      <h3 class="text-xl font-semibold text-white">
        {{ name }}
      </h3>
      <p class="max-w-lg text-gray-300">
        {{ description }}
      </p>
    </div>

    <div
        class="pointer-events-none absolute bottom-0 flex w-full translate-y-10 transform-gpu flex-row items-center p-4 opacity-0 transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100"
    >
      <a
          :href="href"
          class="pointer-events-auto inline-flex items-center rounded-md px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-gray-800 border border-gray-600"
      >
        {{ cta }} →
      </a>
    </div>
    <div
        class="pointer-events-none absolute inset-0 transform-gpu transition-all duration-300 group-hover:bg-white/[.03]"
    />
  </div>
</template>

<script lang="ts" setup>
import {cn} from "@/lib/utils";
import type {HTMLAttributes} from "vue";

interface Props {
  name: string;
  class?: HTMLAttributes["class"];
  icon?: string;
  description: string;
  href: string;
  cta: string;
}
const props = defineProps<Props>();

const emit = defineEmits('settings-click', 'main-click');
</script>

<style scoped>
.settings-button {
  position: absolute;
  top: 1.25rem;
  right: 1.25rem;
  z-index: 30;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  cursor: pointer;
  opacity: 0;
  transform: scale(0.8) translateY(-5px);
  transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  overflow: hidden;
}

.button-content {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.settings-icon {
  width: 20px;
  height: 20px;
  color: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.button-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.4) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  transition: all 0.5s cubic-bezier(0.16, 1, 0.3, 1);
  opacity: 0;
}

.tooltip {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  color: white;
  font-size: 0.75rem;
  font-weight: 500;
  white-space: nowrap;
  opacity: 0;
  transform: translateY(-10px);
  transition: all 0.3s ease;
  pointer-events: none;
  z-index: 40;
}

.tooltip::before {
  content: '';
  position: absolute;
  bottom: 100%;
  right: 12px;
  width: 0;
  height: 0;
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-bottom: 4px solid rgba(0, 0, 0, 0.8);
}

/* 卡片悬停时显示按钮 */
.group:hover .settings-button {
  opacity: 1;
  transform: scale(1) translateY(0);
}

/* 按钮悬停效果 */
.settings-button:hover {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(139, 92, 246, 0.15) 100%);
  border-color: rgba(99, 102, 241, 0.4);
  transform: scale(1.05);
  box-shadow:
      0 8px 25px rgba(99, 102, 241, 0.15),
      0 0 0 1px rgba(99, 102, 241, 0.1),
      inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.settings-button:hover .settings-icon {
  color: white;
  transform: scale(1.1) rotate(90deg);
}

.settings-button:hover .button-glow {
  width: 60px;
  height: 60px;
  opacity: 1;
}

.settings-button:hover .tooltip {
  opacity: 1;
  transform: translateY(0);
}

/* 按钮点击效果 */
.settings-button:active {
  transform: scale(0.95);
  transition: transform 0.1s ease;
}

.settings-button:active .settings-icon {
  transform: scale(0.9) rotate(45deg);
}

/* 按钮焦点状态（无障碍支持） */
.settings-button:focus {
  outline: none;
  box-shadow:
      0 0 0 3px rgba(99, 102, 241, 0.3),
      0 8px 25px rgba(99, 102, 241, 0.15);
}

.settings-button:focus:not(:hover) {
  opacity: 1;
  transform: scale(1) translateY(0);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .settings-button {
    top: 1rem;
    right: 1rem;
    width: 36px;
    height: 36px;
    opacity: 0.8; /* 移动端默认显示，便于点击 */
    transform: scale(1);
  }

  .settings-icon {
    width: 18px;
    height: 18px;
  }

  .group:hover .settings-button {
    opacity: 1;
    transform: scale(1);
  }
}

/* 动画关键帧 */
@keyframes pulse-glow {
  0%, 100% {
    box-shadow: 0 0 0 0 rgba(99, 102, 241, 0.4);
  }
  50% {
    box-shadow: 0 0 0 6px rgba(99, 102, 241, 0);
  }
}

/* 可选：添加微妙的脉动动画吸引注意力 */
.settings-button {
  animation: pulse-glow 3s infinite;
}
</style>