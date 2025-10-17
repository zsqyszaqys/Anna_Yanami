<template>
  <div class="video-hover-shell" @mouseenter="onEnter" @mousemove="onMove" @mouseleave="onLeave">
    <video
        ref="video"
        loop
        autoplay
        muted
        class="video"
        :src="videoSrc"
        style="height: 100%; transform: translate(36px, -8px);"
    ></video>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 视频路径（你可以改成 @ 路径或外链）
const videoSrc = '@/assets/video/81909048-c89a-4106-880c-175d52c70140.mp4'

const video = ref(null)
const startX = ref(0)

function onEnter(e) {
  startX.value = e.clientX
  video.value.style.transition = 'none'
}

function onMove(e) {
  const disx = e.clientX - startX.value
  const move = 36 - disx / -20
  video.value.style.transform = `translate(${move}px, -8px)`
}

function onLeave() {
  video.value.style.transition = '.3s'
  video.value.style.transform = 'translate(36px, -8px)'
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
}

body {
  display: flex;
  justify-content: center;
  overflow: hidden;
}

.video-hover-shell {
  width: 100%;
  height: 162px;
  display: flex;
  justify-content: center;
}

.video {
  object-fit: cover;
}
</style>
