package com.example.guitar_share.data.repository

import com.example.guitar_share.R
import com.example.guitar_share.data.remote.UberChordApi
import com.example.guitar_share.domain.model.LessonContent
import android.util.Log

class LessonRepository(private val api: UberChordApi) {

    suspend fun getLessonContent(lessonTitle: String): LessonContent {
        // 1. Check if it's a chord lesson (simple heuristic for this demo)
        if (lessonTitle.startsWith("Acorde ")) {
            val chordName = lessonTitle.removePrefix("Acorde ").substringBefore(" ") // Extract "C", "A", "G"
            return try {
                val response = api.getChord(chordName)
                if (response.isNotEmpty()) {
                    val chordData = response[0]
                    LessonContent(
                        title = lessonTitle,
                        subtitle = "Aprende el acorde ${chordData.chordName}",
                        imageUrl = R.drawable.changotristeconlibro, // Placeholder, normally would be a dynamic diagram
                        body = "El acorde ${chordData.chordName} es fundamental. \nDedos: ${chordData.fingering} \nCuerdas: ${chordData.strings}",
                        chordName = chordData.chordName,
                        fingering = chordData.fingering,
                        strings = chordData.strings
                    )
                } else {
                    getStaticContent(lessonTitle)
                }
            } catch (e: Exception) {
                Log.e("LessonRepository", "Error fetching chord: ${e.message}")
                getStaticContent(lessonTitle) // Fallback
            }
        }
        
        return getStaticContent(lessonTitle)
    }

    private fun getStaticContent(title: String): LessonContent {
        return when (title) {
            // FUNDAMENTOS
            "Púa y rasgueo" -> LessonContent(
                title = "Púa y rasgueo",
                subtitle = "Dominando la mano derecha",
                imageUrl = R.drawable.lesson1,
                body = "El uso correcto de la púa es esencial para lograr un sonido limpio y consistente. Sostén la púa firmemente entre el pulgar y el índice, dejando solo la punta visible. \n\nEjercicio 1: Rasgueo Alternado (Alternate Picking)\n- Toca una cuerda hacia abajo.\n- Luego, toca la misma cuerda hacia arriba.\n- Repite este movimiento en cada cuerda, manteniendo un ritmo constante como el tic-tac de un reloj.\n\nRecuerda mantener la muñeca relajada; el movimiento debe prevenir de la rotación de la muñeca, no del codo."
            )
            "Digitación abajo" -> LessonContent(
                title = "Digitación abajo",
                subtitle = "Control y presión",
                imageUrl = R.drawable.lesson2,
                body = "La digitación descendente se refiere a colocar los dedos en el diapasón de manera eficiente. Usa la punta de tus dedos, no la huella, para presionar la cuerda justo detrás del traste de metal.\n\nConsejo Clave: Mantén el pulgar detrás del mástil, alineado aproximadamente con tu dedo medio. Esto crea un efecto de 'pinza' natural que reduce la fatiga."
            )
            "Digitación arriba" -> LessonContent(
                title = "Digitación arriba",
                subtitle = "Independencia de dedos",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Al levantar los dedos, intenta mantenerlos cerca de las cuerdas. Un error común es alejar demasiado los dedos, lo que reduce la velocidad.\n\nEjercicio: 'La Araña'\n- Coloca los dedos 1, 2, 3 y 4 en los primeros cuatro trastes de la sexta cuerda.\n- Levántalos uno por uno y muévelos a la quinta cuerda en el mismo orden.\n- Este ejercicio mejora la independencia y economía de movimiento."
            )
            "Nombres cuerdas" -> LessonContent(
                title = "Nombres de las cuerdas",
                subtitle = "E-A-D-G-B-e",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La guitarra estándar tiene 6 cuerdas. Se cuentan de abajo (la más fina) hacia arriba (la más gruesa).\n\n1. Mi (E) - Aguda\n2. Si (B)\n3. Sol (G)\n4. Re (D)\n5. La (A)\n6. Mi (E) - Grave\n\nUna frase para recordar: 'El Buen Guitarrista Debe Aprender Eso' (leído de agudo a grave o viceversa según tu mnemotecnia favorita)."
            )
            "Partes y postura" -> LessonContent(
                title = "Partes y postura",
                subtitle = "La base de todo",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Conocer tu instrumento es vital. Las partes principales son:\n- Clavijero: Mecanismo para afinar.\n- Mástil/Diapasón: Donde se digitan las notas. Dividido por trastes.\n- Cuerpo: La caja de resonancia (acústica) o donde se alojan las pastillas (eléctrica).\n\nPostura Correcta:\nSiéntate en el borde de una silla sin brazos. La guitarra debe descansar sobre tu pierna (derecha para estilo casual, izquierda para clásico) manteniendo el mástil elevado a unos 45 grados. Tu espalda debe estar recta."
            )
            
            // ACORDES BASE
            "Cambios de acorde" -> LessonContent(
                title = "Cambios de acorde",
                subtitle = "Transiciones fluidas",
                imageUrl = R.drawable.changotristeconlibro,
                body = "El secreto para cambiar rápido de acordes es identificar los 'dedos pivote' (dedos que no se mueven entre dos acordes) o 'dedos guía' (dedos que se deslizan por la misma cuerda).\n\nPráctica: Cambia entre Do Mayor y La Menor por 1 minuto. Solo tienes que mover el dedo anular; los otros dos permanecen igual. Visualiza el siguiente acorde antes de mover la mano."
            )
            "Canción simple" -> LessonContent(
                title = "Canción simple",
                subtitle = "Tu primera melodía",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Vamos a aplicar lo aprendido con una progresión simple: G - D - C - G.\n\nRasgueo: Abajo, Abajo, Arriba, Arriba, Abajo, Arriba.\n\nEsta progresión es la base de miles de canciones populares. Intenta mantener el ritmo constante, incluso si tienes que detenerte para cambiar de acorde al principio. La fluidez vendrá con el tiempo."
            )

            // RITMO PRÁCTICO
            "Compás 4/4" -> LessonContent(
                title = "Compás 4/4",
                subtitle = "El pulso de la música",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La mayoría de la música rock y pop está en 4/4. Esto significa que hay 4 tiempos por compás.\n\nCuenta en voz alta: 1, 2, 3, 4. El acento suele ir en el 1 y el 3, o en el 2 y el 4 para rock (backbeat)."
            )
            "Negras y Corcheas" -> LessonContent(
                title = "Negras y Corcheas",
                subtitle = "Subdividiendo el tiempo",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Negra: Vale 1 tiempo (1 golpe por click).\nCorcheas: Valen medio tiempo. Entran 2 notas por click. Cuéntalas como '1-y, 2-y, 3-y, 4-y'.\nPractica alternar un compás de negras y uno de corcheas."
            )
            "Silencios" -> LessonContent(
                title = "Silencios",
                subtitle = "La música es también silencio",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Un error común es ignorar los silencios. Apaga el sonido de las cuerdas con tu mano derecha (palm mute) o izquierda para crear espacios. El 'groove' vive en los silencios."
            )
            "Ritmo de Rock" -> LessonContent(
                title = "Ritmo de Rock",
                subtitle = "Corcheas con acento",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Toca corcheas constantes hacia abajo (Downstrokes) en las cuerdas graves (Power Chords). Acentúa los tiempos 2 y 4 para dar sensación de empuje."
            )
            "Sincopa básica" -> LessonContent(
                title = "Síncopa",
                subtitle = "Acentuando el contratiempo",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La síncopa ocurre cuando tocas una nota en la parte débil del tiempo (el 'y') y la dejas sonar sobre el tiempo fuerte. Crea una sensación de sorpresa y movimiento."
            )

            // ESCALA PENTATÓNICA
            "Posición 1 (La Menor)" -> LessonContent(
                title = "Posición 1 (Am)",
                subtitle = "La escala rey de la guitarra",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Trastes en 6ta cuerda: 5-8\n5ta: 5-7\n4ta: 5-7\n3ra: 5-7\n2da: 5-8\n1ra: 5-8.\nEsta es tu zona segura para improvisar en La Menor (Am) o Do Mayor (C)."
            )
            "Posición 2" -> LessonContent(
                title = "Posición 2",
                subtitle = "Expandiendo el mapa",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La posición 2 comienza donde termina la 1. Empieza en el traste 8 de la sexta cuerda (nota Do). Conectar posiciones te permite recorrer todo el mástil."
            )
            "Uso en Solos" -> LessonContent(
                title = "Uso en Solos",
                subtitle = "Creando melodías",
                imageUrl = R.drawable.changotristeconlibro,
                body = "No subas y bajes la escala como un robot. Salta cuerdas, repite notas y varía el ritmo. Intenta cantar una frase y luego tocarla."
            )
            "Patrones Melódicos" -> LessonContent(
                title = "Patrones Melódicos",
                subtitle = "Secuencias",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Practica patrones numéricos, por ejemplo '1-2-3', '2-3-4', '3-4-5'. Esto rompe la monotonía de tocar la escala en orden lineal."
            )
            "Improvisando" -> LessonContent(
                title = "Improvisando",
                subtitle = "Tu primer solo",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Pon una pista de acompañamiento (Backing Track) en La Menor. Usa solo las notas de la Posición 1. Cierra los ojos y escucha. Si suena bien, ¡está bien!"
            )

            // INTERMEDIO - TÉCNICAS
            "Hammer-on" -> LessonContent(
                title = "Hammer-on",
                subtitle = "Ligado ascendente",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Toca una nota y 'golpea' (hammer) el siguiente traste con otro dedo sin volver a pulsar con la mano derecha. Debe sonar fuerte y claro."
            )
            "Pull-off" -> LessonContent(
                title = "Pull-off",
                subtitle = "Ligado descendente",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Lo opuesto al hammer-on. Toca una nota y 'jala' el dedo hacia abajo para hacer sonar la nota anterior (que ya debes tener pisada). Es como un pequeño pellizco a la cuerda."
            )
            "Slide (Deslizado)" -> LessonContent(
                title = "Slide",
                subtitle = "Conectando notas",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Toca una nota y desliza el dedo hasta otro traste manteniendo la presión. No dejes de presionar hasta llegar al destino. Es genial para vocalizar melodías."
            )
            "Bending (Estirado)" -> LessonContent(
                title = "Bending",
                subtitle = "Haciendo llorar la guitarra",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Empuja la cuerda hacia arriba (o abajo) para subir su tono. Usa la muñeca para girar, no solo la fuerza de los dedos. Apoya el dedo que estira con los dedos de atrás para más fuerza."
            )
            "Vibrato" -> LessonContent(
                title = "Vibrato",
                subtitle = "El alma del tono",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Una serie de micro-bendings rápidos y rítmicos que dan vida a una nota sostenida. Cada guitarrista tiene un vibrato único. Practica lento y amplio, luego rápido y sutil."
            )

            // CEJILLAS
            "Técnica de Cejilla" -> LessonContent(
                title = "La temida Cejilla",
                subtitle = "Fuerza y técnica",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Usa el lado huesudo de tu dedo índice, no la parte carnosa plana. Colócalo muy cerca del traste metálico. Apoya con el pulgar en el centro del mástil."
            )
            "Acorde Fa (F)" -> LessonContent(
                title = "Acorde Fa (F)",
                subtitle = "El primer desafío",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Cejilla en traste 1 + forma de Mi Mayor. Asegúrate de que suenen todas las cuerdas. Si duele, descansa. Es normal al principio."
            )
            "Acorde Si Menor (Bm)" -> LessonContent(
                title = "Acorde Si Menor (Bm)",
                subtitle = "Cejilla en 5ta cuerda",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Cejilla en traste 2 (desde la 5ta cuerda) + forma de La Menor. Es un acorde triste y melancólico muy usado."
            )
            "Moviendo acordes" -> LessonContent(
                title = "Acordes Movibles",
                subtitle = "El poder de la cejilla",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La forma de Fa en el traste 3 es Sol (G). En el 5 es La (A). ¡Con una sola forma te sabes 12 acordes!"
            )
            "Resistencia" -> LessonContent(
                title = "Resistencia",
                subtitle = "Entrenando la mano",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Practica canciones con muchas cejillas (como 'Creep' de Radiohead) para construir fuerza. Relaja la presión en cuanto dejes de tocar el acorde."
            )

            // ARPEGIOS
            "Púa alternada" -> LessonContent(
                title = "Púa alternada",
                subtitle = "Eficiencia",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Estricto Abajo-Arriba-Abajo-Arriba. Incluso si cambias de cuerda. Esto es crucial para la velocidad."
            )
            "Fingerpicking 101" -> LessonContent(
                title = "Fingerpicking",
                subtitle = "Sin púa",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Pulgar (P) para bajos (cuerdas 6, 5, 4). Índice (i) para 3ra. Medio (m) para 2da. Anular (a) para 1ra."
            )
            "Patrón P-i-m-a" -> LessonContent(
                title = "Patrón P-i-m-a",
                subtitle = "Arpegio clásico",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Toca Pulgar -> Índice -> Medio -> Anular. Intenta hacerlo fluido, como una cascada de notas."
            )
            "Arpegios de tríadas" -> LessonContent(
                title = "Tríadas",
                subtitle = "Barridos",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Toca las notas del acorde una por una (Tónica, 3ra, 5ta) en lugar de rasguear. Útil para intros y baladas."
            )
            "Romanza (Intro)" -> LessonContent(
                title = "Romanza",
                subtitle = "Clásico español",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Usa el dedo anular para la melodía en la 1ra cuerda, mientras apoyas con arpegios en las otras. Es un excelente estudio de independencia."
            )

            // TEORÍA
            "Intervalos" -> LessonContent(
                title = "Intervalos",
                subtitle = "Distancia entre notas",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Unísono, Segunda, Tercera (Mayor/Menor), Quinta, Octava. Saber esto te permite construir cualquier acorde o escala."
            )
            "Círculo de Quintas" -> LessonContent(
                title = "Círculo de Quintas",
                subtitle = "El mapa armónico",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Do -> Sol -> Re -> La... Te dice qué alteraciones tiene cada tonalidad y qué acordes suenan bien juntos."
            )
            "Formación de Acordes" -> LessonContent(
                title = "Formación de Acordes",
                subtitle = "1 - 3 - 5",
                imageUrl = R.drawable.changotristeconlibro,
                body = "Un acorde mayor se forma con la 1ra, 3ra Mayor y 5ta Justa de la escala. Si bajas la 3ra medio tono, se vuelve Menor."
            )
            "Tonalidad Mayor" -> LessonContent(
                title = "Campo Armónico Mayor",
                subtitle = "Familias de acordes",
                imageUrl = R.drawable.changotristeconlibro,
                body = "En Do Mayor: C, Dm, Em, F, G, Am, Bdim. Aprende este patrón (Más, Men, Men, May, May, Men, Dism) y aplícalo a cualquier tono."
            )
            "Lectura de partitura" -> LessonContent(
                title = "Partitura vs Tab",
                subtitle = "El lenguaje universal",
                imageUrl = R.drawable.changotristeconlibro,
                body = "La tablatura te dice 'donde poner el dedo'. La partitura te dice 'qué nota suena y cuánto dura'. Aprender lo básico del ritmo en partitura es muy útil."
            )

            // GENERIC For Advanced (Improvisación, Jazz, etc. - filling generic specifically for these to avoid 'default')
            "Fraseo" -> LessonContent("Fraseo", "Hablar con la guitarra", R.drawable.changotristeconlibro, "No toques notas al azar. Construye oraciones: Inicio, desarrollo, clímax y resolución.")
            "Call & Response" -> LessonContent("Call & Response", "Pregunta y respuesta", R.drawable.changotristeconlibro, "Toca una frase (pregunta) y respóndete a ti mismo con otra frase complementaria (respuesta). B.B. King era el maestro de esto.")
            "Target Notes" -> LessonContent("Target Notes", "Aterrizaje", R.drawable.changotristeconlibro, "Apunta a las notas del acorde que está sonando en ese momento, especialmente la 3ra o la tónica.")
            "Mezclando escalas" -> LessonContent("Mezclando escalas", "Mayor y Menor", R.drawable.changotristeconlibro, "B.B. King mezclaba la pentatónica mayor y menor constantemente para obtener ese sonido dulce y bluesero.")
            "Desarrollo de motivos" -> LessonContent("Motivos", "Repetición con variación", R.drawable.changotristeconlibro, "Toma una idea simple de 3 notas. Repítela rítmicamente. Transpórtala. Inviértela. Exprime esa idea al máximo.")
            
            "Acordes de 7ma" -> LessonContent("Acordes de 7ma", "Color Jazz", R.drawable.changotristeconlibro, "Maj7 (alegre/nostálgico), m7 (suave), 7 (tenso/blues). Son la base del Jazz.")
            "Acordes de 9na y 13ra" -> LessonContent("Extensiones", "Sofisticación", R.drawable.changotristeconlibro, "Añadir notas más allá de la octava da colores impresionantes. Un Cmaj9 suena mucho más rico que un C normal.")
            "Walking Bass" -> LessonContent("Walking Bass", "Bajo caminante", R.drawable.changotristeconlibro, "Simula un contrabajo tocando negras mientras tocas acordes sincopados. Difícil pero gratificante.")
            "Escala Alterada" -> LessonContent("Escala Alterada", "Super Locrio", R.drawable.changotristeconlibro, "Úsala sobre acordes dominantes para crear tensión máxima antes de resolver.")
            "Standard: Autumn Leaves" -> LessonContent("Autumn Leaves", "El himno del Jazz", R.drawable.changotristeconlibro, "Estudia la progresión ii-V-I tanto en mayor como en menor. Es la escuela perfecta para el jazz.")

            "Funk Strumming" -> LessonContent("Funk", "Mano derecha suelta", R.drawable.changotristeconlibro, "Mantén la mano derecha moviéndose siempre en semicorcheas (1-e-y-a). Solo presiona la mano izquierda cuando quieras que suene el acorde (staccato).")
            "Compases irregulares" -> LessonContent("Odd Time", "5/4, 7/8", R.drawable.changotristeconlibro, "Cuenta en grupos. 5/4 puede ser 1-2-3, 1-2. Siente el pulso desigual.")
            "Polirritmia" -> LessonContent("Polirritmia", "3 contra 2", R.drawable.changotristeconlibro, "Tocar tresillos sobre un pulso binario. Da una sensación de 'flotar' sobre el ritmo.")
            "Rasgueo híbrido" -> LessonContent("Hybrid Picking", "Púa + Dedos", R.drawable.changotristeconlibro, "Usa la púa para los bajos y los dedos medio/anular para las cuerdas agudas simultáneamente. Estilo Country/Chicken Pickin'.")
            "Groove y Pocket" -> LessonContent("El Pocket", "Donde se siente bien", R.drawable.changotristeconlibro, "No te adelantes ni te atrases. Siente el bombo y la caja de la batería. Tocar 'en el pocket' es más importante que tocar rápido.")

            "Jónico y Eólico" -> LessonContent("Modos Naturales", "Mayor y Menor", R.drawable.changotristeconlibro, "Jónico es la escala mayor natural. Eólico es la menor natural. Ya los conoces.")
            "Dórico (Santana)" -> LessonContent("Modo Dórico", "Menor pero brillante", R.drawable.changotristeconlibro, "Escala menor con la 6ta Mayor. Sonido clásico de Santana o Funk.")
            "Mixolidio (Rock/Blues)" -> LessonContent("Modo Mixolidio", "Dominante", R.drawable.changotristeconlibro, "Escala mayor con la 7ma menor. Sonido AC/DC o Blues rock.")
            "Lidio (Sueño)" -> LessonContent("Modo Lidio", "Mágico", R.drawable.changotristeconlibro, "Escala mayor con la 4ta aumentada (#4). Sonido de ensueño, cine, Steve Vai.")
            "Aplicación modal" -> LessonContent("Uso Modal", "Contexto es todo", R.drawable.changotristeconlibro, "No es solo la escala, es el acorde de fondo. Tocar Do Mayor sobre un bajo de Re suena a Re Dórico.")

            else -> LessonContent(
                title = title,
                subtitle = "Lección avanzada",
                imageUrl = R.drawable.changotristeconlibro,
                body = "En esta lección de nivel $title, profundizaremos en conceptos técnicos avanzados. Asegúrate de haber dominado los módulos anteriores. Practica lentamente con metrónomo y enfócate en la limpieza de cada nota antes de aumentar la velocidad. La consistencia es clave para el progreso en la guitarra."
            )
        }
    }
}
