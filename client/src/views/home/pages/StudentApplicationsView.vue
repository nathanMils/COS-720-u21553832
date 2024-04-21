<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Status, type StudentApplicationDTO } from '@/types'
import { LoadingComponent, Modal, StudentApplicationCard } from '@/components'
import { fetchApplications } from '@/api'

onMounted( async () => {
  try {
    const response = await fetchApplications();
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    studentApplications.value = response.data.data?.studentApplicationDTOS || [];
  } catch (error: any) {
    console.error('Error:', error.response);
  }

})

const updateStatus = (applicationId: number, newStatus: Status) => {
  const application = studentApplications.value.find(app => app.applicationId === applicationId);
  if (application) {
    application.status = newStatus;
  }
}

const studentApplications = ref<StudentApplicationDTO[]>([])
</script>

<template>
  <Suspense>
    <template #default>
      <div class="flex-grow overflow-auto px-10 py-10">
        <h1 class="text-2xl font-bold">Welcome to my dashboard!</h1>
        <p class="mt-2 text-gray-600">This is an example dashboard using Tailwind CSS.</p>
        <div class="grid grid-cols-1 gap-4 mt-4 sm:grid-cols-2 lg:grid-cols-3">
          <StudentApplicationCard
            v-for="application in studentApplications"
            :application="application"
            :key="application.applicationId"
            @updateStatus="updateStatus(application.applicationId, $event)"
          />
        </div>
      </div>
    </template>
    <template #fallback>
      <LoadingComponent />
    </template>
  </Suspense>
</template>