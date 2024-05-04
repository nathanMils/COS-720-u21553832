<script setup lang="ts">
import type { ModuleDTO } from '@/types'
defineProps<{
  module: ModuleDTO,
  edit: boolean
}>()
</script>

<template>
  <div class="p-6 bg-white rounded-lg shadow-md dark:bg-gray-800 flex flex-col" style="max-height: 150px;">
    <div class="flex justify-between items-start w-full">
        <h2 class="text-lg font-semibold">{{ module.name }}</h2>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">{{ module.description }}</p>
      <div class="flex justify-center">
        <slot/>
      </div>
    </div>
  </div>
</template>