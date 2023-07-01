<template>
    <ContextMenu ref="menu" :model="statementContextMenuItems" />

    <NewStatementDialog v-model:visible="createStatementDialogParams.visible" :params="createStatementDialogParams" />
    <FirstLvlStatementDialog    v-model:visible="firstLvlStatementDialogParams.visible" :params="firstLvlStatementDialogParams" />
    <EditStatementDialog v-model:visible="editStatementDialogParams.visible" :params="editStatementDialogParams" />

    <div class="wrapper">
        <DataTable :value="getNotArchivedStatements" contextMenu v-model:contextMenuSelection="editStatementDialogParams.statement"
            @row-contextmenu="showStatementContextMenu" responsiveLayout="stack" breakpoint="960px"
            @row-click="openStatement" v-model:filters="filters" :globalFilterFields="['label', 'group.label']">
            <template #empty>Пусто</template>
            <template #header>
                <h2>Ваши ведомости:</h2>
                <div class="toolbar">
                    <span class="p-input-icon-left">
                        <i class="pi pi-search" />
                        <InputText v-model="filters['global'].value" placeholder="Поиск" class="p-inputtext-sm" />
                    </span>
                    <Button label="Создать" icon="pi pi-plus" @click="createStatementDialogParams.visible = true" />
                </div>
            </template>
            <Column field="label" header="Название" filter-field="label"></Column>
            <Column header="Группа" filter-field="group.label">
                <template #body="slotProps">
                    <Tag :value="slotProps.data.group.label"></Tag>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<script>
import { FilterMatchMode } from 'primevue/api'
import Button from "primevue/button"
import Column from 'primevue/column'
import ContextMenu from "primevue/contextmenu"
import DataTable from 'primevue/datatable'
import Dialog from "primevue/dialog"
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'
import { mapGetters } from "vuex"
import NewStatementDialog from '../components/NewStatementDialog.vue'
import FirstLvlStatementDialog  from '../components/FitstLvlStatementDialog.vue'
import EditStatementDialog from '../components/EditStatementDialog.vue'

export default {
    name: "MainPage",
    mounted() {
        this.$store.commit('CLEAR_BREADCRUMB_MENU')
    },
    data() {
        return {
            isEditStatementDialogVisible: false,
            createStatementDialogParams: { visible: false },
            firstLvlStatementDialogParams: {
                visible: false,
                contentHeight: 400,
                sizeIcon: 'pi pi-window-maximize',
                group: null,
                columns: [],
                students: [],
                subjects: [],
                statement: ''
            },
            editStatementDialogParams: { visible: false, statement: null, editableName: '' },
            filters: {
                global: { value: null, matchMode: FilterMatchMode.CONTAINS },
                label: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
                group: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
            },
            statementContextMenuItems: [
                {
                    label: 'Переименовать', icon: 'pi pi-pencil', command: () => {
                        this.editStatementDialogParams.editableName = this.editStatementDialogParams.statement.label
                        this.editStatementDialogParams.statement = this.editStatementDialogParams.statement
                        this.editStatementDialogParams.visible = true
                    }
                },
                { label: 'Удалить', icon: 'pi pi-trash', command: () => this.deleteStatement() }
            ]
        }
    },
    computed: {
        ...mapGetters(['getGroupsMenuItems', 'getSubjectsMenuItems', 'getStatements', 'getSubjects', 'getNotArchivedStatements', 'getSecondLvlStatements']),
    },
    methods: {
        maximizeStatementDialog() {
            this.statementDialogSizeIcon = (this.statementDialogSizeIcon === 'pi pi-window-maximize') ? 'pi pi-window-minimize' : 'pi pi-window-maximize'
            this.statementDialogContentHeight = (this.statementDialogContentHeight === 400) ? 500 : 400
        },
        getMarkBySecondLvlStatment(student, secondLvlStatement) {
            const studentMarks = student.marks.filter((mark) => this.getSecondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey).key === secondLvlStatement.key)

            const result = studentMarks.reduce((acc, curr) => {
                const index = acc.findIndex(item => item.typeId === curr.typeId)
                
                if (index === -1) {
                    acc.push({ typeId: curr.typeId, sum: parseInt(curr.value), count: 1 })
                } else {
                    acc[index].sum += parseInt(curr.value)
                    acc[index].count += (parseInt(curr.value) === 0) ? 0 : 1
                }

                return acc
            }, [])

            const avgMarksArr = result.map(item => item.sum / item.count)
            return Math.round(avgMarksArr.reduce((acc, curr) => acc + curr, 0) / avgMarksArr.length)
        },
        showStatementContextMenu(event) {
            this.$refs.menu.show(event.originalEvent)
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
                reject: () => {
                }
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
        }
    },
    components: {
        DataTable,
        Column,
        Tag,
        InputText,
        Button,
        ContextMenu,
        Dialog,
        NewStatementDialog,
        FirstLvlStatementDialog,
        EditStatementDialog
    }
}
</script>

<style scoped>
.wrapper {
    display: block;
    margin-left: 30px;
}

.toolbar {
    display: flex;
    width: 100%;
}

.toolbar .p-button {
    margin-left: 10px;
}
</style>