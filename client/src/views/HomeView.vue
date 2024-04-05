<template>
  <div>
    <NavComponent />
    <RouterView />
  </div>
</template>

<script setup lang="ts">
import { NavComponent } from '@/components'
import { RouterView } from 'vue-router'
</script>

<style scoped></style>
