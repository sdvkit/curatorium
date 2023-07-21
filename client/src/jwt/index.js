import Cookies from 'js-cookie'
import jwt_decode from 'jwt-decode'

export default {
    decode(tokenValue) {
        return jwt_decode(tokenValue)
    },
    get isTokenPresents() {
        return Cookies.get('jwt') !== undefined
    },
    getExpiration(tokenValue) {
        const decodedToken = this.decode(tokenValue)
        const issuedAt = decodedToken.iat
        const expiresAt = decodedToken.exp

        return Math.ceil((expiresAt - issuedAt)  / (3600 * 24))
    },
    isExpired(tokenValue) {
        const decodedToken = this.decode(tokenValue)
        const expiresAt = new Date(decodedToken.exp * 1000)
        const now = new Date()

        return now > expiresAt
    },
    get getInMemoryUser() {
        const decodedToken = this.decode(Cookies.get('jwt'))
        return {
            username: decodedToken.sub,
            fullName: decodedToken.fullName
        }
    },
    get getInMemoryToken() {
        return Cookies.get('jwt')
    }
}