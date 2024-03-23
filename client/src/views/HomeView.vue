<template>
  <div>
    <SiteNavigation />
    <RouterView />
  </div>
</template>

<script setup lang="ts">
import { SiteNavigation } from '@/components'
import { RouterView } from 'vue-router'
</script>

<style scoped></style>
