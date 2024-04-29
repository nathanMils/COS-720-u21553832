<script setup lang="ts">

import { onMounted, ref } from 'vue'
import { getProfile, type GetProfileResponse } from '@/api'
import { InnerErrorModal, LoadingComponent } from '@/components'

onMounted(async () => {
  try {
    const response = await getProfile()
    if (response.status === 200) {
      profile.value = response.data.data
    }
  } catch (error) {
    displayError('An error occurred while fetching profile')
    console.error('Error:', error)
  }
  loading.value = false
})

const loading = ref(true)
const profile = ref(null as GetProfileResponse | null)

const formatDate = (dateString: string | undefined) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString();
}
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
</script>

<template>
  <InnerErrorModal
    v-model:showError="showError"
    :error="error"
    @close="handleClose"
  />
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div>
      <h1 class="text-4xl font-bold">Profile</h1>
    </div>
    <div>
<div class="grid grid-cols-1 gap-4 mt-4">
        <div class="bg-white dark:bg-gray-800 shadow overflow-hidden sm:rounded-lg">
          <div class="px-4 py-5 sm:px-6">
            <h3 class="text-lg font-medium text-gray-900 dark:text-gray-200">Personal Information</h3>
            <div class="mt-5 grid grid-cols-1 gap-6 sm:grid-cols-6">
              <div class="sm:col-span-3">
                <label for="first_name" class="block text-sm font-medium text-gray-700 dark:text-gray-200">First name</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.firstName }}</p>
              </div>
              <div class="sm:col-span-3">
                <label for="last_name" class="block text-sm font-medium text-gray-700 dark:text-gray-200">Last name</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.lastName }}</p>
              </div>
              <div class="sm:col-span-3">
                <label for="first_name" class="block text-sm font-medium text-gray-700 dark:text-gray-200">Username</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.username }}</p>
              </div>
              <div class="sm:col-span-3">
                <label for="email" class="block text-sm font-medium text-gray-700 dark:text-gray-200">Email address</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.email }}</p>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-white dark:bg-gray-800 shadow overflow-hidden sm:rounded-lg">
          <div class="px-4 py-5 sm:px-6">
            <h3 class="text-lg font-medium text-gray-900 dark:text-gray-200">Account Statistics</h3>
            <div class="mt-5 grid grid-cols-1 gap-6 sm:grid-cols-6">
              <div class="sm:col-span-3">
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-200">Number of current Courses</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.numberOfCoursesTaken }}</p>
              </div>
              <div class="sm:col-span-3">
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-200">Number of current Modules</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.numberOfModulesTaken }}</p>
              </div>
              <div class="sm:col-span-3">
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-200">Number of your Courses</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.numberOfCoursesCreated }}</p>
              </div>
              <div class="sm:col-span-3">
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-200">Number of your Modules</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ profile?.numberOfModulesCreated }}</p>
              </div>
              <div class="sm:col-span-6">
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-200">Joined Date</label>
                <p class="mt-1 text-sm text-gray-900 dark:text-gray-200">{{ formatDate(profile?.createdAt) }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
</template>