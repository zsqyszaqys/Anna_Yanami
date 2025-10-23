<template>
  <div class="oh-my-live2d"></div>
</template>

<script setup lang="ts">
import {loadOml2d} from "oh-my-live2d";
import {ref} from "vue";
import {onMounted} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();
const oml2dRef = ref<HTMLElement | null>();
onMounted(()=>{
  const oml2d = loadOml2d({
    parentElement: oml2dRef.value,
    models: [
      {
        path: '/Furina/Furina.model3.json',
        scale: 0.1,
        name: "芙宁娜",
        position: [20, 50],
        stageStyle:{
          height:300,
        }
      },
    ],
    mobileDisplay:false,
    menus:{
      items:(defaultItems)=>{
        const newItems = [...defaultItems];
        newItems[2]={
          ...newItems[2],
          onClick:()=>{
            oml2d.clearTips();
            oml2d.tipsMessage("更多看板娘在live-2d板块哦", 4000, 10);
          }
        };
        newItems[3]={
          ...newItems[3],
          title: "github",
          icon: "icon-github-fill",
          onClick:()=>{
            onClick: () => {
              window.open("https://github.com/zsqyszaqys/Anna_Yanami", "_blank");
            }
          }
        }
        return newItems;
      }
    }
  })

  oml2d.onStageSlideIn(()=>{
    oml2d.tipsMessage("我是这个网站的看板娘芙宁娜，更多的看板娘在live-2d模块哦", 4000, 10);
  });
})


</script>