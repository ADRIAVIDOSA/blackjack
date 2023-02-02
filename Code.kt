/*
Respecte a la entrega anterior he millorat:
1.He millorat tota la part gràfica (colors, dibuixos ascii, eliminació de informació prescindible, ...)
2. Eliminació de warnings
3. Netejar la pantalla perque no s'enganxi el text
4. He anyadit la funcio de reglas per poder revisar-les en tot moment per saber com funciona el joc
5. Que es vegi els diners que es tenen en tot moment
6. Calculs interns del joc per millorar la precisió
7. 200-300 lineas m'ha costat xD por los pelos
8. Funcions i variables amb el mateix nom
9. Al final no hem conectat els jocs :(((
 */

fun main() {
    bienvenida()
    //println("Bienvenido a mi juego del blackjack! ")
    var repeat = false  //No se reasigna el valor en cada ronda
    while (true) {


        menuPrincipal()
        val menuP = readln().toIntOrNull()
        if (repeat == true) {    //A partir del primer juego se limpiara la pantalla al principio de cada ronda
            cls()
        }
        when (menuP) {
            1 -> {
                juego()//Juego principal (blackjack)
                repeat = true  //Marca que ya se ha realizado un juego
            }

            2 -> reglas()
            3 -> break      //Termina el juego
            69 -> editMoney() //Millonariooooo
            else -> println("Has introducido mal la opcion, por favor repita la seleccion.")
        }
    }
    print("Gracias por jugar :)")
}

fun menuPrincipal() {
    println("1.Jugar 2.Reglas del juego 3.Salir") //En un futuro me gustaria añadir mas opciones
    print("Elije una opcion: ")
}

fun checkMoney() {
    if (money < 0) {    //Elimina posibles valores negativos para el dinero
        money = 0
    }
}

fun showMoney() {    //Muestra las monedas que se tienen
    checkMoney()  //Se pone aqui para que siempre se compruebe errores antes de mostrar el dinero
    // println(" ____________________ ")
    print("\n")
    println("\u001B[30m\u001B[43m\u001B[4m©$money\u001B[0m ")


    println()
}

fun editMoney() { //Funcion para editar el dinero inicial
    // (No se muestra porque se supone que el jugador no puede saberlo, pero yo si puedo y ahora tu tambien jaja)
    print("Dime cuanto dinero quieres mi rey: ")
    val moneyCheat = readln().toIntOrNull()
    if (moneyCheat != null) {
        money = moneyCheat //No me dejaba reasignar el valor de money directamente
    }
}

fun juego() {
    if (money <= 1) {   //Verificacion: Solo permite jugar si se tiene dinero
        println("\nTe has quedado pobre, consigue mas dinero para jugar más al blackjack!!!!")
        return //Vuelve al menu
    }
    showMoney()
    println("Cuanto quieres apostar?")
    val apuesta = readln().toIntOrNull()
    if (apuesta == null || apuesta > money) { // verifica que el jugador tenga dinero suficiente
        println("Entrada inválida, ingresa una cantidad válida")    //Esta vez no insulto al usuario :D
        return
    }
    money -= apuesta  // resta la apuesta al dinero del jugador
    val cardValues = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val suits = arrayOf("♢", "♥", "♣", "♤")
    val deck = mutableListOf<String>()
    for (suit in suits) {
        for (value in cardValues) {
            deck.add("$value$suit")

        }
    }
    // Mezcla la baraja de cartas
    deck.shuffle()

    // Inicializa la mano del jugador y el croupier
    val playerHand = mutableListOf<String>()
    val dealerHand = mutableListOf<String>()

    // Reparte las cartas iniciales
    playerHand.add(deck.removeAt(0))    //Al añadirlas en la mano de un jugador se elimina del "monton"
    dealerHand.add(deck.removeAt(0))    //Al añadirlas en la mano de un jugador se elimina del "monton"
    playerHand.add(deck.removeAt(0))    //Al añadirlas en la mano de un jugador se elimina del "monton"
    dealerHand.add(deck.removeAt(0))    //Al añadirlas en la mano de un jugador se elimina del "monton"

    val resultadoJugador = calculateScore(playerHand)   //Se calcula la baraja del jugador
    var resultadoCroupier = calculateScore(dealerHand)  //Se calcula la baraja del croupier

    // Comprobar si hay algun blackjack
    if (resultadoJugador == 21) {           //Si tu puntuacion es 21 ganas seguro a no ser que el croupier tenga 21 (empate)
        println("\u001B[30m\u001B[42mBlackjack! Tu ganas!\u001B[0m")

        money += (apuesta * 2)              //El premio es el doble de la apuesta
        return
    }
    if (resultadoCroupier == 21) {      //Se pierde el dinero que has apostado
        println("\u001B[30m\u001B[41mEl croupier ha sacado un blackjack! Tu pierdes.\u001B[0m")//Tengo ganas de reirme del usuario pero no lo hare :D

        return
    }

    // Imprime las cartas en mano
    val cartaVisible = dealerHand[0] //Parece una tonteria pero lo hago para poder ponerlo dentro del ascii art
    println("\u001B[34mMano del croupier\u001B[0m")


    println(
        " ------\n" + //Se muestra solo la primera carta que tiene el croupier
                "|      |\n" +
                "|      |\n" +
                "|  $cartaVisible |\n" +
                "|      |\n" +
                "|______|"
    )
    println(
        " ------\n" +
                "|      |\n" +
                "|      |\n" +
                "|  ??? |\n" +
                "|      |\n" +
                "|______|"
    )
    println()
    println("\u001B[34mTu mano\u001B[0m")
    for (card in playerHand) {  //Se muestran todas las cartas de tu mano
        println(
            " ------\n" +
                    "|      |\n" +
                    "|      |\n" +
                    "| $card  |\n" +
                    "|      |\n" +
                    "|______|"
        )
    }
    println()
// Turno del jugador
    var playerScore = calculateScore(playerHand)
    println("\u001B[35mTu puntuacion actual es: $playerScore\u001B[0m")

    while (playerScore < 21) {
        println("1.Doblar    2.Pedir    3.Plantarse")
        val meLaJuego = readln().toIntOrNull()
        when (meLaJuego) {

            1 -> {      //-----DOBLO-----
                val newCard = deck.removeAt(0)
                playerHand.add(newCard)
                playerScore = calculateScore(playerHand)
                println("Tu nueva carta es:")
                println(newCard)

                println("\u001B[35mTu nueva puntuacion es: $playerScore\u001B[0m")
                break
            }
            2 -> {      //-----PIDO-----
                val newCard = deck.removeAt(0)
                playerHand.add(newCard)
                playerScore = calculateScore(playerHand)
                println("Tu nueva carta es:")
                println(
                    " ------\n" +
                            "|      |\n" +
                            "|      |\n" +
                            "| $newCard  |\n" +
                            "|      |\n" +
                            "|______|"
                )

                println("\u001B[35mTu nueva puntuacion es: $playerScore\u001B[0m")
            }           //-----ME PLANTO------
            3 -> break  //Termina el juego
            else -> println("Input invalido porfavor repite.")  //Bfffff
        }
    }
    if (playerScore > 21) {
        println("\u001B[30m\u001B[41mTe has pasado!\u001B[0m")
//xD
        money -= apuesta
    } else {
        // Turno del croupier
        while (resultadoCroupier < 17) {
            val newCard = deck.removeAt(0)
            dealerHand.add(newCard)
            resultadoCroupier = calculateScore(dealerHand)
        }
        if (resultadoCroupier > 21) {
            println("\u001B[30m\u001B[42mCroupier se paso!\u001B[0m")
            money += (apuesta * 2)
        } else if (resultadoJugador > resultadoCroupier) {
            println("\u001B[30m\u001B[42mGanaste!\u001B[0m")
            money += (apuesta * 2)
        } else {
            println("\u001B[30m\u001B[41mPerdiste!\u001B[0m")
            money -= apuesta
        }
    }
    showMoney() //Para que se vea el dinero en todo momento
}
fun calculateScore(hand: List<String>): Int {   //Variante de la funcion calculatescore() mas precisa y compleja
    var score = 0
    var aces = 0
    for (card in hand) {
        when (card[0]) {
            '2', '3', '4', '5', '6', '7', '8', '9' -> score += card[0].toString().toInt()
            '1', 'J', 'Q', 'K' -> score += 10
            'A' -> {
                score += 11
                aces++
            }
        }
    }
    while (score > 21 && aces > 0) {
        score -= 10
        aces--
    }
    return score
}
var money = 100 //Asigno el dinero incial fuera (excepcion)
fun bienvenida() {
    println("----------------------------------------------------------------------------------------------------------------------")
    println(
        " #####    ##         ##      ####    ##  ##     ####     ##       ####    ##  ##                ##     ######   ##  ##\n" +
                " ##♥##   ##        ####    ##  ##   ## ##       ##     ####     ##  ##   ## ##                ####      ##     ##  ##\n" +
                " ##♣##   ##       ##♣##   ##       ####        ##    ##♤##   ##       ####                ##♥##     ##     ##  ##\n" +
                " #####    ##       ######   ##       ###         ##    ######   ##       ###                 ######     ##      ####\n" +
                " ##♢##   ##       ##  ##   ##       ####        ##    ##  ##   ##       ####                ##  ##     ##       ##\n" +
                " ##♤##   ##       ##  ##   ##  ##   ## ##    ## ##    ##  ##   ##  ##   ## ##              ##  ##     ##       ##\n" +
                " #####    ######   ##  ##    ####    ##  ##    ###     ##  ##    ####    ##  ##             ##  ##     ##       ##\n"
    )
    println("------------------------------------------------------------------------------------------------------------------")
}
fun reglas() {
    cls()
    println("------------------------------------------------------------------------------------------------------------------------------------------------------")
    println(
        "              __                                               \n" +
                "        _..-''--'----_.                                        \n" +
                "      ,''.-''| .---/ _`-._                                     \n" +
                "    ,' \\ \\  ;| | ,/ / `-._`-.                                  \n" +
                "  ,' ,',\\ \\( | |// /,-._  / /                                  \n" +
                "  ;.`. `,\\ \\`| |/ / |   )/ /                                   \n" +
                " / /`_`.\\_\\ \\| /_.-.'-''/ /                                    \n" +
                "/ /_|_:.`. \\ |;'`..')  / /                                     \n" +
                "`-._`-._`.`.;`.\\  ,'  / /                                      \n" +
                "    `-._`.`/    ,'-._/ /                                       \n" +
                "      : `-/     \\`-.._/                                        \n" +
                "      |  :      ;._ (                                          \n" +
                "      :  |      \\  ` \\                                         \n" +
                "       \\         \\   |                                         \n" +
                "        :        :   ;                                         \n" +
                "        |           /                                          \n" +
                "        ;         ,'                                           \n" +
                "       /         /                                             \n" +
                "      /         /                                              \n" +
                "               / Aty  "
    )
    println(
        "### ##   ### ###   ## ##   ####       ##      ## ##\n" +
                " ##  ##   ##  ##  ##   ##   ##         ##    ##   ##\n" +
                " ##  ##   ##      ##        ##       ## ##   ####\n" +
                " ## ##    ## ##   ##  ###   ##       ##  ##   #####\n" +
                " ## ##    ##      ##   ##   ##       ## ###      ###\n" +
                " ##  ##   ##  ##  ##   ##   ##  ##   ##  ##  ##   ##\n" +
                "#### ##  ### ###   ## ##   ### ###  ###  ##   ## ##\n" +
                "\n"
    )
    print(
        "1. El objetivo del juego es obtener una mano con un valor total lo más cercano posible a 21 sin pasarse.\n" +
                "2. El juego comienza con cada jugador recibiendo 2 cartas y el dealer (maquina) recibe una carta visible y otra oculta.\n" +
                "3. El jugador puede elegir pedir otra carta (hit) o detenerse (stand) en cualquier momento.\n" +
                "4. Si un jugador supera 21, se ha pasado y pierde automáticamente.\n" +
                "5. El dealer debe seguir tomando cartas hasta tener un total de al menos 17.\n" +
                "6. Si el dealer se pasa de 21, ganas automáticamente.\n" +
                "7. Si ningún jugador ni dealer se pasa de 21, el jugador con un total más cercano a 21 gana.\n" +
                "8. Mucha suerte!(Espero sacar buena nota porque me lo he currado la verdad :)\n\n"
    )
}
fun cls() {
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
}