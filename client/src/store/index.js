import Cookies from 'js-cookie'
import {createStore} from 'vuex'

const store = createStore({
    state () {
        return {
            tempSecondLvlKey: 1,

            user: {
                username: 'sdvkit',
                fullName: 'Nikita Sudaev'
            },
            groupsMenuItems: [
                {
                    key: `1`, 
                    label: `TTO (2022)`, 
                    name: 'TTO', 
                    icon: 'pi pi-folder', 
                    year: 2022, 
                    createdAt: new Date(),
                    isArchived: false,
                    items: [
                        { key: `1_students`, label: 'Учащиеся', icon: 'pi pi-users', items: [
                            {
                                key: 1,
                                label: 'Oleg OO',
                                icon: 'pi pi-user',
                                marks: [
                                    { value: 9, typeId: 1, secondLvlStatementKey: 1 },
                                    { value: 4, typeId: 1, secondLvlStatementKey: 1 },
                                    { value: 7, typeId: 2, secondLvlStatementKey: 1 },
                                ]
                            },
                            {
                                key: 2,
                                label: 'Ivan II',
                                icon: 'pi pi-user',
                                marks: [
                                    { value: 10, typeId: 1, secondLvlStatementKey: 1 },
                                    { value: 1, typeId: 1, secondLvlStatementKey: 2 },
                                    { value: 8, typeId: 3, secondLvlStatementKey: 1 },
                                ]
                            },
                            // { key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },{ key: 22221, label: 'test', icon: 'pi pi-user', marks: [] },
                        ] },
                        { key: `1_add_student`, label: 'Добавить учащегося', icon: 'pi pi-plus'},
                        { key: `1_open`, label: 'Открыть', icon: 'pi pi-folder-open'},
                        { key: `1_rename`, label: 'Переименовать', icon: 'pi pi-pencil'}
                    ]
                }
            ],
            subjectsMenuItems: [
                { key: 1, label: 'Математика', icon: 'pi pi-book' },
                { separator: true },
                { key: 3, label: 'Оп', icon: 'pi pi-book' },
            ],
            breadcrumbMenuItems: [],
            firstLvlStatements: [],
        }
    },
    getters: {
        getUser(state) {
            return state.user
        },
        getNotArchivedGroupsMenuItems(state) {
            return state.groupsMenuItems.filter((group) => !group.isArchived)
        },
        getArchivedGroupsMenuItems(state) {
            return state.groupsMenuItems.filter((group) => group.isArchived)
        },
        getGroupsMenuItems(state) {
            return state.groupsMenuItems
        },
        getSubjectsMenuItems(state) {
            return state.subjectsMenuItems
        },
        getBreadcrumbMenuItems(state) {
            return state.breadcrumbMenuItems
        },
        getStatements(state) {
            return state.firstLvlStatements
        },
        getNotArchivedStatements(state) {
            return state.firstLvlStatements.filter((statement) => !statement.group.isArchived )
        },
        getArchivedStatements(state) {
            return state.firstLvlStatements.filter((statement) => statement.group.isArchived )
        },
        getSubjects(state) {
            const subjects = []
            for (let i = 0; i < state.subjectsMenuItems.length; i += 2) {
                subjects.push(state.subjectsMenuItems[i])
            }
            return subjects
        },
        getSecondLvlStatements(state) {
            const secondLvlStatements = []
            
            state.firstLvlStatements.forEach(firstLvlStatement => {
                firstLvlStatement.secondLvlStatements.forEach(secondLvlStatement => secondLvlStatements.push(secondLvlStatement))
            })

            return secondLvlStatements
        },
    },
    mutations: {
        SAVE_PROFILE_CHANGES(state, payload) {
            const username = payload[0]
            const fullName = payload[1]
            state.user.username = username
            state.user.fullName = fullName
        },
        CLEAR_BREADCRUMB_MENU(state) {
            state.breadcrumbMenuItems = []
        },
        SET_BREADCRUMB_MENU_GROUP(state, group) {
            if (state.breadcrumbMenuItems.length === 1) {
                state.breadcrumbMenuItems.shift()
            }

            state.breadcrumbMenuItems.push({ label: group.label, to: `/groups/${group.key}` })
        },
        DELETE_STUDENT(state, payload) {
            const deletableStudent = payload[0]
            const deletableStudentGroup = payload[1]
            const deletableStudentIndex = deletableStudentGroup.items[0].items.indexOf(deletableStudent)

            deletableStudentGroup.items[0].items.splice(deletableStudentIndex, 1)
        },
        RENAME_STUDENT(state, payload) {
            const student = payload[0]
            student.label = payload[1]
        },
        SAVE_SUBJECT(state, subjectName) {
            if (state.subjectsMenuItems.length > 0) {
                state.subjectsMenuItems.push({ separator: true })
            }

            state.subjectsMenuItems.push({ key: state.subjectsMenuItems.length + 1, label: subjectName, icon: 'pi pi-book' })
        },
        DELETE_SUBJECT(state, payload) {
            const subject = payload[0]
            let result = payload[1]

            for (let i = 0; i < state.firstLvlStatements.length; i++) {
                const statement = state.firstLvlStatements[i]
                
                if (statement.subjects.indexOf(subject) !== -1 && statement.subjects.length === 2) {
                    result.out = false
                    return
                }
            }

            const deletableIndex = state.subjectsMenuItems.indexOf(subject)

            if (deletableIndex !== state.subjectsMenuItems.length - 1) {
                state.subjectsMenuItems.splice(deletableIndex, 2)
            } else {
                state.subjectsMenuItems.splice(deletableIndex -1, 2)
            }

            state.firstLvlStatements.forEach((statement) => {
                if (statement.subjects.indexOf(subject) !== -1) {
                    statement.subjects.splice(statement.subjects.indexOf(subject), 1)
                }
            })

            result.out = true
        },
        RENAME_GROUP(state, payload) {
            const group = payload[0]
            group.name = payload[1]
            group.label = `${group.name} (${group.year})`
        },
        SAVE_GROUP(state, payload) {
            const groupName = payload[0]
            const groupYear = payload[1]
            const openCommand = payload[2]
            const renameCommand = payload[3]
            const addStudentCommand = payload[4]
            const newGroupIndex = state.groupsMenuItems.length + 1

            state.groupsMenuItems.push({
                key: `${newGroupIndex}`, 
                label: `${groupName} (${groupYear})`, 
                name: groupName, 
                icon: 'pi pi-folder', 
                year: groupYear, 
                createdAt: new Date(),
                isArchived: false,
                items: [
                    { key: `${newGroupIndex}_students`, label: 'Учащиеся', icon: 'pi pi-users', items: [] },
                    { key: `${newGroupIndex}_add_student`, label: 'Добавить учащегося', icon: 'pi pi-plus', command: addStudentCommand},
                    { key: `${newGroupIndex}_open`, label: 'Открыть', icon: 'pi pi-folder-open', command: openCommand},
                    { key: `${newGroupIndex}_rename`, label: 'Переименовать', icon: 'pi pi-pencil', command: renameCommand}
                ]
            })
        },
        DELETE_STATEMENT(state, deletableStatement) {
            const deletableIndex = state.firstLvlStatements.indexOf(deletableStatement)
            state.firstLvlStatements.splice(deletableIndex, 1)
        },
        SAVE_STATEMENT(state, payload) {
            const statementName = payload[0]
            const statementSubjects = payload[1]
            const statementGroup = payload[2]

            const entitie = {
                key: state.firstLvlStatements.length + 1,
                label: statementName,
                group: statementGroup,
                secondLvlStatements: []
            }

            statementSubjects.forEach(statementSubject => entitie.secondLvlStatements.push({
                key: state.tempSecondLvlKey++,
                subject: statementSubject
            }))

            state.firstLvlStatements.push(entitie)
        },
        EDIT_STATEMENT(state, payload) {
            const editableStatement = payload[0]
            const newStatementName = payload[1]

            state.firstLvlStatements.find((statement) => {
                if (statement === editableStatement) {
                    statement.label = newStatementName
                }
            })
        },
        SAVE_STUDENT(state, payload) {
            const studentName = payload[0]
            const studentGroup = payload[1]

            studentGroup.items[0].items.push({
                key: studentGroup.items[0].items.length + 1,
                label: studentName,
                icon: 'pi pi-user',
                marks: []
            })

            studentGroup.items[0].items.sort((student1, student2) => student1.label.localeCompare(student2.label))
        },
        ARCHIVE_GROUP(state, group) {
            group.isArchived = true
        },
        DEARCHIVE_GROUP(state, group) {
            group.isArchived = false
        },
        DELETE_GROUP(state, deletableGroup) {
            const deletableIndex = state.groupsMenuItems.indexOf(deletableGroup)

            state.firstLvlStatements = state.firstLvlStatements.filter((statement) => statement.group !== deletableGroup)
            state.groupsMenuItems.splice(deletableIndex, 1)
        },
        CLEAR_ARCHIVE(state) {
            const archivedGroups = state.groupsMenuItems.filter((group) => group.isArchived)

            while (archivedGroups.length !== 0) {
                const deletableGroup = archivedGroups.shift()
                const deletableIndex = state.groupsMenuItems.indexOf(deletableGroup)

                state.firstLvlStatements = state.firstLvlStatements.filter((statement) => statement.group !== deletableGroup)
                state.groupsMenuItems.splice(deletableIndex, 1)
            }
        },
        SAVE_MARK(state, payload) {
            const student = payload[0]
            const mark = payload[1]
            student.marks.push(mark)
        }
    },
    actions: {
    }
})

export default store