<template>
  <div class="mx-auto w-full max-w-4xl text-left px-4">
    <!-- 整体容器 -->
    <div
        class="relative bg-white/70 dark:bg-zinc-900/70 backdrop-blur-xl rounded-3xl p-8 shadow-2xl border border-white/20 dark:border-zinc-700/30"
    >
      <!-- 头像区域 -->
      <div
          class="relative mb-6"
          style="--av-size: 200px; --av-gap: 8px; height: calc(var(--av-size) + var(--av-gap)*2)"
      >
        <div
            class="pointer-events-none absolute left-1/2 top-0 -translate-x-1/2"
            style="width:520px;height:calc(var(--av-size) + var(--av-gap)*2)"
        >
          <!-- 发光背景 -->
          <div
              class="absolute inset-0 rounded-full bg-gradient-to-b from-purple-400/20 via-pink-300/10 to-transparent blur-xl"/>

          <div
              class="flex h-full items-start justify-center [mask-image:_linear-gradient(0deg,transparent,theme(colors.white)_30%,theme(colors.white))]">
            <transition name="testimonial-image">
              <div
                  :key="active"
                  class="absolute left-1/2 -translate-x-1/2 avatar-pos"
              >
                <div class="relative">
                  <div
                      class="absolute inset-0 bg-gradient-to-r from-purple-400 to-pink-400 rounded-full blur-sm scale-110"/>
                  <div
                      class="relative rounded-full border-4 border-white/80 dark:border-zinc-800/80 shadow-2xl avatar-container">
                    <img
                        :src="testimonials[active].img"
                        :alt="testimonials[active].name"
                        class="avatar-img"
                    >
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </div>

      <!-- 引用文字区域 -->
      <div class="mb-8 transition-all duration-500 ease-out">
        <div
            ref="testimonialsRef"
            class="relative flex flex-col items-center"
        >
          <transition-group name="testimonial-text">
            <div
                v-for="(testimonial, index) in testimonials"
                v-show="active === index"
                :key="`text-${index}`"
                class="w-full text-center"
            >
              <!-- 引用符号装饰 -->
              <div class="text-6xl text-purple-300/40 dark:text-pink-400/30 mb-2 leading-none">
                "
              </div>
              <div
                  class="text-2xl md:text-3xl font-light text-zinc-800 dark:text-zinc-200 leading-relaxed italic px-4"
              >
                {{ testimonial.quote }}
              </div>
            </div>
          </transition-group>
        </div>
      </div>

      <!-- 底部控制区域 -->
      <div class="flex items-center justify-between pt-6 border-t border-zinc-200/50 dark:border-zinc-700/50">
        <!-- 左侧箭头 -->
        <button
            class="group/button flex size-12 items-center justify-center rounded-2xl bg-white/80 dark:bg-zinc-800/80 backdrop-blur-sm border border-zinc-200/60 dark:border-zinc-600/60 shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-110 hover:bg-purple-50 dark:hover:bg-purple-900/20"
            @click="handlePrev"
        >
          <i class="iconfont icon-shangyige text-zinc-600 dark:text-zinc-400 text-lg group-hover/button:-translate-x-0.5 transition-transform duration-300"/>
        </button>

        <!-- 中央信息区域 -->
        <div class="flex flex-col items-center gap-3 flex-1 px-8">
          <!-- 角色名称 -->
          <div class="text-center">
            <div class="text-xl font-semibold text-zinc-900 dark:text-zinc-100 mb-1">
              {{ testimonials[active]?.name }}
            </div>
            <div class="text-sm text-zinc-500 dark:text-zinc-400 font-light">
              点击召唤角色
            </div>
          </div>

          <!-- 召唤按钮 -->
          <button
              v-for="(testimonial, index) in testimonials"
              v-show="active === index"
              :key="`indicator-${testimonial.id}`"
              class="group/action relative overflow-hidden bg-gradient-to-r from-purple-500 to-pink-500 text-white px-8 py-3 rounded-2xl font-medium shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 hover:from-purple-600 hover:to-pink-600"
              @click="handleButtonClick(testimonial.id)"
          >
            <span class="relative z-10 flex items-center gap-2">
              <i class="iconfont icon-sparkles text-white text-lg group-hover/action:animate-pulse"/>
              我想见她!
            </span>
            <!-- 按钮 hover 效果 -->
            <div
                class="absolute inset-0 bg-gradient-to-r from-white/20 to-transparent opacity-0 group-hover/action:opacity-100 transition-opacity duration-300"
            />
          </button>
        </div>

        <!-- 右侧箭头 -->
        <button
            class="group/button flex size-12 items-center justify-center rounded-2xl bg-white/80 dark:bg-zinc-800/80 backdrop-blur-sm border border-zinc-200/60 dark:border-zinc-600/60 shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-110 hover:bg-purple-50 dark:hover:bg-purple-900/20"
            @click="handleNext"
        >
          <i class="iconfont icon-next text-zinc-600 dark:text-zinc-400 text-lg group-hover/button:translate-x-0.5 transition-transform duration-300"/>
        </button>
      </div>

      <!-- 底部指示器 -->
      <div class="flex justify-center gap-2 mt-6">
        <button
            v-for="(testimonial, index) in testimonials"
            :key="`dot-${index}`"
            class="group/dot flex items-center justify-center"
            @click="setActiveIndex(index)"
        >
          <div
              class="size-2 rounded-full transition-all duration-300"
              :class="[
              active === index
                ? 'bg-gradient-to-r from-purple-500 to-pink-500 w-8'
                : 'bg-zinc-300 dark:bg-zinc-600 hover:bg-zinc-400 dark:hover:bg-zinc-500 w-2'
            ]"
          />
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, onUnmounted} from "vue";

interface Testimonial {
  img: string;
  quote: string;
  name: string;
  id: number;
}

const props = withDefaults(
    defineProps<{
      testimonials?: Testimonial[];
      autoRotate?: boolean;
      duration?: number;
    }>(),
    {
      autoRotate: true,
      duration: 5,
      testimonials: () => [],
    },
);

const emit = defineEmits<{
  'model-change': [number]
}>();

const active = ref<number>(0);
const autorotate = ref(props.autoRotate);
const testimonialsRef = ref<HTMLElement | null>(null);
let intervalId: number | null = null;

function heightFix() {
  if (testimonialsRef.value && testimonialsRef.value.parentElement) {
    testimonialsRef.value.parentElement.style.height = `${testimonialsRef.value.clientHeight}px`;
  }
}

function setActiveIndex(index: number) {
  active.value = index;
  autorotate.value = false;
  resetAutorotate();
}

function startAutorotate() {
  intervalId = window.setInterval(() => {
    active.value = active.value + 1 === props.testimonials.length ? 0 : active.value + 1;
    heightFix();
  }, props.duration * 1000);
}

function resetAutorotate() {
  if (intervalId) {
    clearInterval(intervalId);
  }
  if (autorotate.value) {
    startAutorotate();
  }
}

function handleNext() {
  setActiveIndex((active.value + 1) % props.testimonials.length);
}

function handlePrev() {
  setActiveIndex((active.value - 1 + props.testimonials.length) % props.testimonials.length);
}

function handleButtonClick(index: number) {
  console.log("第一层触发");
  emit('model-change', index);
}

onMounted(() => {
  heightFix();
  if (autorotate.value) {
    startAutorotate();
  }
});

onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId);
  }
});
</script>

<style>
.testimonial-image-enter-active {
  transition: all 700ms cubic-bezier(0.34, 1.56, 0.64, 1);
}

.testimonial-image-leave-active {
  transition: all 700ms cubic-bezier(0.34, 1.56, 0.64, 1);
}

.testimonial-image-enter-from {
  opacity: 0;
  transform: scale(0.8);
}

.testimonial-image-enter-to {
  opacity: 1;
  transform: scale(1);
}

.testimonial-image-leave-from {
  opacity: 1;
  transform: scale(1);
}

.testimonial-image-leave-to {
  opacity: 0;
  transform: scale(1.2);
}

/* 文字过渡动画保持不变 */
.testimonial-text-enter-active {
  transition: all 600ms cubic-bezier(0.25, 0.46, 0.45, 0.94) 200ms;
}

.testimonial-text-leave-active {
  transition: all 400ms cubic-bezier(0.55, 0.085, 0.68, 0.53);
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

.testimonial-text-enter-from {
  opacity: 0;
  transform: translateY(1rem) scale(0.95);
}

.testimonial-text-enter-to {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.testimonial-text-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.testimonial-text-leave-to {
  opacity: 0;
  transform: translateY(-1rem) scale(0.95);
}

.avatar-img {
  width: var(--av-size);
  height: var(--av-size);
  object-fit: cover;
  border-radius: 50%;
}

.avatar-pos {
  top: var(--av-gap);
}

.avatar-container {
  width: var(--av-size);
  height: var(--av-size);
  border-radius: 50%;
}
</style>