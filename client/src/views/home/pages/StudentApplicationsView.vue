<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Status, type StudentApplicationDTO } from '@/types'
import { LoadingComponent, MissingIcon, StudentApplicationCard } from '@/components'
import { fetchApplications } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchApplications();
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    studentApplications.value = response.data.data || [];
  } catch (error: any) {
    console.error('Error:', error.response);
  }
  loading.value = false;
})

const loading = ref(true);

const updateStatus = (applicationId: number, newStatus: Status) => {
  const application = studentApplications.value.find(app => app.applicationId === applicationId);
  if (application) {
    application.status = newStatus;
  }
}

const studentApplications = ref<StudentApplicationDTO[]>([])
</script>

<template>
  <div v-if="!loading" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div>
      <h1 class="text-4xl font-bold">Student Applications</h1>
      <p class="mt-2 text-gray-600">All pending student applications</p>
    </div>
    <div v-if="studentApplications.length" class="mt-4">
      <StudentApplicationCard
        v-for="application in studentApplications"
        :application="application"
        :key="application.applicationId"
        @updateStatus="updateStatus(application.applicationId, $event)"
        class="mb-4"
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