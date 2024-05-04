<script setup lang="ts">

import { validPassword } from '@/validators';
import { computed, onMounted, reactive, ref } from 'vue'
import { helpers, sameAs } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { AuthStore } from '@/stores'
import { useRoute } from 'vue-router';
import router from '@/router'
import { isValidateToken } from '@/api'
import { ErrorModal, GreenButton, LogoImg } from '@/components'

const route = useRoute();
let token = route.query.token;

const authStore = AuthStore()

const form = reactive({
  password: '',
  confirmPassword: '',
  error: ''
})

const password = computed(() => form.password)

const rules = {
  password: {
    ...validPassword
  },
  confirmPassword: {
    sameAsPassword: helpers.withMessage('passwords do not match', sameAs(password))
  }
}

const v$ = useVuelidate(rules, form);

const loading = ref(true)
const done = ref(false)
const success = ref(false)

onMounted(async () => {
  try {
    if (!token) {
      console.log('no token')
      router.push({ name: 'login' })
    }
    const response = await isValidateToken(token as string)
    if (response.status !== 200) {
      done.value = true
      success.value = false
    }
  } catch (error: any) {
    done.value = true
    success.value = false
    console.log(error)
    displayError('An error occurred while resetting password')
  }
  loading.value = false
})

const resetPassword = async () => {
  const result = await v$.value.$validate();
  if (!result) {
    return;
  }
  loading.value = true;
  const response = await authStore.resetPassword(token as string,form.password)
  switch (response.status) {
    case 200:
      done.value = true
      success.value = true
      break;
    case 400:
      form.password = ''
      form.confirmPassword = ''
      form.error = 'You may not use an old password'
      break;
    case 500:
      form.password = ''
      form.confirmPassword = ''
      displayError('An error occurred while resetting password')
      break;
    default:
      displayError('An error occurred while resetting password')
  }
  loading.value = false
}

let message = ref('')
const show = ref(false)

const handleClose = () => {
  show.value = false
}
const displayError = (msg: string) => {
  message.value = msg
  show.value = true
}
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
              Change Password
            </h1>
            <form class="space-y-4 md:space-y-6" @submit.prevent="resetPassword">
              <div>
                <label
                  for="password"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                >Password</label
                >
                <input
                  v-model="form.password"
                  @input="v$.password.$touch(); form.error = ''"
                  type="password"
                  name="password"
                  id="password"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.password.$error && v$.password.$dirty) || form.error }"
                  placeholder="password"
                />
                <div class="h-4 text-xs text-red-500 dark:text-red-400">
                  <span v-if="v$.password.$error && v$.password.$dirty && !form.error">{{v$.password.$errors[0].$message}}</span>
                  <span v-if="form.error" class="text-xs text-red-500 dark:text-red-400">{{form.error}}</span>
                </div>
              </div>
              <div>
                <label
                  for="confirmPassword"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                >Confirm Password</label
                >
                <input
                  v-model="form.confirmPassword"
                  @input="v$.confirmPassword.$touch(); form.error = ''"
                  type="password"
                  name="confirmPassword"
                  id="confirmPassword"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.confirmPassword.$error && v$.confirmPassword.$dirty) || form.error }"
                  placeholder="confirm password"
                />
                <div class="h-4 text-xs text-red-500 dark:text-red-400">
                  <span v-if="v$.confirmPassword.$error && v$.confirmPassword.$dirty && !form.error">{{v$.confirmPassword.$errors[0].$message}}</span>
                  <span v-if="form.error" class="text-xs text-red-500 dark:text-red-400">{{form.error}}</span>
                </div>
              </div>
              <button type="submit" class="w-full text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">Change Password</button>
            </form>
          </div>
        </div>
      </div>
    </section>
    <section v-else class="flex items-center justify-center h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
      <div v-if="success" class="w-full max-w-md overflow-hidden mx-3">
        <div class="px-4 py-2 mt-2">
          <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
            <LogoImg :size="6" :mr="1"/>
            <h1>Portal</h1>
          </div>
          <div class="flex items-center justify-center">
            <i class="fa-solid fa-check-circle text-green-500 text-7xl"></i>
          </div>
          <h2 class="text-2xl font-semibold text-center text-appText-light dark:text-appText-dark mt-8">Your password has been changed successfully</h2>
          <p class="text-sm text-center text-gray-500 mt-2">
            You may proceed to login.
          </p>
          <div class="flex justify-center mt-8">
            <RouterLink :to="{name: 'login'}">
              <GreenButton>
                Login
              </GreenButton>
            </RouterLink>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="w-full max-w-md overflow-hidden mx-3">
          <div class="px-4 py-2 mt-2">
            <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
              <LogoImg :size="6" :mr="1"/>
              <h1>Portal</h1>
            </div>
            <div class="flex items-center justify-center">
              <i class="fa-solid fa-ban text-green-500 text-7xl"></i>
            </div>
            <h2 class="text-2xl font-semibold text-center text-appText-light dark:text-appText-dark mt-8">An error occurred while resetting password</h2>
            <p class="text-sm text-center text-gray-500 mt-2">
              Please try again later.
            </p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>