<script setup lang="ts">
import { LoadingComponent, MissingIcon, ModuleCard } from '@/components'
import { onMounted, ref } from 'vue'
import type { ModuleDTO } from '@/types'
import { fetchStudentModules } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchStudentModules();
    console.log(response)
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    modules.value = response.data.data || [];
  } catch (error: any) {
    console.error('Error:', error.response);
  }
  loading.value = false;
})

const modules = ref<ModuleDTO[]>([])
const loading = ref(true)
</script>

<template>
    <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
      <div>
        <h1 class="text-4xl font-bold">My Modules</h1>
        <p class="mt-2 text-gray-600">All Student Modules</p>
      </div>
      <div v-if="modules.length" class="grid grid-cols-1 gap-4 mt-4">
        <ModuleCard
          v-for="module in modules"
          :module="module"
          :key="module.id"
          :edit="false"
        />
      </div>
      <div v-else class="flex flex-col items-center justify-center">
        <MissingIcon />
        <p class="text-gray-400 dark:text-gray-600">No Modules</p>
      </div>
    </div>
    <div v-else class="flex items-center justify-center h-full">
      <LoadingComponent />
    </div>
</template>