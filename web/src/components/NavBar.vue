<template>
  <div>
    <!-- 侧边栏 -->
    <div class="shell">
      <ul class="nav">
        <!--logo-->
        <li :class="{ active: activeSection === 'logo' }" id="logo">
          <a href="#" @click="setActive('logo')">
            <div class="icon">
              <div class="imageBox">
                <img :src="logoImage" alt="">
              </div>
            </div>
            <div class="text">Anna_Yanami</div>
          </a>
        </li>

        <!--menu-->
        <li v-for="(item, index) in navItems" :key="index" :class="{ active: activeSection === item.id }">
          <a :href="'#' + item.id" @click="setActive(item.id)">
            <div class="icon">
              <i :class="item.iconClass"></i>
            </div>
            <div class="text">{{ item.text }}</div>
          </a>
        </li>

        <li :class="{ active: activeSection === 'me' }">
          <a href="#me" @click="setActive('me')">
            <div class="icon">
              <div class="imageBox">
                <img :src="profileImage" alt="">
              </div>
            </div>
            <div class="text">ME</div>
          </a>
        </li>
      </ul>
    </div>

    <!-- 内容区域 -->
    <section id="home" ref="section_ref">
      <video class="video" src="@/assets/AboutMe/Mitsuha.mp4" type="video/mp4"
             ref="videoRef"
             autoplay loop playsinline muted disablepictureinpicture>
      </video>

      <!-- 音量滑块 -->
      <div class="volume-control">
        <div class="volume-icon" @click="toggleMute">
          <i :class="volumeIcon"></i>
        </div>
        <input
            type="range"
            min="0"
            max="1"
            step="0.1"
            v-model="volume"
            @input="changeVolume"
            class="volume-slider"
        />
        <div class="volume-value">{{ Math.round(volume * 100) }}%</div>
      </div>

    </section>
    <section id="theme">Theme</section>
    <section id="wallet">Wallet</section>
    <section id="picture">Picture</section>
    <section id="code">QR code</section>
    <section id="authentication">Authentication</section>
    <section id="me">
      <ConnectCard></ConnectCard>
    </section>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, computed} from 'vue';
import "@/assets/css/AboutMe/NavBar.css"
import ConnectCard from "@/components/ConnectCard.vue";

export default {
  name: 'NavBar',
  components: {
    ConnectCard,
  },
  setup() {
    const section_ref = ref(null);
    let scrollDistance = 0;
    let requestId = null;

    const videoRef = ref(null);
    const volume = ref(0); // 默认静音（0 ~ 1）
    const lastVolume = ref(0.5); // 记录上次的音量

    const activeSection = ref('home');//开始定位到home,home高亮
    //头像路径
    const logoImage = require('@/assets/AboutMe/Yanami.png');
    const profileImage = require('@/assets/AboutMe/Yanami.png');

    const updateHeaderClipPath = () => {
      if (!section_ref.value || !videoRef.value) return;

      // 裁剪动画
      const clipPathValue = `polygon(0 0, 100% 0%, 100% ${
          scrollDistance <= 600 ? 100 - (scrollDistance / 600) * 60 : 75
      }%, 0 100%)`;
      section_ref.value.style.clipPath = clipPathValue;

      // 视频缩放动画
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

    // 计算音量图标
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
    // 切换静音/取消静音
    const toggleMute = () => {
      if (volume.value > 0) {
        lastVolume.value = volume.value;
        volume.value = 0;
      } else {
        volume.value = lastVolume.value;
      }
    };

    //阿里icon-font
    const navItems = [
      { id: 'home', iconClass: 'iconfont icon-home', text: 'Home' },
      { id: 'theme', iconClass: 'iconfont icon-zhuti_tiaosepan', text: 'Theme' },
      { id: 'wallet', iconClass: 'iconfont icon-xiangmu', text: 'Project' },
      { id: 'picture', iconClass: 'iconfont icon-picture', text: 'Picture' },
      { id: 'code', iconClass: 'iconfont icon-qr-code', text: 'QR code' },
      { id: 'authentication', iconClass: 'iconfont icon-dunpaibaoxianrenzheng', text: 'Authentication' },
    ];


    const setActive = (section) => {
      activeSection.value = section;

      const targetSection = document.getElementById(section);
      if (targetSection) {
        targetSection.scrollIntoView({ behavior: 'smooth' });

        // 如果跳转到 home，就重置视频缩放和 clip-path
        if (section === 'home') {
          scrollDistance = 0;
        } else {
          // 如果不是 home，scrollDistance 设为最大值，这样视频不会缩放
          scrollDistance = 600;
        }
        updateHeaderClipPath();
      }
    };

    onMounted(() => {
      const observer = new IntersectionObserver(
          (entries) => {
            entries.forEach(entry => {
              if (entry.isIntersecting) {
                activeSection.value = entry.target.id;
              }
            });
          },
          { threshold: 0.5 } // section 50% 进入视口才触发
      );

      document.querySelectorAll('section').forEach(section => observer.observe(section));
    });



    onMounted(() => {
      window.addEventListener("wheel", scrollHandler);
    });

    onUnmounted(() => {
      window.removeEventListener('wheel', scrollHandler);
    });

    return {
      activeSection,
      logoImage,
      profileImage,
      navItems,
      videoRef,
      volume,
      volumeIcon,
      scrollDistance,
      requestId,
      section_ref,

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
</style>