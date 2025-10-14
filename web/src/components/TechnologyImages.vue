<template>
  <div class="technology-shell">
    <div class="shell_body">
      <div class="button">
        <div class="prev" @click="prev"><i class="iconfont icon-backward_filled"></i></div>
        <div class="next" @click="next"><i class="iconfont icon-forward_filled"></i></div>
      </div>

      <div class="shell_slider" ref="slider">
        <div class="item" v-for="(year, index) in years" :key="index">
          <div class="frame">
            <div class="box front">
              <h1>{{ year }}</h1>
              <span>-In the year {{ year }} I reached the age of {{ 13 + (year - 2014) }}-</span>
            </div>
            <div class="box left"></div>
            <div class="box right"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";

export default {
  name: "TechnologySlider",
  setup() {
    const slider = ref(null);
    const years = Array.from({ length: 10 }, (_, i) => 2014 + i);
    const items = ref([]);
    const currIndex = ref(0);
    const interval = ref(null);
    const intervalTime = 3000;
    let width = 0;
    let height = 0;
    const margin = 20;

    const resize = () => {
      width = Math.max(window.innerWidth * 0.2, 275);
      height = window.innerHeight * 0.5;
      if (!slider.value) return;
      const itemElements = slider.value.children;
      const totalWidth = width * itemElements.length;
      slider.value.style.width = totalWidth + "px";
      for (let i = 0; i < itemElements.length; i++) {
        const item = itemElements[i];
        item.style.width = width - margin * 2 + "px";
        item.style.height = height + "px";
      }
    };

    const move = (index) => {
      const itemElements = slider.value.children;
      if (index < 1) index = itemElements.length;
      if (index > itemElements.length) index = 1;
      currIndex.value = index;

      for (let i = 0; i < itemElements.length; i++) {
        const item = itemElements[i];
        const box = item.querySelector(".frame");
        if (i === index - 1) {
          item.classList.add("item--active");
          box.style.transform = "perspective(1200px)";
        } else {
          item.classList.remove("item--active");
          box.style.transform =
              "perspective(1200px) rotateY(" + (i < index - 1 ? 40 : -40) + "deg)";
        }
      }

      slider.value.style.transform =
          "translate3d(" +
          (index * -width + width / 2 + window.innerWidth / 2) +
          "px, 0, 0)";

      const frontBox = itemElements[index - 1].querySelector(".front");
      document.body.style.backgroundImage = frontBox.style.backgroundImage;
    };

    const timer = () => {
      clearInterval(interval.value);
      interval.value = setInterval(() => {
        move(currIndex.value + 1);
      }, intervalTime);
    };

    const prev = () => {
      move(currIndex.value - 1);
      timer();
    };

    const next = () => {
      move(currIndex.value + 1);
      timer();
    };

    onMounted(() => {
      items.value = slider.value.children;
      for (let i = 0; i < items.value.length; i++) {
        const frame = items.value[i].querySelector(".frame");
        const frontBox = frame.querySelector(".front");
        const leftBox = frame.querySelector(".left");
        const rightBox = frame.querySelector(".right");
        const imgIndex = String(i + 1).padStart(2, "0");
        frontBox.style.backgroundImage = `url(./img/${imgIndex}.jpg)`;
        leftBox.style.backgroundImage = `url(./img/${imgIndex}.jpg)`;
        rightBox.style.backgroundImage = `url(./img/${imgIndex}.jpg)`;
      }
      resize();
      move(Math.floor(items.value.length / 2));
      window.addEventListener("resize", resize);
      timer();
    });

    return { years, slider, prev, next };
  },
};
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  font-family: "Source Sans Pro", sans-serif;
}

.technology-shell{
  position: relative;
  width: 1000px;
  min-width: 1000px; min-height: 600px;
  height: 600px; padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 12px; overflow: hidden;
}
.button {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 380px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  bottom: -80px;
}

.prev,
.next {
  transition: transform 0.25s ease;
  z-index: 99999;
  bottom: 5px;
}

.prev i,
.next i {
  font-size: 90px;
  color: #fff;
  cursor: pointer;
  text-shadow: 0 0 10px #ffffff;
}

.shell_body {
  width: 100%;
  transform: scale(0.8);
  padding: 20px 0 150px 0;
}

.shell_slider {
  position: relative;
  transition: transform 1s ease-in-out;
  background: transparent;
  display: flex;
}

.item {
  position: relative;
  float: left;
  margin: 0 20px;
}

.frame {
  position: relative;
  width: 100%;
  height: 100%;
  transition: transform 1s ease-in-out;
  transform-style: preserve-3d;
}

.frame:after {
  content: "";
  position: absolute;
  bottom: -16%;
  width: 100%;
  height: 60px;
  background: #ffffff1c;
  box-shadow: 0px 0px 15px 5px #ffffff1c;
  transform: rotateX(90deg) translate3d(0px, -20px, 0px);
}

.box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 100%;
  border: 4px solid #fff;
  perspective: 1000px;
  transform-style: preserve-3d;
}

.box h1,
.box span {
  color: #fff;
  transform: translateZ(20px);
}

.box h1 {
  text-shadow: 0 0 30px #1f05b4;
  font-size: 100px;
}

.box span {
  position: absolute;
  bottom: 20px;
  padding: 0 25px;
  text-shadow: 0 0 10px #1f05b4;
}

.front,
.left,
.right {
  box-shadow: 0 0 50px #ffffff;
  background-size: cover;
}

.right,
.left {
  top: 0;
  width: 60px;
  backface-visibility: hidden;
}

.left {
  left: 0;
  border-left-width: 5px;
  transform: translate3d(1px, 0, -60px) rotateY(-90deg);
  transform-origin: 0%;
}

.right {
  right: 0;
  border-right-width: 5px;
  transform: translate3d(-1px, 0, -60px) rotateY(90deg);
  transform-origin: 100%;
}
</style>
