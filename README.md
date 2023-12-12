# <p align="center"> Dark (Тёмная)

<p align="center"><img width="30%" alt="Тёмная" src="https://github.com/MaxBQb/TheDarkApp.Client/blob/main/app/src/main/ic_launcher-playstore.png"></p>

###### <p align="center"> Мобильное приложение для тренировки "ночного зрения"

## <p align="center"> [![Latest](https://img.shields.io/github/v/tag/MaxBQb/TheDarkApp.Client?sort=date&label=скачать&style=for-the-badge&color=424242)](https://github.com/MaxBQb/TheDarkApp.Client/releases/latest/download/DarkApp.apk)

## Навигация:
- [Репозиторий API](https://github.com/MaxBQb/TheDarkApp.API)
- **Репозиторий приложения**
- [Документация на API](https://thedarkapp.eu.org/docs/)  [![Состояние сервера](https://img.shields.io/website.svg?style=flat&labelColor=424242&down_color=red&label=%D0%A1%D0%B5%D1%80%D0%B2%D0%B5%D1%80&down_message=%D0%92%D1%8B%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%BD&up_color=green&up_message=%D0%94%D0%BE%D1%81%D1%82%D1%83%D0%BF%D0%B5%D0%BD&url=https://thedarkapp.eu.org/docs)](https://thedarkapp.eu.org/docs/)
- Адрес: `https://thedarkapp.eu.org/docs/`

## Демонстрация функционала
https://github.com/MaxBQb/TheDarkApp.Client/assets/27343275/e36779e2-8957-405d-af64-d3ecaf0b041d

## Технологический стек и немного деталей
- Вёрстка: Jetpack Compose (ранее XML + Data Binding)
- Кратко об архитектуре: Clean Architecture, MVI/MVVM, Многомодульность (by feature & by layer)
- DI: Koin (с кодогенерацией)
- Навигация: Compose Destinations
- Локальное хранение данных: Room (SQL), DataStore (ранее SharedPreferences)
- Работа с сетью: Retrofit2
- Реактивность: Kotlin Coroutines & Kotlin Flow & StateFlow (ранее LiveData)
- Работа с изображениями: Coil (ранее Glide)
- Пагинация: Присутствует (paging lib)
- Общая конфигурация Gradle: Convention Plugins & libs.versions (ещё совсем немного buildSrc)
- Поддержка версий Android 5.0+ (API 21+)
