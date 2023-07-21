import axios from 'axios'
import auth from '../auth'

const api = {
    baseUrl: 'http://localhost:8081/api/v1',
    isLoginError: false,
    isRegistrationError: false,

    async login(loginBody) {
        await axios.post(`${this.baseUrl}/auth/login`, loginBody)
            .then((response) => auth.authenticate(response.data.value))
            .catch(() => { this.isLoginError = true })
    },
    async registration(registrationBody) {
        await axios.post(`${this.baseUrl}/auth/registration`, registrationBody)
            .then((response) => {
                if (response.status === 201) {
                    this.login({ username: registrationBody.username, password: registrationBody.password })
                }
            })
            .catch(() => this.isRegistrationError = true)
    }
}

export default api