<script setup lang="ts">
import {
  CourseCardLink,
  InnerErrorModal,
  GreenButton,
  LoadingComponent,
  MissingIcon,
  RedButton,
  ConfirmDialog
} from '@/components'
import { onMounted, ref } from 'vue'
import type { CourseDTO } from '@/types'
import { deleteCourse, fetchModeratorCourses } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchModeratorCourses();
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    courses.value = response.data.data || [];
  } catch (error: any) {
    console.error('Error:', error.response);
  }
  loading.value = false;
})

const removeCourse = async (courseId: string) => {
  try {
    const response = await deleteCourse(courseId);
    if (response.status !== 200) {
      displayError('Error deleting course')
      console.error('Error:', response);
      return;
    }
    courses.value = courses.value.filter(course => course.id !== courseId);
  } catch (error: any) {
    displayError('Error deleting course')
    console.error('Error:', error.response);
  }
}

const loading = ref(true)
const courses = ref<CourseDTO[]>([])
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
const showConfirmation = ref('')

</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="clearError"
  />
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">My Courses</h1>
        <p class="mt-2 text-gray-600">All your Courses</p>
      </div>
      <RouterLink :to="{name: 'createCourse'}">
        <GreenButton>
          Create a Course
        </GreenButton>
      </RouterLink>
    </div>
    <div v-if="courses.length" class="mt-4">
      <CourseCardLink
        v-for="course in courses"
        :course="course"
        :key="course.id"
        :edit="true"
        class="mb-4"
      >
        <RedButton
          @click="showConfirmation = course.id"
        >
          Remove Course
        </RedButton>
        <ConfirmDialog
          :show="showConfirmation === course.id"
          message="Are you sure you want to delete this course?"
          @confirm="removeCourse(course.id); showConfirmation = ''"
          @close="showConfirmation = ''"
        />
      </CourseCardLink>
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Courses</p>
    </div>
  </div>
  <div v-else class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
</template>

<style scoped>

</style>