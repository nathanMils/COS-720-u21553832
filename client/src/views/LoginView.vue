<template>
  <div>
    <Modal v-show="show" :message="message" @close="handleClose"/>
    <section class="bg-appPrimary-light dark:bg-appPrimary-dark">
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
              Log in to Portal
            </h1>
            <form class="space-y-4 md:space-y-6" @submit.prevent="login">
              <div>
                <label
                  for="username"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                  >Username</label
                >
                <input
                  v-model="formData.username"
                  @input="v$.username.$touch(); formData.error = ''"
                  type="text"
                  name="username"
                  id="username"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.username.$error && v$.username.$dirty) || formData.error }"
                  placeholder="username"
                />
                <span v-if="v$.username.$error && v$.username.$dirty && !formData.error" class="text-xs text-red-500 dark:text-red-400">{{v$.username?.$errors[0].$message}}</span>
                <span v-if="formData.error" class="text-xs text-red-500 dark:text-red-400">{{formData.error}}</span>
              </div>
              <div>
                <label
                  for="password"
                  class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
                  >Password</label
                >
                <input
                  v-model="formData.password"
                  @input="v$.password.$touch(); formData.error = ''"
                  type="password"
                  name="password"
                  id="password"
                  :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.password.$error && v$.password.$dirty) || formData.error }"
                  placeholder="password"
                />
                <span v-if="v$.password.$error && v$.password.$dirty && !formData.error" class="text-xs text-red-500 dark:text-red-400">{{v$.password?.$errors[0].$message}}</span>
                <span v-if="formData.error" class="text-xs text-red-500 dark:text-red-400">{{formData.error}}</span>
              </div>
              <div class="flex items-center justify-between">
                <a
                  href="#"
                  class="text-sm font-medium text-primaryButton-600 hover:underline dark:text-primaryButton-500"
                  >Forgot password?</a
                >
              </div>
              <button type="submit" class="w-full text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">Sign in</button>
              <p class="text-sm font-light text-gray-500 dark:text-gray-400">
                Not a student yet?
                <RouterLink :to="{name: 'apply'}">
                  <a
                    href="#"
                    class="font-medium text-primaryButton-600 hover:underline dark:text-primaryButton-500"
                    >Apply</a
                  >
                </RouterLink>
              </p>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { LogoImg } from '@/components';
import { RouterLink } from 'vue-router';
import { AuthStore } from '@/stores';
import useVuelidate from '@vuelidate/core'
import { reactive, ref } from 'vue'
import { helpers, required } from '@vuelidate/validators'
import { Modal } from '@/components/modal'
import router from '@/router'

const authStore = AuthStore();

let message = ref('')
const show = ref(false)
const handleClose = () => {
  show.value = false
}
const displayError = (msg: string) => {
  message.value = msg
  show.value = true
}

const formData = reactive({
  username: "",
  password: "",
  error: "",
})

const rules = {
  username: {
    required: helpers.withMessage('Username is required', required),
  },
  password: {
    required: helpers.withMessage('Password is required', required),
  }
}

const v$ = useVuelidate(rules, formData);

const login = async () => {
  const result = await v$.value.$validate();
  if (result) {
    let answer = await authStore.Login(
      formData.username,
      formData.password
    )
    console.log(answer);
    switch (answer.status) {
      case 200:
        break;
      case 401:
        if (answer.message === 'INVALID_CREDENTIALS') {
          formData.username = ''
          formData.password = ''
          formData.error = 'Invalid username or password'
        } else if (answer.message === 'EMAIL_NOT_VERIFIED') {
          router.push({ name: 'confirm' })
          displayError('Please verify your email address')
        }
        break;
      default:
        formData.username = ''
        formData.password = ''
        displayError('An error occurred, please try again later')
        break;
    }
  }
}
</script>
