<template>
  <!-- 移除外层容器，只保留按钮 -->
  <a href="#" class="water-button" :class="customClass">
    <slot>Button</slot>
  </a>
</template>

<script>
export default {
  name: "WaterButton",
  props: {
    customClass: {
      type: String,
      default: ''
    }
  }
};
</script>

<style scoped>
/* 只保留按钮核心样式，移除容器和固定尺寸 */
.water-button {
  display: inline-block;
  position: relative;
  z-index: 1;
  overflow: hidden;
  text-decoration: none;
  font-family: sans-serif;
  font-weight: 600;
  font-size: 14px; /* 改为固定大小或使用em相对单位 */
  padding: 12px 24px; /* 固定内边距 */
  color: blue;
  border: 0.15em solid blue;
  border-radius: 2em;
  transition: 4s;
  /* 添加基础尺寸，但允许外部覆盖 */
  min-width: 80px;
  text-align: center;
}

.water-button:before,
.water-button:after {
  content: "";
  position: absolute;
  top: -1.5em;
  z-index: -1;
  width: 200%;
  aspect-ratio: 1;
  border: none;
  border-radius: 40%;
  background-color: rgba(0, 0, 255, 0.25);
  transition: 4s;
}

.water-button:before {
  left: -80%;
  transform: translate3d(0, 5em, 0) rotate(-340deg);
}

.water-button:after {
  right: -80%;
  transform: translate3d(0, 5em, 0) rotate(390deg);
}

.water-button:hover,
.water-button:focus {
  color: white;
}

.water-button:hover:before,
.water-button:hover:after,
.water-button:focus:before,
.water-button:focus:after {
  transform: none;
  background-color: rgba(0, 0, 255, 0.75);
}
</style>