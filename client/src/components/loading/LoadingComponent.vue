<template>
  <div
    class="inline-block animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-green-500 motion-reduce:animate-[spin_1.5s_linear_infinite]"
    :style="{ width: `${size}rem`, height: `${size}rem` }"
    role="status">
  <span
    class="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"
  >Loading...</span
  >
  </div>
</template>

<script setup lang="ts">
defineProps({
  size: {
    type: Number,
    default: 6
  }
})
</script>