<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { deregisterModule, fetchStudentCourse, type FetchStudentCourseResponse, registerModule } from '@/api'
import {
  GreenFuncButton,
  InnerErrorModal,
  LoadingComponent,
  MissingIcon,
  ModuleCardNoLink,
  RedButton
} from '@/components'
import { NotFoundView } from '@/views'


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
    const response = await fetchStudentCourse(courseId as string)
    if (response.status !== 200) {
      displayError('An error occurred while fetching course')
      notFound.value = true
      console.error("An error occurred while fetching course modules")
      return
    }
    course.value = response.data.data
  } catch (error: any) {
    displayError('An error occurred while fetching course')
    notFound.value = true
    console.error(error)
  }
  loading.value = false
})

const loading = ref(true)
const notFound = ref(false)
const course = ref<FetchStudentCourseResponse | null>(null)

const register = async (moduleId: string) => {
  loading.value = true
  try {
    const response = await registerModule(courseId as string, moduleId)
    if (response.status !== 200) {
      displayError('An error occurred while registering module')
      console.error("An error occurred while registering module")
      loading.value = false
      return
    }
    course.value!.modules.find(module => module.id === moduleId)!.registered = true
  } catch (error: any) {
    displayError('An error occurred while registering module')
    console.error(error)
  }
  loading.value = false
}

const drop = async (moduleId: string) => {
  loading.value = true
  try {
    const response = await deregisterModule(courseId as string, moduleId)
    if (response.status !== 200) {
      displayError('An error occurred while dropping module')
      console.error("An error occurred while dropping module")
      loading.value = false
      return
    }
    course.value!.modules.find(module => module.id === moduleId)!.registered = false
  } catch (error: any) {
    displayError('An error occurred while dropping module')
    console.error(error)
  }
  loading.value = false
}
</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="handleClose"
  />
  <NotFoundView v-show="notFound"/>
  <div v-show="loading" class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
  <div v-if="!loading && !notFound" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">{{ course?.courseName }}</h1>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">{{ course?.courseDescription }}</p>
        <p class="mt-2 text-gray-600 overflow-ellipsis overflow-hidden whitespace-nowrap">Moderator: {{ course?.courseModerator }}</p>
      </div>
    </div>
    <div v-if="course?.modules.length" class="mt-4">
      <ModuleCardNoLink
        v-for="module in course?.modules"
        :module="module"
        :key="module.id"
        :edit="false"
        class="mb-4"
      >
        <GreenFuncButton v-if="!module.registered"
          @click="register(module.id)"
        >
          Register
        </GreenFuncButton>
        <RedButton v-else
          @click="drop(module.id)"
        >
          Drop
        </RedButton>
      </ModuleCardNoLink>
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Modules</p>
    </div>
  </div>
</template>
