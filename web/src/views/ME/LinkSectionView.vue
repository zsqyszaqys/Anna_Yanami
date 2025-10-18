<template>
  <BentoGrid class="grid w-full auto-rows-[18rem] grid-cols-4 gap-4 lg:grid-rows-4">
    <BentoGridCard
        v-for="(feature, index) in features"
        :key="index"
        v-bind="feature"
        :class="feature.class"
    >
      <template
          v-if="feature.image"
          #background
      >
        <div
            class="absolute right-0 top-0 size-full bg-center opacity-40 transition duration-150 ease-in-out group-hover:opacity-20"
            :style="`background-image: url('${feature.image}')`"
        ></div>
      </template>
    </BentoGridCard>
  </BentoGrid>
</template>
<script lang="ts" setup>
import BentoGridCard from "@/components/tools/BentoGridCard.vue";
import { ref, computed, onMounted, watch } from "vue";

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
  name: string;
  description: string;
  href: string;
  cta: string;
  class: string;
  image?: string; // 可选：没有就不显示背景
};

const loading = ref(true);
const error = ref<string | null>(null);
const groups = ref<Group[]>([]);
const features = ref<Feature[]>([]);

// 分页
const page = ref(1);
const pageSize = ref(10);
const total = computed(() => groups.value.length);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)));
watch(totalPages, (tp) => { if (page.value > tp) page.value = tp; });

// 你可以自定义的“布局模式”和“图片池”
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
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
    "https://i0.hdslb.com/bfs/new_dyn/c2bcdf324b5268db318ec8cf41c39885520049990.jpg@480w_270h_1s.avif",
];

const IMAGE_BY_SLUG: Record<string, string> = {
  search: "https://images.pexels.com/photos/2376754/pexels-photo-2376754.jpeg?auto=compress&cs=tinysrgb&w=1200",
  calendar: "https://images.pexels.com/photos/7076761/pexels-photo-7076761.jpeg?auto=compress&cs=tinysrgb&w=1200",
  team: "https://images.pexels.com/photos/3183150/pexels-photo-3183150.jpeg?auto=compress&cs=tinysrgb&w=1200",
  // 需要的话继续按 slug 配置
};

// 决定每个卡片的 class（逐项）
function pickClass(g: Group, globalIndex: number): string {
  return CLASS_PATTERN[globalIndex % CLASS_PATTERN.length];
}

// 决定每个卡片的 image（逐项）
function pickImage(g: Group, globalIndex: number): string {
  if (g.slug && IMAGE_BY_SLUG[g.slug]) return IMAGE_BY_SLUG[g.slug];
  // 若你在 Group 上有封面字段（比如 coverUrl），也可以优先用
  // if ((g as any).coverUrl) return (g as any).coverUrl as string;

  // 兜底：从图片池按序取；还可以用 picsum seed 保证稳定
  const fallback = IMAGE_POOL[globalIndex % IMAGE_POOL.length];
  const seeded = `https://picsum.photos/seed/group-${g.id}/1200/800`;
  return fallback || seeded;
}

// 把 Group 逐个转成 Feature，并“一个一个”赋值 push
function getFeatures() {
  features.value.length = 0; // 清空

  const start = (page.value - 1) * pageSize.value;
  const pageGroups = groups.value.slice(start, start + pageSize.value);

  pageGroups.forEach((g, i) => {
    const globalIndex = start + i; // 全局序号，保证翻页后仍有规律
    const f: Feature = {
      name: g.name,
      description: g.description ?? `共 ${g.linkCount ?? 0} 个链接`,
      href: `/`,
      cta: "查看分组",
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
      headers: { Accept: "application/json" },
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
    error.value = e?.message || "加载失败，请稍后重试";
    groups.value = [];
    features.value = [];
  } finally {
    loading.value = false;
  }
}

// 翻页或数据变化时，重新逐个赋值
watch([groups, page, pageSize], () => getFeatures());

onMounted(fetchGroups);
</script>