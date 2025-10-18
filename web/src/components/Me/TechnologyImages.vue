<template>
  <div class="technology-shell">
    <div class="shell_body">
      <div class="button">
        <div
          class="prev"
          @click="prev"
        >
          <i class="iconfont icon-backward_filled" />
        </div>
        <div
          class="next"
          @click="next"
        >
          <i class="iconfont icon-forward_filled" />
        </div>
      </div>

      <div
        ref="slider"
        class="shell_slider"
      >
        <div
          v-for="(tech, index) in Technology"
          :key="index"
          class="item"
        >
          <div class="frame">
            <div class="box front">
              <h1>{{ tech }}</h1>
              <span>
                {{ description[index] }}
              </span>
            </div>
            <div class="box left" />
            <div class="box right" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {onMounted, onUnmounted, ref} from "vue";

export default {
  name: "TechnologySlider",
  setup() {
    const slider = ref(null);
    //const years = Array.from({ length: 10 }, (_, i) => 2014 + i);
    const Technology = ["C/C++", "JAVA", "PYTHON", "SPRING\nBOOT", "DOCKER", "GIT", "THRIFT", "LINUX", "Django", "MYSQL"]
    const description = [
        "C++ is a high-level, general-purpose programming language ",
        "Java is a high-level, general-purpose, memory-safe, object-oriented programming language.",
        "Python is dynamically type-checked and garbage-collected.",
        "Spring Boot is an open-source Java framework ",
        "Docker is a technology that bundles a software program with all the other software",
        "Git is a distributed version control software system.",
        "It uses a remote procedure call (RPC) framework and combines a software stack.",
        "Linux is a family of open source Unix-like operating systems based on the Linux kernel.",
        "Django is a free and open-source, Python-based web framework that runs on a web server.",
        "MySQL is an open-source relational database management system.",
    ]
    const items = ref([]);
    const currIndex = ref(0);
    const interval = ref(null);
    const intervalTime = 3000;
    let width = 0;
    let height = 0;
    const margin = 20;

    const handleKeydown = (e) => {
      if (e.key === "ArrowLeft") prev();
      else if (e.key === "ArrowRight") next();
    };

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

      // 重新定位到当前索引
      move(currIndex.value);
    };

    const move = (index) => {
      if (!slider.value) return;

      const itemElements = slider.value.children;
      if (index < 0) index = itemElements.length - 1;
      if (index >= itemElements.length) index = 0;
      currIndex.value = index;

      for (let i = 0; i < itemElements.length; i++) {
        const item = itemElements[i];
        const box = item.querySelector(".frame");
        if (i === index) {
          item.classList.add("item--active");
          box.style.transform = "perspective(1200px)";
        } else {
          item.classList.remove("item--active");
          box.style.transform =
              "perspective(1200px) rotateY(" + (i < index ? 40 : -40) + "deg)";
        }
      }

      // 关键修改：计算居中的位置
      const containerWidth = window.innerWidth;
      const itemWidth = width;
      //const totalItems = itemElements.length;

      // 计算居中的位置：容器宽度的一半减去项目宽度的一半
      const centerPosition = (containerWidth - itemWidth) / 2;
      // 当前项目应该偏移的位置
      const offset = centerPosition - (index * itemWidth);

      slider.value.style.transform = `translate3d(${offset}px, 0, 0)`;

      // 背景设置
      const frontBox = itemElements[index].querySelector(".front");
      const container = document.getElementById('technology');

      if (container && frontBox.style.backgroundImage) {
        container.style.backgroundImage = frontBox.style.backgroundImage;
        container.style.backgroundSize = 'cover';
        container.style.backgroundPosition = 'center';
        container.style.backgroundRepeat = 'no-repeat';
      } else {
        console.log("背景设置失败")
      }
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
        frontBox.style.backgroundImage = `url(${require(`@/assets/AboutMe/TechnologyImages/${imgIndex}.jpg`)})`;
        leftBox.style.backgroundImage = `url(${require(`@/assets/AboutMe/TechnologyImages/${imgIndex}.jpg`)})`;
        rightBox.style.backgroundImage = `url(${require(`@/assets/AboutMe/TechnologyImages/${imgIndex}.jpg`)})`;
      }

      window.addEventListener("keydown", handleKeydown);
      resize();

      // 关键修改：初始化时显示中间的项目
      const middleIndex = Math.floor(items.value.length / 2);
      move(middleIndex);

      window.addEventListener("resize", resize);
      timer();
    });

    onUnmounted(() => {
      window.removeEventListener("keydown", handleKeydown);
      window.removeEventListener("resize", resize);
      clearInterval(interval.value);
    });

    return {
      slider,
      prev,
      next,
      Technology,
      description,
    };
  },
};
</script>

<style scoped>
/* 样式部分保持不变 */
* {
  padding: 0;
  margin: 0;
  font-family: "Source Sans Pro", sans-serif;
}

.technology-shell{
  position: relative;
  width: 100%;
  height: 100%;
  min-width: 1000px;
  min-height: 600px;
  padding: 25px;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 12px;
  overflow: hidden;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0.7;

  display: flex;
  justify-content: center;
  align-items: center;
}

#technology {
  position: relative;
  width: 100%;
  height: 100%; /* 确保有足够的高度 */
  display: block;
  padding: 0;
  overflow: visible;
  z-index: 5;
  background-size: cover !important;
  background-position: center !important;
  background-repeat: no-repeat !important;
  /* 移除可能干扰背景显示的样式 */
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
  transition: all 0.5s ease;
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
  font-size: 24px;
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