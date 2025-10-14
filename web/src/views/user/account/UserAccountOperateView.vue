<template>
  <div class="shell">
      <!--    注册表单-->
    <div class="container a-container" id="a-container">
      <form class="form" id="a-form">
        <h2 class="form_title title">创建账号</h2>
        <span class="form_span">使用电子邮箱注册喵~</span>
        <input v-model="name" type="text" class="form_input" placeholder="Name" />
        <input v-model="email" type="text" class="form_input" placeholder="Email" />
        <input v-model="password"  type="password" class="form_input" placeholder="Password" />
        <div class="error_message" v-show="error_message">{{ error_message }}</div>
        <WaterButton class="form_button button submit" @click="register">SIGN UP</WaterButton>
      </form>
    </div>

    <!--    登录表单-->
    <div class="container b-container" id="b-container">
      <form class="form" id="b-form">
        <h2 class="form_title title">登入账号</h2>
        <span class="form_span">使用用户名、邮箱或手机号登录喵~</span>
        <input v-model="email" type="text" class="form_input" placeholder="邮箱" />
        <input v-model="password" type="password" class="form_input" placeholder="密码" />
        <div class="error_message" v-show="error_message">{{ error_message }}</div>
        <a class="form_link">忘记密码？</a>
        <WaterButton class="form_button button submit" @click="login">SIGN IN</WaterButton>
      </form>
    </div>

    <div class="switch" id="switch-cnt">
      <!-- 背景图片层 -->
      <div class="bg-image" :style="switchBackground"></div>

      <!-- 毛玻璃层 -->
      <div class="glass-layer"></div>

      <div class="switch_circle"></div>
      <div class="switch_circle switch_circle-t"></div>

      <div class="switch_container" id="switch-c1">
        <h2 class="switch_title title floating-text" style="letter-spacing: 0;">Welcome Back！</h2>
        <p class="switch_description description floating-text">
          已经有账号了嘛，去登入账号来进入奇妙世界吧！！！
        </p>
        <WaterButton class="switch_button button switch-btn floating-text">SIGN IN</WaterButton>
      </div>

      <div class="switch_container is-hidden" id="switch-c2">
        <h2 class="switch_title title floating-text" style="letter-spacing: 0;">Hello Friend！</h2>
        <p class="switch_description description floating-text">
          去注册一个账号，进入我的世界喵~
        </p>
        <WaterButton class="switch_button button switch-btn floating-text">SIGN UP</WaterButton>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, computed, ref } from "vue";
import WaterButton from "@/components/WaterButton.vue";
import {useStore} from "vuex";
import router from "@/router";

export default {
  name:"UserAccountOperateView",
  components: {
    WaterButton,
  },


  setup(){
    const store = useStore();
    let name = ref("");
    let email = ref("");
    let loginId = ref("");
    let password = ref("");
    const isLogin = ref(false);

    const error_message = computed(() => store.state.user.error_message);

    const login = () =>{
      store.dispatch("login",{
        loginId: email.value,
        password: password.value,
        success(){
          store.dispatch("getInfo", {
            success(){
              router.push({name:"me"});
            },
            error(resp){
              console.log(resp);
            }
          })
        },
        error(resp){
         console.log(resp);
      }});
    };

    const register = () =>{
      store.dispatch("register",{
        name: name.value,
        loginId: email.value,
        password: password.value,
        success(resp){
          console.log(resp);
          login();
        },
        error(resp){
          console.log(resp);
        }
        })
    }

    const switchBackground = computed(() => {
      // 根据isLogin的值选择不同的图片
      const imageUrl = isLogin.value
          ? require('@/assets/Login/bird1.png') // 登录状态的图片
          : require('@/assets/Login/bird3.jpg'); // 注册状态的图片

      return {
        backgroundImage: `url(${imageUrl})`,
      };
    });

    // 页面加载后绑定事件
    onMounted(() => {
      const switchCtn = document.querySelector("#switch-cnt");
      const switchC1 = document.querySelector("#switch-c1");
      const switchC2 = document.querySelector("#switch-c2");
      const switchCircle = document.querySelectorAll(".switch_circle");
      const switchBtn = document.querySelectorAll(".switch-btn");
      const aContainer = document.querySelector("#a-container");
      const bContainer = document.querySelector("#b-container");
      const allButtons = document.querySelectorAll(".submit");

      const getButtons = (e) => e.preventDefault();

      const changeForm = () => {
        isLogin.value = !isLogin.value;

        switchCtn.classList.add("is-gx");
        setTimeout(() => {
          switchCtn.classList.remove("is-gx");
        }, 1500);
        switchCtn.classList.toggle("is-txr");
        switchCircle[0].classList.toggle("is-txr");
        switchCircle[1].classList.toggle("is-txr");
        switchC1.classList.toggle("is-hidden");
        switchC2.classList.toggle("is-hidden");
        aContainer.classList.toggle("is-txl");
        bContainer.classList.toggle("is-txl");
        bContainer.classList.toggle("is-z");
      };

      allButtons.forEach((btn) => btn.addEventListener("click", getButtons));
      switchBtn.forEach((btn) => btn.addEventListener("click", changeForm));

    });

    return{
      name,
      loginId,
      email,
      password,
      switchBackground,
      error_message,

      login,
      register,
    }
  },
}
</script>

<style scoped>
/* 导入可变字体 */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  user-select: none;
}

body {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  background-color: #ecf0f3;
  color: #a0a5a8;
  font-family: 'Inter', sans-serif; /* 使用可变字体 */
}

.shell {
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 12px;
  overflow: hidden;
}

/* 响应式 */
@media (max-width: 1200px) {
  .shell {
    transform: scale(0.7);
  }
}

@media (max-width: 1000px) {
  .shell {
    transform: scale(0.6);
  }
}

@media (max-width: 800px) {
  .shell {
    transform: scale(0.5);
  }
}

@media (max-width: 600px) {
  .shell {
    transform: scale(0.4);
  }
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  width: 600px;
  height: 100%;
  padding: 25px;
  background-color: #ecf0f3;
  transition: 1.25s;
}

.form {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.iconfont {
  margin: 0 5px;
  border: rgba(0, 0, 0, 0.5) 2px solid;
  border-radius: 50%;
  font-size: 25px;
  padding: 3px;
  opacity: 0.5;
  transition: 0.1s;
}

.iconfont:hover {
  opacity: 1;
  transition: 0.15s;
  cursor: pointer;
}

.form_input {
  width: 350px;
  height: 40px;
  margin: 4px 0;
  padding-left: 25px;
  font-size: 13px;
  letter-spacing: 0.15px;
  border: none;
  outline: none;
  background-color: #ecf0f3;
  transition: 0.25s ease;
  border-radius: 8px;
  box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
}

.form_input:focus {
  box-shadow: inset 4px 4px 4px #d1d9e6, inset -4px -4px 4px #f9f9f9;
}

.form_span {
  margin-top: 30px;
  margin-bottom: 12px;
}

.form_link {
  color: #181818;
  font-size: 15px;
  margin-top: 25px;
  border-bottom: 1px solid #a0a5a8;
  line-height: 2;
}

.title {
  font-size: 34px;
  font-weight: 700;
  line-height: 3;
  color: #181818;
  letter-spacing: 10px;
}

.description {
  font-size: 14px;
  letter-spacing: 0.25px;
  text-align: center;
  line-height: 1.6;
}

.button {
  width: 180px;
  height: 50px;
  border-radius: 25px;
  margin-top: 50px;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
  border: none;
  outline: none;
}

.a-container {
  z-index: 100;
  left: calc(100% - 600px);
}

.b-container {
  left: calc(100% - 600px);
  z-index: 0;
}

.switch {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 400px;
  padding: 50px;
  z-index: 200;
  transition: 1.25s;
  overflow: hidden;
  box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #d1d9e6;
  border-radius: 10px;
  position: relative;
}

/* 背景图片层样式 */
.bg-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: all 0.5s ease;
  z-index: 0;
  transform: scale(1.1);
}

/* 毛玻璃层样式 - 调整后的清晰版本 */
.glass-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.08); /* 减少透明度，让背景更清晰 */
  backdrop-filter: blur(4px);          /* 减少模糊半径，从10px降到4px */
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.15); /* 调整边框透明度 */
  box-shadow:
      0 8px 32px rgba(0, 0, 0, 0.12),    /* 减少阴影强度 */
      inset 0 0 12px rgba(255, 255, 255, 0.08); /* 减少内发光强度 */
  z-index: 1;
  transition: all 0.5s ease;
  opacity: 1;
}

/* 默认状态：毛玻璃显示，背景图片放大到110% */
.switch .bg-image {
  transform: scale(1.1); /* 图片放大到110%，确保占满容器 */
}

/* 悬停状态：毛玻璃消失，背景图片缩小到100% */
.switch:hover .glass-layer {
  opacity: 0;
  backdrop-filter: blur(0);
}

.switch:hover .bg-image {
  transform: scale(1); /* 图片缩小到100%，但仍占满容器 */
}
.switch_circle {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background-color: #ecf0f3;
  box-shadow: inset 8px 8px 12px #b8bec7, inset -8px -8px 12px #fff;
  bottom: -60%;
  left: -60%;
  transition: 1.25s;
  opacity: 0.3;
}

.switch_circle-t {
  top: -30%;
  left: 60%;
  width: 300px;
  height: 300px;
  background-color: #ecf0f3; /* 添加背景色 */
  box-shadow: inset 8px 8px 12px #b8bec7, inset -8px -8px 12px #fff; /* 添加阴影 */
  opacity: 0.3;
}

.switch_container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: absolute;
  width: 400px;
  padding: 50px 55px;
  transition: 1.25s;
  z-index: 3;
}

.switch_button {
  cursor: pointer;
}

.switch_button:hover,
.submit:hover {
  box-shadow: 6px 6px 10px #d1d9e6, -6px -6px 10px #f9f9f9;
  transform: scale(0.985);
  transition: 0.25s;
}

.switch_button:active,
.switch_button:focus {
  box-shadow: 2px 2px 6px #d1d9e6, -2px -2px 6px #f9f9f9;
  transform: scale(0.97);
  transition: 0.25s;
}

.is-txr {
  left: calc(100% - 400px);
  transition: 1.25s;
  transform-origin: left;
}

.is-txl {
  left: 0;
  transition: 1.25s;
  transform-origin: right;
}

.is-z {
  z-index: 200;
  transition: 1.25s;
}

.is-hidden {
  visibility: hidden;
  opacity: 0;
  position: absolute;
  transition: 1.25s;
}

.is-gx {
  animation: is-gx 1.25s;
}

@keyframes is-gx {
  0%,
  10%,
  100% {
    width: 400px;
  }

  30%,
  50% {
    width: 500px;
  }
}

/* 浮动文字效果 - 仅应用于左侧切换面板 */
.floating-text {
  font-family: 'Inter', sans-serif;
  font-variation-settings: 'wght' 400, 'slnt' 0;
  animation: float 6s infinite ease-in-out;
  transform-style: preserve-3d;
  backface-visibility: hidden;
}

/* 标题浮动动画 */
@keyframes float {
  0% {
    transform: translate3d(0, 0, 0) rotateX(0deg) rotateY(0deg);
    font-variation-settings: 'wght' 400, 'slnt' 0;
    text-shadow: 0 0 0 rgba(0, 0, 0, 0.1);
  }
  25% {
    transform: translate3d(0, -5px, 5px) rotateX(5deg) rotateY(5deg);
    font-variation-settings: 'wght' 600, 'slnt' -5;
    text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
  }
  50% {
    transform: translate3d(0, 0, 10px) rotateX(0deg) rotateY(10deg);
    font-variation-settings: 'wght' 500, 'slnt' 0;
    text-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
  }
  75% {
    transform: translate3d(0, 5px, 5px) rotateX(-5deg) rotateY(5deg);
    font-variation-settings: 'wght' 600, 'slnt' 5;
    text-shadow: -2px 2px 5px rgba(0, 0, 0, 0.1);
  }
  100% {
    transform: translate3d(0, 0, 0) rotateX(0deg) rotateY(0deg);
    font-variation-settings: 'wght' 400, 'slnt' 0;
    text-shadow: 0 0 0 rgba(0, 0, 0, 0.1);
  }
}

/* 为左侧切换面板的不同元素设置不同的动画延迟，创造波浪效果 */
.switch_title.floating-text {
  animation-delay: 0s;
}

.switch_description.floating-text {
  animation-delay: 0.3s;
}

.switch_button.floating-text {
  animation-delay: 0.5s;
}

/* 为右侧表单容器应用毛玻璃效果 */
.container .form {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 30px; /* 增加内边距 */
  display: flex;
  flex-direction: column;
  gap: 20px; /* 使用gap统一控制子元素间距 */
}

/* 优化输入框样式 */
.form_input {
  width: 100%; /* 让输入框宽度适应容器 */
  height: 45px; /* 统一高度 */
  padding-left: 15px;
  font-size: 14px;
  letter-spacing: 0.15px;
  border: none;
  outline: none;
  background-color: rgba(236, 240, 243, 0.6); /* 轻微半透的背景 */
  border-radius: 8px;
  box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
  transition: all 0.3s ease;
}

/* 输入框交互效果 */
.form_input:hover {
  transform: scale(1.02);
  background-color: rgba(236, 240, 243, 0.8);
}

.form_input:focus {
  transform: scale(1.02);
  box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9, 0 0 0 2px rgba(76, 112, 226, 0.5);
}

/* 优化按钮样式与交互 */
.form_button {
  width: 100%;
  height: 50px;
  border-radius: 25px;
  margin-top: 20px; /* 调整顶部间距 */
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  border: none;
  outline: none;
  box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
  cursor: pointer;
  transition: all 0.3s ease;
}

.form_button:hover {
  transform: scale(1.05);
  box-shadow: 6px 6px 12px #d1d9e6, -6px -6px 12px #f9f9f9;
}

.form_button:active {
  transform: scale(0.98);
  box-shadow: 2px 2px 6px #d1d9e6, -2px -2px 6px #f9f9f9;
}

/* 现代错误提示样式 */
.error_message {
  color: #d93025;
  font-size: 14px;
  margin: 12px 0;
  padding: 12px 16px;
  background: linear-gradient(135deg, #fff5f5 0%, #ffeaea 100%);
  border-radius: 12px;
  border: 1.5px solid #ffcdd2;
  text-align: center;
  min-height: 20px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  letter-spacing: 0.3px;
  box-shadow:
      0 2px 8px rgba(217, 48, 37, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.6);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

/* 错误图标 */
.error_message::before {
  content: "⚠";
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #d93025, #ea4335);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  flex-shrink: 0;
}

/* 微妙的闪烁动画 */
@keyframes gentlePulse {
  0%, 100% {
    box-shadow:
        0 2px 8px rgba(217, 48, 37, 0.08),
        inset 0 1px 0 rgba(255, 255, 255, 0.6);
  }
  50% {
    box-shadow:
        0 4px 12px rgba(217, 48, 37, 0.12),
        inset 0 1px 0 rgba(255, 255, 255, 0.8);
  }
}

/* 错误信息出现时的动画 */
.error_message {
  animation: gentlePulse 2s ease-in-out infinite,
  slideInUp 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(8px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 悬浮效果 */
.error_message:hover {
  transform: translateY(-1px);
  box-shadow:
      0 4px 16px rgba(217, 48, 37, 0.12),
      inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .error_message {
    font-size: 13px;
    padding: 10px 12px;
    margin: 8px 0;
  }
}

/* 确保错误信息在表单中的正确位置 */
.form {
  position: relative;
  z-index: 10;
}

/* 固定高度的错误信息容器 */
.error-container {
  height: 0; /* 容器高度为0，不占布局空间 */
  position: relative; /* 为绝对定位的错误信息提供参考 */
  margin: 8px 0; /* 保持原有的间距 */
  min-height: 0; /* 确保不会扩展 */
}

/* 现代错误提示样式 - 修改为绝对定位 */
.error_message {
  color: #d93025;
  font-size: 14px;
  padding: 12px 16px;
  background: linear-gradient(135deg, #fff5f5 0%, #ffeaea 100%);
  border-radius: 12px;
  border: 1.5px solid #ffcdd2;
  text-align: center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  letter-spacing: 0.3px;
  box-shadow:
      0 2px 8px rgba(217, 48, 37, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.6);
  position: absolute; /* 改为绝对定位 */
  top: 0;
  left: 0;
  right: 0;
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transform: translateY(-10px);
  pointer-events: none;
  z-index: 10;
}

/* 显示状态 */
.error_message.show {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

/* 错误图标 */
.error_message::before {
  content: "⚠";
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #d93025, #ea4335);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  flex-shrink: 0;
}

/* 微妙的闪烁动画 */
@keyframes gentlePulse {
  0%, 100% {
    box-shadow:
        0 2px 8px rgba(217, 48, 37, 0.08),
        inset 0 1px 0 rgba(255, 255, 255, 0.6);
  }
  50% {
    box-shadow:
        0 4px 12px rgba(217, 48, 37, 0.12),
        inset 0 1px 0 rgba(255, 255, 255, 0.8);
  }
}

/* 错误信息出现时的动画 */
.error_message.show {
  animation: gentlePulse 2s ease-in-out infinite;
}

/* 悬浮效果 */
.error_message:hover {
  transform: translateY(-1px);
  box-shadow:
      0 4px 16px rgba(217, 48, 37, 0.12),
      inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .error_message {
    font-size: 13px;
    padding: 10px 12px;
  }
}

/* 为错误信息添加微妙的背景光晕 */
.error_message::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at center, rgba(217, 48, 37, 0.03) 0%, transparent 70%);
  border-radius: 12px;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.error_message:hover::after {
  opacity: 1;
}

/* 确保表单布局稳定 */
.form {
  position: relative;
  z-index: 10;
  display: flex;
  flex-direction: column;
  gap: 16px; /* 使用 gap 统一控制间距 */
}

/* 调整输入框和按钮的间距 */
.form_input {
  width: 100%;
  height: 45px;
  margin: 0;
  /* 其他样式保持不变 */
}

.form_button {
  margin-top: 0;
}
</style>