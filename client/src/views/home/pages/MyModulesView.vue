<script setup lang="ts">
import { LoadingComponent, ModuleCard } from '@/components'
import { onMounted, ref } from 'vue'
import type { ModuleDTO } from '@/types'
import { fetchStudentModules } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchStudentModules();
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    modules.value = response.data.data;
  } catch (error: any) {
    console.error('Error:', error.response);
  }

})

const modules = ref<ModuleDTO[]>([])
</script>

<template>
  <Suspense>
    <template #default>
      <div v-if="modules.length" class="flex-grow overflow-auto px-10 py-10">
        <h1 class="text-2xl font-bold">My Modules</h1>
        <div class="grid grid-cols-1 gap-4 mt-4 sm:grid-cols-2 lg:grid-cols-3">
          <div v-for="mod in modules"  :key="mod.id" class="p-4 bg-white rounded-lg shadow-md dark:bg-gray-800">
            <ModuleCard :module="mod"/>
          </div>
        </div>
      </div>
      <div v-else class="flex items-center justify-center h-full">
        <p class="text-gray-600">No modules found.</p>
      </div>
    </template>
    <template #fallback>
      <LoadingComponent />
    </template>
  </Suspense>
</template>