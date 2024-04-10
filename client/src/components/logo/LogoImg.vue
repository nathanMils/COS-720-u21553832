<template>
    <img :src="isDark ? darkModeImageUrl : lightModeImageUrl" alt="Missing" class="mx-4" :style="{ width: `${size}rem`, height: `${size}rem`}">
</template>

<script setup lang="ts">
import { useDark } from '@vueuse/core';
import { defineProps } from 'vue';
const isDark = useDark();

defineProps({
    size: {
        type: Number,
        default: 3
    }
});

const darkModeImageUrl = '/src/assets/logoDark.svg';
const lightModeImageUrl = '/src/assets/logoLight.svg';
</script>