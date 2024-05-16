<script setup lang="ts">
import type { ModuleDTO } from '@/types'
import { RouterLink } from 'vue-router'
defineProps<{
  module: ModuleDTO,
  edit: boolean
}>()
const cleanString = (str: string) => {
  return str.replace(/\s/g, ' ')
}
</script>

<template>
  <div class="p-6 bg-white rounded-lg shadow-md dark:bg-gray-800 flex flex-col" style="max-height: 150px;">
    <div class="flex justify-between items-start w-full">
      <RouterLink :to=" edit? `/module/${module.id}/edit`: `/module/${module.id}`">
        <h2 class="text-lg font-semibold">{{ cleanString(module.name) }}</h2>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">{{ cleanString(module.description) }}</p>
      </RouterLink>
      <div class="flex justify-center">
        <slot/>
      </div>
    </div>
  </div>
</template>