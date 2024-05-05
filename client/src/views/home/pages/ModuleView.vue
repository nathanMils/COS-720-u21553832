<script setup lang="ts">

import { onMounted, ref } from 'vue'
import {
  InnerErrorModal,
  LoadingComponent,
  MissingIcon,
  PostCard
} from '@/components'
import { type FetchModuleContentResponse, fetchStudentModuleContent } from '@/api'
import { useRoute } from 'vue-router'
import { NotFoundView } from '@/views'

const route = useRoute()
const moduleId = route.params.moduleId

const module = ref<FetchModuleContentResponse | null>(null)

onMounted( async () => {
  try {
    const response = await fetchStudentModuleContent(moduleId as string);
    if (response.status !== 200) {
      displayError('An error occurred while fetching module')
      notFound.value = true;
      console.error('Error:', response);
      return;
    }
    module.value = response.data.data || null;
  } catch (error: any) {
    displayError('An error occurred while fetching module')
    notFound.value = true;
    console.error('Error:', error.response);
  }
  loading.value = false;
})

const loading = ref(true)
const notFound = ref(false)
const error = ref('')
const showError = ref(false)
const displayError = (message: string) => {
  showError.value = true
  error.value = message
}
const clearError = () => {
  showError.value = false
  error.value = ''
}
</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="clearError"
  />
  <NotFoundView v-show="notFound"/>
  <div v-show="loading" class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
  <div v-if="!loading && !notFound" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">{{ module?.name }}</h1>
        <p class="mt-2 text-gray-600">{{ module?.description }}</p>
      </div>
    </div>
    <div v-if="module?.postDTOS.length" class="mt-4">
      <PostCard
        v-for="post in module?.postDTOS"
        :key="post.id"
        :post="post"
        :edit="true"
      >
      </PostCard>
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Posts</p>
    </div>
  </div>
</template>

<style scoped>

</style>