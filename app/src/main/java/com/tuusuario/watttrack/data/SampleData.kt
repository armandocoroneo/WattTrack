package com.tuusuario.watttrack.data

import java.util.UUID

object SampleData {
    suspend fun populateDatabase(dao: MeterDao) {
        val meters = dao.getAllMeters()
        if (meters.isNotEmpty()) return

        val mainId = UUID.randomUUID().toString()
        val sub1Id = UUID.randomUUID().toString()
        val sub2Id = UUID.randomUUID().toString()
        val sub3Id = UUID.randomUUID().toString()

        dao.insertMeter(Meter(mainId, "Casa Principal", "MAIN", null, "#f59e0b", "🏠"))
        dao.insertMeter(Meter(sub1Id, "Local Comercial", "SUB", mainId, "#3b82f6", "🏪"))
        dao.insertMeter(Meter(sub2Id, "Depa 3B", "SUB", mainId, "#ec4899", "🏢"))
        dao.insertMeter(Meter(sub3Id, "Depa 2A", "SUB", mainId, "#22c55e", "🏠"))

        val ahora = System.currentTimeMillis()
        val unMes = 30L * 24 * 60 * 60 * 1000

        // Lecturas MAIN
        var baseMain = 1000
        for (i in 5 downTo 0) {
            val ts = ahora - (i * (unMes / 2))
            val cons = 150 + (i * 10)
            val valKwh = baseMain + cons
            dao.insertReading(Reading(UUID.randomUUID().toString(), mainId, ts, valKwh, baseMain, cons, "Punta", 0.18f, cons * 0.18f, "Lectura de prueba MAIN", "MANUAL", null))
            baseMain = valKwh
        }

        // Lecturas SUB1
        var baseSub1 = 200
        for (i in 5 downTo 0) {
            val ts = ahora - (i * (unMes / 2))
            val cons = 40 + (i * 5)
            val valKwh = baseSub1 + cons
            dao.insertReading(Reading(UUID.randomUUID().toString(), sub1Id, ts, valKwh, baseSub1, cons, "Pico", 0.24f, cons * 0.24f, "Comercio activo", "MANUAL", null))
            baseSub1 = valKwh
        }

        // Lecturas SUB2
        var baseSub2 = 150
        for (i in 5 downTo 0) {
            val ts = ahora - (i * (unMes / 2))
            val cons = 30
            val valKwh = baseSub2 + cons
            dao.insertReading(Reading(UUID.randomUUID().toString(), sub2Id, ts, valKwh, baseSub2, cons, "Valle", 0.12f, cons * 0.12f, "Consumo estable", "MANUAL", null))
            baseSub2 = valKwh
        }

        // Lecturas SUB3
        var baseSub3 = 300
        for (i in 5 downTo 0) {
            val ts = ahora - (i * (unMes / 2))
            val cons = 50 - i
            val valKwh = baseSub3 + cons
            dao.insertReading(Reading(UUID.randomUUID().toString(), sub3Id, ts, valKwh, baseSub3, cons, "Punta", 0.18f, cons * 0.18f, "Lectura Depa 2A", "MANUAL", null))
            baseSub3 = valKwh
        }
    }
}
