<template>
  <section class="flex items-center justify-center h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
    <div class="w-full max-w-md overflow-hidden mx-3">
      <div class="px-4 py-2 mt-2">
        <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
          <LogoImg :size="6" :mr="1"/>
          <h1>Portal</h1>
        </div>
        <h2 class="text-2xl font-semibold text-center text-appTertiary-light dark:text-appText-dark mt-2">Account Confirmation</h2>
        <p class="text-sm text-center text-gray-500 mt-2">
          We have sent a confirmation link to your email. Please check your inbox and click on the link to confirm your account. Once your account is confirmed, you can proceed to login.
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
  </section>
</template>

<script setup lang="ts">
import LogoImg from '@/components/logo/LogoImg.vue';
import { GreenButton } from '@/components'
</script>