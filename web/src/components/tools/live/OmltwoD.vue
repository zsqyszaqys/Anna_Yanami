<template>
  <div>
    <div
        class="oh-my-live2d"
        ref="oml2dRes"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { loadOml2d } from "oh-my-live2d";
import { ref, onMounted } from "vue";

const oml2dRes = ref<HTMLElement>();
const currentScale = ref(0.1);
let oml2dInstance: any = null;

// 初始化 Live2D
const initLive2D = () => {
  if (!oml2dRes.value) return;

  // 清理旧实例
  if (oml2dInstance) {
    oml2dInstance.destroy();
  }

  oml2dInstance = loadOml2d({
    parentElement: oml2dRes.value,
    models: [
      {
        path: '/Furina/Furina.model3.json',
        scale: 0.4,
        name:"芙宁娜",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[320, -30],
      },
      {
        path: '/Elysia/Elysia.model3.json',
        scale: 0.12,
        name:"爱莉希雅",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[400, 25],
      },
      {
        path: '/Nahida/Nahida_1080.model3.json',
        scale: 0.2,
        name:"纳西妲",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[500, -50]
      },
      {
        path: '/Robin/Robin.model3.json',
        scale: currentScale.value,
        name:"知更鸟",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[500, 0],
      },
      {
        path: '/seele/Seele.model3.json',
        scale: 0.23,
        name:"希尔",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[400, 0],
      },
      {
        path: '/Ellen/Ellencomp.model3.json',
        scale: 0.18,
        name:"艾莲",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[300, -100],
      },
      {
        path: '/kafuka/kafuka1.model3.json',
        scale: 0.16,
        name:"卡芙卡",
        stageStyle:{
          width: "100%",
          height: "100%",
        },
        position:[550, -110],
      },
    ],
  });

  oml2dInstance.onModelLoad?.((model: any) => {
    console.log("success!");
  });

  oml2dInstance.onStageSlideIn(() => {
    oml2dInstance.tipsMessage(`舞台已全部滑入`, 3000, 10);
  });

};

onMounted(() => {
  initLive2D();
});

</script>

<style scoped>
</style>

