<template>
  <div
    class="text-overlay"
    @click="createText($event)"
  >
    <span
      v-for="item in texts"
      :key="item.id"
      :style="{
        left: item.x + 'px',
        top: item.y + 'px',
        opacity: item.opacity,
        transform: item.transform,
        '--hue': item.hue
      }"
      class="floating-text"
    >
      {{ item.content }}
    </span>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  name: "RandomText",
  setup() {
    const texts = ref([]);

    // 随机文字数组
    const arr = [
      "最是人间留不住，朱颜辞镜花辞树",
      "君住长江头，我住长江尾，日日思君不见君，共饮长江水",
      "少言自寡，胜过千言万语",
      "千万不要在奋斗的年纪选择安逸",
      "除了你自己，没人会时刻在意你",
      "命运的每一个弯曲，都是你走向成功的一个转折",
      "年少的期待，都会在日后被一一兑现，哪怕它们换了形态，且姗姗来迟",
      "当你不能拥有的时候，唯一能做的便是不能忘记",
      "如果命运是条孤独的河流，那么你就是你的灵魂摆渡人",
      "燃烧，使你获得最终的宁静",
      "生命的价值，在于始终坚持一个目标",
      "没办法，我喜欢她，我对得起自己的喜欢",
      "不求苍天俯就我的美意，但求永远恣意挥洒",
      "每个人心底都有一座坟墓，是用来埋葬所爱的人",
      "黑夜无论多么漫长，白昼总会到来",
      "无论谁，领先一步，都是暂时的",
      "这是黄昏的太阳，我却当做是黎明的曙光",
      "人生处处有诱惑，贪欲者自上钩",
      "认知自己的无知是最大的智慧",
      "一路曲折，换来的是生命的成长",
      "既然已经伤害了过去，就不要再辜负将来",
      "人类最大的勇气就是豁出去的心",
      "人成熟的标志在于，该动脑时，不在动情",
      "对美好的追求，对残缺的接纳",
      "青春时光转瞬即逝",
      "生活不是一种刁难，而是一种雕刻",
      "永远不要在别人面前调侃你的理想，你为他付出的是生命",
      "凡是都有偶然的凑巧，结果却如宿命般的必然",
      "总之岁月漫长，所有值得等待"
    ];

    const createText = (e) => {
      // 检查点击的是否是音量控制区域
      const volumeControl = e.target.closest('.volume-control');
      if (volumeControl) {
        return; // 如果是音量控制区域，不创建文字
      }

      const id = Date.now() + Math.random();
      const newText = {
        id,
        x: e.clientX,
        y: e.clientY,
        content: arr[Math.floor(Math.random() * arr.length)],
        opacity: 0,
        transform: "translateY(0px) scale(0.8) rotate(0deg)",
        hue: Math.floor(Math.random() * 360) // 随机色相
      };
      texts.value.push(newText);

      // 动画：上升 + 渐隐 + 旋转
      setTimeout(() => {
        const t = texts.value.find(item => item.id === id);
        if (t) {
          t.opacity = 1;
          t.transform = "translateY(-120px) scale(1) rotate(2deg)";
        }
      }, 50);

      setTimeout(() => {
        const t = texts.value.find(item => item.id === id);
        if (t) {
          t.opacity = 0.3;
          t.transform = "translateY(-250px) scale(1.1) rotate(-1deg)";
        }
      }, 1800);

      // 移除元素
      setTimeout(() => {
        texts.value = texts.value.filter(item => item.id !== id);
      }, 3000);
    };

    return {
      texts,
      createText
    };
  }
};
</script>

<style scoped>
.text-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  overflow: hidden;
  z-index: 100;
  cursor: pointer;
  pointer-events: auto;
}

.floating-text {
  position: absolute;
  font-size: 18px;
  font-weight: 600;
  font-family:
      'SF Pro Display',
      'Segoe UI',
      -apple-system,
      BlinkMacSystemFont,
      sans-serif;
  user-select: none;
  cursor: default;
  transition: all 1.2s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  pointer-events: none;
  white-space: nowrap;
  text-align: center;

  /* 现代化文字效果 */
  background: linear-gradient(
      135deg,
      hsl(var(--hue, 45), 85%, 60%) 0%,
      hsl(var(--hue, 45), 95%, 75%) 50%,
      hsl(var(--hue, 45), 85%, 60%) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  background-size: 200% auto;

  /* 多重阴影增强立体感 */
  text-shadow:
      0 2px 4px rgba(0, 0, 0, 0.3),
      0 4px 12px rgba(0, 0, 0, 0.2),
      0 0 20px rgba(255, 255, 255, 0.1);

  /* 边框效果 */
  padding: 12px 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.05);

  /* 悬停微效果 */
  animation: floatHue 4s ease-in-out infinite;
}

/* 色相浮动动画 */
@keyframes floatHue {
  0%, 100% {
    filter: hue-rotate(0deg) brightness(1);
  }
  50% {
    filter: hue-rotate(10deg) brightness(1.1);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .floating-text {
    font-size: 16px;
    padding: 10px 16px;
    border-radius: 12px;
  }
}

@media (max-width: 480px) {
  .floating-text {
    font-size: 14px;
    padding: 8px 12px;
    border-radius: 10px;
    max-width: 200px;
    white-space: normal;
    text-align: center;
    line-height: 1.4;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .floating-text {
    background-color: rgba(0, 0, 0, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.15);
  }
}

/* 性能优化 */
.floating-text {
  will-change: transform, opacity;
  transform: translateZ(0);
}

/* 加载字体备用 */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
</style>