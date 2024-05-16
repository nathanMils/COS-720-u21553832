<script setup lang="ts">

import { onMounted, ref } from 'vue'
import {
  InnerErrorModal,
  LoadingComponent,
  MissingIcon,
  PostCard, RedButton
} from '@/components'
import { type FetchModuleContentResponse, fetchStudentModuleContent } from '@/api'
import { useRoute } from 'vue-router'
import { NotFoundView } from '@/views'
import LectureCard from '@/components/cards/LectureCard.vue'

const route = useRoute()
const moduleId = route.params.moduleId

const tab = ref(0)
const items = [
  { tab: 'Posts' },
  { tab: 'Lectures' }
]

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
const cleanString = (str: string) => {
  return str.replace(/\s/g, ' ')
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
        <h1 class="text-4xl font-bold">{{ cleanString(module?.name!) }}</h1>
        <p class="mt-2 text-gray-600">{{ cleanString(module?.description!) }}</p>
        <div class="flex justify-center">
          <button
            v-for="(item, index) in items"
            :key="index"
            @click="tab = index"
            class="mt-4 px-4 py-2 h-10 w-32 text-center"
            :class="{
              'bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium text-white': tab === index,
              'bg-gray-600 text-white': tab !== index
            }"
          >
            {{ item.tab }}
          </button>
        </div>
      </div>
    </div>
    <div v-if="module?.postDTOS.length && tab===0" class="mt-4">
      <PostCard
        v-for="post in module?.postDTOS"
        :key="post.id"
        :post="post"
        :edit="true"
        class="mb-4"
      />
    </div>
    <div v-if="!module?.postDTOS.length && tab===0" class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Posts</p>
    </div>
    <div v-if="module?.lectures.length && tab === 1" class="mt-4">
      <LectureCard
        v-for="lecture in module?.lectures"
        :key="lecture.id"
        :lecture="lecture"
        class="mb-4"
      />
    </div>
    <div v-if="!(module?.lectures.length) && tab === 1" class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Lectures</p>
    </div>
  </div>
</template>

<style scoped>

</style>