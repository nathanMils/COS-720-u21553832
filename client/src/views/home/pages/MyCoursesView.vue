<script setup lang="ts">

import type { CourseDTO } from '@/types'
import { onMounted, ref } from 'vue'
import { CourseCardLink, GreenButton, LoadingComponent, MissingIcon} from '@/components'
import { fetchStudentCourses } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchStudentCourses();
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

const courses = ref<CourseDTO[]>([])
const loading = ref(true)

</script>

<template>
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">My Courses</h1>
        <p class="mt-2 text-gray-600">All Student Courses</p>
      </div>
      <RouterLink :to="{name: 'applyCourse'}">
        <GreenButton>
          Apply for a Course
        </GreenButton>
      </RouterLink>
    </div>
    <div v-if="courses.length" class="mt-4">
      <CourseCardLink
        v-for="course in courses"
        :course="course"
        :key="course.id"
        :edit="false"
        class="mb-4"
      >
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