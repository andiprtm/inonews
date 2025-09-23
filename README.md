# 📰 Inonews

Inonews is a modern Android news reader built with **Kotlin**, **MVVM**, and **Jetpack components**. It delivers top headlines with **offline-first caching**, smooth infinite scrolling, polished detail pages, and an in-app web reader — all designed to showcase production-ready architecture, resilient networking, and clean UI practices.

---

## 📑 Table of Contents

* [Overview](#overview)
* [Features](#features)
* [Tech Stack & Architecture](#tech-stack--architecture)
* [Screens](#screens)
* [Getting Started](#getting-started)

  * [Prerequisites](#prerequisites)
  * [Setup](#setup)
  * [Run the App](#run-the-app)
* [Build Variants](#build-variants)
* [Highlights (What I’m Proud Of)](#highlights-what-im-proud-of)
* [What I’d Improve Next](#what-id-improve-next)
* [Challenges & Solutions](#challenges--solutions)
* [Project Structure](#project-structure)

---

## 🌟 Overview

* **Endless feed with Paging 3**: Streams headlines from [NewsAPI](https://newsapi.org), handles loading, empty, and error states gracefully.
* **Offline-first caching with Room**: Articles are persisted locally with `RemoteMediator`, so users can browse even without internet.
* **Detail screen**: Displays hero images, author/date metadata, and story content with a CTA to continue reading.
* **In-app web reader**: WebView with progress indicator and proper lifecycle cleanup to prevent leaks.
* **Single-activity navigation**: `MainActivity` hosts a Navigation Graph that orchestrates screen transitions and back-stack behavior.

---

## 🚀 Features

* **Infinite headline feed** with retry, error messaging, and empty-state UI.
* **Offline caching**: Articles are saved in Room DB and refreshed transparently on new API calls.
* **Detailed story view** with polished presentation (Glide images, ISO8601 → human-friendly date formatting).
* **Full article WebView** with loading spinner and explicit lifecycle teardown.
* **Navigation-driven single activity** following best practices.

---

## 🛠 Tech Stack & Architecture

* **Kotlin + AndroidX** (min SDK 24, target SDK 36, Kotlin 2.0, ViewBinding).
* **MVVM + Repository Pattern** for separation of concerns.
* **Paging 3 + RemoteMediator** for incremental loading, offline-first support, and error/retry handling.
* **Room Database** with `ArticleEntity` + `RemoteKeys` to sync local and remote data.
* **Retrofit + OkHttp** with interceptors for logging and API key injection from `local.properties`.
* **Glide** for image loading with placeholders and rounded corners.
* **Navigation Component** for fragment navigation and back-stack management.
* **Manual DI** via a lightweight `Injection` + `ViewModelFactory`.

---

## 📱 Screens

| Screen           | Description                                                                                                           |
| ---------------- | --------------------------------------------------------------------------------------------------------------------- |
| **Home**         | Paginated headline list backed by Room (offline-first), with pull-to-refresh retry and empty-state messaging.         |
| **Detail**       | Rich article view displaying title, author, publication date, hero image, and summary with a CTA to continue reading. |
| **Full Article** | Embedded WebView for uninterrupted reading plus a progress spinner while pages load.                                  |

---

## ⚡ Getting Started

### Prerequisites

* **Android Studio**: Narwhal Feature Drop | 2025.1.2 Patch
* JDK 17.
* [NewsAPI.org](https://newsapi.org) API key.

### Setup

1. **Clone the project**

   ```bash
   git clone https://github.com/<your-username>/inonews.git
   cd inonews
   ```
2. **Create or update `local.properties`** in the project root:

   ```properties
   sdk.dir=/path/to/Android/sdk
   news_api=<your_api_key>
   dev_base_url=https://newsapi.org/v2/
   base_url=https://newsapi.org/v2/
   ```
3. (Optional) Add signing configs for release build:

   ```properties
   keystore.file=/path/to/keystore.jks
   keystore.password=****
   keystore.alias=****
   keystore.key_password=****
   ```

### Run the App

* **Android Studio**: Sync Gradle → Run `app` on emulator/device (API 24+).
* **CLI**:

  ```bash
  ./gradlew assembleDebug
  ./gradlew installDebug
  ```

---

## 🧪 Build Variants

* **debug**: uses `dev_base_url`, enables OkHttp logging, suffixes `.debug`.
* **release**: uses `base_url`, disables verbose logs, ready for Play Store.

---

## 💡 Highlights (What I’m Proud Of)

* **Offline-first architecture**: Integrated Room + RemoteMediator so data persists and is browsable offline.
* **Graceful paging UX**: Combined Paging 3 with `LoadStateAdapter` for retry, empty state, and progress indicators.
* **Unlimited scrolling with error recovery**: Seamless retry when paging fails.
* **Reusable `Result` wrapper**: Keeps UI observers consistent for one-shot API calls.
* **Polished visuals**: Glide image handling + human-readable date formatting.
* **Memory-safe WebView**: Explicitly clears cache/history to avoid leaks.
* **Clean architecture**: MVVM with well-separated concerns.

---

## 🔮 What I’d Improve Next

* **Search & filter support** by keyword, category, or country.
* **Dynamic theming & accessibility** (TalkBack labels, larger touch targets).
* **Test coverage** with mocked API + UI navigation tests.
* **Dependency injection with Hilt** to replace manual factories and centralize lifecycle-aware bindings.
* **Jetpack Compose migration** for a modern declarative UI while keeping interoperability with XML.

---

## 🧩 Challenges & Solutions

* **Offline First**: Solved with `RemoteMediator` to sync local + remote cleanly.
* **Duplicate & missing items**: Fixed with Room primary key (`url`) + `OnConflictStrategy.REPLACE`.
* **Load state vs Result**: Separated concerns → `Result` for single calls, `LoadStateFlow` for paging.
* **Activity → Fragment migration**: Ensured `viewLifecycleOwner` for LiveData observers to avoid leaks.
* **API key security**: Gradle `buildConfigField` from `local.properties`, excluded from VCS.
* **WebView leaks**: Explicitly destroyed in `onDestroyView`.

---

## 📂 Project Structure

```
app/
├── data/
│   ├── local/          # Room database, entities, DAO, RemoteKeys
│   ├── remote/         # Retrofit service, DTOs, PagingSource
│   └── mapper/         # DTO → Entity mappers
├── di/                 # Injection helpers
├── repository/         # Repository layer
├── ui/
│   ├── adapter/        # PagingDataAdapter + LoadStateAdapter
│   ├── screen/         # Home, Detail, FullArticle fragments
│   ├── MainActivity    # NavHost container
│   └── MainViewModel   # ViewModel
└── utils/              # ViewModelFactory helpers, Result wrapper
```
