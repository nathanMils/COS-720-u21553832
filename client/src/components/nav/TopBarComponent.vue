<script setup lang="ts">

import { DarkMode } from '@/components'
</script>

<template>
  <div class="flex items-center justify-end h-16 bg-appSecondary-light dark:bg-appSecondary-dark border-b-appBorder-light dark:border-appBorder-dark shadow-xl z-10">
    <div class="flex items-center justify-end flex-1 pr-4">
      <DarkMode />
    </div>
  </div>
</template>

<style scoped>

</style>