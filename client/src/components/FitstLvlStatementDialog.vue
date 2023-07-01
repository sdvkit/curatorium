<template>
    <Dialog maximizable :style="{ width: '75vw' }" :contentStyle="{ height: `${params.contentHeight + 140}px` }">
        <template #header>
            <header>
                <Tag :value="params.group.label"></Tag>
                <span :style="{ fontSize: '16pt', marginLeft: '20px' }">{{ params.statement.label }}</span>
            </header>
        </template>

        <SecondLvlStatementDialog v-model:visible="secondLvlStatementDialogParams.visible" :params="secondLvlStatementDialogParams" />

        <MultiSelect id="statement-subjects" v-model="params.subjects" :options="params.subjects" display="chip" option-label="label"
                     class="w-full md:w-20rem" disabled :style="{ maxWidth: '100%', opacity: '1', marginBottom: '10px' }" empty-message="Пусто" />
        <DataTable :value="params.students" showGridlines scrollable :scrollHeight="`${params.contentHeight}px`">
            <template #empty>Пусто</template>
            <template #footer>
                <Button label="Обновить данные" icon="pi pi-refresh" @click="refreshData" text />
                <Button v-tooltip="'При расчетах отметок 0 (ноль) игнорируется. Итоговые отметки, которые больше, либо равны 3, выделены зеленым цветом. Итоговые отметки, ниже 3, выделены красным.'" label="" icon="pi pi-info-circle" text size="large" rounded :style="{ float: 'right' }" />
            </template>
            <ColumnGroup type="header">
                <Row>
                    <Column header="Учащиеся" :rowspan="2"></Column>
                    <Column header="Предметы" :colspan="params.columns.length" frozen></Column>
                </Row>
                <Row>
                    <Column v-for="column in params.subjects" :field="column.field">
                        <template #header>
                            <Button :label="column.label" :style="{ width: '100%' }" text @click="openSecondLvlStatementDialog(column)" />
                        </template>
                    </Column>
                    <Column header="Итого" />
                </Row>
            </ColumnGroup>
            <Column v-for="column in params.columns" :frozen="column.label === 'Учащиеся'" :header="column.label">
                <template #body="slotProps">
                    {{ (isNaN(slotProps.data[column.field]) && column.field !== 'label') ? 0 : slotProps.data[column.field] }}
                </template>
            </Column>
            <Column>
                <template #body="slotProps">
                    <strong :style="{ color: (getTotalMark(slotProps.data) >= 3) ? '#17B169' : '#FF0000' }">
                        {{ getTotalMark(slotProps.data) }}
                    </strong>
                </template>
            </Column>
        </DataTable>
        <template #maximizeicon>
            <Button :icon="params.sizeIcon" text @click="maximizeStatementDialog" :style="{ color: '#666666' }" />
        </template>
    </Dialog>
</template>

<script>
import Dialog from 'primevue/dialog'
import Tag from 'primevue/tag'
import MultiSelect from 'primevue/multiselect'
import ColumnGroup from 'primevue/columngroup'
import Row from 'primevue/row'
import Column from 'primevue/column'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import SecondLvlStatementDialog from './SecondLvlStatementDialog.vue'
import { mapGetters } from 'vuex'

export default {
    name: 'statement-info-dialog',
    props: ['params'],
    data() {
        return {
            secondLvlStatementDialogParams: {
                visible: false,
                subject: null,
                sizeIcon: 'pi pi-window-maximize',
                students: [],
                subjectKey: 0
            }
        }
    },
    methods: {
        maximizeStatementDialog() {
            this.params.sizeIcon = (this.params.sizeIcon === 'pi pi-window-maximize') ? 'pi pi-window-minimize' : 'pi pi-window-maximize'
            this.params.contentHeight = (this.params.contentHeight === 400) ? 500 : 400
        },
        getMarkBySecondLvlStatment(student, secondLvlStatement) {
            const studentMarks = student.marks.filter((mark) => this.getSecondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey).key === secondLvlStatement.key)

            const result = studentMarks.reduce((acc, curr) => {
                const index = acc.findIndex(item => item.typeId === curr.typeId)
                
                if (index === -1) {
                    acc.push({ typeId: curr.typeId, sum: parseInt(curr.value), count: (curr.value === 0) ? 0 : 1 })
                } else {
                    acc[index].sum += parseInt(curr.value)
                    acc[index].count += (parseInt(curr.value) === 0) ? 0 : 1
                }

                return acc
            }, [])

            const avgMarksArr = result.map(item => item.sum / item.count)
            return Math.round(avgMarksArr.reduce((acc, curr) => acc + curr, 0) / avgMarksArr.length)
        },
        refreshData() {
            this.params.students = []

            this.params.group.items[0].items.forEach(student => {
                const entity = {}
                entity.label = student.label
                this.params.statement.secondLvlStatements.forEach(secondLvlStatement => 
                    entity[secondLvlStatement.subject.label] = this.getMarkBySecondLvlStatment(student, secondLvlStatement))
                this.params.students.push(entity)
            })
        },
        getTotalMark(data) {
            const clonedData = { ...data }
            delete clonedData.label
            const marks = Object.values(clonedData)

            for(let i = 0; i < marks.length; i++) {
                if (isNaN(marks[i])) {
                    marks.splice(i, 1)
                }
            }

            if (Array.isArray(marks[marks.length - 1])) {
                marks.pop()
            }
            
            const result = Math.round(marks.reduce((a, b) => a + b, 0) / marks.length)

            return isNaN(result) ? 0 : result
        },
        openSecondLvlStatementDialog(subjectColumn) {
            this.secondLvlStatementDialogParams.group = this.params.group
            this.secondLvlStatementDialogParams.subject = subjectColumn
            this.secondLvlStatementDialogParams.visible = true
            this.secondLvlStatementDialogParams.students = []
            this.secondLvlStatementDialogParams.firstLvlStatement = this.params.statement
            this.secondLvlStatementDialogParams.subjectKey = this.getSubjects.find((subject) => subject.label === subjectColumn.label).key

            this.params.group.items[0].items.forEach((student) => {
                let entitie = { label: student.label }
        
                const marks = student.marks.filter((mark) => {
                    const foundSecondLvlStatement = this.params.statement.secondLvlStatements.find(secondLvlStatement => secondLvlStatement.key === mark.secondLvlStatementKey)
                    return foundSecondLvlStatement !== undefined && foundSecondLvlStatement.subject.key === this.secondLvlStatementDialogParams.subjectKey
                })

                marks.forEach((mark) => entitie[`mark_${Object.keys(entitie).length}`] = mark)
                entitie['key'] = student.key
                this.secondLvlStatementDialogParams.students.push(entitie)
            })
        }
    },
    computed: {
        ...mapGetters(['getSubjects', 'getSecondLvlStatements'])
    },
    components: {
        Dialog,
        Tag,
        MultiSelect,
        ColumnGroup,
        Row,
        Column,
        Button,
        DataTable,
        SecondLvlStatementDialog
    }
}
</script>