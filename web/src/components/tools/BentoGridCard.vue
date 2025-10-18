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
    <slot name="background" />

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
      ></div>
      <h3 class="text-xl font-semibold text-white">
        {{ name }}
      </h3>
      <p class="max-w-lg text-gray-300">{{ description }}</p>
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
import { cn } from "@/lib/utils";
import type { HTMLAttributes } from "vue";

interface Props {
  name: string;
  class?: HTMLAttributes["class"];
  icon?: string;
  description: string;
  href: string;
  cta: string;
}

const props = defineProps<Props>();
</script>