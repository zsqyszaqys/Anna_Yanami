<template>
  <!--  新增分组按钮-->
  <div class="fixed bottom-10 right-10 z-20">
    <button
      class="add-group-button"
      @click="isAddModalOpen = true"
    >
      <div class="button-content">
        <svg
          class="add-icon"
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
        <div class="button-glow" />
        <div class="pulse-ring" />
      </div>
      <div class="tooltip">
        新建分组
      </div>
    </button>
  </div>

  <!--  分组-->
  <BentoGrid class="grid w-full auto-rows-[18rem] grid-cols-4 gap-4 lg:grid-rows-4">
    <BentoGridCard
      v-for="(feature, index) in features"
      :key="index"
      v-bind="feature"
      :class="feature.class"
      @click="openGroup(feature, $event)"
      @settings-click="openSettingsModal(feature.groupId)"
    >
      <template
        v-if="feature.image"
        #background
      >
        <div class="absolute inset-0 overflow-hidden">
          <div
            class="w-full h-full transition-all duration-300 ease-in-out transform opacity-90 group-hover:opacity-20 group-hover:scale-105"
            :style="{
              backgroundImage: `url('${feature.image}')`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat'
            }"
          />
        </div>
      </template>
    </BentoGridCard>
  </BentoGrid>

  <!--  分页按钮-->
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
      <i class="fas fa-angle-double-left" />
    </button>

    <!-- 上一页按钮 -->
    <button
      class="pagination-btn"
      :disabled="page === 1"
      title="上一页"
      @click="prev"
    >
      <i class="fas fa-angle-left" />
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
      <i class="fas fa-angle-right" />
    </button>

    <!-- 末页按钮 -->
    <button
      class="pagination-btn"
      :disabled="page === totalPages"
      title="末页"
      @click="goto(totalPages)"
    >
      <i class="fas fa-angle-double-right" />
    </button>

    <!-- 页码信息 -->
    <div class="pagination-text">
      <span
        v-if="loading"
        class="loading-text"
      >
        <div class="loading-indicator" />
        加载中...
      </span>
      <span v-else>
        第 {{ page }} / {{ totalPages }} 页（共 {{ total }} 个分组）
      </span>
    </div>
  </nav>

  <!--  设置模态框-->
  <GroupSettingsModal
    v-model="isSettingModalOpen"
    :group="selectedGroup"
    @group-updated="handleGroupUpdated"
    @group-deleted="handleGroupDeleted"
  />

  <!--    新增模态框-->
  <GroupAddModal
    v-model="isAddModalOpen"
    @group-created="handleGroupCreated"
  />
</template>

<script lang="ts" setup>
import BentoGridCard from "@/components/tools/Groups/BentoGridCard.vue";
import {ref, computed, onMounted, watch} from "vue";
import router from "@/router";
import GroupSettingsModal from "@/components/tools/Groups/GroupSettingsModal.vue";
import GroupAddModal from "@/components/tools/Groups/GroupAddModal.vue";

type Group = {
  id: number;
  name: string;
  slug?: string | null;
  description?: string | null;
  color?: string | null;
  icon?: any;
  orderIndex: number;
  isPinned: boolean;
  linkCount?: number | null;
};

type Feature = {
  groupId: number;
  name: string;
  description: string;
  href: string;
  cta: string;
  class: string;
  image?: string; // 可选：没有就不显示背景
};

// 页码列表（含省略号）
type PagerItem = { type: 'page' | 'dots'; page?: number; key: string };

const loading = ref(true);
const error = ref<string | null>(null);
const groups = ref<Group[]>([]);
const features = ref<Feature[]>([]);

const isSettingModalOpen = ref(false);  // 控制模态框是否显示
const isAddModalOpen = ref(false);
const selectedGroup = ref<Group | null>(null); // 存储当前正在编辑的那个 group 对象


// 分页
const page = ref(1);
const pageSize = ref(10); // 固定每页显示10条
const total = computed(() => groups.value.length);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)));

watch(totalPages, (tp) => {
  if (page.value > tp) page.value = tp;
});

// 你可以自定义的"布局模式"和"图片池"
const CLASS_PATTERN = [
  "xl:col-span-2 xl:row-span-2",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-2 xl:row-span-1",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-1 xl:row-span-2",
  "xl:col-span-1 xl:row-span-1",
  "xl:col-span-2 xl:row-span-1",
];

const IMAGE_POOL = [
  "https://cc-img.townimg.com/uploads/2025/06/20250629224504270.webp",//鲨鱼妹
  "https://cc-img.townimg.com/uploads/2025/09/20250918111851185.webp",//乃乃
  "https://cc-img.townimg.com/uploads/2025/09/20250928223258828.webp",//长夜月
  "https://cc-img.townimg.com/uploads/2025/09/20250928223251542.webp",//哥伦比亚
  "https://cc-img.townimg.com/uploads/2025/09/20250901154149139.webp",
  "https://cc-img.townimg.com/uploads/2025/09/20250901154217704.webp",//乃乃
  "https://cc-img.townimg.com/uploads/2025/05/20250511192718645.webp",
  "https://cc-img.townimg.com/uploads/2025/06/20250613161609412.webp",//流萤
  "https://cc-img.townimg.com/uploads/2025/06/20250629224428999.webp",//
  "https://cc-img.townimg.com/uploads/2025/05/20250511192714574.webp",//银狼
];

const IMAGE_BY_SLUG: Record<string, string> = {
  search: "https://images.pexels.com/photos-2376754/pexels-photo-2376754.jpeg?auto=compress&cs=tinysrgb&w=1200",
  calendar: "https://images.pexels.com/photos-7076761/pexels-photo-7076761.jpeg?auto=compress&cs=tinysrgb&w=1200",
  team: "https://images.pexels.com/photos-3183150/pexels-photo-3183150.jpeg?auto=compress&cs=tinysrgb&w=1200",
};

const canPrev = computed(() => page.value > 0);
const canNext = computed(() => page.value < totalPages.value);

function handleGroupCreated(){
  console.log("新分组已创建，正在重新拉取列表...");
  fetchGroups();
}



function goto(p: number) {
  const clamped = Math.min(Math.max(1, p), totalPages.value);
  if (clamped !== page.value) {
    page.value = clamped;
  }
}

function prev() {
  if (canPrev.value) {
    page.value--;
  }
}

function next() {
  if (canNext.value) {
    page.value++;
  }
}

const pageList = computed<PagerItem[]>(() => {
  const tp = totalPages.value;
  const cur = page.value;
  const out: PagerItem[] = [];
  // 辅助函数：添加页码按钮
  const addPage = (p: number) => out.push({type: 'page', page: p, key: `p${p}`});

  // 辅助函数：添加省略号
  const addDots = (k: string) => out.push({type: 'dots', key: `d${k}`});

  if (tp <= 7) {
    for (let i = 1; i <= tp; i++) addPage(i);
  } else {
    addPage(1);
    if (cur > 4) addDots('l');
    const start = Math.max(2, cur - 1);
    const end = Math.min(tp - 1, cur + 1);
    for (let p = start; p <= end; p++) addPage(p);
    if (cur < tp - 3) addDots('r');
    addPage(tp);
  }
  return out;
});

// 打开模态框
function openSettingsModal(groupId: number) {
  const groupToEdit = groups.value.find(g => g.id === groupId);

  if (groupToEdit) {
    selectedGroup.value = {
      id: groupToEdit.id,
      name: groupToEdit.name,
      description: groupToEdit.description,
      slug: groupToEdit.slug,
      color: groupToEdit.color,
      icon: groupToEdit.icon,
      orderIndex: 1,
      isPinned: false,
    };
    isSettingModalOpen.value = true;
  } else {
    console.error("无法找到要编辑的分组，ID:", groupId);
    alert("出错了，无法找到要编辑的分组。");
  }
}

// 当模态框通知“更新成功”时
function handleGroupUpdated() {
  console.log("分组已更新，正在重新拉取列表...");
  fetchGroups();
}

// 当模态框通知“删除成功”时
function handleGroupDeleted(deletedGroupId: number) {
  console.log(`分组 ${deletedGroupId} 已删除，正在从UI移除...`);
  const index = groups.value.findIndex(g => g.id === deletedGroupId);

  if (index > -1) groups.value.splice(index, 1);

  // 注意：因为 `features` 依赖于 `groups`，当你修改 `groups` 时，
  // `watch([groups, ...], getFeatures)` 会自动触发，重新生成 `features`，UI 也会随之更新。
}

// 决定每个卡片的 class（逐项）
function pickClass(g: Group, globalIndex: number): string {
  return CLASS_PATTERN[globalIndex % CLASS_PATTERN.length];
}

// 决定每个卡片的 image（逐项）
function pickImage(g: Group, globalIndex: number): string {
  const fallback = IMAGE_POOL[globalIndex % IMAGE_POOL.length];
  return fallback;
}

// 把 Group 逐个转成 Feature，并"一个一个"赋值 push
function getFeatures() {
  features.value.length = 0; // 清空

  const start = (page.value - 1) * pageSize.value;
  const pageGroups = groups.value.slice(start, start + pageSize.value);

  pageGroups.forEach((g, i) => {
    const globalIndex = start + i; // 全局序号，保证翻页后仍有规律
    const f: Feature = {
      groupId: g.id,
      name: g.name,
      description: g.description ?? `共 ${g.linkCount ?? 0} 个链接`,
      href: `/groups/${g.id}`,
      cta: "查看组内链接",
      class: pickClass(g, globalIndex),
      image: pickImage(g, globalIndex),
    };
    features.value.push(f);
  });
}

// 拉取分组并初始化
async function fetchGroups() {
  loading.value = true;
  error.value = null;
  try {
    const res = await fetch("http://localhost:3000/api/me/groups", {
      method: "GET",
      headers: {Accept: "application/json"},
      credentials: "include",
    });
    const json = await res.json();
    if (!res.ok || json?.status !== true || json?.code !== 200) {
      throw new Error(json?.message || `加载失败(${res.status})`);
    }
    groups.value = Array.isArray(json.data) ? json.data : [];
    page.value = 1;
    getFeatures(); // 逐个赋值
  } catch (e: any) {
    groups.value = [];
    features.value = [];
  } finally {
    loading.value = false;
  }
}

//打开group,按住ctrl可以再新标签页打开
function openGroup(f: Feature, e?: MouseEvent) {
  let groupId = f.groupId;
  if (typeof groupId === 'string') {
    if (groupId === '{groupid}' || groupId === '%7Bgroupid%7D') {
      return;
    }
    // 尝试转换字符串为数字
    groupId = Number(groupId);
  }

  const newTab = !!(e && (e.ctrlKey || e.metaKey));
  const to = {name: 'GroupLinks', params: {groupId: f.groupId}, query: {pageNo: '1'}};

  if (newTab) {
    const url = router.resolve(to).href;
    window.open(url, '_blank');
  } else {
    router.push(to);
  }
}

// 翻页或数据变化时，重新逐个赋值
watch([groups, page, pageSize], () => getFeatures());

onMounted(fetchGroups);
</script>

<style scoped>
.pagination-container {
  position: absolute;
  bottom: 20px;
  left: 50%; /* 左边界在 50% */
  transform: translateX(-50%); /* 水平居中 */
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 12px 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.6);
  z-index: 10; /* 保证浮在最上层 */
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

/* Groups.vue */
.add-group-button {
  /* 基本样式 */
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  cursor: pointer;

  /* 尺寸和形状 */
  width: 64px;
  height: 64px;
  border-radius: 50%;

  /* 阴影和过渡 */
  box-shadow:
      0 8px 25px rgba(102, 126, 234, 0.4),
      0 0 0 1px rgba(255, 255, 255, 0.1),
      inset 0 1px 0 rgba(255, 255, 255, 0.2);
  transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);

  /* 定位和层级 */
  position: relative;
  overflow: hidden;
}

.button-content {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  z-index: 2;
}

.add-icon {
  width: 28px;
  height: 28px;
  color: white;
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
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, transparent 70%);
  transform: translate(-50%, -50%);
  transition: all 0.5s cubic-bezier(0.16, 1, 0.3, 1);
  opacity: 0;
  z-index: 1;
}

.pulse-ring {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.4);
  animation: pulse 3s infinite;
  opacity: 0;
  z-index: 0;
}

.tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  margin-bottom: 12px;
  padding: 8px 16px;
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: white;
  font-size: 0.875rem;
  font-weight: 500;
  white-space: nowrap;
  opacity: 0;
  transform: translateX(-50%) translateY(10px);
  transition: all 0.3s ease;
  pointer-events: none;
  z-index: 30;
}

.tooltip::before {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid rgba(0, 0, 0, 0.8);
  transform: translateX(-50%);
}

/* 悬停效果 */
.add-group-button:hover {
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
  box-shadow:
      0 12px 35px rgba(102, 126, 234, 0.6),
      0 0 0 1px rgba(255, 255, 255, 0.2),
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
  transform: translateY(-4px) scale(1.05);
}

.add-group-button:hover .add-icon {
  transform: scale(1.1) rotate(90deg);
}

.add-group-button:hover .button-glow {
  width: 80px;
  height: 80px;
  opacity: 1;
}

.add-group-button:hover .tooltip {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
}

/* 点击效果 */
.add-group-button:active {
  transform: translateY(-2px) scale(1.02);
  transition: transform 0.1s ease;
}

.add-group-button:active .add-icon {
  transform: scale(0.95) rotate(45deg);
}

/* 焦点状态（无障碍支持） */
.add-group-button:focus {
  outline: none;
  box-shadow:
      0 0 0 3px rgba(102, 126, 234, 0.4),
      0 12px 35px rgba(102, 126, 234, 0.6);
}

.add-group-button:focus:not(:hover) {
  transform: scale(1.05);
}

/* 脉动动画 */
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.5;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

/* 可选：添加微妙的浮动动画 */
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

.add-group-button {
  animation: float 6s ease-in-out infinite;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .add-group-button {
    width: 56px;
    height: 56px;
    bottom: 20px;
    right: 20px;
  }

  .add-icon {
    width: 24px;
    height: 24px;
  }

  .tooltip {
    font-size: 0.75rem;
    padding: 6px 12px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .add-group-button {
    box-shadow:
        0 8px 25px rgba(102, 126, 234, 0.5),
        0 0 0 1px rgba(255, 255, 255, 0.05),
        inset 0 1px 0 rgba(255, 255, 255, 0.1);
  }

  .add-group-button:hover {
    box-shadow:
        0 12px 35px rgba(102, 126, 234, 0.7),
        0 0 0 1px rgba(255, 255, 255, 0.1),
        inset 0 1px 0 rgba(255, 255, 255, 0.2);
  }
}

/* 加载状态（如果需要的话） */
.add-group-button.loading {
  pointer-events: none;
  opacity: 0.7;
}

.add-group-button.loading .add-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>