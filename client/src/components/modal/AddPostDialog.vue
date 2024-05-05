<template>
  <div v-if="show" class="fixed z-20 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-30 dark:bg-black dark:bg-opacity-50 transition-opacity" aria-hidden="true"></div>
      <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
      <form @submit.prevent="submit" class="inline-block align-bottom rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
        <div class="bg-appPrimary-light dark:bg-appPrimary-dark pt-5 pb-4 sm:p-6 sm:pb-4">
          <div class="">
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-appText-light dark:text-appText-dark leading-6 font-medium" id="modal-title">
                Create Post
              </h3>
              <div class="mt-2 flex justify-center flex-col">
                <textarea
                  v-model="postForm.content"
                  @input="v$.content.$touch()"
                  id="content"
                  name="content"
                  rows="5"
                  class="w-full bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0"
                  :class="{'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.content.$error && v$.content.$dirty}"
                  placeholder="Post Content"
                ></textarea>
                <div class="h-4 text-xs text-red-500 dark:text-red-400">
                  <span v-if="v$.content.$error && v$.content.$dirty">{{v$.content.$errors[0].$message}}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-appPrimary-light justify-between dark:bg-appPrimary-dark px-4 py-3 sm:px-6 sm:flex">
          <button @click="cancel" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm">
            Cancel
          </button>
          <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primaryButton-600 text-base font-medium text-white hover:bg-primaryButton-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primaryButton-500 sm:ml-3 sm:w-auto sm:text-sm">
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { helpers, maxLength, minLength, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'

defineProps({
  show: Boolean,
  message: String
})
const emits = defineEmits(['submit', 'close'])

const postForm = reactive({
  content: '',
})

const rules = {
  content: {
    required: helpers.withMessage('Content is required', required),
    minLength: helpers.withMessage('Content must be at least 3 characters', minLength(3)),
    maxLength: helpers.withMessage('Content must be at most 500 characters', maxLength(200)),
  },
}

const v$ = useVuelidate(rules, postForm)
const cancel = () => {
  postForm.content = '';
  v$.value.$reset();
  emits('close');
}
const submit = async () => {
  const result = await v$.value.$validate();
  if (!result) {
    return;
  }
  emits('submit', postForm.content);
  cancel()
}
</script>