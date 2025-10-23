<template>
  <div
    ref="lightSpeedRef"
    class="w-full h-full overflow-hidden block"
  />
</template>

<script lang="ts" setup>
import type { LightSpeedOptions, LightSpeedProps } from "@/components/tools/Light/LightSpeedApp";
import { defaultOptions, distortions, LightSpeedApp } from "@/components/tools/Light/LightSpeedApp";
import {onMounted} from "vue";
import {useTemplateRef} from "vue";

const props = defineProps<LightSpeedProps>();

const containerRef = useTemplateRef("lightSpeedRef");

onMounted(() => {
  if (!containerRef.value) return;

  const mergedOptions: LightSpeedOptions = {
    ...defaultOptions,
    ...(props.effectOptions || {}),
  };

  if (typeof mergedOptions.distortion === "string") {
    mergedOptions.distortion = distortions[mergedOptions.distortion];
  }

  const lightSpeedApp = new LightSpeedApp(containerRef.value, mergedOptions);
  lightSpeedApp.loadAssets().then(lightSpeedApp.init);
});
</script>