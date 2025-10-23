<template>
  <div>
    <!-- 侧边栏 -->
    <div class="shell">
      <ul class="nav">
        <!--logo-->
        <li
          id="logo"
          :class="{ active: activeSection === 'logo' }"
        >
          <a
            href="#"
            @click="setActive('logo')"
          >
            <div class="icon">
              <div class="imageBox">
                <img
                  :src="logoImage"
                  alt=""
                >
              </div>
            </div>
            <div class="text">Anna_Yanami</div>
          </a>
        </li>

        <!--menu-->
        <li
          v-for="(item, index) in navItems"
          :key="index"
          :class="{ active: activeSection === item.id }"
        >
          <a
            :href="'#' + item.id"
            @click="setActive(item.id)"
          >
            <div class="icon">
              <i :class="item.iconClass" />
            </div>
            <div class="text">{{ item.text }}</div>
          </a>
        </li>

        <li :class="{ active: activeSection === 'me' }">
          <a
            href="#me"
            @click="setActive('me')"
          >
            <div class="icon">
              <div class="imageBox">
                <img
                  :src="profileImage"
                  alt=""
                >
              </div>
            </div>
            <div class="text">ME</div>
          </a>
        </li>
      </ul>
    </div>
    <!-- 内容区域 -->
    <section
      id="home"
      ref="section_ref"
    >
      <RandomText />
      <video
        ref="videoRef"
        class="video"
        :src="videoSrc"
        type="video/mp4"
        autoplay
        loop
        playsinline
        muted
        disablepictureinpicture
      />

      <!-- 打字机特效 -->
      <div class="typing-container">
        <h1
          ref="typingTextRef"
          class="typing-text"
        />
      </div>

      <!-- 音量滑块 -->
      <div class="volume-control">
        <div
          class="volume-icon"
          @click="toggleMute"
        >
          <i :class="volumeIcon" />
        </div>
        <input
          v-model="volume"
          type="range"
          min="0"
          max="1"
          step="0.1"
          class="volume-slider"
          @input="changeVolume"
        >
        <div class="volume-value">
          {{ Math.round(volume * 100) }}%
        </div>
      </div>
    </section>

    <section
      id="links"
      class="links-container"
    >
      <LinkSectionView />
    </section>
    <!--    <section id="wallet">-->
    <!--      Wallet-->
    <!--    </section>-->
    <section id="live-2d">
      <BendingGallery
        :items="items"
        :bend="2"
        :border-radius="0.1"
      />
    </section>
    <section id="technology">
      <TechnologySlider class="technology-slider" />
    </section>

    <section id="me">
      <!-- 左栏容器 -->
      <div class="left-column">
        <MorphingText
          class="mt-6"
          :texts="texts"
        />
        <ConnectCard />
      </div>

      <!-- 右栏容器 -->
      <div class="right-column">
        <div class="lightspeed-wrapper">
          <LightSpeed
            :key="preset"
            :effect-options="selectedPreset"
          />
          <div class="absolute inset-0 flex items-center justify-center text-2xl text-neutral-500 pointer-events-none">
            选择一套预设
          </div>
        </div>
        <div>
          <label
            for="preset-select"
            class="mr-4 text-white"
          >Select a Preset:</label>
          <select
            id="preset-select"
            v-model="preset"
            class="rounded-md bg-zinc-800 px-4 py-2 text-white"
          >
            <option
              v-for="effectKey in Object.keys(lightSpeedPresets)"
              :key="effectKey"
              :value="effectKey"
            >
              {{ getName(effectKey) }}
            </option>
          </select>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import {ref, onMounted, onUnmounted, computed, nextTick} from 'vue';
import '@/assets/css/AboutMe/NavBar.css';
import ConnectCard from '@/components/Me/ConnectCard.vue';
import TechnologySlider from '@/components/Me/TechnologyImages.vue';
import RandomText from '@/components/Me/RandomText.vue';
import LinkSectionView from "@/views/ME/LinkSectionView.vue";
import BendingGallery from "@/components/tools/Card/BendingGallery.vue";
import LightSpeed from "@/components/tools/Light/LightSpeed.vue";
import {lightSpeedPresets} from "@/components/tools/Light/presets";
import MorphingText from "@/components/tools/MorphingText.vue";

import Furina from '/public/Gallery/Furina.jpg';
import Elysia from '/public/Gallery/Elysia.png';
import Nahida from '/public/Gallery/Nahida.jpg'
import Robin from '/public/Gallery/Robin.png'
import Seele from '/public/Gallery/Seele.png'
import Ellen from '/public/Gallery/Ellen.png'
import Kafuka from '/public/Gallery/Kafuka.jpg'


import logoImage from '@/assets/AboutMe/Yanami.png';
import profileImage from '@/assets/AboutMe/Yanami.png';
import videoSrc from '@/assets/AboutMe/Mitsuha.mp4';

export default {
  name: 'NavBar',
  components: {
    TechnologySlider,
    ConnectCard,
    RandomText,
    LinkSectionView,
    BendingGallery,
    LightSpeed,
    MorphingText,
  },
  setup() {
    const section_ref = ref(null);
    let scrollDistance = 0;
    let requestId = null;

    const videoRef = ref(null);
    const volume = ref(0);
    const lastVolume = ref(0.5);

    const activeSection = ref('home');

    // 打字机效果
    const typingTextRef = ref(null);
    const textsToType = [
      "Hello, I'm Anna_Yanami",
      '这是我的个人网页',
      '欢迎来到我的世界嘿嘿',
      '很高兴在超过80亿个节点的Graph上与你建立起边',
      '或许你对我感兴趣话，下面有我的联系方式',
      '这样我们就可以建立起一条双向边',
      '通过不断的交流来加大我们的权重吧',
      '点击右上角可跳转本项目github仓库',
      '本项目内置了live2c模型，在live-2d区块查看',
      "live-2d模型对话功能开发ing",
      'MADE BY @SDU ANNA_YANAMI IN 2025/10',
    ];

    const texts = [
      "你好",
      "我是Anna_Yanami",
      "下面就是我的联系方式了",
      "点击之后展开",
      "你可以添加我的社交软件",
      "也可以查看我的github仓库",
      "很高兴认识你~",
      "欢迎交流~",
    ];
    let currentLineIndex = 0;
    let typingIndex = 0;
    let typingTimer = null;
    let isDeleting = false;

    const preset = ref("one");
    const selectedPreset = computed(() => lightSpeedPresets[preset.value]);
    function getName(key) {
      const names = {
        one: "Light 1",
        two: "Light 2",
        three: "Light 3",
        four: "Light 4",
        five: "Light 5",
        six: "Light 6",
      };
      return names[key];
    }

    //live-2d
    const items = ref([
      {
        image: Furina,
        text: "",
      },
      {
        image: Elysia,
        text: "",
      },
      {
        image: Nahida,
        text: "",
      },
      {
        image: Robin,
        text: "",
      },
      {
        image: Seele,
        text: "",
      },
      {
        image: Ellen,
        text: "",
      },
      {
        image: Kafuka,
        text: "",
      },
    ]);

    const typeWriter = () => {
      if (!typingTextRef.value) return;
      const currentText = textsToType[currentLineIndex];
      if (!isDeleting) {
        if (typingIndex < currentText.length) {
          typingTextRef.value.textContent += currentText.charAt(typingIndex);
          typingIndex++;
          typingTimer = setTimeout(typeWriter, 100);
        } else {
          isDeleting = true;
          typingTimer = setTimeout(typeWriter, 1500);
        }
      } else {
        if (typingIndex > 0) {
          typingTextRef.value.textContent = currentText.substring(0, typingIndex - 1);
          typingIndex--;
          typingTimer = setTimeout(typeWriter, 50);
        } else {
          isDeleting = false;
          currentLineIndex = (currentLineIndex + 1) % textsToType.length;
          typingTimer = setTimeout(typeWriter, 500);
        }
      }
    };
    const resetTyping = () => {
      if (typingTextRef.value) {
        typingTextRef.value.textContent = '';
        currentLineIndex = 0;
        typingIndex = 0;
        isDeleting = false;
        clearTimeout(typingTimer);
      }
    };

    const updateHeaderClipPath = () => {
      if (!section_ref.value || !videoRef.value) return;
      const clipPathValue = `polygon(0 0, 100% 0%, 100% ${
          scrollDistance <= 600 ? 100 - (scrollDistance / 600) * 60 : 75
      }%, 0 100%)`;
      section_ref.value.style.clipPath = clipPathValue;

      const scaleValue = 1 + (scrollDistance / 600) * 1;
      videoRef.value.style.transform = `scale(${scaleValue})`;
    };

    const scrollHandler = (event) => {
      if (event.deltaY < 0) {
        scrollDistance = Math.max(0, scrollDistance + event.deltaY);
      } else {
        scrollDistance = Math.min(600, scrollDistance + event.deltaY);
      }
      if (!requestId) {
        requestId = window.requestAnimationFrame(() => {
          updateHeaderClipPath();
          requestId = null;
        });
      }
    };

    const volumeIcon = computed(() => {
      if (volume.value === 0) return 'fas fa-volume-mute';
      if (volume.value < 0.5) return 'fas fa-volume-down';
      return 'fas fa-volume-up';
    });

    const changeVolume = () => {
      if (videoRef.value) {
        videoRef.value.volume = volume.value;
        videoRef.value.muted = volume.value === 0;
        lastVolume.value = volume.value > 0 ? volume.value : lastVolume.value;
      }
    };
    const toggleMute = () => {
      if (volume.value > 0) {
        lastVolume.value = volume.value;
        volume.value = 0;
      } else {
        volume.value = lastVolume.value;
      }
    };

    const navItems = [
      {id: 'home', iconClass: 'iconfont icon-home', text: 'Home'},
      {id: 'links', iconClass: 'iconfont icon-link', text: 'Links'},
      // {id: 'wallet', iconClass: 'iconfont icon-xiangmu', text: 'Project'},
      {id: 'live-2d', iconClass: 'iconfont icon-picture', text: 'live-2d'},
      {id: 'technology', iconClass: 'iconfont icon-yingyong', text: 'technology'}
    ];

    const setActive = (section) => {
      activeSection.value = section;
      const targetSection = document.getElementById(section);
      if (targetSection) {
        targetSection.scrollIntoView({behavior: 'smooth'});
        if (section === 'home') {
          scrollDistance = 0;
          resetTyping();
          nextTick(() => typeWriter());
        } else {
          scrollDistance = 600;
        }
        updateHeaderClipPath();
      }
    };


    onMounted(() => {
      const observer = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) activeSection.value = entry.target.id;
            });
          },
          {threshold: 0.5}
      );
      document.querySelectorAll('section').forEach((section) => observer.observe(section));

      window.addEventListener('wheel', scrollHandler);

      nextTick(() => {
        typeWriter();
      });
    });

    onUnmounted(() => {
      window.removeEventListener('wheel', scrollHandler);
      clearTimeout(typingTimer);
    });

    return {
      // 状态
      activeSection,
      navItems,

      // 资源
      logoImage,
      profileImage,
      videoSrc,
      // 引用与数值
      videoRef,
      volume,
      volumeIcon,
      scrollDistance,
      requestId,
      section_ref,
      typingTextRef,
      items,

      preset,
      selectedPreset,
      lightSpeedPresets,
      getName,
      texts,

      // 方法
      toggleMute,
      changeVolume,
      setActive,
      updateHeaderClipPath,
      scrollHandler,
    };
  }
};
</script>

<style scoped>
@import "@/assets/css/AboutMe/NavBar.css";

#me {
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  height: 100vh;
  padding: 2rem;
  gap: 2rem;
  background-color: #111827;
}

/* 左栏样式 */
.left-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 2rem;
  flex: 0.8;
  max-width: 45%;
  height: auto;
  align-self: flex-start;
}
/* 右栏样式 */
.right-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 1.5rem;
  flex: 1.2;
  max-width: 50%;
  height: 100%;
  position: relative;
}

.lightspeed-wrapper {
  position: relative;
  width: 100%;
  flex: 1;
  min-height: 0;
  border-radius: 1rem;
  border: 1px solid #374151;
  overflow: hidden; /* 确保内容不溢出 */
}

/* 悬浮下拉框样式 */
.floating-select {
  position: absolute;
  left: 1rem; /* 距离左边 */
  bottom: 1rem; /* 距离底部 */
  z-index: 50; /* 确保在 LightSpeed 之上 */
}

/* 下拉框触发器样式 */
.floating-select-trigger {
  background-color: rgba(31, 41, 55, 0.9) !important;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(75, 85, 99, 0.7) !important;
  border-radius: 0.5rem;
}

/* 确保下拉内容向上弹出 */
:deep(.floating-select-content) {
  z-index: 100 !important;
  position: relative;
}

/* 如果使用 Tailwind，可能需要覆盖默认的弹出方向 */
:deep([data-side="top"]) {
  bottom: 100% !important;
  top: auto !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
  #me {
    flex-direction: column;
    height: auto;
    min-height: 100vh;
  }

  .left-column,
  .right-column {
    max-width: 100%;
    flex: none;
    width: 100%;
  }

  .right-column {
    height: 60vh;
  }

  .floating-select {
    left: 0.5rem;
    bottom: 0.5rem;
  }
}
</style>