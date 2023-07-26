<template>
    <div class="wrapper" v-if="getCurrentUrlPath === '/'">
        <Toast></Toast>
        <ConfirmDialog></ConfirmDialog>
        <ContextMenu ref="menu" :model="subjectContextMenuItems"/>
        <ContextMenu ref="statementMenu" :model="statementContextMenuItems" />
        <ContextMenu ref="studentMenu" :model="studentContextMenuItems" />

        <NewStatementDialog v-model:visible="createStatementDialogParams.visible" :params="createStatementDialogParams" />
        <StatementInfoDialog v-model:visible="firstLvlStatementDialogParams.visible" :params="firstLvlStatementDialogParams" />
        <EditStatementDialog v-model:visible="editStatementDialogParams.visible" :params="editStatementDialogParams" />
        <EditStudentDialogVue v-model:visible="editStudentDialogParams.visible" :params="editStudentDialogParams" />

        <Dialog v-model:visible="isTerminalDialogVisible" :style="{ width: '42rem' }" header="Терминал" modal>
            <Terminal :welcomeMessage="`New terminal session started: ${new Date()}`" prompt="root:~$ㅤ" />
        </Dialog>

        <Dialog v-model:visible="isNewGroupDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '25rem' }" header="Добавить группу" modal>        
            <div :style="{ display: 'flex', marginTop: '10px' }">
                <span :style="{ width: 'calc(100% - 150px)' }" class="p-float-label">
                    <InputText id="group-name" v-model="newGroupName" v-tooltip="'Длинна названия должна быть больше 3 и меньше 15'" :style="{ width: '100%' }" />
                    <label for="group-name">Название группы</label>
                </span>
                <span class="p-float-label" :style="{ width: '140px', position: 'absolute', right: '20px' }">
                    <Calendar v-tooltip="'Год формирования группы не должен быть позднее настоящего.'" v-model="newGroupYear" inputId="group-date" view="year" dateFormat="yy" />
                    <label for="group-date">Год форм.</label>
                </span>
            </div>
            <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить группу" @click="saveGroup"/>
        </Dialog>

        <Dialog v-model:visible="isRenameGroupDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '20rem' }" header="Переименовать группу" modal>
            <span :style="{ marginTop: '5px' }" class="p-float-label">
                <InputText id="group-name" v-model="newGroupName" v-tooltip="'Длинна названия должна быть больше 3, меньше 15 и не совпадать с предыдущим названием'" :style="{ width: '100%' }"/>
                <label for="group-name">Название группы</label>
            </span>
            <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить" @click="renameGroup"/>
        </Dialog>

        <Dialog v-model:visible="isNewStudentDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '20rem' }" header="Добавить учащегося" modal>
            <span :style="{ marginTop: '5px' }" class="p-float-label">
                <InputText id="student-name" v-model="newStudentName" v-tooltip="'Длинна ФИО должна быть больше 4 и меньше 50'" :style="{ width: '100%' }"/>
                <label for="student-name">ФИО учащегося</label>
            </span>
            <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить" @click="saveStudent"/>
        </Dialog>

        <Dialog v-model:visible="isNewSubjectDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '20rem' }" header="Добавить предмет" modal>
            <span :style="{ marginTop: '5px' }" class="p-float-label">
                <InputText id="subject-name" v-model="newSubjectName" v-tooltip="'Длинна названия должна быть больше 1 и меньше 50. Название предмета не должно совпадать со служебными названиями (например \'label\')'" :style="{ width: '100%' }"/>
                <label for="subject-name">Название предмета</label>
            </span>
            <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить" @click="saveSubject"/>
        </Dialog>

        <Dialog v-model:visible="isRenameSubjectDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '20rem' }" header="Переименовать предмет" modal>
            <span :style="{ marginTop: '5px' }" class="p-float-label">
                <InputText id="subject-name" v-model="newSubjectName" v-tooltip="'Длинна названия должна быть больше 2, меньше 50 и не совпадать с предыдущим названием. Название предмета не должно совпадать со служебными названиями (например \'label\')'" :style="{ width: '100%' }"/>
                <label for="subject-name">Название предмета</label>
            </span>
            <Button :style="{ marginTop: '10px', width: '100%' }" icon="pi pi-check" label="Сохранить" @click="renameSubject"/>
        </Dialog>

        <Dialog v-model:visible="isOpenedGroupDialogVisible" :breakpoints="{ '960px': '75vw', '641px': '100vw' }" :style="{ width: '40rem' }" modal>
            <template #header>
                <span :style="{ fontSize: '14pt' }">
                    {{ (openedGroup !== null) ? openedGroup.label : '' }}
                    <Button icon="pi pi-pencil" @click="() => {
                        editableGroup = openedGroup
                        newGroupName = openedGroup.name
                        isRenameGroupDialogVisible = true
                    }" text :style="{ marginBottom: '-5px' }" />
                </span>
            </template>
            <TabView>
                <TabPanel header="О группе">
                    <div :style="{ display: 'flex', width: '100%' }">
                        <div class="about-group">
                            <Chip icon="pi pi-clock" :label="`Создано: ${ formatDate(openedGroup.createdAt) }`" />
                            <Chip icon="pi pi-calendar" :label="`Год формирования: ${ openedGroup.year }`" />
                            <Chip icon="pi pi-users" :label="`Всего ${ openedGroup.items[0].items.length } учащихся`" />
                            <Chip icon="pi pi-file" :label="`Всего ${ getGroupUsages.length } ведомостей`" />
                        </div>
                    </div>
                    <Button v-show="!openedGroup.isArchived" label="Архивировать группу" icon="pi pi-eye-slash" :style="{ marginTop: '10px' }" severity="danger" @click="archiveOpenedGroup" />
                    <Button v-show="openedGroup.isArchived" label="Разархивировать группу" icon="pi pi-eye" :style="{ marginTop: '10px' }" @click="dearchiveOpenedGroup" />
                    <br>
                    <Button v-show="openedGroup.isArchived" label="Удалить группу" icon="pi pi-trash" :style="{ marginTop: '10px' }" severity="danger" @click="deleteGroup" />
                </TabPanel>
                <TabPanel header="Учащиеся">
                    <DataTable :value="openedGroup.items[0].items" contextMenu v-model:contextMenuSelection="editStudentDialogParams.student" @row-contextmenu="showStudentContextMenu"
                            scrollable scroll-height="260px" v-model:filters="filters" :globalFilterFields="['label']"
                            responsiveLayout="stack" breakpoint="960px">
                        <template #empty>Пусто</template>
                        <template #header>
                            <span class="p-input-icon-left">
                                <i class="pi pi-search" />
                                <InputText v-model="filters['global'].value" placeholder="Поиск" class="p-inputtext-sm" />
                            </span>
                            <Button v-show="!openedGroup.isArchived" label="Добавить" icon="pi pi-plus" :style="{ position: 'absolute', right: '0' }" @click="() => {
                                newStudentGroup = openedGroup
                                isNewStudentDialogVisible = true
                            }" />
                        </template>
                        <Column header="Имя" field="label" frozen />
                    </DataTable>
                </TabPanel>
                <TabPanel header="Ведомости">
                    <DataTable :value="getGroupUsages" @row-click="openStatement"
                            scrollable scroll-height="260px" v-model:filters="filters" :globalFilterFields="['label']"
                            @row-contextmenu="showStatementContextMenu" v-model:contextMenuSelection="editStatementDialogParams.statement" 
                            responsiveLayout="stack" breakpoint="960px">
                        <template #empty>Пусто</template>
                        <template #header>
                            <span class="p-input-icon-left">
                                <i class="pi pi-search" />
                                <InputText v-model="filters['global'].value" placeholder="Поиск" class="p-inputtext-sm" />
                            </span>
                            <Button v-show="!openedGroup.isArchived" label="Создать" icon="pi pi-plus" @click="() => {
                                createStatementDialogParams.visible = true
                                createStatementDialogParams.currentGroup = openedGroup
                            }" :style="{ position: 'absolute', right: '0' }" />
                        </template>
                        <Column header="Название" field="label" frozen />
                    </DataTable>
                </TabPanel>
            </TabView>
        </Dialog>

        <div class="wrapper">
            <div class="nav-panel">
                <Button v-tooltip="'Это мы - Curatorium'" text @mouseenter="startListenCnsl" @mouseleave="stopListenCnsl">
                    <template #icon>
                        <img src="../public/logo.png" :style="{ height: '27px', width: '27px' }" />
                    </template>
                </Button>
                <hr :style="{ width: '100%', marginTop: '3px' }">
                <Button v-tooltip="'Группы'" icon="pi pi-users" @click="toggleGroupMenu"/>
                <Button v-tooltip="'Предметы'" icon="pi pi-book" @click="toggleSubjectsMenu"/>
                <Button v-tooltip="'Архив'" icon="pi pi-inbox" @click="toggleArchiveMenu"/>
                <Button v-tooltip="'Профиль'" icon="pi pi-user" @click="toggleProfileMenu"/>
            </div>

            <Splitter class="mb-5" style="height: 100vh">
                <SplitterPanel v-show="isSideMenuVisible" :min-size="20" :size="20" :style="{ background: '#f7f8fa', padding: '5px' }" class="flex align-items-center justify-content-center">
                    <div v-show="isGroupMenuVisible">
                        <Button :style="{ float: 'left' }" label="Группы" text/>
                        <Button v-tooltip="'Добавить группу'" :style="{ float: 'right' }" icon="pi pi-plus" text @click="isNewGroupDialogVisible = true"/>
                        <Button v-show="JSON.stringify(groupsExpandedKeys) !== '{}'" v-tooltip="'Свернуть все'"
                                :style="{ float: 'right' }" icon="pi pi-window-minimize" text @click="groupsExpandedKeys = {}"/>
                        <br><br>
                        <hr>
                        <ScrollPanel style="width: 100%; height: 90vh">
                            <PanelMenu v-model:expanded-keys="groupsExpandedKeys" :model="getNotArchivedGroupsMenuItems" /> 
                        </ScrollPanel>
                    </div>

                    <div v-show="isSubjectsMenuVisible">
                        <Button :style="{ float: 'left' }" label="Предметы" text/>
                        <Button v-tooltip="'Добавить предмет'" :style="{ float: 'right' }" icon="pi pi-plus"
                                text @click="isNewSubjectDialogVisible = true"/>
                        <br><br>
                        <hr>
                        <ScrollPanel v-show="getSubjectsMenuItems.length > 0" style="width: 100%; height: 90vh">
                            <Menu :model='getSubjectsMenuItems'>
                                <template #item="slotProps">
                                    <div class="subject-menu-item" @contextmenu="(event) => { selectSubject(event, slotProps.item) }">
                                        <i :class="slotProps.item.icon"></i>
                                        <span>{{ slotProps.item.label }}</span>
                                    </div>
                                </template>
                            </Menu>
                        </ScrollPanel>
                    </div>

                    <div v-show="isArchiveMenuVisible">
                        <Button :style="{ float: 'left' }" label="Архив" text/>
                        <Button v-show="getArchivedGroupsMenuItems.length > 0" v-tooltip="'Очистить архив'" :style="{ float: 'right' }" icon="pi pi-trash" severity="danger"
                            text @click="clearArchive"/>
                        <Button v-show="JSON.stringify(archivedGroupsExpandedKeys) !== '{}'" v-tooltip="'Свернуть все'"
                            :style="{ float: 'right' }" icon="pi pi-window-minimize" text @click="archivedGroupsExpandedKeys = {}"/>
                        <br><br>
                        <hr>
                        <ScrollPanel style="width: 100%; height: 90vh">
                            <PanelMenu v-model:expanded-keys="archivedGroupsExpandedKeys" :model="getArchivedGroupsMenuItems" /> 
                        </ScrollPanel>
                    </div>

                    <div v-show="isProfileMenuVisible" :style="{ textAlign: 'center' }">
                        <Button :style="{ float: 'left' }" label="Профиль" text/>
                        <Button v-tooltip="'Выйти из аккаунта'" icon="pi pi-sign-out" @click="exitFromProfile" text :style="{ float: 'right' }" severity="danger" />
                        <br><br><hr><br>
                        <Avatar v-badge.success="'Онлайн'" class="p-overlay-badge" label="N" size="xlarge" shape="circle" :style="{ marginTop: '20px' }" />
                        <br><br>
                        <span :style="{ fontSize: '14pt'}" v-if="!isProfileEditModeEnabled">
                            {{ getUser.fullName }}
                        </span>
                        <span class="p-float-label" v-else :style="{ width: '250px', marginLeft: 'calc((100% - 250px) / 2)' }">
                            <InputText v-tooltip="'Длинна полного имени должна быть больше 3 и меньше 26'" id="full-name" v-model="newProfileName" class="p-inputtext-sm" :style="{ width: '100%' }" />
                            <label for="full-name">Полное имя</label>
                        </span>
                        <br>
                        <span :style="{ color: 'var(--primary-color)' }" v-if="!isProfileEditModeEnabled">
                            ({{ getUser.username }})
                        </span>
                        <span class="p-float-label" v-else :style="{ width: '250px', marginLeft: 'calc((100% - 250px) / 2)' }">
                            <InputText v-tooltip="'Длинна никнейма должна быть больше 3 и меньше 16'" id="username" v-model="newProfileUsername" class="p-inputtext-sm" :style="{ width: '100%' }"/>
                            <label for="username">Никнейм</label>
                        </span>
                        <br><br v-if="!isProfileEditModeEnabled">
                        <Button v-if="!isProfileEditModeEnabled" label="Редактировать профиль" icon="pi pi-pencil" size="small" @click="editProfile" />
                        <Button v-else label="Сохранить изменения" icon="pi pi-check" @click="saveProfileChanges" :style="{ width: '250px' }" />
                    </div> 
                </SplitterPanel>

                <SplitterPanel :min-size="30" :size="80" class="flex align-items-center justify-content-center">
                    <Breadcrumb :home="{ icon: 'pi pi-home',to: '/' }" :model="getBreadcrumbMenuItems"/>
                    <ScrollPanel style="width: 100%; height: 90vh">
                        <router-view/>
                    </ScrollPanel>
                </SplitterPanel>
            </Splitter>
        </div>
    </div>
    <router-view v-else />
</template>

<script>
import { mapGetters, mapActions } from "vuex"
import { FilterMatchMode } from "primevue/api"
import { useRoute } from 'vue-router'
import cnsl from '../cnsl.json'
import terminalHandler from './terminal/index'
import Splitter from 'primevue/splitter'
import SplitterPanel from 'primevue/splitterpanel'
import Button from "primevue/button"
import ScrollPanel from 'primevue/scrollpanel'
import PanelMenu from 'primevue/panelmenu'
import Dialog from "primevue/dialog"
import InputText from "primevue/inputtext"
import ConfirmDialog from 'primevue/confirmdialog'
import Toast from "primevue/toast"
import Menu from 'primevue/menu'
import ContextMenu from 'primevue/contextmenu'
import Breadcrumb from 'primevue/breadcrumb'
import DataTable from "primevue/datatable"
import Column from "primevue/column"
import TabView from 'primevue/tabview'
import TabPanel from 'primevue/tabpanel'
import Calendar from 'primevue/calendar'
import Chip from 'primevue/chip'
import NewStatementDialog from "./components/NewStatementDialog.vue"
import StatementInfoDialog from "./components/FitstLvlStatementDialog.vue"
import EditStatementDialog from "./components/EditStatementDialog.vue"
import EditStudentDialogVue from "./components/EditStudentDialog.vue"
import Terminal from 'primevue/terminal'
import TerminalService from 'primevue/terminalservice'
import Avatar from 'primevue/avatar'
import auth from "./auth"
import jwt from './jwt'

export default {
    mounted() {
        TerminalService.on('command', terminalHandler.handle)

        if (jwt.isTokenPresents) {
            this.loadUser()
            this.loadSubjects()
            this.loadGroups([
                (event) => this.openGroup(event),
                (event) => this.openRenameGroupDialod(event),
                (event) => this.openAddStudentDialog(event)
            ])
                .then(() => this.loadFirstLvlStatements())
        }
    },
    beforeUnmount() {
        TerminalService.off('command', terminalHandler.handle)
    },
    data() {
        return {
            cnslCd: '',
            isTerminalDialogVisible: false,
            isSideMenuVisible: true,
            isGroupMenuVisible: true,
            isSubjectsMenuVisible: false,
            isArchiveMenuVisible: false,
            isProfileMenuVisible: false,
            isNewGroupDialogVisible: false, newGroupName: '', newGroupYear: null,
            isRenameGroupDialogVisible: false, editableGroup: { key: '', label: '', icon: '', items: [] },
            isNewSubjectDialogVisible: false, newSubjectName: '',
            isRenameSubjectDialogVisible: false,
            isNewStudentDialogVisible: false, newStudentName: '', newStudentGroup: null,
            isOpenedGroupDialogVisible: false, openedGroup: null,
            editStatementDialogParams: { visible: false, statement: null, editableName: '' },
            createStatementDialogParams: { visible: false, currentGroup: null },
            editStudentDialogParams: { visible: false, student: null, editableName: '' },
            isProfileEditModeEnabled: false, newProfileName: '', newProfileUsername: '', 
            firstLvlStatementDialogParams: {
                visible: false,
                contentHeight: 400,
                sizeIcon: 'pi pi-window-maximize',
                group: null,
                columns: [],
                students: [],
                subjects: [],
                statementName: ''
            },
            subjectContextMenuItems: [
                {
                    label: 'Переименовать', icon: 'pi pi-pencil', command: () => {
                        this.newSubjectName = this.selectedSubject.label
                        this.isRenameSubjectDialogVisible = true
                    }
                },
                { label: 'Удалить', icon: 'pi pi-trash', command: () => this.deleteSubject() }
            ],
            statementContextMenuItems: [
                {
                    label: 'Переименовать', icon: 'pi pi-pencil', command: () => {
                        this.editStatementDialogParams.editableName = this.editStatementDialogParams.statement.label
                        this.editStatementDialogParams.visible = true
                    }
                },
                { label: 'Удалить', icon: 'pi pi-trash', command: () => this.deleteStatement() }
            ],
            studentContextMenuItems: [
                { 
                    label: 'Переименовать', icon: 'pi pi-pencil', command: () => {
                        this.editStudentDialogParams.editableName = this.editStudentDialogParams.student.label
                        this.editStudentDialogParams.visible = true 
                    }
                },
                { label: 'Удалить', icon: 'pi pi-trash', command: () => this.deleteStudent() }
            ],
            groupsExpandedKeys: {}, archivedGroupsExpandedKeys: {},
            selectedSubject: {},
            filters: {
                global: { value: null, matchMode: FilterMatchMode.CONTAINS },
                label: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
            },
        }
    },
    computed: {
        ...mapGetters([
            'getGroupsMenuItems', 'getSubjectsMenuItems', 'getBreadcrumbMenuItems', 'getStatements', 
            'getArchivedGroupsMenuItems', 'getNotArchivedGroupsMenuItems', 'getUser'
        ]),
        getGroupUsages() {
            const usages = []

            this.getStatements.forEach((statement) => {
                if (statement.group === this.openedGroup) {
                    usages.push(statement)
                }
            })

            return usages
        },
        getCurrentUrlPath() {
            return useRoute().path
        },
    },
    methods: {
        ...mapActions(['loadUser', 'loadGroups', 'loadSubjects', 'loadFirstLvlStatements']),
        openRenameGroupDialod(event) {
            this.editableGroup = this.getGroupsMenuItems.find((menuItem) => menuItem.key === event.item.key.replace('_rename', ''))
                    
            if (this.editableGroup === undefined) {
                return
            }

            if (this.editableGroup.isArchived) {
                this.$toast.add({ severity: 'info', summary: 'Отмена', detail: `Нельзя переименовать архивированную группу.`, life: 3000 })
                return
            } 
                
            this.newGroupName = this.editableGroup.name
            this.isRenameGroupDialogVisible = true
        },
        openAddStudentDialog(event) {
            this.newStudentGroup = this.getGroupsMenuItems.find((menuItem) => menuItem.key === event.item.key.replace('_add_student', ''))

            if (this.newStudentGroup === undefined) {
                return
            }

            if (this.newStudentGroup.isArchived) {
                this.$toast.add({ severity: 'info', summary: 'Отмена', detail: `Нельзя добавить учащегося в архивированную группу.`, life: 3000 })
                return
            } 
                    
            this.isNewStudentDialogVisible = true
        },
        exitFromProfile() {
            this.$confirm.require({
                message: `Вы действительно хотите выйти из аккаунта?`,
                header: 'Потдверждение выхода',
                icon: 'pi pi-info-circle',        
                accept: () => auth.logout(),
                reject: () => {}
            })
        },
        editProfile() {
            this.isProfileEditModeEnabled = true
            this.newProfileName = this.getUser.fullName
            this.newProfileUsername = this.getUser.username
        },
        saveProfileChanges() {
            const fullNameInput = document.getElementById('full-name')
            const usernameInput = document.getElementById('username')

            if (this.newProfileName.length < 4 || this.newProfileName.length > 25) {
                fullNameInput.classList.add('p-invalid')
                return;
            }

            if (this.newProfileUsername.length < 4 || this.newProfileUsername.length > 15) {
                usernameInput.classList.add('p-invalid')
                return;
            }
        
            this.$confirm.require({
                message: `Вы действительно хотите сохранить изменения в профиле?`,
                header: 'Потдверждение изменения',
                icon: 'pi pi-info-circle',        
                accept: () => {
                    this.$store.commit('SAVE_PROFILE_CHANGES', [this.newProfileUsername, this.newProfileName])
                    this.$toast.add({ severity: 'info', summary: 'Изменено', detail: `Профиль был изменен`, life: 3000 })
                    
                    this.newProfileName = ''
                    this.newProfileUsername = ''
                    this.isProfileEditModeEnabled = false
                },
                reject: () => {
                    this.newProfileName = ''
                    this.newProfileUsername = ''
                    this.isProfileEditModeEnabled = false
                }
            })
        },
        startListenCnsl() {
            window.onkeydown = (event) => {
                this.cnslCd += event.key
            }
        },
        stopListenCnsl() {
            window.onkeydown = () => null
            this.isTerminalDialogVisible = this.cnslCd === cnsl.cd
            this.cnslCd = ''
        },
        formatDate(date) {
            const day = date.getDate().toString().padStart(2, '0')
            const month = (date.getMonth() + 1).toString().padStart(2, '0')
            const year = date.getFullYear()
            return `${day}.${month}.${year}`
        },
        toggleGroupMenu() {
            this.isSideMenuVisible = !(this.isGroupMenuVisible && this.isSideMenuVisible)
            this.isGroupMenuVisible = true
            this.isSubjectsMenuVisible = false
            this.isProfileMenuVisible = false
            this.isArchiveMenuVisible = false
        },
        toggleSubjectsMenu() {
            this.isSideMenuVisible = !(this.isSubjectsMenuVisible && this.isSideMenuVisible)
            this.isSubjectsMenuVisible = true
            this.isGroupMenuVisible = false
            this.isProfileMenuVisible = false
            this.isArchiveMenuVisible = false
        },
        toggleArchiveMenu() {
            this.isSideMenuVisible = !(this.isArchiveMenuVisible && this.isSideMenuVisible)
            this.isArchiveMenuVisible = true
            this.isSubjectsMenuVisible = false
            this.isGroupMenuVisible = false
            this.isProfileMenuVisible = false
        },
        toggleProfileMenu() {
            this.isSideMenuVisible = !(this.isProfileMenuVisible && this.isSideMenuVisible)
            this.isProfileMenuVisible = true
            this.isSubjectsMenuVisible = false
            this.isGroupMenuVisible = false
            this.isArchiveMenuVisible = false
        },
        showStatementContextMenu(event) {
            this.$refs.statementMenu.show(event.originalEvent);
        },
        showStudentContextMenu(event) {
            this.$refs.studentMenu.show(event.originalEvent);
        },
        getCurrentStatement() {
            return this.getStatements.find((statement) => statement.label === this.firstLvlStatementDialogParams.statementName)
        },
        getStudentsMarkBySubjectKey(student, subjectKey) {
            const foundMark = student.marks.find((mark) => {
                const secondLvlStatement = this.getSecondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey)
                const subject = secondLvlStatement.subject
                return subject.key === subjectKey
            })
            
            return (foundMark === undefined) ? 0 : foundMark.value
        },
        clearArchive() {
            this.$confirm.require({
                    message: `Вы действительно хотите очистить архив? Все ведомости и группы из архива будут удалены! `,
                    header: 'Подтверждение очистки',
                    icon: 'pi pi-info-circle',                    
                    acceptClass: 'p-button-danger',
                    accept: () => {
                        this.$store.commit('CLEAR_ARCHIVE')
                        this.$toast.add({ severity: 'error', summary: 'Очищено', detail: `Архив был очищен`, life: 3000 })
                    },
                    reject: () => {}
                })
        },
        deleteGroup() {
            this.$confirm.require({
                    message: `Вы действительно хотите удалить группу ${this.openedGroup.label}? Вместе с этой группой удалятся все ведомости, исспользующие ее.`,
                    header: 'Подтверждение удаления',
                    icon: 'pi pi-info-circle',                    
                    acceptClass: 'p-button-danger',
                    accept: () => {
                        this.$store.commit('DELETE_GROUP', this.openedGroup)
                        this.$toast.add({ severity: 'error', summary: 'Удалено', detail: `Группа ${this.openedGroup.label} удалена`, life: 3000 })
                        this.isOpenedGroupDialogVisible = false
                    },
                    reject: () => {}
                })
        },
        deleteStudent() {
            this.$confirm.require({
                message: `Вы действительно хотите удалить учащегося \"${this.editStudentDialogParams.student.label}\" из группы \"${this.openedGroup.label}\"?`,
                header: 'Подтверждение удаления',
                icon: 'pi pi-info-circle',
                acceptClass: 'p-button-danger',
                accept: () => {
                    this.$store.commit('DELETE_STUDENT', [this.editStudentDialogParams.student, this.openedGroup])
                    this.$toast.add({ severity: 'error', summary: 'Удалено', detail: `Учащийся \"${this.editStudentDialogParams.student.label}\" удален из группы ${this.openedGroup.label}`, life: 3000 })
                    this.editStudentDialogParams.student = null
                },
                reject: () => {}
            });
        },
        openStatement(event) {
            this.firstLvlStatementDialogParams.columns = [{ label: 'Учащиеся', field: 'label' }]
            this.firstLvlStatementDialogParams.students = []
            this.firstLvlStatementDialogParams.statement = event.data
            this.firstLvlStatementDialogParams.group = event.data.group

            event.data.group.items[0].items.forEach((student) => {
                const entity = {}
                entity.label = student.label
                event.data.secondLvlStatements.forEach(secondLvlStatement => 
                    entity[secondLvlStatement.subject.label] = this.getMarkBySecondLvlStatment(student, secondLvlStatement))
                this.firstLvlStatementDialogParams.students.push(entity)
            })

            event.data.secondLvlStatements.forEach(secondLvlStatement => this.firstLvlStatementDialogParams.columns.push(
                { label: secondLvlStatement.subject.label, field: secondLvlStatement.subject.label }
            ))

            this.firstLvlStatementDialogParams.subjects = this.firstLvlStatementDialogParams.columns.slice(1, this.firstLvlStatementDialogParams.columns.length)
            this.firstLvlStatementDialogParams.visible = true
        },
        deleteStatement() {
            this.$confirm.require({
                message: `Вы действительно хотите удалить ведомость \"${this.editStatementDialogParams.statement.label}\"?`,
                header: 'Подтверждение удаления',
                icon: 'pi pi-info-circle',
                acceptClass: 'p-button-danger',
                accept: () => {
                    this.$store.commit('DELETE_STATEMENT', this.editStatementDialogParams.statement)
                    this.$toast.add({ severity: 'error', summary: 'Удалено', detail: `Ведомость \"${this.editStatementDialogParams.statement.label}\" удалена`, life: 3000 })
                    this.editStatementDialogParams.statement = {}
                },
                reject: () => {}
            });
        },
        saveGroup() {
            const groupNameInput = document.getElementById('group-name')
            const groupDateInput = document.getElementById('group-date')

            if (this.newGroupName.length < 4 || this.newGroupName.length > 15) {
                groupNameInput.classList.add('p-invalid')
                return;
            }

            if (!this.newGroupYear) {
                groupDateInput.classList.add('p-invalid')
                return;
            }

            const currentGroupDate = this.newGroupYear.toLocaleDateString('en-US')
            const currentGroupYear = parseInt(currentGroupDate.slice(currentGroupDate.length - 4, currentGroupDate.length))

            if (new Date().getFullYear() < currentGroupYear) {
                groupDateInput.classList.add('p-invalid')
                return;
            }

            const foundGroup = this.getGroupsMenuItems.filter((item) => item.label === `${this.newGroupName} (${currentGroupYear})`)
            if (foundGroup.length !== 0) {
                groupNameInput.classList.add('p-invalid')
                return
            }

            this.$store.commit('SAVE_GROUP', [
                this.newGroupName,
                currentGroupYear,
                (event) => this.openGroup(event),
                (event) => this.openRenameGroupDialod(event),
                (event) => this.openAddStudentDialog(event)
            ])

            this.newGroupName = ''
            this.newGroupYear = null
            this.isNewGroupDialogVisible = false
        },
        openGroup(event) {
            this.openedGroup = this.getGroupsMenuItems.find((menuItem) => menuItem.key === event.item.key.replace('_open', ''))
            this.isOpenedGroupDialogVisible = true
        },
        archiveGroup(event) {
            const group = this.getGroupsMenuItems.find((menuItem) => menuItem.key === event.item.key.replace('_archive', ''))

            if (group.isArchived) {
                this.$toast.add({ severity: 'info', summary: 'Отмена', detail: `Нельзя архивировать группу, которая уже архивирована.`, life: 3000 })
                return
            } 

            this.$confirm.require({
                    message: `Вы действительно хотите архивировать группу ${group.label}? Вместе с этой группой архивируются все ведомости, исспользующие ее.`,
                    header: 'Подтверждение архивирования',
                    icon: 'pi pi-info-circle',
                    acceptClass: 'p-button-danger',
                    accept: () => {
                        this.$store.commit('ARCHIVE_GROUP', group)
                        this.$toast.add({ severity: 'error', summary: 'Архивировано', detail: `Группа ${group.label} архивирована`, life: 3000 })
                    },
                    reject: () => {}
                })
        },
        archiveOpenedGroup() {
            this.$confirm.require({
                message: `Вы действительно хотите архивировать группу ${this.openedGroup.label}? Вместе с этой группой архивировать все ведомости, исспользующие ее.`,
                header: 'Подтверждение архивирования',
                icon: 'pi pi-info-circle',
                acceptClass: 'p-button-danger',
                accept: () => {
                    this.$store.commit('ARCHIVE_GROUP', this.openedGroup)
                    this.$toast.add({ severity: 'error', summary: 'Архивировано', detail: `Группа ${this.openedGroup.label} архивирована`, life: 3000 })
                    this.openedGroup = null
                    this.isOpenedGroupDialogVisible = false
                },
                reject: () => {}
            })
        },
        dearchiveOpenedGroup() {
            this.$confirm.require({
                message: `Разархивировать группу ${this.openedGroup.label}?`,
                header: 'Подтверждение разархивирования',
                icon: 'pi pi-info-circle',
                accept: () => {
                    this.$store.commit('DEARCHIVE_GROUP', this.openedGroup)
                    this.$toast.add({ severity: 'info', summary: 'Разархивировано', detail: `Группа ${this.openedGroup.label} разархивирована`, life: 3000 })
                    this.openedGroup = null
                    this.isOpenedGroupDialogVisible = false
                },
                reject: () => {}
            })
        },
        renameGroup() {
            const groupNameInput = document.getElementById('group-name')

            if (this.newGroupName.length < 4 || this.newGroupName.length > 15) {
                groupNameInput.classList.add('p-invalid')
                return;
            }

            const foundGroup = this.getGroupsMenuItems.filter((item) => item.label === this.newGroupName)
            if (foundGroup.length !== 0) {
                groupNameInput.classList.add('p-invalid')
                return
            }

            this.$confirm.require({
                message: `Вы действительно хотите переименовать группу в ${this.newGroupName}?`,
                header: 'Подтверждение переименования',
                icon: 'pi pi-info-circle',
                accept: () => {
                    this.isRenameGroupDialogVisible = false
                    this.$store.commit('RENAME_GROUP', [this.editableGroup, this.newGroupName])
                    this.$toast.add({ severity: 'info', summary: 'Изменено', detail: `Группа \"${this.editableGroup.label}\" переименована`, life: 3000 })
                    this.newGroupName = ''
                },
                reject: () => {}
            });   
        },
        saveSubject() {
            const subjectNameInput = document.getElementById('subject-name')

            if (this.newSubjectName.length < 2 || this.newSubjectName.length > 50 || this.newSubjectName === 'label') {
                subjectNameInput.classList.add('p-invalid')
                return;
            }

            const foundSubject = this.getSubjectsMenuItems.filter((item) => item.label === this.newSubjectName)
            if (foundSubject.length !== 0) {
                subjectNameInput.classList.add('p-invalid')
                return
            }

            this.$store.commit('SAVE_SUBJECT', this.newSubjectName)

            this.newSubjectName = ''
            this.isNewSubjectDialogVisible = false
        },
        selectSubject(event, item) {
            this.selectedSubject = item
            this.$refs.menu.show(event)
        },
        deleteSubject() {
            this.$confirm.require({
                message: `Вы действительно хотите удалить предмет \"${this.selectedSubject.label}\"?`,
                header: 'Подтверждение удаления',
                icon: 'pi pi-info-circle',
                acceptClass: 'p-button-danger',
                accept: () => {
                    let result = {}
                    this.$store.commit('DELETE_SUBJECT', [this.selectedSubject, result])

                    if (!result.out) {
                        this.$toast.add({ severity: 'info', summary: 'Удаление отменено', detail: `Не удалось удалить предмет ${ this.selectedSubject.label }, так как ведомости должны содержать минимум 2 предмета`, life: 5000 }) 
                        return
                    }

                    this.$toast.add({ severity: 'error', summary: 'Удалено', detail: `Предмет \"${this.selectedSubject.label}\" удален`, life: 3000 })
                    this.selectedSubject = {}
                },
                reject: () => {}
            });
        },
        renameSubject() {
            const subjectNameInput = document.getElementById('subject-name')

            if (this.newSubjectName.length < 2 || this.newSubjectName.length > 50 || this.newSubjectName === 'label') {
                subjectNameInput.classList.add('p-invalid')
                return;
            }

            const foundGroup = this.getSubjectsMenuItems.filter((item) => item.label === this.newSubjectName)
            if (foundGroup.length !== 0) {
                subjectNameInput.classList.add('p-invalid')
                return
            }

            this.$confirm.require({
                message: `Вы действительно хотите переименовать предмет в ${this.newSubjectName}?`,
                header: 'Подтверждение переименования',
                icon: 'pi pi-info-circle',
                accept: () => {
                    this.selectedSubject.label = this.newSubjectName
                    this.isRenameSubjectDialogVisible = false
                    this.$toast.add({ severity: 'info', summary: 'Изменено', detail: `Предмет \"${this.newSubjectName}\" переименован`, life: 3000 })
                    this.newSubjectName = ''
                    this.selectedSubject = {}
                },
                reject: () => {}
            });
        },
        saveStudent() {
            const studentNameInput = document.getElementById('student-name')

            if (this.newStudentName.length < 4 || this.newStudentName.length > 50) {
                studentNameInput.classList.add('p-invalid')
                return;
            }

            this.$store.commit('SAVE_STUDENT', [this.newStudentName, this.newStudentGroup])

            this.newStudentName = ''
            this.newStudentGroup = null
            this.isNewStudentDialogVisible = false
        }
    },
    components: {
        Splitter,
        SplitterPanel,
        Button,
        ScrollPanel,
        PanelMenu,
        Dialog,
        InputText,
        ConfirmDialog,
        Toast,
        Menu,
        ContextMenu,
        Breadcrumb,
        DataTable,
        Column,
        TabPanel,
        TabView,
        Calendar,
        Chip,
        NewStatementDialog,
        StatementInfoDialog,
        EditStatementDialog,
        EditStudentDialogVue,
        Terminal,
        Avatar
    }
}
</script>

<style scoped>
.wrapper {
    display: flex;
    justify-content: center;
}

.nav-panel {
    z-index: 999;
    position: absolute;
    left: 0;
    top: 0;
    display: flex;
    flex-flow: column;
    padding: 8px;
    height: 100vh;
    background-color: #f7f8fa;
    border-right: 2px solid #e2e3e6;
}

.nav-panel .p-button {
    height: 30px;
    width: 30px;
    margin-bottom: 10px;
}

.p-splitter {
    position: absolute;
    width: calc(100% - 46px);
    right: 0;
    top: 0;
}

.subject-menu-item {
    position: relative;
    padding: 10px;
    display: flex;
    cursor: pointer;
}

.subject-menu-item i {
    position: absolute;
    top: 50%;
    left: 15px;
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
}

.subject-menu-item span {
    margin-left: 30px;
    -webkit-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.group-menu-item {
    position: relative;
    padding: 10px;
    display: flex;
    cursor: pointer;
}

.group-menu-item i {
    position: absolute;
    top: 50%;
    left: 15px;
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
}

.group-menu-item span {
    margin-left: 30px;
    -webkit-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.p-splitter {
    border: 0;
}

.about-group {
    margin-top: 10px;
    margin-bottom: 10px;
}

.about-group > * {
    margin-right: 5px;
    margin-top: 5px;
}
</style>