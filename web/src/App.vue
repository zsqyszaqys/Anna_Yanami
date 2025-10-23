<template>
  <github-corners
    fixed
    target="__blank"
    href="https://github.com/zsqyszaqys/Anna_Yanami"
    :z-index="9999"
    class="global-github-corner"
  />
  <SleekLineCursor />
  <Live2DWidget v-if="!$route.meta.hideGlobalWidget" />
  <router-view />
</template>

<script>
import { defineComponent } from 'vue';
import GithubCorners from '@uivjs/vue-github-corners';
import SleekLineCursor from "@/components/tools/cursor/SleekLineCursor.vue";
import Live2DWidget from "@/components/tools/live/Live2DWidget.vue";
import "@/assets/fonts/iconfont.css";

export default defineComponent({
  components: {
    GithubCorners,
    SleekLineCursor,
    Live2DWidget,
  }
});
</script>

<style>
.global-github-corner {
  z-index: 9999 !important;
  position: fixed !important;
}
</style>