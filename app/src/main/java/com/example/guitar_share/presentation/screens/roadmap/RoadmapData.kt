package com.example.guitar_share.presentation.screens.roadmap

import androidx.compose.ui.graphics.Color
import com.example.guitar_share.R

data class RoadmapModule(
    val title: String,
    val color: Color,
    val imageRes: Int = R.drawable.changotristeconlibro // Placeholder default
)

object RoadmapData {
    // Defined Colors
    private val Yellow = Color(0xFFFFC107)
    private val Pink = Color(0xFFEC407A)
    private val Blue = Color(0xFF42A5F5)
    private val Green = Color(0xFF66BB6A)

    private val beginnerModules = listOf(
        RoadmapModule("Fundamentos", Yellow, R.drawable.modulo1),
        RoadmapModule("Acordes Base", Pink, R.drawable.modulo2),
        RoadmapModule("Ritmo Práctico", Blue, R.drawable.modulo3),
        RoadmapModule("Escala Pentatónica", Green, R.drawable.modulo4)
    )

    private val intermediateModules = listOf(
        RoadmapModule("Técnicas", Yellow),
        RoadmapModule("Cejillas", Pink),
        RoadmapModule("Arpegios", Blue),
        RoadmapModule("Teoría Musical", Green)
    )

    private val advancedModules = listOf(
        RoadmapModule("Improvisación", Yellow),
        RoadmapModule("Jazz & Fusion", Pink),
        RoadmapModule("Rítmica Pro", Blue),
        RoadmapModule("Modos Griegos", Green)
    )

    fun getModulesForLevel(level: String?): List<RoadmapModule> {
        return when (level?.lowercase()) {
            "principiante" -> beginnerModules
            "intermedio" -> intermediateModules
            "avanzado" -> advancedModules
            else -> beginnerModules // Default to beginner
        }
    }

    data class Lesson(
        val id: Int,
        val title: String,
        val isCompleted: Boolean = false, // True = Unlocked/Orange, False = Locked/Grey
        val isCurrent: Boolean = false,
        val imageRes: Int = R.drawable.changotristeconlibro
    )

    fun getLessonsForModule(moduleTitle: String): List<Lesson> {
        // Mock data logic - 5 lessons per module as requested
        return when (moduleTitle) {
            "Fundamentos" -> listOf(
                Lesson(1, "Púa y rasgueo", true, imageRes = R.drawable.lesson1),
                Lesson(2, "Digitación abajo", true, imageRes = R.drawable.lesson2),
                Lesson(3, "Digitación arriba", false, imageRes = R.drawable.lesson1), // Alternating for variety or placeholder
                Lesson(4, "Nombres cuerdas", false, imageRes = R.drawable.lesson2),
                Lesson(5, "Partes y postura", false)
            )

            "Acordes Base" -> listOf(
                Lesson(6, "Acorde Do (C)", true),
                Lesson(7, "Acorde La (A)", false),
                Lesson(8, "Acorde Sol (G)", false),
                Lesson(9, "Cambios de acorde", false),
                Lesson(10, "Canción simple", false)
            )

            "Ritmo Práctico" -> listOf(
                Lesson(11, "Compás 4/4", true),
                Lesson(12, "Negras y Corcheas", false),
                Lesson(13, "Silencios", false),
                Lesson(14, "Ritmo de Rock", false),
                Lesson(15, "Sincopa básica", false)
            )

            "Escala Pentatónica" -> listOf(
                Lesson(16, "Posición 1 (La Menor)", true),
                Lesson(17, "Posición 2", false),
                Lesson(18, "Uso en Solos", false),
                Lesson(19, "Patrones Melódicos", false),
                Lesson(20, "Improvisando", false)
            )
            // INTERMEDIO
            "Técnicas" -> listOf(
                Lesson(21, "Hammer-on", true),
                Lesson(22, "Pull-off", false),
                Lesson(23, "Slide (Deslizado)", false),
                Lesson(24, "Bending (Estirado)", false),
                Lesson(25, "Vibrato", false)
            )

            "Cejillas" -> listOf(
                Lesson(26, "Técnica de Cejilla", true),
                Lesson(27, "Acorde Fa (F)", false),
                Lesson(28, "Acorde Si Menor (Bm)", false),
                Lesson(29, "Moviendo acordes", false),
                Lesson(30, "Resistencia", false)
            )

            "Arpegios" -> listOf(
                Lesson(31, "Púa alternada", true),
                Lesson(32, "Fingerpicking 101", false),
                Lesson(33, "Patrón P-i-m-a", false),
                Lesson(34, "Arpegios de tríadas", false),
                Lesson(35, "Romanza (Intro)", false)
            )

            "Teoría Musical" -> listOf(
                Lesson(36, "Intervalos", true),
                Lesson(37, "Círculo de Quintas", false),
                Lesson(38, "Formación de Acordes", false),
                Lesson(39, "Tonalidad Mayor", false),
                Lesson(40, "Lectura de partitura", false)
            )
            // AVANZADO
            "Improvisación" -> listOf(
                Lesson(41, "Fraseo", true),
                Lesson(42, "Call & Response", false),
                Lesson(43, "Target Notes", false),
                Lesson(44, "Mezclando escalas", false),
                Lesson(45, "Desarrollo de motivos", false)
            )

            "Jazz & Fusion" -> listOf(
                Lesson(46, "Acordes de 7ma", true),
                Lesson(47, "Acordes de 9na y 13ra", false),
                Lesson(48, "Walking Bass", false),
                Lesson(49, "Escala Alterada", false),
                Lesson(50, "Standard: Autumn Leaves", false)
            )

            "Rítmica Pro" -> listOf(
                Lesson(51, "Funk Strumming", true),
                Lesson(52, "Compases irregulares", false),
                Lesson(53, "Polirritmia", false),
                Lesson(54, "Rasgueo híbrido", false),
                Lesson(55, "Groove y Pocket", false)
            )

            "Modos Griegos" -> listOf(
                Lesson(56, "Jónico y Eólico", true),
                Lesson(57, "Dórico (Santana)", false),
                Lesson(58, "Mixolidio (Rock/Blues)", false),
                Lesson(59, "Lidio (Sueño)", false),
                Lesson(60, "Aplicación modal", false)
            )

            else -> emptyList() // Should not happen if all are covered
        }
    }
}