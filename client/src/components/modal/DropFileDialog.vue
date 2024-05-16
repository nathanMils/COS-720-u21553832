<template>
  <div v-if="show" class="fixed z-20 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-30 dark:bg-black dark:bg-opacity-50 transition-opacity" aria-hidden="true"></div>
      <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
      <div class="inline-block align-middle rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
        <div class="bg-appPrimary-light dark:bg-appPrimary-dark p-6">
          <div class="text-center">
            <div v-if="!fileName"
              class="dropzone border-2 border-dashed border-gray-500 p-6 mb-4 flex flex-col items-center justify-center"
              @dragover.prevent
              @drop="onDrop"
            >
              <input type="file" id="file" ref="file" v-show="false" @change="onFileChange" accept=".pdf"/>
              <label for="file" class="cursor-pointer flex flex-col items-center text-blue-500 hover:text-blue-700">
                <i class="fas fa-upload text-2xl mb-2"></i>
                <span class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300">Browse</span>
              </label>
              <p>or drag and drop files here</p>
            </div>
            <div v-else class="mb-4">
              <p class="overflow-ellipsis overflow-hidden">{{ fileName }}</p>
            </div>
          </div>
          <div class="flex justify-between">
            <button v-if="fileName" @click="confirm" type="button" class="w-full sm:w-auto inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primaryButton-600 text-base font-medium text-white hover:bg-primaryButton-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primaryButton-500">
              Confirm
            </button>
            <button @click="cancel" type="button" class="w-full sm:w-auto inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'

const file = reactive({file: null as File | null});
const fileName = ref('');

const onDrop = (event: any) => {
  console.log('onDrop called');
  const droppedFile = event.dataTransfer.files[0];
  if (droppedFile.type !== 'application/pdf') {
    alert('Only PDF files are accepted');
    return;
  }
  file.file = droppedFile as File;
  fileName.value = file.file?.name || '';
  console.log('onDrop:', file.file);
};

const onFileChange = (event: any) => {
  console.log('onFileChange called');
  const selectedFile = event.target.files[0];
  if (selectedFile.type !== 'application/pdf') {
    alert('Only PDF files are accepted');
    return;
  }
  file.file = selectedFile as File;
  fileName.value = file.file?.name || '';
  console.log('onFileChange:', file.file);
};

const cancel = () => {
  file.file = null;
  fileName.value = '';
  console.log('cancel:', file.file);
  emits('cancel');
};

const confirm = () => {
  console.log('confirm start:', file.file);
  if (file.file) {
    console.log('confirm:', file.file);
    emits('confirm', file.file);
  } else {
    console.log('confirm failed: file is null');
  }
};

defineProps({
  show: Boolean
})
const emits = defineEmits(['cancel','confirm'])
</script>