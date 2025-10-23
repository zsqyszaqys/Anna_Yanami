<template>
  <div class="relative h-screen w-full overflow-hidden">
    <VorTex
        background-color="black"
        :range-y="800"
        :particle-count="500"
        :base-hue="120"
        class="absolute inset-0"
    >
      <div class="relative mx-auto grid h-full w-full grid-rows-[auto,1fr,auto] gap-4 px-4 py-4">
        <!-- 工具栏区域 -->
        <header class="row-start-1 row-end-2 flex items-center justify-between">
          <WaterButton
              class="rounded-lg border border-white/20 bg-white/10 px-3 py-1.5 text-sm text-white/80 hover:bg-white/20 transition-colors"
              @click="router.back()"
          >
            <i class="iconfont icon-fanhui_fanhui text-white/80 text-base"/>
            Exit
          </WaterButton>
        </header>

        <!-- 内容区域 - 保持可滚动 -->
        <main class="row-start-2 row-end-3 overflow-auto">
          <!-- 使用自动行高，让内容自然流动 -->
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 pb-4">
            <div
                v-if="loading"
                class="col-span-full grid place-items-center py-8 text-white/80"
            >
              加载中…
            </div>
            <div
                v-else-if="!links.length"
                class="col-span-full grid place-items-center py-8 text-white/80"
            >
              暂无数据
            </div>

            <!-- 卡片内容 -->
            <CardContainer
                v-for="link in links"
                :key="link.id"
                class="min-h-[300px]"
                @click="openAndTrack(link)"
            >
              <CardBody
                  class="group/card relative h-full rounded-xl border border-emerald-500/20 bg-gradient-to-br from-gray-800 to-gray-900 p-6 backdrop-blur-sm flex flex-col"
              >
                <CardItem
                    :translate-z="50"
                    class="text-xl font-bold text-white line-clamp-2"
                >
                  {{ link.title }}
                </CardItem>
                <CardItem
                    as="p"
                    translate-z="60"
                    class="mt-2 flex-1 text-sm text-gray-300 line-clamp-3"
                >
                  {{ link.description || '暂无描述' }}
                </CardItem>
                <CardItem
                    :translate-z="100"
                    :rotate-x="20"
                    :rotate-z="-10"
                    class="mt-4 w-full"
                >
                  <img
                      :src="link.ogImageUrl || '/placeholder-image.jpg'"
                      height="1000"
                      width="1000"
                      class="h-40 w-full rounded-xl object-cover group-hover/card:shadow-xl"
                      alt="thumbnail"
                      @error="handleImageError"
                  >
                </CardItem>
                <div class="mt-4 flex items-center justify-between">
                  <CardItem
                      :translate-z="20"
                      :translate-x="-30"
                      as="button"
                      class="rounded-xl px-4 py-2 text-xs font-normal"
                      @click.stop="openEditLinkModal(link)"
                  >
                    <InteractiveHoverButton text="Modift"/>
                  </CardItem>
                  <CardItem
                      :translate-z="20"
                      :translate-x="30"
                      as="button"
                      class="rounded-xl bg-black px-4 py-2 text-xs font-bold text-white dark:bg-white dark:text-black"
                      @click.stop="openAndTrack(link)"
                  >
                    Start
                  </CardItem>
                </div>
              </CardBody>
            </CardContainer>
          </div>
        </main>

        <!-- 新的分页条（替换原有的） -->
        <nav
            class="pagination-container"
            aria-label="Pagination"
        >
          <!-- 首页按钮 -->
          <button
              class="pagination-btn"
              :disabled="page === 1"
              title="首页"
              @click="goto(1)"
          >
            <i class="fas fa-angle-double-left"/>
          </button>

          <!-- 上一页按钮 -->
          <button
              class="pagination-btn"
              :disabled="page === 1"
              title="上一页"
              @click="prev"
          >
            <i class="fas fa-angle-left"/>
          </button>

          <!-- 数字页码（带省略号） -->
          <template
              v-for="item in pageList"
              :key="item.key"
          >
            <button
                v-if="item.type === 'page'"
                class="pagination-page"
                :class="item.page === page ? 'active' : ''"
                @click="goto(item.page!)"
            >
              {{ item.page }}
            </button>
            <span
                v-else
                class="pagination-dots"
            >…</span>
          </template>

          <!-- 下一页按钮 -->
          <button
              class="pagination-btn"
              :disabled="page === totalPages"
              title="下一页"
              @click="next"
          >
            <i class="fas fa-angle-right"/>
          </button>

          <!-- 末页按钮 -->
          <button
              class="pagination-btn"
              :disabled="page === totalPages"
              title="末页"
              @click="goto(totalPages)"
          >
            <i class="fas fa-angle-double-right"/>
          </button>

          <!-- 页码信息 -->
          <div class="pagination-text">
            <span
                v-if="loading"
                class="loading-text"
            >
              <div class="loading-indicator"/>
              加载中...
            </span>
            <span v-else>
              第 {{ page }} / {{ totalPages }} 页（共 {{ total }} 个链接）
            </span>
          </div>

          <!--  新增按钮-->
          <div class="ml-auto">
            <button @click="isAddLinkModalOpen = true" class="fab-add-link-side" title="新增链接">
              <i class="iconfont icon-add"></i>
            </button>
          </div>
        </nav>
      </div>
    </VorTex>

    <!--编辑模态框-->
    <LinkEditModal
        v-model="isEditLinkModalOpen"
        :link="selectedLink"
        @link-updated="handleLinkUpdated"
        @link-deleted="handleLinkDeleted"
    />

    <!--    新建连接模态框-->
    <LinkAddModal
        v-model="isAddLinkModalOpen"
        :group-id="groupId"
        @link-created="handleLinkCreated"
    />

  </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router';
import {ref, computed, watch} from 'vue';

import CardBody from "@/components/tools/Card/CardBody.vue";
import CardContainer from "@/components/tools/Card/CardContainer.vue";
import CardItem from "@/components/tools/Card/CardItem.vue";
import VorTex from "@/components/tools/VortexBackground/VorTex.vue";
import InteractiveHoverButton from "@/components/tools/Button/InteractiveHoverButton.vue";
import WaterButton from "@/components/tools/Button/WaterButton.vue";
import LinkAddModal from "@/components/tools/Groups/LinkAddModal.vue";
import LinkEditModal from "@/components/tools/Groups/LinkEditModal.vue";

// 接口原始类型（时间是字符串）
type LinkDTO = {
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
  lastClickedAt: string | null;
  createdAt: string;
  updatedAt: string;
  deletedAt: string | null;
};

// 业务层类型（时间转成 Date）
type Link = {
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
};

type Page<T> = { records: T[]; total: number; size: number; current: number };


// DTO -> 业务类型：把字符串日期转为 Date
function toLink(d: LinkDTO): Link {
  return {
    ...d,
    createdAt: new Date(d.createdAt),
    updatedAt: new Date(d.updatedAt),
    lastClickedAt: d.lastClickedAt ? new Date(d.lastClickedAt) : null,
    deletedAt: d.deletedAt ? new Date(d.deletedAt) : null,
  }
}

const route = useRoute();
const router = useRouter();

const groupId = computed(() => Number(route.params.groupId));
const pageNo = ref(Number(route.query.pageNo) || 1);
const pageSize = ref(16); // 保持16条每页
const sort = ref<'default' | 'recent'>((route.query.sort as any) || 'default');
const q = ref((route.query.q as string) || ''); // 搜索变量

const loading = ref(false);
const error = ref<string | null>(null);
const links = ref<Link[]>([]);
const total = ref(0);
const isAddLinkModalOpen = ref(false);

const isEditLinkModalOpen = ref(false);
const selectedLink = ref<Link | null>(null);

// 计算属性
const page = computed(() => pageNo.value);
const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

// 分页列表计算
const pageList = computed(() => {
  const current = pageNo.value;
  const totalPage = totalPages.value;
  const list: Array<{ type: 'page' | 'dots'; page?: number; key: string }> = [];

  if (totalPage <= 7) {
    // 总页数小于等于7，显示所有页码
    for (let i = 1; i <= totalPage; i++) {
      list.push({type: 'page', page: i, key: i.toString()});
    }
  } else {
    // 总页数大于7，显示部分页码和省略号
    if (current <= 4) {
      // 当前页在前4页
      for (let i = 1; i <= 5; i++) {
        list.push({type: 'page', page: i, key: i.toString()});
      }
      list.push({type: 'dots', key: 'dots1'});
      list.push({type: 'page', page: totalPage, key: totalPage.toString()});
    } else if (current >= totalPage - 3) {
      // 当前页在后4页
      list.push({type: 'page', page: 1, key: '1'});
      list.push({type: 'dots', key: 'dots1'});
      for (let i = totalPage - 4; i <= totalPage; i++) {
        list.push({type: 'page', page: i, key: i.toString()});
      }
    } else {
      // 当前页在中间
      list.push({type: 'page', page: 1, key: '1'});
      list.push({type: 'dots', key: 'dots1'});
      for (let i = current - 1; i <= current + 1; i++) {
        list.push({type: 'page', page: i, key: i.toString()});
      }
      list.push({type: 'dots', key: 'dots2'});
      list.push({type: 'page', page: totalPage, key: totalPage.toString()});
    }
  }

  return list;
});

function reload(p: number) {
  toPage(p);
}

async function fetchLinks() {
  if (!groupId.value || groupId.value <= 0) return;

  loading.value = true;
  error.value = null;

  try {
    // 添加搜索参数
    const searchParams = new URLSearchParams({
      pageNo: pageNo.value.toString(),
      pageSize: pageSize.value.toString(),
      sort: sort.value,
      ...(q.value && {q: q.value}) // 如果有搜索词就添加
    });

    const url = `http://localhost:3000/api/me/groups/${groupId.value}/links?${searchParams}`;
    const res = await fetch(url, {
      headers: {Accept: 'application/json'},
      credentials: 'include'
    });

    const json = await res.json();

    if (!res.ok || json?.status != true || json?.code != 200) {
      throw new Error(json?.message || `加载失败(${res.status})`);
    }

    const pageData = json.data as Page<LinkDTO>;

    links.value = pageData.records.map(toLink);
    total.value = pageData.total || 0;

    console.log(links.value);
  } catch (e: any) {
    error.value = e?.message || '加载失败，请稍后重试';
    links.value = [];
  } finally {
    loading.value = false;
    console.log('Fetch completed, loading:', loading.value);
  }
}

function toPage(p: number) {
  const clamped = Math.max(1, Math.min(p, totalPages.value));
  if (clamped === pageNo.value) return;

  pageNo.value = clamped;
  router.replace({
    name: 'GroupLinks',
    params: {
      groupId: groupId.value
    },
    query: {
      pageNo: String(pageNo.value),
      sort: sort.value,
      q: q.value // 保持搜索词
    }
  });
}

function prev() {
  if (pageNo.value > 1) {
    toPage(pageNo.value - 1);
  }
}

function next() {
  if (pageNo.value < totalPages.value) {
    toPage(pageNo.value + 1);
  }
}

// 打开新标签 + 点击计数
async function openAndTrack(link: Link) {
  window.open(link.url, '_blank');
  try {
    await fetch(
        `http://localhost:3000/api/me/links/${link.id}/click`,
        {method: 'POST', headers: {Accept: 'application/json'}, credentials: 'include'}
    );
    // 更新点击计数
    link.clickCount += 1;
    link.lastClickedAt = new Date();
  } catch (e: any) {
    console.error('点击计数失败:', e);
  }
}

// 处理图片加载失败
function handleImageError(event: Event) {
  const img = event.target as HTMLImageElement;
  img.src = 'https://cdn.pixcc.net/large/135316275_p0_1200.webp'; // 备用图片
}

function goto(page: number) {
  toPage(page);
}

function handleLinkCreated() {
  console.log("新链接已创建，正在刷新列表...");
  fetchLinks();
}

function handleLinkUpdated() {
  console.log("链接已更新，正在刷新列表...");
  fetchLinks();
}

function handleLinkDeleted(deletedLinkId: number) {
  console.log(`链接 ${deletedLinkId} 已删除，正在从UI移除...`);
  const index = links.value.findIndex(l => l.id === deletedLinkId);
  if (index >= 0) {
    links.value.splice(index, 1);
  }
}

function openEditLinkModal(link: Link) {
  selectedLink.value = { ...link };
  isEditLinkModalOpen.value = true;
}


// 监听参数变化，重新加载数据
watch([groupId, pageNo, pageSize, sort, q], fetchLinks, {immediate: true});

</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pagination-container {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 12px 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.6);
  z-index: 10;
  margin: 0 auto;
  width: fit-content;
}

.pagination-container:hover {
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.12);
}

.pagination-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  border: none;
  background: #f8f9fa;
  color: #6c757d;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  font-size: 0.85rem;
}

.pagination-btn:hover:not(:disabled) {
  background: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.pagination-btn:active {
  transform: translateY(0);
}

.pagination-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  transform: none;
}

.pagination-page {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  border: none;
  background: transparent;
  color: #6c757d;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.85rem;
}

.pagination-page:hover {
  background: #e9ecef;
}

.pagination-page.active {
  background: linear-gradient(135deg, #3498db, #9b59b6);
  color: white;
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.4);
}

.pagination-dots {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 36px;
  color: #adb5bd;
  font-weight: bold;
}

.pagination-text {
  margin: 0 12px;
  color: #6c757d;
  font-size: 0.85rem;
  white-space: nowrap;
}

.loading-indicator {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(52, 152, 219, 0.3);
  border-radius: 50%;
  border-top-color: #3498db;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

.loading-text {
  display: flex;
  align-items: center;
  color: #3498db;
  font-weight: 500;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .pagination-container {
    flex-wrap: wrap;
    justify-content: center;
    padding: 10px 15px;
    max-width: 95%;
  }

  .pagination-text {
    margin: 8px 0;
    order: 3;
    width: 100%;
    text-align: center;
  }

  .pagination-btn,
  .pagination-page {
    width: 32px;
    height: 32px;
    font-size: 0.8rem;
  }
}

.fab-add-link-side {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(145deg, #5DADE2, #8E44AD); /* 漂亮的渐变色 */
  color: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: all 0.3s ease-in-out;
}

.fab-add-link-side:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

/* 也可以为图标本身设置一下大小，让它更饱满 */
.fab-add-link-side .iconfont {
  font-size: 24px; /* 调整为你认为合适的图标大小 */
}
</style>