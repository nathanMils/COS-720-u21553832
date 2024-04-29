<script setup lang="ts">
import type {StudentApplicationDTO} from '@/types'
import { Status } from '@/types'
import { acceptStudent,denyStudent } from '@/api'
const props = defineProps<{
  application: StudentApplicationDTO
}>()

const emit = defineEmits(['updateStatus'])

const acceptApplication = async () => {
  try {
    const response = await acceptStudent(props.application.applicationId);
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    console.log('Application accepted');
    emit('updateStatus', Status.ACCEPTED);
  } catch (error: any) {
    console.error('Error:', error.response);
  }
}

const denyApplication = async () => {
  try {
    const response = await denyStudent(props.application.applicationId);
    if (response.status !== 200) {
      console.error('Error:', response);
      return;
    }
    console.log('Application denied');
    emit('updateStatus', Status.DENIED);
  } catch (error: any) {
    console.error('Error:', error.response);
  }
}
</script>

<template>
  <div class="p-6 bg-white rounded-lg shadow-md dark:bg-gray-800 flex flex-col" style="max-height: 200px;">
    <div class="flex justify-between items-start w-full">
      <div>
        <h2 class="text-lg font-semibold">{{ application.userFirstName + ' ' + application.userLastName }}</h2>
        <p class="mt-2 text-gray-600">{{ application.courseName }}</p>
        <p class="mt-2 text-gray-600">{{ application.description }}</p>
        <p class="mt-2 text-blue-500">{{ application.status }}</p>
      </div>
      <div class="flex flex-col justify-center items-center">
        <button v-show="application.status === Status.PENDING" @click="acceptApplication" class="mt-2 bg-green-500 text-white px-4 py-2 rounded-md w-32">Accept</button>
        <button v-show="application.status === Status.PENDING" @click="denyApplication" class="mt-2 bg-red-500 text-white px-4 py-2 rounded-md w-32">Reject</button>
      </div>
    </div>
  </div>
</template>



