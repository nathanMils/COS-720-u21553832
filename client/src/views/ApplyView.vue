<template>
  <div class="flex h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
    <ErrorModal v-show="show" :message="message" @close="handleClose"/>
    <div class="w-1/2 flex flex-col justify-center items-center space-y-4 md:space-y-6">
      <h1
        class="text-xl font-bold leading-tight tracking-tight text-center text-appText-light md:text-2xl dark:text-appText-dark"
      >
        Apply for Portal
      </h1>
      <form
        @submit.prevent="apply"
        class="w-3/5 space-y-4 md:space-y-3 max-h-screen overflow-auto"
      >
        <div class="flex space-x-4">
          <div class="w-1/2">
            <label
              for="firstName"
              class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
            >First Name</label
            >
            <input
              v-model="formData.firstName"
              @input="v$.firstName.$touch()"
              type="text"
              name="firstName"
              id="firstName"
              :class="{
                        'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                        'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.firstName.$error && v$.firstName.$dirty}"
              placeholder="first name"
            />
            <div class="h-4 text-xs text-red-500 dark:text-red-400">
              <span v-if="v$.firstName.$error && v$.firstName.$dirty">{{v$.firstName.$errors[0].$message}}</span>
            </div>
          </div>
          <div class="w-1/2">
            <label
              for="lastName"
              class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
            >Last Name</label
            >
            <input
              v-model="formData.lastName"
              @input="v$.lastName.$touch()"
              type="text"
              name="lastName"
              id="lastName"
              :class="{
                        'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                        'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.lastName.$error && v$.lastName.$dirty}"
              placeholder="last name"
            />
            <div class="h-4 text-xs text-red-500 dark:text-red-400">
              <span v-if="v$.lastName.$error && v$.lastName.$dirty">{{v$.lastName.$errors[0].$message}}</span>
            </div>
          </div>
        </div>
        <div>
          <label
            for="email"
            class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
          >Email</label
          >
          <input
            v-model="formData.email"
            @input="v$.email.$touch()"
            type="email"
            name="email"
            id="email"
            :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.email.$error && v$.email.$dirty}"
            placeholder="your@email.com"
          />
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.email.$error && v$.email.$dirty">{{v$.email.$errors[0].$message}}</span>
          </div>
        </div>
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
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': (v$.username.$error && v$.username.$dirty) || formData.error}"
            placeholder="username"
          />
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.username.$error && v$.username.$dirty && !formData.error">{{v$.username.$errors[0].$message}}</span>
            <span v-if="formData.error" class="text-xs text-red-500 dark:text-red-400">{{formData.error}}</span>
          </div>
        </div>
        <div>
          <label
            for="password"
            class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
          >Password</label
          >
          <input
            v-model="formData.password"
            @input="v$.password.$touch()"
            type="password"
            name="password"
            id="password"
            :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.password.$error && v$.password.$dirty }"
            placeholder="password"
          />
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.password.$error && v$.password.$dirty">{{v$.password.$errors[0].$message}}</span>
          </div>
        </div>
        <div>
          <label
            for="confirmPassword"
            class="block mb-2 text-sm font-medium text-appText-light dark:text-appText-dark"
          >Confirm Password</label
          >
          <input
            v-model="formData.confirmPassword"
            @input="v$.confirmPassword.$touch()"
            type="password"
            name="confirmPassword"
            id="confirmPassword"
            :class="{
                    'bg-gray-50 border border-appBorder-light text-appText-light sm:text-sm rounded-lg block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 focus:outline-none focus:ring-0': true,
                    'border-red-500 focus:border-red-500 focus:border-2 dark:border-red-400 dark:focus:border-red-400': v$.confirmPassword.$error && v$.confirmPassword.$dirty }"
            placeholder="confirm password"
          />
          <div class="h-4 text-xs text-red-500 dark:text-red-400">
            <span v-if="v$.confirmPassword.$error && v$.confirmPassword.$dirty">{{v$.confirmPassword.$errors[0].$message}}</span>
          </div>
        </div>
        <button
          type="submit"
          class="w-full text-white bg-primaryButton-600 mt-4 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800"
        >
          Apply
        </button>
        <p class="text-sm font-light text-gray-500 dark:text-gray-400">
          Already a student?
          <RouterLink :to="{name: 'login'}">
            <a
              href="#"
              class="font-medium text-primaryButton-600 hover:underline dark:text-primaryButton-500"
            >Login</a
            >
          </RouterLink>
        </p>
      </form>
    </div>
    <div class="w-1/2 flex flex-col justify-center items-center text-8xl font-bold text-appText-light dark:text-appText-dark">
      <LogoImg :size="6"/>
      <h1>Portal</h1>
    </div>
  </div>
</template>
  
  <script setup lang="ts">
  import { RouterLink } from 'vue-router'
  import { LogoImg } from '@/components';
  import { computed, reactive, ref } from 'vue'
  import useVuelidate from '@vuelidate/core'
  import { validEmail, validName, validPassword, validUsername } from '@/validators'
  import { helpers, required, sameAs } from '@vuelidate/validators'
  import { AuthStore } from '@/stores'
  import { ErrorModal } from '@/components/modal'

  const authStore = AuthStore()

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
    confirmPassword: "",
    email: "",
    firstName: "",
    lastName: "",
    error: ""
  })

  const password = computed(() => formData.password)

  const rules = {
    username: {
      ...validUsername
    },
    password: {
      ...validPassword
    },
    email: {
      ...validEmail
    },
    firstName: {
      required: helpers.withMessage('fist name is required', required),
      ...validName
    },
    lastName: {
      required: helpers.withMessage('last name is required', required),
      ...validName
    },
    confirmPassword: {
      sameAsPassword: helpers.withMessage('passwords do not match', sameAs(password))
    }
  }

  const v$ = useVuelidate(rules, formData);

  const apply = async () => {
    const result = await v$.value.$validate();
    if (result) {
      let answer = await authStore.Apply(
        formData.username,
        formData.password,
        formData.email,
        formData.firstName,
        formData.lastName
      )
      switch (answer.status) {
        case 200:
          break;
        case 409:
          formData.username = ''
          formData.error = 'username already exists'
          break;
        case 500:
          console.log(500)
          displayError('An error occurred')
          break;
        default:
          alert('An error occurred')
          break;
      }
    }
  }
  </script>
  