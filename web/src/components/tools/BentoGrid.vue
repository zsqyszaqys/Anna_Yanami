<template>
  <div
      :class="
      cn(
        'grid w-full max-w-none gap-4 grid-cols-1 sm:grid-cols-3 xl:grid-cols-4 auto-rows-[16rem] md:auto-rows-[18rem] xl:auto-rows-[22rem]',
        props.class,
      )
    "
  >
    <slot />
  </div>
</template>

<script lang="ts" setup>
import { cn } from "@/lib/utils";
import type { HTMLAttributes } from "vue";

interface Props {
  class?: HTMLAttributes["class"];
}

const props = defineProps<Props>();
</script>