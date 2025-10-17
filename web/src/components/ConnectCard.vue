<template>
  <div class="card-shell">
    <div class="photo">
      <img
        src="@/assets/AboutMe/Yanami.png"
        alt=""
      >
    </div>
    <div class="content">
      <div class="text">
        <h3>Contact Me</h3>
      </div>
      <div
        class="btn"
        :class="{ active: isOpen }"
        @click="toggleOpen"
      >
        <span />
      </div>
    </div>
    <div
      class="box"
      :class="{ open: isOpen }"
    >
      <i
        class="iconfont icon-QQ"
        @click="handleQQClick"
      />
      <i
        class="iconfont icon-weixin"
        @click="handleWeixinClick"
      />
      <i
        class="iconfont icon-douyin"
        @click="handleDouyinClick"
      />
      <i
        class="iconfont icon-bilibili"
        @click="handleBilibiliClick"
      />
      <i
        class="iconfont icon-github-fill"
        @click="handleGithubClick"
      />
    </div>

    <!-- 使用Teleport将图片传送到body根部，确保在全屏中心显示 -->
    <teleport to="body">
      <div
        v-if="showFullscreenImage"
        class="global-fullscreen-overlay"
        @click="closeFullscreen"
      >
        <img
          :src="currentImage"
          alt="Fullscreen Image"
          class="global-centered-image"
          @click.stop
        >
        <button
          class="global-close-btn"
          @click="closeFullscreen"
        >
          ×
        </button>
      </div>
    </teleport>
  </div>
</template>

<script>
import QQ_Qrcode from '@/assets/AboutMe/QRcode/QQ_Qrcode.png';
import Wechat_Qrcode from '@/assets/AboutMe/QRcode/Wechat_Qrcode.png';
import Douyin_Qrcode from '@/assets/AboutMe/QRcode/Douyin_Qrcode.png';

export default {
  name: "ConnectCard",

  data() {
    return {
      isOpen: false,
      showFullscreenImage: false,
      currentImage: "",
      qrCodes: {
        qq: QQ_Qrcode,
        weixin: Wechat_Qrcode,
        douyin: Douyin_Qrcode,
      },
      socialLinks: {
        qq: "#",
        weixin: "#",
        douyin: "https://www.douyin.com/user/self",
        bilibili: "https://space.bilibili.com/413365215?spm_id_from=333.1007.0.0",
        github: "https://github.com/zsqyszaqys"
      }
    };
  },
  methods: {
    toggleOpen() {
      this.isOpen = !this.isOpen;
    },

    handleQQClick() {
      this.showImageModal(this.qrCodes.qq);
    },

    handleWeixinClick() {
      this.showImageModal(this.qrCodes.weixin);
    },

    handleDouyinClick() {
      this.showImageModal(this.qrCodes.douyin);
    },

    handleBilibiliClick() {
      window.open(this.socialLinks.bilibili, '_blank');
    },

    handleGithubClick() {
      window.open(this.socialLinks.github, '_blank');
    },

    showImageModal(imagePath) {
      this.currentImage = imagePath;
      this.showFullscreenImage = true;
      document.body.style.overflow = 'hidden';
      document.addEventListener('keydown', this.handleKeydown);
    },

    closeFullscreen() {
      this.showFullscreenImage = false;
      document.body.style.overflow = 'auto';
      document.removeEventListener('keydown', this.handleKeydown);
    },

    handleKeydown(event) {
      if (event.key === 'Escape') {
        this.closeFullscreen();
      }
    }
  }
};
</script>
<style scoped>
@import url('@/assets/fonts/iconfont.css');
* {
  padding: 0;
  margin: 0;
}

body {
  color: #ffffff;
  background-color: #f3f3f3;
}

.card-shell {
  width: 500px; /* 从 330px 增大 */
  height: 150px; /* 从 100px 增大 */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  background-color: #fafafa;
  box-shadow: 0 0 2rem #babbbc;
  animation: show-shell 0.5s forwards ease-in-out;
}

@keyframes show-shell {
  0% {
    width: 0;
  }
}

.photo,
.content {
  box-sizing: border-box;
}

.photo {
  width: 150px; /* 从 100px 增大 */
  height: 150px; /* 从 100px 增大 */
  border-radius: 50%;
  overflow: hidden;
  border: 5px solid #fafafa;
  background-color: #fafafa;
  margin-left: -75px; /* 从 -50px 调整 */
  box-shadow: 0 0 0.5rem #babbbc;
  animation: rotate-photo 0.5s forwards ease-in-out;
}

@keyframes rotate-photo {
  100% {
    transform: rotate(-360deg);
  }
}

.photo img {
  width: 100%;
}

.content {
  padding: 10px;
  overflow: hidden;
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.content::before {
  content: "";
  position: absolute;
  width: 350px; /* 从 230px 增大 */
  height: 225px; /* 从 150px 增大 */
  left: 0;
  top: -30px; /* 从 -20px 调整 */
  z-index: -1;
  transform: rotate(-8deg);
  background-image: linear-gradient(to top, #6866ee 0%, #fbc8d4 100%);
}

.content .text {
  margin-top: 30px; /* 从 20px 增大 */
  margin-left: 75px; /* 从 50px 增大 */
}

.content .text h3,
.content .text h6 {
  font-weight: normal;
  margin: 3px 0;
  letter-spacing: 0.5px;
  white-space: normal; /* 从 nowrap 改为 normal，允许换行 */
}

.content .text h3 {
  font-size: 1.5em; /* 可选：增大 h3 字体 */
}

.content .text h6 {
  font-size: 1.2em; /* 可选：增大 h6 字体 */
}

.content .btn {
  background-color: rgb(106, 106, 245);
  width: 70px; /* 从 50px 增大到 70px */
  height: 70px; /* 从 50px 增大到 70px */
  position: absolute;
  right: 35px; /* 调整位置以适应更大按钮，从 40px 略减 */
  top: 35px; /* 调整位置以适应更大按钮，从 40px 略减 */
  border-radius: 50%;
  z-index: 1;
  cursor: pointer;
  transition-duration: 0.3s;
  animation: pop-btn 0.3s both ease-in-out 0.5s;
}

@keyframes pop-btn {
  0% {
    transform: scale(0);
  }

  80% {
    transform: scale(1.2);
  }

  100% {
    transform: scale(1);
  }
}

.content .btn:hover {
  box-shadow: 0 0 0 7px rgba(170, 187, 204, 0.5); /* 阴影调整，从 5px 增大 */
}

.content .btn span {
  width: 60%;
  height: 3px; /* 从 2px 增大线条厚度 */
  position: absolute;
  background-color: white;
  top: 50%;
  left: 20%;
  transform: translateY(-50%);
  animation: to-hamburger 0.3s forwards ease-in-out;
}

.content .btn span::before,
.content .btn span::after {
  content: "";
  width: 100%;
  height: 3px; /* 从 2px 增大线条厚度 */
  position: absolute;
  background-color: white;
  transition-duration: 0.3s;
  transform: rotate(0deg);
  right: 0;
}

.content .btn span::before {
  margin-top: -10px; /* 从 -7px 调整，以匹配更大按钮 */
}

.content .btn span::after {
  margin-top: 10px; /* 从 7px 调整，以匹配更大按钮 */
}

.content .btn.active span {
  animation: to-arrow 0.3s forwards ease-in-out;
}

.content .btn.active span::before,
.content .btn.active span::after {
  width: 60%;
  right: -1px;
}

.content .btn.active span::before {
  transform: rotate(45deg);
}

.content .btn.active span::after {
  transform: rotate(-45deg);
}

@keyframes to-hamburger {
  from {
    transform: translateY(-50%) rotate(-180deg);
  }
}

@keyframes to-arrow {
  from {
    transform: translateY(-50%) rotate(0deg);
  }

  to {
    transform: translateY(-50%) rotate(180deg);
  }
}

.box {
  opacity: 0;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.7);
  position: absolute;
  top: 50%;
  right: -30%;
  transform: translate(-50%, -50%);
  transition-duration: 0.3s;
  box-shadow: 0 0 10px #fff;
  border: 5px #fff solid;
}

.box::after {
  content: '';
  display: block;
  width: 180px; /* 从 120px 增大 */
  height: 180px; /* 从 120px 增大 */
  background-image: url(~@/assets/AboutMe/button.gif);
  background-size: cover;
  opacity: .7;
  border-radius: 50%;
}

.box i {
  width: 90px; /* 从 60px 增大 */
  height: 90px; /* 从 60px 增大 */
  border-radius: 50%;
  background-color: #ececec;
  font-size: 40px; /* 从 26px 增大 */
  text-align: center;
  line-height: 90px; /* 调整 */
  position: absolute;
  left: 27px; /* 从 18px 调整 */
  top: calc(90px - 75px/2); /* 调整计算 */
  box-shadow: 0 0 10px #fff;
  color: rgb(106, 106, 245);
  background-color: #fff;
  transition-duration: 0.3s;
}

.box i:hover {
  transition-delay: initial !important;
  box-shadow: 0 0 0 5px #babbbc;
  background-color: rgb(106, 106, 245);
  color: #fff;
}

.box.open {
  opacity: 1;
}

.box.open i {
  left: 30px; /* 从 20px 调整 */
  opacity: 1;
}

.box.open i:nth-of-type(1) {
  transform: rotate(-90deg) translateX(180px) rotate(90deg); /* 从 120px 增大 */
  transition-delay: 0s;
}

.box.open i:nth-of-type(2) {
  transform: rotate(-45deg) translateX(180px) rotate(45deg); /* 从 120px 增大 */
  transition-delay: 0.1s;
}

.box.open i:nth-of-type(3) {
  transform: rotate(0deg) translateX(195px) rotate(0deg); /* 从 130px 增大 */
  transition-delay: 0.2s;
}

.box.open i:nth-of-type(4) {
  transform: rotate(45deg) translateX(180px) rotate(-45deg); /* 从 120px 增大 */
  transition-delay: 0.3s;
}

.box.open i:nth-of-type(5) {
  transform: rotate(90deg) translateX(165px) rotate(-90deg); /* 从 110px 增大 */
  transition-delay: 0.4s;
}
.global-fullscreen-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: transparent;
  z-index: 9999;
  animation: fadeIn 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 全局居中图片 */
.global-centered-image {
  max-width: 90vw;
  max-height: 90vh;
  object-fit: contain;
  border-radius: 10px;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.3);
  background-color: rgba(255, 255, 255, 0.95);
  padding: 10px;
  box-sizing: border-box;
}

/* 全局关闭按钮 */
.global-close-btn {
  position: fixed;
  top: 20px;
  right: 30px;
  background: rgba(0, 0, 0, 0.3);
  border: 2px solid rgba(255, 255, 255, 0.7);
  color: rgba(255, 255, 255, 0.9);
  font-size: 40px;
  font-weight: bold;
  cursor: pointer;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
  z-index: 10000;
}

.global-close-btn:hover {
  background-color: rgba(0, 0, 0, 0.6);
  border-color: rgba(255, 255, 255, 0.9);
  color: rgba(255, 255, 255, 1);
  transform: scale(1.1);
}

/* 为图标添加悬停效果和点击反馈 */
.box i {
  cursor: pointer;
  transition: all 0.3s ease;
}

.box i:hover {
  transform: scale(1.1);
  box-shadow: 0 0 15px rgba(106, 106, 245, 0.5);
}
</style>