package org.example;
import java.util.*;
import java.util.concurrent.*;

public class TorneoTenis {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            jugadores.add(new Jugador(i));
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        String[] nombresRondas = {"OCTAVOS DE FINAL", "CUARTOS DE FINAL", "SEMIFINALES", "FINAL"};
        int rondaIndex = 0;

        while (jugadores.size() > 1) {
            imprimirEncabezadoRonda(nombresRondas[rondaIndex]);

            List<Future<Jugador>> futurosGanadores = new ArrayList<>();

            if (rondaIndex == 0) {
                // Octavos: asignar llaves específicas (1v16, 2v15, ..., 8v9)
                for (int i = 0; i < 8; i++) {
                    Jugador jugadorA = jugadores.get(i);
                    Jugador jugadorB = jugadores.get(15 - i); // espejo
                    Partido partido = new Partido(jugadorA, jugadorB);
                    futurosGanadores.add(executor.submit(partido));
                }
            } else {
                // Cuartos en adelante: emparejar por llaves consecutivas
                for (int i = 0; i < jugadores.size(); i += 2) {
                    Jugador jugadorA = jugadores.get(i);
                    Jugador jugadorB = jugadores.get(i + 1);
                    Partido partido = new Partido(jugadorA, jugadorB);
                    futurosGanadores.add(executor.submit(partido));
                }
            }

            // Limpiar y esperar resultados
            jugadores.clear();
            for (Future<Jugador> futuro : futurosGanadores) {
                Jugador ganador = futuro.get(); // bloquea hasta terminar
                jugadores.add(ganador);
            }

            rondaIndex++;
        }

        executor.shutdown();
        System.out.println("\n🏆 ¡El campeón del torneo es el Jugador " + jugadores.get(0).getId() + "! 🏆");
    }

    private static void imprimirEncabezadoRonda(String nombreRonda) {
        System.out.println("=".repeat(50));
        System.out.printf("%" + (30 + nombreRonda.length() / 2) + "s%n", "===== " + nombreRonda + " =====");
        System.out.println("=".repeat(50));
    }
}