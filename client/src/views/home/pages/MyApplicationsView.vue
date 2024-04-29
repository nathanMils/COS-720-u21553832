<script setup lang="ts">
import { LoadingComponent, MissingIcon, ApplicationCard, InnerErrorModal } from '@/components'
import { onMounted, ref } from 'vue'
import { dropApplication, fetchMyApplications } from '@/api'
import type { StudentApplicationDTO } from '@/types'
onMounted(async () => {
  try {
    const response = await fetchMyApplications()
    if (response.status !== 200) {
      console.error('Error:', response)
      return
    }
    applications.value = response.data.data || []
  } catch (error: any) {
    console.error('Error:', error.response)
  }
  loading.value = false
})

const applications = ref<StudentApplicationDTO[]>([])
const loading = ref(true)

const drop = async (applicationId: number) => {
  loading.value = true
  try {
    const response = await dropApplication(applicationId.toString())
    if (response.status !== 200) {
      displayError('Error dropping application')
      console.error('Error:', response)
      loading.value = false
      return
    }
    applications.value = applications.value.filter(application => application.applicationId !== applicationId)
  } catch (error: any) {
    displayError('Error dropping application')
    console.error('Error:', error.response)
  }
  loading.value = false
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
    :show="showError"
    :message="error"
    @close="handleClose"
  />
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div>
      <h1 class="text-4xl font-bold">Student Applications</h1>
      <p class="mt-2 text-gray-600">All pending student applications</p>
    </div>
    <div v-if="applications.length" class="grid grid-cols-1 gap-4 mt-4">
      <ApplicationCard
        v-for="application in applications"
        :application="application"
        :key="application.applicationId"
        @drop="drop(application.applicationId)"
      />
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No new Student Applications</p>
    </div>
  </div>
  <div v-else class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
</template>