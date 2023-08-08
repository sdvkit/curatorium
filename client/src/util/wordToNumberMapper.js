const numbers = {
    'один': 1, 'одна': 1, 'два': 2, 'две': 2, 'три': 3, 'четыре': 4, 'пять': 5,
    'шесть': 6, 'семь': 7, 'восемь': 8, 'девять': 9,
    'десять': 10, 'одиннадцать': 11, 'двенадцать': 12, 'тринадцать': 13,
    'четырнадцать': 14, 'пятнадцать': 15, 'шестнадцать': 16, 'семнадцать': 17,
    'восемнадцать': 18, 'девятнадцать': 19,
    'двадцать': 20, 'тридцать': 30, 'сорок': 40,
    'пятьдесят': 50, 'шестьдесят': 60, 'семьдесят': 70, 'восемьдесят': 80,
    'девяносто': 90, 'сто': 100, 'двести': 200, 'триста': 300,
    'четыреста': 400, 'пятьсот': 500, 'шестьсот': 600, 'семьсот': 700,
    'восемьсот': 800, 'девятьсот': 900,
    'тысяча': 1000, 'тысячи': 1000, 'тысяч': 1000,
    'миллион': 1000000, 'миллиона': 1000000, 'миллионов': 1000000
}

const wordToNumberMapper = {
    isNumber(text) {
        return Object.values(numbers)
            .indexOf(text) !== -1
    },
    parse(text) {
        const words = text.split(' ')
        let currentSum = 0
        let currentPart = 0
    
        for (let i = 0; i < words.length; i++) {
            const word = words[i]
    
            if (word === 'тысяча' || word === 'тысячи' || word === 'тысяч') {
                if (currentPart === 0) {
                    throw new Error('Ошибка: не указан предел чисел перед словом "тысяча".')
                }
    
                if (word === 'тысяча' || word === 'тысячи' || word === 'тысяч') {
                    currentSum += currentPart * 1000
                }
                currentPart = 0
            } else if (word === 'миллион' || word === 'миллиона' || word === 'миллионов') {
                if (currentPart === 0) {
                    throw new Error('Ошибка: не указан предел чисел перед словом "миллион".')
                }
    
                if (word === 'миллион' || word === 'миллиона' || word === 'миллионов') {
                    currentSum += currentPart * 1000000
                }
                currentPart = 0
            } else if (word in numbers) {
                currentPart += numbers[word]
            }
        }
    
        if (currentPart > 0) {
            currentSum += currentPart
        }
    
        return currentSum
    }
}

export default wordToNumberMapper