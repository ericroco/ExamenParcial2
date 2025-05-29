package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

class Partido implements Callable<Jugador> {
    private final Jugador jugador1;
    private final Jugador jugador2;
    private static final Random random = new Random();

    public Partido(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    @Override
    public Jugador call() throws Exception {
        int puntosJugador1 = 0;
        int puntosJugador2 = 0;

        List<String> resultadosSets = new ArrayList<>();

        for (int set = 1; set <= 3; set++) {
            Jugador ganadorSet = simularSet();
            resultadosSets.add("Set " + set + ": " + ganadorSet);

            if (ganadorSet == jugador1) puntosJugador1++;
            else puntosJugador2++;

            if (set >= 2 && (puntosJugador1 == 2 || puntosJugador2 == 2)) break;
        }

        Jugador ganador = puntosJugador1 > puntosJugador2 ? jugador1 : jugador2;

        // Imprimir todo el partido al finalizar
        System.out.println(jugador1 + " vs " + jugador2);
        for (String resultado : resultadosSets) {
            System.out.println(resultado);
        }
        System.out.println("Ganador del partido: " + ganador + "\n");

        return ganador;
    }

    private Jugador simularSet() throws InterruptedException {
        int duracion = ThreadLocalRandom.current().nextInt(1500, 2001); // entre 1.5 y 2 segundos
        Thread.sleep(duracion);
        return random.nextBoolean() ? jugador1 : jugador2;
    }
}