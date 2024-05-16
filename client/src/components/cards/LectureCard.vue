<script setup lang="ts">
import type { LectureDTO } from '@/types'
import { RouterLink } from 'vue-router'
defineProps<{
  lecture: LectureDTO
}>()
const formatDate = (dateString: string | undefined) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString();
}
</script>

<template>
  <div class="p-6 bg-white rounded-lg shadow-md dark:bg-gray-800 flex flex-col" style="max-height: 150px;">
    <div class="flex justify-between items-start w-full">
      <RouterLink :to="`/lecture/${lecture.id}`">
        <h2 class="text-lg font-semibold">{{ lecture.fileName }}</h2>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">{{ formatDate(lecture.createdAt) }}</p>
      </RouterLink>
      <div class="flex justify-center">
        <slot/>
      </div>
    </div>
  </div>
</template>