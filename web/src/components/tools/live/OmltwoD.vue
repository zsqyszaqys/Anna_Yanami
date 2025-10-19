<template>
  <div ref="oml2dContainer" class="live2d-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { loadOml2d } from 'oh-my-live2d';

// 定义组件 Props
interface Props {
  modelPath?: string;// 模型 JSON 路径
  scale?: number; // 模型缩放
  position?: [number, number];// 模型在舞台的位置
  stageHeight?: number; // 舞台高度
  tipsOffsetY?: number; // 提示消息偏移
  autoShowTips?: boolean;// 是否自动显示提示
}

const props = withDefaults(defineProps<Props>(), {
  modelPath: 'https://model.oml2d.com/Senko_Normals/senko.model3.json',
  scale: 0.08,
  position: () => [0, 60] as [number, number],
  stageHeight: 450,
  tipsOffsetY: 40,
  autoShowTips: true
});

// 定义组件 Emits
const emit = defineEmits<{
  stageSlideIn: [];
  modelLoad: [];
  tipsShow: [message: string];
}>();

// 容器引用和实例
const oml2dContainer = ref<HTMLElement>();
let oml2dInstance: any = null;

// 初始化 Live2D
const initLive2D = () => {
  if (!oml2dContainer.value) return;

  try {
    oml2dInstance = loadOml2d({
      container: oml2dContainer.value,
      models: [
        {
          path: props.modelPath,
          scale: props.scale,
          position: props.position,
          stageStyle: {
            height: props.stageHeight
          }
        }
      ],
      tips: {
        style: {
          offsetY: props.tipsOffsetY
        }
      }
    });

    // 事件监听
    oml2dInstance.onStageSlideIn(() => {
      emit('stageSlideIn');

      if (props.autoShowTips) {
        showTips("欢迎来到我的网站！");
      }
    });

    oml2dInstance.onModelLoad(() => {
      emit('modelLoad');
    });

  } catch (error) {
    console.error('Live2D 初始化失败:', error);
  }
};

// 显示提示消息
const showTips = (message: string) => {
  if (oml2dInstance && oml2dInstance.tipsMessage) {
    oml2dInstance.tipsMessage(message);
    emit('tipsShow', message);
  }
};

// 切换模型
const switchModel = (modelPath: string, config?: any) => {
  if (oml2dInstance && oml2dInstance.switchModel) {
    oml2dInstance.switchModel(modelPath, config);
  }
};

// 显示/隐藏模型
const showModel = () => {
  if (oml2dInstance && oml2dInstance.show) {
    oml2dInstance.show();
  }
};

const hideModel = () => {
  if (oml2dInstance && oml2dInstance.hide) {
    oml2dInstance.hide();
  }
};

// 组件挂载时初始化
onMounted(() => {
  initLive2D();
});

// 组件卸载时清理
onUnmounted(() => {
  if (oml2dInstance && oml2dInstance.destroy) {
    oml2dInstance.destroy();
  }
});

// 暴露方法给父组件
defineExpose({
  showTips,
  switchModel,
  showModel,
  hideModel
});
</script>

<style scoped>
.live2d-container {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 9999;
  width: 300px;
  height: 400px;
  pointer-events: none;
}

/* 确保 Live2D 画布可以交互 */
.live2d-container :deep(canvas) {
  pointer-events: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .live2d-container {
    right: 10px;
    bottom: 10px;
    width: 200px;
    height: 300px;
    transform: scale(0.8);
    transform-origin: bottom right;
  }
}
</style>