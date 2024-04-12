<template>
    <img v-if="isDark" src="/src/assets/logoDark.svg" alt="Missing" class="mr-4" :style="{ width: `${size}rem`, height: `${size}rem`}">
    <img v-else src="/src/assets/logoLight.svg" alt="Missing" class="mr-4" :style="{ width: `${size}rem`, height: `${size}rem`}">
</template>

<script setup lang="ts">
import { useDark } from '@vueuse/core';
const isDark = useDark();

defineProps({
    size: {
        type: Number,
        default: 3
    }
});
</script>