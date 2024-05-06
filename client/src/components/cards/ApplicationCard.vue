<script setup lang="ts">
import { type StudentApplicationDTO } from '@/types'

defineProps<{
  application: StudentApplicationDTO
}>()
const emit = defineEmits(['drop'])
const handleDrop = () => {
  emit('drop')
}
</script>

<template>
  <div class="p-6 bg-white rounded-lg shadow-md dark:bg-gray-800 flex flex-col" style="max-height: 200px;">
    <div class="flex justify-between items-start w-full">
      <div>
        <h2 class="text-lg font-semibold">{{ application.userFirstName + ' ' + application.userLastName }}</h2>
        <p class="mt-2 text-gray-600">{{ application.courseName }}</p>
        <p class="mt-2 text-gray-600">{{ application.description }}</p>
        <p class="mt-2 text-blue-500">{{ application.status }}</p>
      </div>
      <div class="flex flex-col justify-center items-center">
        <button @click="handleDrop" class="mt-2 bg-red-500 text-white px-4 py-2 rounded-md w-32">Drop</button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>