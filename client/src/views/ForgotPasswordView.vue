<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ErrorModal, LogoImg } from '@/components'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { AuthStore } from '@/stores'
import router from '@/router'

const authStore = AuthStore()

let message = ref('')
const show = ref(false)
const done = ref(false)
const handleClose = () => {
  show.value = false
}
const displayError = (msg: string) => {
  message.value = msg
  show.value = true
}

const forgotPassword = async () => {
  const response = await authStore.forgotPassword(resetForm.username, resetForm.email)
  if (response.status !== 200) {
    resetForm.username = ''
    resetForm.email = ''
    displayError('An error occurred while resetting password')
    return
  }
  done.value = true
}

const cancel = () => {
  router.push({ name: 'login' })
}

const resetForm = reactive({
  username: '',
  email: ''
})

const rules = {
  username: {
    required: helpers.withMessage('Username is required', required),
  },
  email: {
    required: helpers.withMessage('Email is required', required),
  }
}

const v$ = useVuelidate(rules, resetForm);
</script>

<template>
  <div>
    <ErrorModal v-show="show" :message="message" @close="handleClose"/>
    <section v-if="!done" class="bg-appPrimary-light dark:bg-appPrimary-dark">
      <div class="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <div class="flex flex-row items-center justify-evenly text-4xl mb-8 font-bold text-appText-light dark:text-appText-dark">
          <LogoImg :size="3" :mr="1"/>
          <h1>Portal</h1>
        </div>
        <div
          class="w-full bg-white rounded-lg shadow-xl border border-appBorder-light dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-appSecondary-dark dark:border-appBorder-dark dark:shadow-none"
        >
          <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1
              class="text-xl font-bold leading-tight tracking-tight text-center text-appText-light md:text-2xl dark:text-appText-dark"
            >
              Forgot Password
            </h1>
            <form class="space-y-4 md:space-y-6" @submit.prevent="forgotPassword">
              <div>
                <label
                  for="username"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                >Username</label
                >
                <input
                  v-model="resetForm.username"
                  @input="v$.username.$touch()"
                  type="text"
                  name="username"
                  id="username"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.username.$error && v$.username.$dirty)}"
                  placeholder="username"
                />
                <span v-if="v$.username.$error && v$.username.$dirty" class="text-xs text-red-500 dark:text-red-400">{{v$.username?.$errors[0].$message}}</span>
              </div>
              <div>
                <label
                  for="email"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                >Email</label
                >
                <input
                  v-model="resetForm.email"
                  @input="v$.email.$touch()"
                  type="email"
                  name="email"
                  id="email"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.email.$error && v$.email.$dirty)}"
                  placeholder="email"
                />
                <span v-if="v$.email.$error && v$.email.$dirty" class="text-xs text-red-500 dark:text-red-400">{{v$.email?.$errors[0].$message}}</span>
              </div>
              <div class="flex justify-between mt-8">
                <button @click="cancel" class="w-1/3 text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-800">Cancel</button>
                <button type="submit" class="w-1/3 text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">Reset</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
    <section v-else class="flex items-center justify-center h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
      <div class="w-full max-w-md overflow-hidden mx-3">
        <div class="px-4 py-2 mt-2">
          <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
            <LogoImg :size="6" :mr="1"/>
            <h1>Portal</h1>
          </div>
          <div class="flex items-center justify-center">
            <i class="fa-solid fa-check-circle text-green-500 text-7xl"></i>
          </div>
          <h2 class="text-2xl font-semibold text-center text-appText-light dark:text-appText-dark mt-8">An email has been sent to your account</h2>
          <p class="text-sm text-center text-gray-500 mt-2">
            Please check your email for further instructions.
          </p>
        </div>
      </div>
    </section>
  </div>
</template>