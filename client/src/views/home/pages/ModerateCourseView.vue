<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { fetchCourse,deleteModule, type FetchCourseResponse } from '@/api'
import {
  ConfirmDialog,
  GreenButton,
  InnerErrorModal,
  LoadingComponent,
  MissingIcon,
  ModuleCard,
  RedButton
} from '@/components'


const route = useRoute()
const courseId = route.params.courseId
const error = ref('')

const showError = ref(false)
const handleClose = () => {
  showError.value = false
  error.value = ''
}
const displayError = (msg: string) => {
  showError.value = true
  error.value = msg
}

onMounted( async () => {
  try {
    const response = await fetchCourse(courseId as string)
    if (response.status !== 200) {
      displayError('An error occurred while fetching course')
      console.error("An error occurred while fetching course modules")
      return
    }
    course.value = response.data.data
  } catch (error: any) {
    displayError('An error occurred while fetching course')
    console.error(error)
  }
  loading.value = false
})

const loading = ref(true)
const course = ref<FetchCourseResponse | null>(null)

const removeModule = async (moduleId: string) => {
  try {
    const response = await deleteModule(courseId as string,moduleId)
    if (response.status !== 200) {
      displayError('Error deleting module')
      console.error('Error:', response)
      return
    }
    course.value!.modules = course.value!.modules.filter(module => module.id !== moduleId)
  } catch (error: any) {
    displayError('Error deleting module')
    console.error('Error:', error.response)
  }
}

const showConfirmation = ref(false)
</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="handleClose"
  />
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">{{ course!.courseName }}</h1>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">{{ course!.courseDescription }}</p>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">Moderator: {{ course!.courseModerator }}</p>
      </div>
      <RouterLink :to="{name: 'createModule'}">
        <GreenButton>
          Add Module
        </GreenButton>
      </RouterLink>
    </div>
    <div v-if="course!.modules.length" class="grid grid-cols-1 gap-4 mt-4">
      <ModuleCard
        v-for="module in course!.modules"
        :module="module"
        :key="module.id"
        :edit="true"
      >
        <RedButton
          @click="showConfirmation = true"
        >
          Remove Module
        </RedButton>
        <ConfirmDialog
          :show="showConfirmation"
          message="Are you sure you want to delete this module?"
          @confirm="removeModule(module.id); showConfirmation = false"
          @close="showConfirmation = false"
        />
      </ModuleCard>
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
