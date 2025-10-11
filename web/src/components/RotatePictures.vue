<template>
  <div class="Rotate-shell">
    <div class="content" :style="{ transform: `translateZ(-35vw) rotateY(${currentAngle}deg)` }">
      <div class="item"></div>
      <div class="item"></div>
      <div class="item"></div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue"

export default {
  setup(props, { expose }) {
    const currentAngle = ref(0)
    const step = 120

    function next() {
      currentAngle.value -= step
    }
    function prev() {
      currentAngle.value += step
    }

    // 暴露给父组件
    expose({ next, prev })

    return { currentAngle } // 需要返回给模板使用
  }
}
</script>

<style scoped>
.Rotate-shell {
  position: relative;
  width: 60vw;
  height: 40vw;
  max-width: 380px;
  max-height: 250px;
  margin: 0 auto;
  color: white;
  perspective: 1000px;
  transform-origin: center;
}

.content {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 100%;
  transform-origin: center;
  transform-style: preserve-3d;
  transition: transform 1s;
}

.item {
  position: absolute;
  width: 60vw;
  height: 40vw;
  max-width: 380px;
  max-height: 250px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  border-radius: 6px;
  background-size: cover;
  background-position: center;
  -webkit-box-reflect: below 25px -webkit-linear-gradient(transparent 50%, rgba(255, 255, 255, 0.3));
}

.item:nth-child(1) {
  background-image: url(~@/assets/AboutMe/QRcode/QQ_Qrcode.png);
  transform: rotateY(0deg) translateZ(35vw);
}

.item:nth-child(2) {
  background-image: url(~@/assets/AboutMe/QRcode/Douyin_Qrcode.png);
  transform: rotateY(120deg) translateZ(35vw);
}

.item:nth-child(3) {
  background-image: url(~@/assets/AboutMe/QRcode/Wechat_Qrcode.png);
  transform: rotateY(240deg) translateZ(35vw);
}
</style>
