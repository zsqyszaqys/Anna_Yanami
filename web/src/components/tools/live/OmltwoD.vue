<template>
  <div class="relative w-full h-screen overflow-hidden custom-background">
<!--  返回主页-->
    <div class="absolute top-4 left-4 z-20">
      <GradientButton @click="handleGradientButtonClick">
        <i class="iconfont icon-fanhui_fanhui mr-2" />
        返回主页
      </GradientButton>
    </div>

    <!-- Live2D 舞台：置底 -->
    <div
      ref="oml2dRes"
      class="absolute inset-0"
    />

    <!-- Slider 组件 -->
    <div class="absolute left-8 top-20 z-10 w-[560px] max-w-[45vw]">
      <TestimonialSlider
        class="slider"
        :testimonials="testimonials"
        @model-change="loadModelByIndex"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {loadOml2d} from "oh-my-live2d";
import {ref, onMounted} from "vue";
import '@/assets/fonts/iconfont';
import TestimonialSlider from "@/components/tools/Slider/TestimonialSlider.vue";
import GradientButton from "@/components/tools/Button/GradientButton.vue";
import {useRouter} from "vue-router";


const router = useRouter();
const oml2dRes = ref<HTMLElement>();
const currentScale = ref(0.1);
let oml2dInstance: any = null;

const testimonials = [
  {
    img: '/Gallery/Furina.jpg',
    quote: '欢迎来到水的国度，我芙卡洛斯将承认你们旅途的价值与意义，现在，你们可以尽情欢呼了。',
    name: '芙宁娜',
    id: 0,
  },
  {
    img: '/Gallery/Elysia.png',
    quote: '嗨，我又来啦。多夸夸我好吗？我会很开心的～♪',
    name: '爱里希雅',
    id: 1,
  },
  {
    img: '/Gallery/Nahida.jpg',
    quote: '我叫纳西妲，别看我像个孩子，我比任何一位大人都了解这个世界。所以，我可以用我的知识，换取你路上的见闻吗？',
    name: '纳西妲',
    id: 2,
  },
  {
    img: '/Gallery/Robin.png',
    quote: '又见面了，这回想听什么歌？',
    name: '知更鸟',
    id: 3,
  },
  {
    img: '/Gallery/Seele.png',
    quote: '在量子之海中，时间与空间都失去了意义。但此刻，我只想守护在你身边。',
    name: '希尔',
    id: 4,
  },
  {
    img: '/Gallery/Ellen.png',
    quote: '为了主人的愿望！打工的加班费，要双倍才行！',
    name: '艾莲',
    id: 5,
  },
  {
    img: '/Gallery/Kafuka.jpg',
    quote: '命运就像蜘蛛丝，看似脆弱却连接着一切。那么，你准备好接受命运的安排了么？',
    name: '卡芙卡',
    id: 6,
  },
]

const handleGradientButtonClick = () => {
  if (oml2dInstance) {
    oml2dInstance.tipsMessage('你要离开我吗?再陪我一秒好不好', 3000, 10);
  }
  setTimeout(()=>{
    router.back()
  }, 1000);
}

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
        name: "芙宁娜",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [520, -30],
      },
      {
        path: '/Elysia/Elysia.model3.json',
        scale: 0.12,
        name: "爱莉希雅",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [600, 25],
      },
      {
        path: '/Nahida/Nahida_1080.model3.json',
        scale: 0.2,
        name: "纳西妲",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [700, -50]
      },
      {
        path: '/Robin/Robin.model3.json',
        scale: currentScale.value,
        name: "知更鸟",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [700, 0],
      },
      {
        path: '/seele/Seele.model3.json',
        scale: 0.23,
        name: "希尔",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [700, 0],
      },
      {
        path: '/Ellen/Ellencomp.model3.json',
        scale: 0.18,
        name: "艾莲",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [500, -100],
      },
      {
        path: '/kafuka/kafuka1.model3.json',
        scale: 0.16,
        name: "卡芙卡",
        stageStyle: {
          width: "100%",
          height: "100%",
        },
        position: [750, -50],
      },
    ],
    mobileDisplay: false,
    menus: {
      items: (defaultItems) => {
        const newItems = [...defaultItems];

        newItems[1] = {
          ...newItems[1],
          title: "上一个模型",
          icon: "icon-shangyige",
          onClick: (oml2dInstance) => {
            let idx = oml2dInstance.modelIndex;
            let tot = oml2dInstance.options.models.length;

            oml2dInstance.loadModelByIndex((idx + tot - 1) % tot);
          },
        };
        newItems[2] = {
          ...newItems[2],
          title: "下一个模型",
          icon: "icon-next",
        };
        newItems[3] = {
          ...newItems[3],
          title: "github",
          icon: "icon-github-fill",
          onClick: () => {
            window.open("https://github.com/zsqyszaqys/Anna_Yanami", "_blank");
          }
        };

        newItems.push({
          id: "随机模型",
          title: "随机切换模型",
          icon: "icon-switch",
          onClick: (oml2dInstance) => {
            oml2dInstance.loadRandomModel();
          },
        })

        return newItems;
      },
      style:{
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        backdropFilter: 'blur(12px)',
        border: '1px solid rgba(255, 255, 255, 0.2)',
        borderRadius: '16px',
        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.1)',
      },
    },
    statusBar:{
      style:{
        zIndex:100,
      },
    },
    tips:(_, modelIndex)=>{
      const characterMessages = {
        0: [ // 芙宁娜
          "欢迎来到水的国度~",
          "今天也要尽情享受戏剧哦~",
          "我的表演可是很精彩的！",
          "水面之下，藏着无数秘密呢~"
        ],
        1: [ // 爱莉希雅
          "嗨，想我了吗？♪",
          "今天的你也很可爱呢~",
          "要听听我新学的歌吗？",
          "快乐的时间总是过得特别快呢~"
        ],
        2: [ // 纳西妲
          "知识就是最好的礼物~",
          "想要了解这个世界吗？",
          "每个生命都值得被珍视~",
          "智慧之树正在生长呢~"
        ],
        3: [ // 知更鸟
          "音乐是心灵的翅膀~",
          "想听什么歌呢？",
          "旋律中藏着无数故事~",
          "歌声能够治愈一切~"
        ],
        4: [ // 希尔
          "量子之海在呼唤~",
          "时间与空间的界限~",
          "此刻即是永恒~",
          "在虚数中寻找真实~"
        ],
        5: [ // 艾莲
          "冰雪之下是炽热的心~",
          "寒冷无法冻结意志~",
          "北风带来了远方消息~",
          "在冰原上起舞吧~"
        ],
        6: [ // 卡芙卡
          "命运之线交织缠绕~",
          "每个选择都有意义~",
          "在蛛网上漫步~",
          "未来早已注定~"
        ]
      };

      return{
        style: {
          zIndex:100,
          pointerEvents: 'none',
          // 背景和边框
          backgroundColor: "rgba(255, 255, 255, 0.4)",
          backdropFilter: "blur(20px)",
          border: "2px solid rgba(255, 255, 255, 0.5)",
          borderRadius: "20px",
          boxShadow: `
            0 25px 50px rgba(0, 0, 0, 0.15),
            0 10px 25px rgba(0, 0, 0, 0.1),
            inset 0 1px 0 rgba(255, 255, 255, 0.8)
          `,

          // 文字样式
          color: "#374151",
          fontSize: "15px",
          fontWeight: "500",
          lineHeight: "1.5",
          fontFamily: "'Segoe UI', system-ui, sans-serif",

          // 内边距和尺寸
          padding: "16px 20px",
          maxWidth: "320px",
          minHeight: "60px",

          // 动画过渡
          transition: "all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1)",

          // 文字阴影
          textShadow: "0 1px 2px rgba(255, 255, 255, 0.8)",
        },
        copyTips:{
          message: [
            "复制了什么有趣的内容呢？🎉",
            "记得要注明出处哦~ 📝",
            "知识需要分享，但也要尊重原创~ 💫",
            "复制成功！想要保存什么秘密呢？✨"
          ],
          priority: 10,
        },
        idleTips:{
          message:characterMessages[modelIndex],
          interval:10000,
          wordTheDay:true,
          priority:2,
        },
        messageLine:4,
      }
    }
  });

  oml2dInstance.onStageSlideIn(() => {
    oml2dInstance.showModelHitAreaFrames();
  });
};

const loadModelByIndex = (index: number) => {
  if (!oml2dInstance) return;
  const total = oml2dInstance.options?.models?.length ?? 0;
  if (index < 0 || index >= total) return;

  if (oml2dInstance.modelIndex === index) {
    oml2dInstance.clearTips();
    oml2dInstance.tipsMessage('为什么要重复选择我呢~,看来你真的很喜欢我呢~', 3000, 10);
    return;
  }

  // 一次性回调
  const once = () => {
    oml2dInstance.offStageSlideIn?.(once); // 如果版本不支持 off，可忽略
    oml2dInstance.clearTips?.();
    oml2dInstance.tipsMessage?.('你是想我了吗~', 3000, 10);
  };
  oml2dInstance.onStageSlideIn?.(once);

  // 立刻切换
  oml2dInstance.loadModelByIndex(index);
}

onMounted(() => {
  initLive2D();
});

</script>

<style scoped>
.custom-background {
  background-image: url('@/assets/AboutMe/background.png'); /* 替换为你的图片路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed; /* 可选：固定背景 */

  /* 添加暗色遮罩，确保前方内容清晰可见 */
  position: relative;
}

.custom-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* 暗色遮罩，可根据需要调整透明度 */
  z-index: 0;
}

/* 确保其他元素在遮罩之上 */
.absolute.inset-0,
.absolute.left-8 {
  z-index: 1;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .custom-background {
    background-attachment: scroll; /* 移动端取消固定背景 */
  }
}
.slider{
  opacity: 0.6;
}

</style>