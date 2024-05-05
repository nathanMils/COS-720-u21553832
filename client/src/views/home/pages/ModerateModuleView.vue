<script setup lang="ts">

import { onMounted, ref } from 'vue'
import {
  AddPostDialog,
  GreenButton,
  InnerErrorModal,
  LoadingComponent,
  MissingIcon,
  PostCard,
  RedButton
} from '@/components'
import { type FetchModuleContentResponse, fetchModuleContent, createPost, deletePost } from '@/api'
import { useRoute } from 'vue-router'
import { NotFoundView } from '@/views'

const route = useRoute()
const moduleId = route.params.moduleId

const module = ref<FetchModuleContentResponse | null>(null)

onMounted( async () => {
  try {
    const response = await fetchModuleContent(moduleId as string);
    if (response.status !== 200) {
      displayError('An error occurred while fetching module')
      notFound.value = true;
      console.error('Error:', response);
      return;
    }
    module.value = response.data.data || null;
  } catch (error: any) {
    displayError('An error occurred while fetching module')
    notFound.value = true;
    console.error('Error:', error.response);
  }
  loading.value = false;
})

const showAddPostDialog = ref(false)
const addPost = () => {
  showAddPostDialog.value = true
}

const sendPost = async (postContent: string) => {
  loading.value = true;
  try {
    const response = await createPost(moduleId as string, postContent);
    if (response.status !== 200) {
      displayError('An error occurred while creating post')
      console.error('Error:', response);
      loading.value = false;
      return;
    }
    module.value?.postDTOS.push(response.data.data!)
    showAddPostDialog.value = false
  } catch (error: any) {
    displayError('An error occurred while creating post')
    console.error('Error:', error.response);
  }
  loading.value = false;
}

const removePost = async (postId: string) => {
  loading.value = true;
  try {
    const response = await deletePost(moduleId as string, postId);
    if (response.status !== 200) {
      displayError('Error deleting post')
      console.error('Error:', response);
      loading.value = false;
      return;
    }
    module.value!.postDTOS = module.value!.postDTOS.filter(post => post.id !== postId);
  } catch (error: any) {
    displayError('Error deleting post')
    console.error('Error:', error.response);
  }
  loading.value = false;
}

const loading = ref(true)
const notFound = ref(false)
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
</script>

<template>
  <InnerErrorModal
    :show="showError"
    :message="error"
    @close="clearError"
  />
  <AddPostDialog
    :show="showAddPostDialog"
    @close="showAddPostDialog = false"
    @submit="sendPost"
  />
  <NotFoundView v-show="notFound"/>
  <div v-show="loading" class="flex items-center justify-center h-full">
    <LoadingComponent />
  </div>
  <div v-if="!loading && !notFound" class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-4xl font-bold">{{ module?.name }}</h1>
        <p class="mt-2 text-gray-600">{{ module?.description }}</p>
      </div>
      <div>
        <GreenButton @click="addPost">
          Add Post
        </GreenButton>
      </div>
    </div>
    <div v-if="module?.postDTOS.length" class="mt-4">
      <PostCard
        v-for="post in module?.postDTOS"
        :key="post.id"
        :post="post"
        :edit="true"
      >
        <RedButton @click="removePost(post.id)">
          Remove Post
        </RedButton>
      </PostCard>
    </div>
    <div v-else class="flex flex-col items-center justify-center">
      <MissingIcon />
      <p class="text-gray-400 dark:text-gray-600">No Posts</p>
    </div>
  </div>
</template>

<style scoped>

</style>