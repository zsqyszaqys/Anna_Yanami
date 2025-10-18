<template>
  <!-- 修复后的粒子背景 -->
  <div class="particles-wrapper">
    <VorTex
      background-color="black"
      :range-y="800"
      :particle-count="500"
      :base-hue="120"
      class="flex size-full flex-col items-center justify-center px-2 py-4 md:px-10"
    >
      <!-- 在粒子背景上添加内容 -->
      <div class="text-center text-white">
        <h1 class="text-4xl md:text-6xl font-bold mb-4">
          分组 {{ groupId }}
        </h1>
        <p class="text-lg md:text-xl opacity-90">
          探索这个分组中的所有链接
        </p>
      </div>
    </VorTex>
  </div>

  <!-- 内容区域 -->
  <div class="min-h-screen bg-slate-50 relative z-10">
    <div class="container mx-auto px-4 py-6">
      <header class="mb-4 flex items-center justify-between">
        <h1 class="text-xl font-semibold">
          分组 {{ groupId }}
        </h1>
        <div class="flex gap-2">
          <input
            v-model="q"
            placeholder="搜索标题"
            class="border rounded px-3 py-1 text-sm"
            @keyup.enter="reload(1)"
          >
          <select
            v-model="sort"
            class="border rounded px-2 py-1 text-sm"
            @change="reload(1)"
          >
            <option value="default">
              默认排序
            </option>
            <option value="recent">
              最近创建
            </option>
          </select>
        </div>
      </header>

      <div
        v-if="loading"
        class="text-sm text-gray-500"
      >
        加载中...
      </div>
      <div v-else>
        <div
          v-if="links.length === 0"
          class="text-sm text-gray-500"
        >
          暂无链接
        </div>

        <div class="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
          <a
            v-for="link in links"
            :key="link.id"
            class="rounded-lg border bg-white p-4 hover:shadow transition"
            :href="link.url"
            target="_blank"
            rel="noopener"
            @click="trackClick(link.id)"
          >
            <div class="flex items-center gap-3">
              <img
                v-if="link.faviconUrl"
                :src="link.faviconUrl"
                class="w-5 h-5"
              >
              <div class="font-medium truncate">{{ link.title }}</div>
            </div>
            <div class="text-xs text-gray-500 mt-1 line-clamp-2">{{ link.description }}</div>
          </a>
        </div>

        <!-- 分页 -->
        <div
          v-if="totalPages > 1"
          class="mt-4 flex justify-center items-center gap-2"
        >
          <button
            class="px-3 py-1 border rounded text-sm"
            :disabled="pageNo===1"
            @click="changePage(1)"
          >
            «
          </button>
          <button
            class="px-3 py-1 border rounded text-sm"
            :disabled="pageNo===1"
            @click="changePage(pageNo-1)"
          >
            上一页
          </button>
          <span class="text-xs text-gray-500">第 {{ pageNo }} / {{ totalPages }} 页（共 {{ total }} 条）</span>
          <button
            class="px-3 py-1 border rounded text-sm"
            :disabled="pageNo===totalPages"
            @click="changePage(pageNo+1)"
          >
            下一页
          </button>
          <button
            class="px-3 py-1 border rounded text-sm"
            :disabled="pageNo===totalPages"
            @click="changePage(totalPages)"
          >
            »
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { ref, computed, watch, onMounted, onErrorCaptured } from 'vue';

// 动态导入 VorTex 组件，处理可能的加载错误
let VorTex: any = null;
const vortexError = ref<string | null>(null);

onErrorCaptured((err) => {
  console.error('组件错误:', err);
  vortexError.value = err.message;
  return false;
});

// 尝试加载 VorTex 组件
try {
  VorTex = (await import("@/components/tools/Vortex Background/VorTex.vue")).default;
  console.log('Vortex 组件加载成功');
} catch (error) {
  console.error('Vortex 组件加载失败:', error);
  vortexError.value = 'Vortex 组件加载失败，请检查依赖和组件路径';
}

// 简单类型
type Link = {
  id: number; groupId: number;
  title: string; url: string;
  description?: string | null;
  faviconUrl?: string | null;
  ogImageUrl?: string | null;
  isFavorite: boolean;
  orderIndex: number;
  clickCount: number;
};
type Page<T> = { records: T[]; total: number; size: number; current: number; };

const route = useRoute();
const router = useRouter();

const groupId = computed(() => Number(route.params.id));
const pageNo = ref(Number(route.query.pageNo || 1));
const pageSize = ref(24);
const sort = ref((route.query.sort as 'default' | 'recent') || 'default');
const q = ref((route.query.q as string) || '');

const loading = ref(false);
const links = ref<Link[]>([]);
const total = ref(0);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)));

async function fetchLinks() {
  loading.value = true;
  try {
    const res = await fetch(`/api/me/groups/${groupId.value}/links?pageNo=${pageNo.value}&pageSize=${pageSize.value}&sort=${sort.value}&q=${encodeURIComponent(q.value)}`, {
      headers: {Accept: 'application/json'},
      credentials: 'include',
    });
    const json = await res.json();
    if (!res.ok || json?.status !== true || json?.code !== 200) throw new Error(json?.message || '加载失败');
    const pageData = json.data as Page<Link>;
    links.value = pageData.records || [];
    total.value = pageData.total || 0;
  } finally {
    loading.value = false;
  }
}

function changePage(p: number) {
  const clamped = Math.min(Math.max(1, p), totalPages.value);
  if (clamped === pageNo.value) return;
  pageNo.value = clamped;
  // 同步到地址栏（可刷新保持状态）
  router.replace({
    name: 'GroupLinks',
    params: {id: groupId.value},
    query: {pageNo: String(pageNo.value), sort: sort.value, q: q.value}
  });
}

function reload(p = 1) {
  changePage(p);
  fetchLinks();
}

// 点击统计（忽略失败）
async function trackClick(id: number) {
  try {
    await fetch(`/api/me/links/${id}/click`, {method: 'POST', credentials: 'include'});
  } catch {
    // 先空着
  }
}

watch([groupId, pageNo, pageSize, sort, q], fetchLinks, {immediate: true});
onMounted(fetchLinks);
</script>

<style scoped>
.particles-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: 1;
}
</style>