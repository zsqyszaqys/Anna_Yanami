<template>
  <section class="links-wrap">
    <div class="glass">
      <div class="blob blob-a"></div>
      <div class="blob blob-b"></div>

      <LinksToolbar
          :q="q"
          :sort="sort"
          :onlyFav="onlyFav"
          @update:q="v => q = v"
          @update:sort="v => { sort = v; reload(); }"
          @update:onlyFav="v => { onlyFav = v; }"
          @search="reload"
          @create="openCreate"
      />

      <GroupChips
          :groups="groups"
          :activeGroupId="activeGroupId"
          @select="switchGroup"
      />

      <div class="content">
        <div v-if="loading" class="grid">
          <div v-for="i in 8" :key="i" class="card skeleton"></div>
        </div>

        <div v-else-if="filteredRecords.length === 0" class="empty">
          <div class="emoji">🔗</div>
          <div class="txt">暂无链接</div>
          <button class="btn ghost" @click="openCreate">马上添加</button>
        </div>

        <div v-else class="grid">
          <LinkCard
              v-for="item in filteredRecords"
              :key="item.id"
              :item="item"
              @open="openLink"
              @favorite="onFavorite"
              @copy="onCopy"
              @edit="onEdit"
              @move="onMove"
              @delete="onDelete"
          />
        </div>

        <div v-if="page.total > page.size" class="pager">
          <button class="btn ghost" :disabled="pageNo<=1" @click="pageNo--; reload()">上一页</button>
          <span>{{ pageNo }} / {{ Math.ceil(page.total / pageSize) }}</span>
          <button class="btn ghost" :disabled="pageNo>=Math.ceil(page.total/pageSize)" @click="pageNo++; reload()">
            下一页
          </button>
        </div>
      </div>
    </div>

    <CreateLinkModal
        v-model="creating"
        :groups="groups"
        :initial="editInit"
        @submit="onSubmitCreate"
        @update:modelValue="v => { creating = v; if (!v) editInit = null; }"
    />
  </section>
</template>

<script>
import {computed, onMounted, reactive, ref} from 'vue';
import LinksToolbar from '@/components/links/LinksToolbar.vue';
import GroupChips from '@/components/links/GroupChips.vue';
import LinkCard from '@/components/links/LinkCard.vue';
import CreateLinkModal from '@/components/links/CreateLinkModal.vue';
import {
  getGroups, pageLinks, createLink, updateLink,
  deleteLink, moveLink, favoriteLink, clickLink
} from '@/services/meApi';

export default {
  name: 'LinksSection',
  components: {LinksToolbar, GroupChips, LinkCard, CreateLinkModal},
  setup() {
    const groups = ref([]);
    const activeGroupId = ref(null);

    const q = ref('');
    const sort = ref('default');
    const onlyFav = ref(false);

    const page = reactive({records: [], total: 0, size: 24, current: 1});
    const pageNo = ref(1);
    const pageSize = ref(24);
    const loading = ref(false);

    const creating = ref(false);
    const editInit = ref(null);

    const filteredRecords = computed(() => onlyFav.value ? page.records.filter(r => r.isFavorite) : page.records);

    async function loadGroups() {
      const data = await getGroups();
      groups.value = data || [];
      if (!activeGroupId.value && groups.value.length) activeGroupId.value = groups.value[0].id;
    }

    async function loadLinks() {
      if (!activeGroupId.value) return;
      loading.value = true;
      try {
        const data = await pageLinks(activeGroupId.value, q.value, pageNo.value, pageSize.value, sort.value);
        Object.assign(page, data || {records: [], total: 0, size: pageSize.value, current: pageNo.value});
      } finally {
        loading.value = false;
      }
    }

    function reload() {
      pageNo.value = 1;
      loadLinks();
    }

    function switchGroup(id) {
      if (activeGroupId.value === id) return;
      activeGroupId.value = id;
      reload();
    }

    function openCreate() {
      creating.value = true;
      editInit.value = {id: undefined, groupId: activeGroupId.value, title: '', url: '', description: ''};
    }

    async function onSubmitCreate(payload) {
      if (payload.id) {
        await updateLink(payload.id, payload);
      } else {
        await createLink(payload);
      }
      creating.value = false;
      editInit.value = null;
      reload();
    }

    async function onFavorite(item) {
      await favoriteLink(item.id, !item.isFavorite);
      item.isFavorite = !item.isFavorite;
    }

    async function onCopy(item) {
      try {
        await navigator.clipboard.writeText(item.url);
      } catch (e) {
        // ignore clipboard errors
      }
    }

    function onEdit(item) {
      creating.value = true;
      editInit.value = {...item};
    }

    async function onMove(item) {
      const gid = prompt('移动到分组ID：', String(activeGroupId.value || ''));
      if (!gid) return;
      await moveLink(item.id, Number(gid));
      reload();
    }

    async function onDelete(item) {
      if (!confirm('确认删除该链接？')) return;
      await deleteLink(item.id);
      reload();
    }

    async function openLink(item) {
      window.open(item.url, '_blank');
      item.clickCount = (item.clickCount || 0) + 1;
      clickLink(item.id).catch(() => {
        // 这里也是空块
      });
    }

    onMounted(async () => {
      await loadGroups();
      await loadLinks();
    });

    return {
      groups, activeGroupId, q, sort, onlyFav,
      page, pageNo, pageSize, loading,
      creating, editInit, filteredRecords,
      reload, switchGroup, openCreate, onSubmitCreate,
      onFavorite, onCopy, onEdit, onMove, onDelete, openLink
    };
  }
}
</script>

<style scoped>
.links-wrap {
  padding: 24px;
}

.glass {
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, .18);
  background: rgba(255, 255, 255, .6);
  backdrop-filter: blur(8px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, .06);
}

.blob {
  position: absolute;
  pointer-events: none;
  filter: blur(40px);
  opacity: .6;
}

.blob-a {
  width: 360px;
  height: 360px;
  left: -120px;
  top: -120px;
  background: radial-gradient(circle, #f59e0b33, #0000 60%);
}

.blob-b {
  width: 420px;
  height: 420px;
  right: -160px;
  bottom: -160px;
  background: radial-gradient(circle, #22c55e33, #0000 60%);
}

.content {
  padding: 12px 16px 18px 16px;
}

.grid {
  display: grid;
  gap: 12px;
  grid-template-columns: repeat(1, minmax(0, 1fr));
}

@media (min-width: 720px) {
  .grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (min-width: 1080px) {
  .grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (min-width: 1360px) {
  .grid {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}

.card.skeleton {
  background: linear-gradient(100deg, #f3f4f6 40%, #e5e7eb 50%, #f3f4f6 60%);
  background-size: 200% 100%;
  animation: shimmer 1s infinite;
  height: 84px;
  border-radius: 14px;
}

@keyframes shimmer {
  to {
    background-position-x: -200%;
  }
}

.empty {
  text-align: center;
  padding: 36px 0;
  color: #6b7280;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.empty .emoji {
  font-size: 40px;
}

.empty .txt {
  font-size: 14px;
}

.pager {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.btn {
  padding: 8px 12px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  background: #fff;
  cursor: pointer;
}

.btn.ghost {
  background: #fff;
}
</style>