# 🎬 MovieApp

A modern Android app built with **Jetpack Compose**, **MVVM**, and **Clean Architecture principles (without use cases)**.  
The app fetches movies from **The Movie Database (TMDB)** API and stores them locally using **Room** — the local database acts as the **single source of truth** for all UI screens.

---


➡️ [Download MovieApp APK](https://github.com/andreiflo94/MovieApp/raw/main/builds/app-debug.apk)


## 🧭 Features

- **🏠 Home**  
  - Tabs: `Now Playing`, `Popular`, `Top Rated`, `Upcoming`  
  - Each tab filters movies and displays them in a grid layout  
  - Movies are cached locally and shown instantly  

- **❤️ Favorites**  
  - Shows favorited movies  
  - Uses the same grid UI as Home  
  - Movies can be removed via the heart toggle  

- **🔍 Search**  
  - Search movies by title  
  - Real-time filtering with a text input  
  - Results displayed in a grid  

- **🎥 Movie Details**  
  - Poster, rating, genres, release year, and overview  
  - Heart button to add or remove from favorites  

---

## ⚙️ Tech Stack

| Layer | Technology |
|-------|-------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose, Material3, Navigation Compose |
| **Architecture** | MVVM, Repository Pattern |
| **Async** | Kotlin Coroutines, Flow, StateFlow |
| **Network** | Retrofit, OkHttp |
| **Database** | Room |
| **DI** | Hilt |
| **Images** | Coil |
| **API** | [TMDB](https://developers.themoviedb.org/3) |

---
## 🔌 API Endpoints

- `https://api.themoviedb.org/3/movie/now_playing`  
- `https://api.themoviedb.org/3/movie/popular`  
- `https://api.themoviedb.org/3/movie/top_rated`  
- `https://api.themoviedb.org/3/movie/upcoming`  
- `https://api.themoviedb.org/3/search/movie?query={query}`  
- Images: `https://image.tmdb.org/t/p/w500/`

---

## 🚀 Setup

1. Get an API key from [TMDB](https://developers.themoviedb.org/3/getting-started/introduction)  
2. Add it to global `gradle.properties`:
```
API_KEY= $api_key
VERSION_NAME=1.0.0
```

## 📸 Screenshots
<img width="250" src="https://github.com/user-attachments/assets/eff965fb-9321-4427-97dc-740e180477d5" />
<img width="250" src="https://github.com/user-attachments/assets/41e6234e-4618-4b19-b3ba-7913d7939f36" />
<img width="250" src="https://github.com/user-attachments/assets/3d4c2f58-33ac-45c0-b994-189d91999cc8" />
<img width="250" src="https://github.com/user-attachments/assets/a416f897-f0cf-4edd-989a-f28793d14808" />


