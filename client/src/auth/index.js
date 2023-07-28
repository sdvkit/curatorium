import Cookies from "js-cookie"
import router from "../router"
import jwt from "../jwt"
import store from "../store"
import api from "../api"

const auth = {
    get isAuthenticated() {
        const token = Cookies.get('jwt')
        return token !== undefined && !jwt.isExpired(token)
    },
    authenticate(tokenValue) {
        Cookies.set('jwt', tokenValue, { expires: jwt.getExpiration(tokenValue) })
        store.commit('SAVE_USER_INFO', jwt.getInMemoryUser)
        router.push({ path: '/' })
    },
    logout() {
        api.logoutUser()
            .then(() => {
                Cookies.remove('jwt')
                router.push({ path: '/login' })
            })
    }
}

export default auth