<template>
    <div class="flex justify-center items-center h-screen">
    <label for="darkModeToggle" class="flex items-center cursor-pointer">
      <div class="relative">
        <input type="checkbox" id="darkModeToggle" class="hidden" v-model="isDark">
        <div class="toggle__line w-10 h-4 bg-gray-400 rounded-full shadow-inner"></div>
        <div :class="{ 'bg-gray-800': isDark, 'bg-white': !isDark }" class="toggle__dot absolute w-6 h-6 bg-white rounded-full shadow inset-y-0 left-0"></div>
      </div>
      <div class="ml-3 text-gray-700 dark:text-gray-300 font-medium">Dark Mode</div>
    </label>
  </div>
</template>

<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
const toggleDark = useToggle(isDark);
</script>

<style scoped>
/* Custom styles for the toggle switch */
.toggle__line {
  transition: background-color 0.3s ease;
}
.toggle__dot {
  transition: transform 0.3s ease;
}
</style>