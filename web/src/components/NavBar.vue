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
      <!--      单击弹出文字-->
      <RandomText></RandomText>
      <video class="video" src="@/assets/AboutMe/Mitsuha.mp4" type="video/mp4" ref="videoRef"
             autoplay loop playsinline muted disablepictureinpicture>
      </video>
      <!--      打字机特效-->
      <div class="typing-container">
        <h1 class="typing-text" ref="typingTextRef"></h1>
      </div>
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
    <section id="links">
      <InteractiveHoverButton></InteractiveHoverButton>
    </section>
    <section id="wallet">Wallet</section>
    <section id="picture">Picture</section>
    <section id="code">QR code</section>
    <section id="technology">
      <TechnologySlider class="technology-slider"></TechnologySlider>
    </section>
    <section id="me">
      <ConnectCard></ConnectCard>
    </section>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, computed, nextTick} from 'vue';
import "@/assets/css/AboutMe/NavBar.css"
import ConnectCard from "@/components/ConnectCard.vue";
import TechnologySlider from "@/components/TechnologyImages.vue";
import RandomText from "@/components/RandomText.vue";
import InteractiveHoverButton from "@/components/tools/InteractiveHoverButton.vue";

export default {
  name: 'NavBar',
  components: {
    TechnologySlider,
    ConnectCard,
    RandomText,
    InteractiveHoverButton,
  },
  setup() {
    const section_ref = ref(null);
    let scrollDistance = 0;
    let requestId = null;

    const videoRef = ref(null);
    const volume = ref(0); // 默认静音（0 ~ 1）
    const lastVolume = ref(0.5); // 记录上次的音量

    const activeSection = ref('home');//开始定位到home,home高亮

    // 打字机效果相关变量
    const typingTextRef = ref(null);
    const textsToType = [
        "Hello, I'm Anna_Yanami",
        "这是我的个人网页",
        "欢迎来到我的世界嘿嘿",
        "很高兴在超过80亿个节点的Graph上与你建立起边",
        "或许你对我感兴趣话，下面有我的联系方式",
        "这样我们就可以建立起一条双向边",
        "通过不断的交流来加大我们的权重吧",
        "如果是可爱的小姐姐的的话就更好了嘿嘿，我愿意被小姐姐骗",
        "点击右上角可跳转本项目github仓库",
        "MADE BY @SDU ANNA_YANAMI IN 2025/10"

    ]; // 多行文本数组
    let currentLineIndex = 0;
    let typingIndex = 0;
    let typingTimer = null;
    let isDeleting = false; // 是否处于删除状态

    //头像路径
    const logoImage = require('@/assets/AboutMe/Yanami.png');
    const profileImage = require('@/assets/AboutMe/Yanami.png');

    // 打字机效果函数
    const typeWriter = () => {
      if (!typingTextRef.value) return;

      const currentText = textsToType[currentLineIndex];

      if (!isDeleting) {
        // 打字模式
        if (typingIndex < currentText.length) {
          typingTextRef.value.textContent += currentText.charAt(typingIndex);
          typingIndex++;
          typingTimer = setTimeout(typeWriter, 100); // 打字速度
        } else {
          // 当前行打完，暂停一下然后开始删除或切换到下一行
          isDeleting = true;
          typingTimer = setTimeout(typeWriter, 1500); // 行间暂停时间
        }
      } else {
        // 删除模式
        if (typingIndex > 0) {
          typingTextRef.value.textContent = currentText.substring(0, typingIndex - 1);
          typingIndex--;
          typingTimer = setTimeout(typeWriter, 50); // 删除速度
        } else {
          // 当前行删除完毕，切换到下一行
          isDeleting = false;
          currentLineIndex = (currentLineIndex + 1) % textsToType.length; // 循环播放
          typingTimer = setTimeout(typeWriter, 500); // 切换到下一行的延迟
        }
      }
    };

    // 重置打字机效果
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
      { id: 'links', iconClass: 'iconfont icon-link', text: 'Links' },
      { id: 'wallet', iconClass: 'iconfont icon-xiangmu', text: 'Project' },
      { id: 'picture', iconClass: 'iconfont icon-picture', text: 'Picture' },
      { id: 'code', iconClass: 'iconfont icon-qr-code', text: 'QR code' },
      { id: 'technology', iconClass: 'iconfont icon-yingyong', text: 'technology' },

    ];


    const setActive = (section) => {
      activeSection.value = section;

      const targetSection = document.getElementById(section);
      if (targetSection) {
        targetSection.scrollIntoView({ behavior: 'smooth' });

        // 如果跳转到 home，就重置视频缩放和 clip-path
        if (section === 'home') {
          scrollDistance = 0;

          // 重置并重新开始打字效果
          resetTyping();
          nextTick(() => {
            typeWriter();
          });

        } else {
          // 如果不是 home，scrollDistance 设为最大值，这样视频不会缩放
          scrollDistance = 600;
        }
        updateHeaderClipPath();
      }
    };

    onMounted(() => {
      // Intersection Observer
      const observer = new IntersectionObserver(
          (entries) => {
            entries.forEach(entry => {
              if (entry.isIntersecting) {
                activeSection.value = entry.target.id;
              }
            });
          },
          { threshold: 0.5 }
      );
      document.querySelectorAll('section').forEach(section => observer.observe(section));

      // 滚动事件监听
      window.addEventListener("wheel", scrollHandler);

      // 开始打字效果
      nextTick(() => {
        typeWriter();
      });
    });

    onUnmounted(() => {
      window.removeEventListener('wheel', scrollHandler);
      clearTimeout(typingTimer);
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
      TechnologySlider,
      typingTextRef,

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