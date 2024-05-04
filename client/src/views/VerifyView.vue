<script setup lang="ts">
import { onMounted, ref } from 'vue'
import router from '@/router';
import { LogoImg, LoadingComponent, GreenButton } from '@/components'
import { AuthStore} from '@/stores'

const authStore = AuthStore();

const message = ref("");
const success = ref(true);
const loading = ref(true);
onMounted(async () => {
  try {
    if (!router.currentRoute.value.query.token || typeof router.currentRoute.value.query.token !== 'string') {
      router.push({ name: 'login' })
      return;
    }
    console.log(router.currentRoute.value.query.token)
    const response = await authStore.VerifyEmail(router.currentRoute.value.query.token); // replace with your actual API function
    if (response.status !== 200) {
      message.value = 'Unfortunately this link has expired or is invalid. Please try again.';
      success.value = false;
    } else {
      message.value = 'Your account has been activated. You can now login.';
    }
  } catch (error: any) {
    console.error('Error:', error.response);
    message.value = 'An error occurred. Please try again later.';
    success.value = false;
  }
  loading.value = false;
});
</script>

<template>
  <section class="flex items-center justify-center h-screen bg-appPrimary-light dark:bg-appPrimary-dark">
    <div class="w-full max-w-md overflow-hidden mx-3">
      <div class="px-4 py-2 mt-2">
        <div class="flex flex-row items-center justify-center text-7xl mb-8 font-bold text-appText-light dark:text-appText-dark">
          <LogoImg :size="6" :mr="1"/>
          <h1>Portal</h1>
        </div>
        <div v-if="!loading" >
          <h2 class="text-2xl font-semibold text-center text-appTertiary-light dark:text-appText-dark mt-2">Account Activation</h2>
          <p class="text-sm text-center text-gray-500 mt-2">
            {{ message }}
          </p>
          <div class="flex justify-center mt-8">
            <RouterLink :to="{name: 'login'}">
              <GreenButton>
                Login
              </GreenButton>
            </RouterLink>
          </div>
        </div>
        <div v-else>
          <LoadingComponent />
        </div>
      </div>
    </div>
  </section>
</template>