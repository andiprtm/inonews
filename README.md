# 📰 Inonews

Inonews is a modern Android news reader built with **Kotlin**, **MVVM**, and **Jetpack components**. It delivers top headlines with smooth infinite scrolling, polished detail pages, and an in-app web reader — all designed to showcase production-ready architecture, resilient networking, and clean UI practices.

---

## 📑 Table of Contents
- [Overview](#overview)  
- [Features](#features)  
- [Tech Stack & Architecture](#tech-stack--architecture)  
- [Screens](#screens)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Setup](#setup)  
  - [Run the App](#run-the-app)  
- [Build Variants](#build-variants)  
- [Highlights (What I’m Proud Of)](#highlights-what-im-proud-of)  
- [What I’d Improve Next](#what-id-improve-next)  
- [Challenges & Solutions](#challenges--solutions)  
- [Project Structure](#project-structure)  

---

## 🌟 Overview
- **Endless feed with Paging 3**: Streams headlines from [NewsAPI](https://newsapi.org), handling loading, empty, and error states gracefully.  
- **Detail screen**: Displays hero images, author/date metadata, and story content with a CTA to continue reading.  
- **In-app web reader**: WebView with progress indicator and proper lifecycle cleanup to prevent leaks.  
- **Single-activity navigation**: `MainActivity` hosts a Navigation Graph that orchestrates screen transitions and back-stack behavior.  

---

## 🚀 Features
- **Infinite headline feed** with retry, error messaging, and empty-state UI.  
- **Detailed story view** with polished presentation (Glide images, ISO8601 → human-friendly date formatting).  
- **Full article WebView** with loading spinner and explicit lifecycle teardown.  
- **Navigation-driven single activity** following best practices.  

---

## 🛠 Tech Stack & Architecture
- **Kotlin + AndroidX** (min SDK 24, target SDK 36, Kotlin 2.0, ViewBinding).  
- **MVVM + Repository Pattern** for separation of concerns.  
- **Paging 3** for incremental loading with error/retry support.  
- **Retrofit + OkHttp** with interceptors for logging and API key injection from `local.properties`.  
- **Glide** for image loading with placeholders and rounded corners.  
- **Navigation Component** for fragment navigation and back-stack management.  
- **Manual DI** via a lightweight `Injection` + `ViewModelFactory`.  

---

## 📱 Screens
| Screen       | Description                                                                 |
|--------------|-----------------------------------------------------------------------------|
| **Home**     | Paginated headline list with pull-to-refresh retry support and empty-state messaging when no content is available. |
| **Detail**   | Rich article view displaying title, author, publication date, hero image, and summary with a CTA to continue reading. |
| **Full Article** | Embedded WebView for uninterrupted reading plus a progress spinner while pages load. |

---

## ⚡ Getting Started
### Prerequisites
- **Android Studio**: Narwhal Feature Drop | 2025.1.2 Patch  
- JDK 17.  
- [NewsAPI.org](https://newsapi.org) API key.  

### Setup
1. **Clone the project**
   ```bash
   git clone https://github.com/<your-username>/inonews.git
   cd inonews

2. **Create or update `local.properties`** in the project root:

   ```properties
   sdk.dir=/path/to/Android/sdk
   news_api=<your_api_key>
   dev_base_url=https://newsapi.org/v2/
   base_url=https://newsapi.org/v2/
   ```
   > The dev_base_url was added to simulate a real-world Android development workflow where different endpoints are used for development (staging server) and release (production server).
This separation makes it easier to test new features without affecting live production data

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

* **Graceful paging UX**: Integrated `LoadStateAdapter` for retry, empty state, and progress indicators.
* **Unlimited scrolling with error recovery:** Implemented Paging 3 infinite scrolling and added a retry button in the footer so users can recover seamlessly if loading fails.
* **Reusable `Result` wrapper**: Keeps UI observers consistent across repository functions.
* **Polished visuals**: Glide image handling + human-readable date formatting.
* **Memory-safe WebView**: Explicitly clears cache/history to avoid leaks.
* **Clean architecture**: MVVM with well-separated concerns.

---

## 🔮 What I’d Improve Next

* **Offline-first caching** with Room + RemoteMediator.
* **Search & filter support** by keyword, category, or country.
* **Dynamic theming & accessibility** (TalkBack labels, larger touch targets).
* **Test coverage** with mocked API + UI navigation tests.
* **Dependency injection with Hilt** to replace manual factories and centralize lifecycle-aware bindings.  
* **Jetpack Compose migration** for a more modern, declarative UI approach while keeping interoperability with XML where needed.

---

## 🧩 Challenges & Solutions

* **Paging edge cases**: NewsAPI sometimes returns empty or inconsistent pages. → Wrapped API calls in granular `try/catch` and surfaced clear messages via `LoadStateAdapter`.
* **Load state vs Result**: Initially confused about mixing my `Result` wrapper with Paging’s `LoadState`. → Solved by using `Result` for one-shot calls and `LoadStateFlow` for paging.
* **Activity → Fragment migration**: Switching to Navigation Component meant observers must use `viewLifecycleOwner` instead of `this`, avoiding crashes/memory leaks.
* **API key security**: Injected via Gradle `buildConfigField` from `local.properties`, keeping source control clean.
* **WebView leaks**: Properly destroyed in `onDestroyView` to avoid holding references.

---

## 📂 Project Structure

```
app/
├── data/
│   └── remote/        # Retrofit service, DTOs, PagingSource
├── di/                # Injection helpers
├── repository/        # Repository layer
├── ui/
│   ├── adapter/       # PagingDataAdapter + LoadStateAdapter
│   ├── screen/        # Home, Detail, FullArticle fragments
│   ├── MainActivity   # NavHost container
│   └── MainViewModel  # ViewModel
└── utils/             # ViewModelFactory helpers
