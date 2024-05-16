<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { fetchLecture } from '@/api'
import {
  InnerErrorModal,
  LoadingComponent
} from '@/components'
import { NotFoundView } from '@/views'
import PdfApp from 'vue3-pdf-app';
import "vue3-pdf-app/dist/icons/main.css";



const route = useRoute()
const lectureId = route.params.lectureId
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

const pdfUrl = ref('');

onMounted( async () => {
  try {
    const response = await fetchLecture(lectureId as string)
    if (response.status !== 200) {
      displayError('An error occurred while fetching lecture')
      notFound.value = true
      console.error("An error occurred while fetching lecture")
      return
    }
    if (response.data) {
      pdfUrl.value = URL.createObjectURL(new Blob([response.data]));
    }
  } catch (error: any) {
    displayError('An error occurred while fetching course')
    notFound.value = true
    console.error(error)
  }
  loading.value = false
})

const loading = ref(true)
const notFound = ref(false)

</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="handleClose"
  />
  <NotFoundView v-show="notFound"/>
  <div v-show="loading" class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
  <div v-if="!loading && !notFound" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">Lecture</h1>
      </div>
    </div>
    <div class="mt-4">
      <pdf-app :pdf="pdfUrl" />
    </div>
  </div>
</template>

<style scoped>
/* for dark theme */
.dark .pdf-app {
  --pdf-app-background-color: #1f2937;
  --pdf-button-hover-font-color: #10621e;
  --pdf-button-toggled-color: #1abe42;
  --pdf-dialog-button-color: #1abe42;
  --pdf-toolbar-sidebar-color: #111827;
  --pdf-toolbar-font-color: #f9fafb; /* Icon color in dark mode */
  --pdf-toolbar-color: #111827;
  --pdf-horizontal-toolbar-separator-color: #f9fafb;
}

/* for light theme */
html .pdf-app {
  --pdf-app-background-color: #ffffff;
  --pdf-button-hover-font-color: #10621e;
  --pdf-button-toggled-color: #96f3c0;
  --pdf-dialog-button-color: #96f3c0;
  --pdf-toolbar-sidebar-color: #f9fafb;
  --pdf-toolbar-font-color: #111827; /* Icon color in light mode */
  --pdf-toolbar-color: #f9fafb;
  --pdf-horizontal-toolbar-separator-color: #111827;
}
</style>