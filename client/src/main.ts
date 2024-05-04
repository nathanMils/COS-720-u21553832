import './assets/tailwind.css'

import { createApp, ref } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import axios from 'axios'

const isLoading = ref(false)

const app = createApp(App)
app.provide('isLoading', isLoading)
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL
app.config.globalProperties.$axios = axios
app.use(createPinia())
app.use(router)

app.mount('#app')
