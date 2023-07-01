import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router"
import store from "@/store"

import PrimeVue from 'primevue/config'
import "primevue/resources/themes/mdc-light-indigo/theme.css"
import "primevue/resources/primevue.min.css"
import 'primeicons/primeicons.css'
import Tooltip from "primevue/tooltip"
import ConfirmationService from 'primevue/confirmationservice'
import ToastService from 'primevue/toastservice'
import BadgeDirective from 'primevue/badgedirective'

import 'boxicons'
import './assets/main.css'

createApp(App)
    .use(router)
    .use(store)
    .use(PrimeVue, {
        locale: {
            accept: 'Принять',
            reject: 'Отклонить',
        }
    })
    .use(ConfirmationService)
    .use(ToastService)
    .directive('tooltip', Tooltip)
    .directive('badge', BadgeDirective)
    .mount('#app')
