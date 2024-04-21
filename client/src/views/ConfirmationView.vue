<template>
  <section class="bg-appPrimary-light dark:bg-appPrimary-dark">
    <div class="flex flex-col space-y-10 items-center justify-center py-8 mx-auto md:h-screen lg:py-0">
      <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
        <LogoImg :size="6" :mr="1"/>
        <h1>Portal</h1>
      </div>
      <div
        class="w-full bg-white rounded-lg shadow-xl border border-appBorder-light dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-appSecondary-dark dark:border-appBorder-dark dark:shadow-none"
      >
        <div class="p-6 space-y-4 md:space-y-10 sm:p-8">
          <h1
            class="text-xl font-bold leading-tight tracking-tight text-center text-appText-light md:text-xl dark:text-appText-dark"
          >
            Please check your email to confirm your account
          </h1>
          <button v-on:click="router.push({name: 'login'})" class="w-3/6 m-auto block text-white bg-primaryButton-600 hover:bg-primaryButton-700 focus:ring-4 focus:outline-none focus:ring-primaryButton-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primaryButton-600 dark:hover:bg-primaryButton-700 dark:focus:ring-primaryButton-800">Proceed to Login</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import LogoImg from '@/components/logo/LogoImg.vue';
import router from '@/router';
</script>