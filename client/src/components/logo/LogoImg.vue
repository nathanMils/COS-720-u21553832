<template>
    <img v-if="isDark" src="/src/assets/logoDark.svg" alt="Missing" :style="{ width: `${size}rem`, height: `${size}rem`, marginRight: `${mr}rem`}">
    <img v-else src="/src/assets/logoLight.svg" alt="Missing" :style="{ width: `${size}rem`, height: `${size}rem`, marginRight: `${mr}rem`}">
</template>

<script setup lang="ts">
import { useDark } from '@vueuse/core';
const isDark = useDark();

defineProps({
    size: {
        type: Number,
        default: 3
    },
    mr: {
      type: Number,
      default: 0
    }
});
</script>