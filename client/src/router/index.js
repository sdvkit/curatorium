import {createRouter, createWebHashHistory} from "vue-router"
import StatementsPage from "@/views/StatementsPage.vue"
import NotFoundPage from "@/views/NotFoundPage.vue"
import LoginForm from "@/views/LoginForm.vue"
import RegistrationForm from "@/views/RegistrationForm.vue"

const routes = [
    {
        name: 'statements-page',
        path: '/',
        component: StatementsPage
    },
    {
        name: 'login-form',
        path: '/login',
        component: LoginForm
    },
    {
        name: 'registration-form',
        path: '/registration',
        component: RegistrationForm
    },
    {
        name: 'not-found',
        path: '/:pathMatch(.*)*',
        component: NotFoundPage
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router;