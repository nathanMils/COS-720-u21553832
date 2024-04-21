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
  <div class="p-4 bg-white rounded-lg shadow-md dark:bg-gray-800">
    <h2 class="text-lg font-semibold">{{application.userFirstName +' '+application.userLastName}}</h2>
    <p class="mt-2 text-gray-600">{{application.courseName}}</p>
    <p class="mt-2 text-gray-600">{{application.description}}</p>
    <p class="mt-2 text-blue-500">{{application.status}}</p>
    <button @click="acceptApplication" class="mt-2 bg-green-500 text-white px-4 py-2 rounded-md">Accept</button>
    <button @click="denyApplication" class="mt-2 bg-red-500 text-white px-4 py-2 rounded-md">Reject</button>
  </div>
</template>