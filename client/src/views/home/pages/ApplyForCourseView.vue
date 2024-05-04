<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="clearError"
  />
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div>
      <h1 class="text-4xl font-bold">Apply for a Course</h1>
      <p class="mt-2 text-gray-600">All available courses</p>
    </div>
    <div v-if="courses.length" class="mt-4">
      <CourseCard
        v-for="course in courses"
        :course="course"
        :key="course.id"
        class="mb-4"
      >
        <button
          @click="applyForCourse(course.id)"
          class="w-full text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800"
        >
          Apply
        </button>
      </CourseCard>
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">Sorry there are no available courses</p>
    </div>
  </div>
  <div v-else class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { CourseDTO } from '@/types'
import { LoadingComponent, MissingIcon, CourseCard, InnerErrorModal } from '@/components'
import { applyCourse, fetchOtherCourses } from '@/api'

// Mock course data, replace this with actual course data from your server
const courses = ref<CourseDTO[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const response = await fetchOtherCourses()
    if (response.status !== 200) {
      console.error('Error:', response)
      return
    }
    courses.value = response.data.data || []
  } catch (error: any) {
    console.error('Error:', error.response)
  }
  loading.value = false
})

const applyForCourse = async (courseId: string) => {
  try {
    const response = await applyCourse(courseId)
    if (response.status !== 200) {
      console.error('Error:', response)
      displayError('Error applying for course')
      return
    }
    courses.value = courses.value.filter(course => course.id !== courseId)
  } catch (error: any) {
    displayError('Error applying for course')
    console.error('Error:', error.response)
  }
}
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