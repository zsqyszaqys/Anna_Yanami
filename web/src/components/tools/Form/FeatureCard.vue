<template>
  <div
    :key="name"
    :class="
      cn(
        'group relative col-span-1 xl:col-span-2 flex flex-col justify-end overflow-hidden rounded-xl',
        'bg-black [box-shadow:0_0_0_1px_rgba(255,255,255,.1),0_2px_4px_rgba(255,255,255,.05),0_12px_24px_rgba(255,255,255,.05)]',
        props.class,
      )
    "
  >
    <!-- 设置按钮 -->
    <button
      class="absolute top-4 right-4 z-20 opacity-0 group-hover:opacity-100 transition-all duration-300 p-2 rounded-lg bg-black/50 hover:bg-black/70 border border-white/20 hover:border-white/30 backdrop-blur-sm"
      aria-label="编辑分组"
      @click="showEditForm = true"
    >
      <svg
        class="w-5 h-5 text-white"
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
    </button>

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

    <!-- 编辑表单弹窗 -->
    <div
      v-if="showEditForm"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm"
      @click.self="showEditForm = false"
    >
      <div class="bg-gray-900 rounded-2xl p-6 w-full max-w-md mx-4 border border-gray-700 shadow-2xl">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-xl font-semibold text-white">
            编辑分组
          </h3>
          <button
            class="p-2 hover:bg-gray-800 rounded-lg transition-colors"
            @click="showEditForm = false"
          >
            <svg
              class="w-5 h-5 text-gray-400"
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

        <form
          class="space-y-4"
          @submit.prevent="handleSubmit"
        >
          <div>
            <label
              for="groupName"
              class="block text-sm font-medium text-gray-300 mb-2"
            >
              分组名称
            </label>
            <input
              id="groupName"
              v-model="editForm.name"
              type="text"
              class="w-full px-3 py-2 bg-gray-800 border border-gray-600 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              placeholder="输入分组名称"
              required
            >
          </div>

          <div>
            <label
              for="groupDescription"
              class="block text-sm font-medium text-gray-300 mb-2"
            >
              描述
            </label>
            <textarea
              id="groupDescription"
              v-model="editForm.description"
              rows="3"
              class="w-full px-3 py-2 bg-gray-800 border border-gray-600 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent resize-none"
              placeholder="输入分组描述"
            />
          </div>

          <div class="flex gap-3 pt-4">
            <button
              type="button"
              class="flex-1 px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg transition-colors font-medium flex items-center justify-center gap-2"
              :disabled="deleteLoading"
              @click="handleDelete"
            >
              <svg
                v-if="deleteLoading"
                class="w-4 h-4 animate-spin"
                fill="none"
                viewBox="0 0 24 24"
              >
                <circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                />
                <path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                />
              </svg>
              <svg
                v-else
                class="w-4 h-4"
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
              删除分组
            </button>

            <button
              type="submit"
              class="flex-1 px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg transition-colors font-medium"
              :disabled="submitLoading"
            >
              <span v-if="submitLoading">保存中...</span>
              <span v-else>保存更改</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { cn } from "@/lib/utils";
import type { HTMLAttributes } from "vue";
import { ref, reactive } from 'vue';

// 定义组件属性
interface Props {
  name: string;
  class?: HTMLAttributes["class"];
  icon?: string;
  description: string;
  href: string;
  cta: string;
  groupId?: string | number;
}

const props = defineProps<Props>();

// 响应式数据
const showEditForm = ref(false);
const submitLoading = ref(false);
const deleteLoading = ref(false);

// 编辑表单数据
const editForm = reactive({
  name: props.name,
  description: props.description
});

// 定义组件事件
const emit = defineEmits<{
  'update-group': [data: { id: string | number; name: string; description: string }];
  'delete-group': [id: string | number];
}>();

// 提交表单
const handleSubmit = async () => {
  submitLoading.value = true;

  try {
    // 触发更新事件
    emit('update-group', {
      id: props.groupId || props.name,
      name: editForm.name,
      description: editForm.description
    });

    // 关闭弹窗
    showEditForm.value = false;
  } catch (error) {
    console.error('更新分组失败:', error);
  } finally {
    submitLoading.value = false;
  }
};

// 删除分组
const handleDelete = async () => {
  if (!confirm('确定要删除这个分组吗？此操作不可撤销。')) {
    return;
  }

  deleteLoading.value = true;

  try {
    // 触发删除事件
    emit('delete-group', props.groupId || props.name);

    // 关闭弹窗
    showEditForm.value = false;
  } catch (error) {
    console.error('删除分组失败:', error);
  } finally {
    deleteLoading.value = false;
  }
};
</script>