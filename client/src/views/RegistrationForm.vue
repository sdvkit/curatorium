<template>
    <div class="wrapper">
        <div class="form">
            <div class="features-side">
                <CuratoriumFeatures />
            </div>
            <div class="registration">
                <strong :style="{ fontSize: '18pt' }">
                    Регистрация
                </strong>
                <div class="inputs" :style="{ marginTop: '65px' }">
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <InputText v-tooltip="'Никнейм нужен для того, чтобы идентифицировать пользователя. Длинна никнейма должна быть больше 3 и меньше 16.'" id="username" v-model="username" :style="{ width: '100%' }" />
                        <label for="username">Никнейм</label>
                    </span>
                    <br>
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <InputText v-tooltip="'Полное имя исспользуется для того, чтобы другие люди могли вас опознать. Длинна полного имени должна быть больше 3 и меньше 26.'" id="full-name" v-model="fullName" :style="{ width: '100%' }" />
                        <label for="full-name">Полное имя</label>
                    </span>
                    <br>
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <Password v-tooltip="'Пароль исспользуется для защиты вашего аккаунта. Все пароли шифруются так, чтобы их невозможно было расшифровать. Длинна пароля должна быть больше 5 и меньше 31.'" v-model="password" inputId="password" toggleMask />
                        <label for="password">Пароль</label>
                    </span>
                    <br>
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <Password v-tooltip="'Повторный пароль должен совпадать с предыдущим.'" v-model="repeatPassword" :feedback="false" inputId="repeat-password" toggleMask />
                        <label for="repeat-password">Повторный пароль</label>
                    </span>
                </div>
                <br>
                <Button label="Зарегистрировать аккаунт" :style="{ width: '100%' }" @click="registration" />
                <br><br>
                <div class="w-full md:w-2">
                    <Divider layout="horizontal" class="hidden md:flex" align="center">Или</Divider>
                </div>
                <Button label="Войти в аккаунт" text :style="{ width: '100%' }" @click="this.$router.push({ path: '/login' })" />
            </div>
        </div>
    </div>
</template>

<script>
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Divider from 'primevue/divider'
import CuratoriumFeatures from '../components/CuratoriumFeatures.vue'
import api from '../api'

export default {
    name: 'registration-form',
    data() {
        return {
            username: '',
            fullName: '',
            password: '',
            repeatPassword: ''
        }
    },
    methods: {
        registration() {
            const usernameInput = document.getElementById('username')
            const fullNameInput = document.getElementById('full-name')
            const passwordInput = document.getElementById('password')
            const repeatPasswordInput = document.getElementById('repeat-password')

            if (this.username.length < 4 || this.username.length > 15) {
                usernameInput.classList.add('p-invalid')
                return
            }

            if (this.fullName.length < 4 || this.fullName.length > 25) {
                fullNameInput.classList.add('p-invalid')
                return
            }

            if (this.password.length < 6 || this.password.length > 30) {
                passwordInput.classList.add('p-invalid')
                return
            }

            if (this.password !== this.repeatPassword) {
                passwordInput.classList.add('p-invalid')
                repeatPasswordInput.classList.add('p-invalid')
                return
            }

            api.registration({
                fullName: this.fullName,
                username: this.username,
                password: this.password
            }).then(() => {
                if (api.isRegistrationError) {
                    this.$toast.add({ severity: 'error', summary: 'Ошибка при регистрации', detail: `Что-то пошло не так.`, life: 6000 })
                    api.isRegistrationError = false
                }
            })
        }
    },
    components: {
        Button,
        InputText,
        Password,
        Divider,
        CuratoriumFeatures
    }
}
</script>

<style scoped>
.wrapper {
    background: url('../assets/sign-in-bg.jpg') center no-repeat;
    background-size: 250vh;
    position: absolute;
    top: -1px;
    left: -1px;
    height: 100vh;
    width: 100%;
}

.form {
    display: flex;
    height: 650px;
    width: 80%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

.features-side {
    background: rgba(255, 255, 255, 0.7);
    height: 100%;
    width: 50%;
    padding: 80px;
}

.registration {
    height: 100%;
    width: 50%;
    background: #fff;
    padding: 80px;
}

.p-password {
    width: 100%;
}

@media screen and (max-width: 960px) {
    .wrapper {
        background: none;
    }
    
    .form {
        flex-direction: column;
        width: 100%;
        top: 0;
        left: 0;
        transform: none;
    }

    .registration {
        margin-top: 140px;
        width: 100%;
    }

    .features-side {
        width: 100%;
        padding: 80px;
    }

    .features li .description {
        width: 65%;
    }  
}

@media screen and (max-width: 850px) {
    .registration {
        margin-top: 280px;
        width: 100%;
    }
}

@media screen and (max-width: 700px) {
    .registration {
        margin-top: 280px;
        width: 100%;
    }
}

@media screen and (max-width: 550px) {
    .registration {
        margin-top: 460px;
    }
}
</style>