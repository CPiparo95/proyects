function lowestNumber(array1) {
    var lownumber = array1[0];
    for (var n = 0; n < array1.length; n++) {
        if (lownumber > array1[n]) {
            lownumber = array1[n];
        }
    }
    return lownumber;
}

function biggestNumber(array1) {
    var bignumber = array1[0];
    for (var n = 0; n < array1.length; n++) {
        if (bignumber < array1[n]) {
            bignumber = array1[n];
        }
    }
    return bignumber;
}

function preciseNumber(array1, index) {
    return array1[index];
}

function repeatedNumbers(array1) {
    var repeatNumbersArray = [];
    for (h = 0; h < array1.length; h++) {
        var positionArray = array1.indexOf(array1[h], h + 1);
        if (positionArray > -1) {
            if (-1 == repeatNumbersArray.indexOf(array1[h])) {
                repeatNumbersArray.push(array1[positionArray]);
            }
        }
    }
    return repeatNumbersArray;
}

function concat(array1) {
    var texto = "";
    for (var n = 0; n < array1.length; n++) {
        texto = texto + ", " + array1[n];
    }
    return texto;
}

function reverse(item) {
    var texto = "";
    item = item.toString();
    for (n = item.length; n >= 0; n--) {
        texto = texto + item.slice(n, n + 1);
    }
    return texto;
}

function alphabetOrder(texto) {
    var variable = texto.split('');
    variable.sort();
    return variable
}

function mayus(texto) {
    return texto.charAt(0).toUpperCase() + texto.slice(1);
}

function parserStrArrAndShowLongestWord(str) {
    var array = str.split(" ");
    var longestWord = -5;
    var wordIndex = 0;
    for (n = 0; n < array.length; n++) {
        if (longestWord < array[n].length) {
            longestWord = array[n].length;
            wordIndex = n;
        }
    }
    return array[wordIndex];
}

function FirstLetterStrUpperCase(str) {
    var array = str.split(" ");
    var phrase = "";
    for (n = 0; n < array.length; n++) {
        phrase = phrase + " " + mayus(array[n]);
    }
    return phrase;
}

// comienzo del task 1 - JS Basics
console.log("Starting javascript...");

//ejer 1
var myName = "Claudio Piparo";
console.log(myName);

//ejer2
var myAge = 23;
console.log(myAge);

//ejer3
var ingaseAge = 32;
var ageDiff = myAge - ingaseAge;
console.log("the value of ignasiAge - myAge is: ", ageDiff);

//ejer4
if (myAge >= 21) {
    console.log("I am older than 21");
} else {
    console.log("I am not older than 21");
}

//ejer5
if (myAge > ingaseAge) {
    console.log("I am older than ignasiAge");
} else if (myAge == ingaseAge) {
    console.log("I am the same age than ignaseAge");
} else {
    console.log("I am younger than ignaseAge");
}

// comienzo del task 1 - JS Arrays functions
//ejer1
var names = ["Alcides", "Alejandra Zacarias", "Amanda Gerez", "Andriano Nicolas", "Tomas Aprile",
    "Victor Asmad", "Marco Avila", "Aylen Romero", "Marcela Ines Cerda", "Nicolas Chiovetta", "Claudio D Piparo",
    "Tadeo Cullen", "Silvina Dardik", "Diego Tomas Carreras", "Pablo Duchovny", "Eduardo Giannattasio",
    "Florencia Russo", "Rodrigo Garcia Ribeiro", "Georgina Maranghello", "Griselda Otalora", "Jesus Rosas",
    "Vago leonardo", "Lilia Uperiachenko", "Linda", "Liza", "Matias Quintas", "Santiago Quintas", "Roberto Zitto",
    "Sanchez Roxanac", "Silowen07"
]

names.sort();
console.log(names[0]);
console.log(names[names.length - 1]);

for (var contador = 0; contador < names.length; contador++) {
    console.log(names[contador]);
}

//ejer2
var ages = [23, 22, 25, 23, 32, 27, 21, 29, 22, 22, 24, 26, 23, 18, 19, 20, 21, 17, 16]

for (var n = 0; n < ages.length; n++) {
    if (ages[n] % 2 == 0) {
        console.log(ages[n]);
    }
}

//ejer3
console.log(lowestNumber(ages));

//ejer4
console.log(biggestNumber(ages));

//ejer5
console.log(preciseNumber(ages, 8));

//ejer6
console.log(repeatedNumbers(ages))

//ejer7
console.log(concat(names));

// comienzo del task 1 - JS String Functions

//ejer1
var numero = 1234567890;
console.log(reverse(numero));

//ejer2
var textoPrueba = "chicho es un perro bueno";
console.log(alphabetOrder(textoPrueba));

//ejer3
var textoPrueba = "chicho es un perro bueno";
console.log(FirstLetterStrUpperCase(textoPrueba));

//ejer4
var textoPrueba = "chicho es un perro bueno";
console.log(parserStrArrAndShowLongestWord(textoPrueba));

//fin del task 1