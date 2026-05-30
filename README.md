# Medical Center Manager

Medical Center Manager to desktopowa aplikacja do podstawowego zarządzania przychodnią medyczną. Projekt umożliwia obsługę pacjentów, lekarzy, wizyt, statusów wizyt oraz prostych raportów.

## Technologie

- Java 17
- JavaFX
- Gradle
- JUnit 5
- AssertJ
- Mockito

## Funkcjonalności

- Zarządzanie pacjentami - implementer: Vladyslav Bychkovskyi
- Zarządzanie lekarzami - implementer: Vladyslav Bychkovskyi
- Zarządzanie wizytami - implementer: Vladyslav Bychkovskyi
- Maszyna stanów wizyt - implementer: Vladyslav Bychkovskyi
- Raporty - implementer: Vladyslav Bychkovskyi

## Opis funkcji

### Zarządzanie pacjentami

Aplikacja pozwala dodawać pacjentów, wyświetlać ich w tabeli oraz wyszukiwać po imieniu, nazwisku lub numerze telefonu.

### Zarządzanie lekarzami

Aplikacja pozwala dodawać lekarzy, wyświetlać ich w tabeli oraz wyszukiwać po specjalizacji.

### Zarządzanie wizytami

Aplikacja pozwala tworzyć wizyty dla wybranego pacjenta i lekarza, wyświetlać listę wizyt oraz zmieniać status wizyty z poziomu interfejsu.

### Maszyna stanów wizyt

Status wizyty może być zmieniany tylko zgodnie z dozwolonymi przejściami:

- `PLANNED -> CONFIRMED`
- `PLANNED -> CANCELLED`
- `CONFIRMED -> IN_PROGRESS`
- `CONFIRMED -> CANCELLED`
- `IN_PROGRESS -> COMPLETED`

Niepoprawne przejścia kończą się wyjątkiem `InvalidStatusTransitionException`.

### Raporty

Aplikacja pokazuje liczbę wizyt według statusu oraz liczbę wizyt anulowanych i zakończonych.

## Struktura projektu

```text
src/main/java/pl/kul/medicalcenter
+-- appointment
+-- doctor
+-- patient
+-- report
+-- statemachine
+-- storage
+-- ui
```

Najważniejsze pakiety:

- `appointment` - model, repozytorium i serwis wizyt
- `doctor` - model, repozytorium i serwis lekarzy
- `patient` - model, repozytorium i serwis pacjentów
- `report` - serwis raportów
- `statemachine` - maszyna stanów wizyt
- `storage` - miejsce na przyszłe mechanizmy przechowywania danych
- `ui` - interfejs graficzny JavaFX

## Uruchamianie aplikacji

Wymagany jest JDK 17.

```bash
./gradlew run
```

Na Windows można użyć:

```powershell
.\gradlew.bat run
```

## Uruchamianie testów

```bash
./gradlew test
```

Na Windows:

```powershell
.\gradlew.bat test
```

## Zrzuty ekranu

### Widok pacjentów

Miejsce na zrzut ekranu.

### Widok lekarzy

Miejsce na zrzut ekranu.

### Widok wizyt

Miejsce na zrzut ekranu.

### Widok raportów

Miejsce na zrzut ekranu.

## Autorzy

- Vladyslav Bychkovskyi
