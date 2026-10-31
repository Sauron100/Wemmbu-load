# Character Loading Screens (Fabric)

Ersetzt Minecraft-Hintergrund und Ladebildschirm durch Wemmbu- bzw. ManePear-Artwork.
Charakter wählen: **Mod Menu → Character Loading Screens → Konfigurieren**.

| Ordner | Minecraft | Java | Fabric API |
|---|---|---|---|
| `mc-1.21.1/` | 1.21.1 | 21 | 0.116.14+1.21.1 |
| `mc-26.3/`   | 26.3   | 25 | 0.161.0+26.3 |

## .jar bauen (ohne lokale Installation)
1. Alles in ein neues GitHub-Repository hochladen.
2. Reiter *Actions* → *build* abwarten (zwei Jobs, einer pro Version).
3. Unten bei *Artifacts* `character-loading-screens-mc-1.21.1` bzw. `-mc-26.3` herunterladen,
   darin liegt die fertige `.jar` (die `-sources.jar` NICHT hochladen).

## Modrinth
Pro Version eine eigene Datei hochladen: Loader Fabric, Spielversion 1.21.1 bzw. 26.3,
Abhängigkeit Fabric API (required), Mod Menu (optional).

## Bildverteilung
Ladebildschirm/Beitreten: Bild 1 · Übergang zum Hauptmenü: Bild 2 · Hauptmenü + Einstellungen: Bild 3 ·
Welten-/Serverliste: Bild 4 · alle anderen Menüs: Bild 5 (Wemmbu = Sek. 2, 3, 5, 6, 7).

## Hinweise
* Nicht kompiliert/getestet. Die 26.3-Variante nutzt Mojang-Namen (z. B. `GuiGraphicsExtractor`, `LoadingOverlay`);
  Namen wurden aus Dokumentation abgeleitet, kleine Compile-Fehler sind dort am wahrscheinlichsten.
* Bilder stammen aus fremden Videos (averagedevil / Synccraft "Unstable SMP", Skins von Wemmbu / ManePear):
  vor öffentlicher Veröffentlichung Erlaubnis einholen.
