<template>
  <div class="flex h-screen bg-gray-100">
    <NavComponent />
    <div class="flex flex-col h-full w-full bg-appPrimary-light dark:bg-appPrimary-dark text-appText-light dark:text-appText-dark">
      <TopBarComponent />
      <RouterView />
    </div>
  </div>
</template>

<script setup lang="ts">
import { NavComponent, TopBarComponent } from '@/components'
import { RouterView } from 'vue-router'
</script>

<style scoped></style>
