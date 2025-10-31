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
import {ref} from "vue";

export default {
  name: "RandomText",
  setup() {
    const texts = ref([]);

    // 随机文字数组
    const arr = [
      "最是人间留不住，朱颜辞镜花辞树",
      "君住长江头，我住长江尾，日日思君不见君，共饮长江水",
      "此水几时休，此恨何时已。只愿君心似我心，定不负相思意。",
      "赠君茉莉，愿君莫离",
      "相见时难别亦难，东风无力百花残。",
      "锦瑟无端五十弦，一弦一柱思华年。",
      "庄生晓梦迷蝴蝶，望帝春心托杜鹃",
      "沧海月明珠有泪，蓝田日暖玉生烟",
      "此情可待成追忆，只是当时已惘然",
      "山有木兮木有枝，心悦君兮君不知",
      "身无彩凤双飞翼，心有灵犀一点通",
      "落霞与孤鹜齐飞，秋水共长天一色",
      "我见青山多妩媚，料青山见我应如是",
      "欲买桂花同载酒，终不似，少年游",
      "你站在桥上看风景，看风景人在楼上看你。明月装饰了你的窗子，你装饰了别人的梦。",
      "歌未竟，东方白",
      "我有一所房子，面朝大海，春暖花开",
      "You are the only protagonist in my life",
      "我喜欢琴声伴奏下海面泛起淡淡连漪",
      "我喜欢看雨幕里你带有忧伤的眼睛",
      "我喜欢香水在身上散发淡淡香气",
      "我喜欢雾气散去你我相望那集",
      "我喜欢雨后阴暗平静的天气",
      "我喜欢连绵不绝的雪落地",
      "我喜欢用心绘画每一笔",
      "我喜欢茉莉花香相聚",
      " 我喜欢画笔下的你",
      "我喜欢旧事重提",
      "我喜欢梅雨季",
      "我喜欢琴音",
      "我喜欢你",
      "我说我",
      "喜欢",
      "你",
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
  font-family: 'SF Pro Display',
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
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3),
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