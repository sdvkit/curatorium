const recognizer = {
    recognition: null,
    isListening: false,

    initialize() {
        this.recognition = new webkitSpeechRecognition()
        this.recognition.lang = 'ru-RU'
        this.recognition.continuous = true
    },
    startRecognition() {
        this.recognition.start()
        this.isListening = true
    },
    stopRecognition() {
        this.recognition.stop()
        this.isListening = false
    },
    onResult(eventHandler) {
        this.recognition.onresult = eventHandler
        this.isListening = false
    }
}

export default recognizer