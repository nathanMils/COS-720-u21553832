<template>
  <RouterView v-if="!isLoading" />
  <div v-else class="flex items-center justify-center min-h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
    <LoadingComponent />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import { inject, type Ref } from 'vue'
import { LoadingComponent } from '@/components'

const isLoading = inject<Ref<boolean>>('isLoading')

if (isLoading === undefined) throw new Error('isLoading not provided')
</script>