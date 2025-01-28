Programowanie urządzeń mobilnych laboratorium L_X_ 

# Dokumentacja projetu: **Aplikacja Alarmowa**

## Zespoł projetowy:
_Kacper Bułaś_

## Opis projektu
Aplikacja Helply to proste w urzyciu narzędzie bezpieczeństwa osobistego, które umożliwia natychmiastowe powiadomienie służb ratunkowych lub zdefiniowanych kontaktów w sytuacjach kryzysowych. Głównym celem jest zapewnienie szybkiej i prostej komunikacji w sytuacjach zagrożenia. 

## Zakres projektu opis funkcjonalności:
1. Natychmiastowe wysłanie wiadomości SOS do predefiniowanych kontaktów
2. Automatyczne udostępnienie aktualnej lokalizacji
3. Wywołanie połączenia alarmowego do pierwszego dodanego kontaktu
4. Zarządzanie kontaktami awaryjnymi

## Panele / zakładki aplikacji 
- Menu główne

![image](https://github.com/user-attachments/assets/40ff6c27-e7af-4631-a662-235ba1f55e62)

- Panel z wybranymi kontaktami alarmowymi

![image](https://github.com/user-attachments/assets/3b547f11-d94e-4374-b0e9-89ca65136509)

- Panel wyświetlania wybranych kontaktów

![image](https://github.com/user-attachments/assets/723b520d-df85-4718-94ad-f39190be68c0)

- Panel do wyboru kontaktu

![image](https://github.com/user-attachments/assets/47f945ad-6c26-443e-9b25-8f013ef251be)

- Panel wyboru akcji "ratunkowej"

![image](https://github.com/user-attachments/assets/22ef177c-112a-4130-9813-7906bcc56233)


## Baza danych
Do przechowywania nazwy oraz numeru wybranych kontaktów używane jest SharedPreferences.
```
data class Contact(val name: String, val phone: String)
```

## Wykorzystane uprawnienia aplikacji do:
- Wyświetlanie Kontaktów,
- Wykonywanie połączeń
- Wysyłanie SMS-ów,
- Lokalizacji,

## [Plik instalacyjny](.Helply/app/release/app-release.apk)
