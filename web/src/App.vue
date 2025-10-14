<template>
  <github-corners
      fixed
      target="__blank"
      href="https://github.com/zsqyszaqys/Anna_Yanami"
      :z-index="9999"
      class="global-github-corner"
  />
  <router-view/>
</template>

<script>
import { defineComponent } from 'vue';
import GithubCorners from '@uivjs/vue-github-corners';

export default defineComponent({
  components: {
    GithubCorners
  }
});
</script>

<style>
.global-github-corner {
  z-index: 9999 !important;
  position: fixed !important;
}
</style>