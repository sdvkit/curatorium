const mapper = {
    types: {
        0: 'DEFAULT',
        1: 'MANDATORY_CONTROL_WORK',
        2: 'COURSE_WORK'
    },
    fromStringToInt(markType) {
        return Object.keys(this.types)
            .find(key => this.types[key] === markType)
    },
    fromIntToString(markTypeIndex) {
        return this.types[markTypeIndex]
    }
}

export default mapper