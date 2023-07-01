<template>
    <div class="wrapper">
        <div class="form">
            <div class="features-side">
                <CuratoriumFeatures />
            </div>
            <div class="login">
                <strong :style="{ fontSize: '18pt' }">
                    Вход в систему
                </strong>
                <div class="inputs" :style="{ marginTop: '65px' }">
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <InputText id="username" v-model="username" :style="{ width: '100%' }" />
                        <label for="username">Никнейм</label>
                    </span>
                    <br>
                    <span class="p-float-label" :style="{ width: '100%' }">
                        <Password v-model="password" :feedback="false" inputId="password" toggleMask />
                        <label for="password">Пароль</label>
                    </span>
                </div>
                <br>
                <Button label="Войти" :style="{ width: '100%' }" @click="login" />
                <br><br>
                <div class="w-full md:w-2">
                    <Divider layout="horizontal" class="hidden md:flex" align="center">Или</Divider>
                </div>
                <Button label="Зарегистрироваться" text :style="{ width: '100%' }" @click="this.$router.push({ path: '/registration' })" />
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

export default {
    name: 'login-form',
    data() {
        return {
            username: '',
            password: ''
        }
    },
    methods: {
        login() {
            this.$router.push({ path: '/' })
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

.login {
    height: 100%;
    width: 50%;
    background: #fff;
    padding: 80px;
}

.features {
    position: absolute;
    left: 40px;
    list-style-type: none;
}

.features li {
    display: flex;
    align-items: start;
    margin-bottom: 20px;
}

.features li button {
    margin-right: 20px;
}

.features li .description {
    width: 35%;
}  

.features li .description span {
    opacity: 0.6;
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

    .login {
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
    .login {
        margin-top: 280px;
        width: 100%;
    }
}

@media screen and (max-width: 700px) {
    .login {
        margin-top: 280px;
        width: 100%;
    }
}

@media screen and (max-width: 550px) {
    .login {
        margin-top: 460px;
    }
}
</style>