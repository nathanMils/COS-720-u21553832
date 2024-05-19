<script setup lang="ts">
import { reactive, ref } from 'vue'
import useVuelidate from '@vuelidate/core'
import { helpers, maxLength, minLength, required } from '@vuelidate/validators'
import { InnerErrorModal, LoadingComponent } from '@/components'
import { createModule } from '@/api'
import { useRoute } from 'vue-router'

const route = useRoute()
const courseId = route.params.courseId

const create = async () => {
  const result = await v$.value.$validate();
  if (!result) {
    return;
  }
  loading.value = true;
  try {
    const response = await createModule(courseId as string,moduleForm.name,moduleForm.description);
    if (response.status !== 200) {
      displayError("An error occurred while creating the module")
      loading.value = false;
      return;
    }
    success.value = true;
  } catch (error: any) {
    switch (error.response.data.internalCode) {
      case 'MODULE_NAME_EXISTS':
        moduleForm.name = '';
        moduleForm.error = 'Module name already exists';
        break;
      default:
        moduleForm.name = '';
        moduleForm.description = '';
        displayError("An error occurred while creating the module")
    }
    loading.value = false;
  }
}

const resetForm = () => {
  moduleForm.name = '';
  moduleForm.description = '';
  moduleForm.error = '';
  success.value = false;
  loading.value = false;
  v$.value.$reset();
}

const showError = ref(false)
const handleClose = () => {
  showError.value = false
  error.value = ''
}
const displayError = (msg: string) => {
  showError.value = true
  error.value = msg
}

const moduleForm = reactive({
  name: '',
  description: '',
  error: ''
})

const error = ref('')

const rules = {
  name: {
    required: helpers.withMessage('Name is required', required),
    minLength: helpers.withMessage('Name must be at least 3 characters', minLength(3)),
    maxLength: helpers.withMessage('Name must be at most 30 characters', maxLength(30))
  },
  description: {
    required: helpers.withMessage('Description is required', required),
    minLength: helpers.withMessage('Description must be at least 10 characters', minLength(10)),
    maxLength: helpers.withMessage('Description must be at most 500 characters', maxLength(500)),
  }
}

const v$ = useVuelidate(rules, moduleForm)

const loading = ref(false)
const success = ref(false)
</script>

<template>
  <div class="flex-grow overflow-auto px-10 py-10 grid grid-rows-[auto,1fr]">
    <InnerErrorModal
      :show="showError"
      :message="error"
      @close="handleClose"
    />
    <div class="flex justify-center items-center text-center">
      <div>
        <h1 class="text-4xl font-bold">Create a Module</h1>
        <p class="mt-2 text-gray-600">Please fill in the form to create an new module</p>
      </div>
    </div>
    <div v-if="!loading">
      <form
        @submit.prevent="create"
        class="w-3/5 space-y-4 md:space-y-3 max-h-screen overflow-auto justify-center items-center mt-8 md:mt-4 md:mx-auto md:px-4 md:py-4 bg-white shadow-lg rounded-lg dark:bg-gray-800 dark:shadow-none dark:border dark:border-gray-600 dark:border-opacity-50 dark:divide-y dark:divide-gray-600 dark:divide-opacity-50"
      >
        <div class="flex flex-col space-y-2">
          <label
            for="firstName"
            class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
          >Module Name</label
          >
          <input
            v-model="moduleForm.name"
            @input="v$.name.$touch(); moduleForm.error = ''"
            type="text"
            id="name"
            name="name"
            :class="{
                        'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                        'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.name.$error && v$.name.$dirty}"
            placeholder="Module Name"
          />
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.name.$error && v$.name.$dirty && !moduleForm.error">{{v$.name.$errors[0].$message}}</span>
            <span v-if="moduleForm.error">{{ moduleForm.error }}</span>
          </div>
        </div>
        <div class="flex flex-col space-y-2">
          <label
            for="description"
            class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
          >Module Description</label>
          <textarea
            v-model="moduleForm.description"
            @input="v$.description.$touch()"
            id="description"
            name="description"
            rows="5"
            :class="{
                  'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                  'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.description.$error && v$.description.$dirty}"
            placeholder="Module Description"
          ></textarea>
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.description.$error && v$.description.$dirty">{{v$.description.$errors[0].$message}}</span>
          </div>
        </div>
        <div class="flex justify-end">
          <button
            type="submit"
            class="w-1/4 text-white bg-primaryButton-600 mt-4 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800"
          >
            Create Module
          </button>
        </div>
      </form>
    </div>
    <div v-else class="flex items-center justify-center h-full">
      <div v-if="success" class="flex flex-col items-center justify-center">
        <i class="fa-solid fa-check-circle text-green-500 text-9xl"></i>
        <p class="text-green-500 text-4xl mt-5">Course Created Successfully</p>
        <button
          @click="resetForm"
          class="text-white bg-primaryButton-600 mt-8 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800"
        >
          Create Another Module
        </button>
      </div>
      <div v-else>
        <LoadingComponent />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>