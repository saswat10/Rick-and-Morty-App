#  Rick and Morty Mobile App


## Overview

[<img src="/images%2Ficon.png" width="60" height="60" />](apk%2Fapp-debug.apk)

This mobile application is built using **Kotlin** and **Jetpack Compose**, leveraging the **Rick and
Morty API** to provide a seamless browsing experience for fans of the show. The app follows the *
*Model-View-ViewModel (MVVM)** architectural pattern, ensuring a clean and maintainable codebase.
Users can search, filter, and explore the show's characters, episodes, and other details through an
intuitive and visually appealing interface.


---

## Features

- **Home Screen**  
  Displays an overview of the app with quick access to key sections like Characters, Episodes, and
  Search.

- **Characters Screen**  
  Browse and explore all the characters from the show. Includes a **search** and **filter** feature
  to refine results based on user preferences (e.g., species, gender, or status).

- **Character Details Screen**  
  View detailed information about a selected character, including their image, species, gender,
  status, and episode appearances.

- **Episodes Screen**  
  Explore episodes by season. Each episode card provides a brief overview, including air date and
  the characters featured in the episode.

- **Search Screen**  
  Search for characters, episodes, or other show-related content. Includes advanced filtering
  capabilities for precise results.

---

<table>
  <tr>
    <td>All Episodes Page</td>
    <td>Character Details Page</td>
    <td>Character Episodes List</td>
    <td>Home Screen</td>
    <td>Search Screen</td>
  </tr>
  <tr>
    <td><img src="/images%2FAllEpisodes.jpeg" width="270" height="350" /></td>
    <td><img src="/images%2FCharacterDetails.jpeg" width="270" height="350"/> </td>
    <td><img src="/images%2FEpisodesScreen.jpeg" width="270" height="350" /></td>
    <td><img src="/images%2FHomeScreen.jpeg" width="270" height="350" /></td>
    <td><img src="/images%2FSearchScreen.jpeg" width="270" height="350" /></td>
  </tr>
 </table>

---

## Tech Stack

- **Programming Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **API Integration:** Retrofit for consuming the Rick and Morty API
- **Dependency Injection:** Hilt
- **State Management:** ViewModel and Compose State APIs
- **Navigation:** Jetpack Navigation Component

--- 

Huge shout out to [Android Factory](https://github.com/the-android-factory) for
this [Youtube Playlist](https://youtube.com/playlist?list=PLLgF5xrxeQQ1yTgJKBbEAgsEFAoMV93qS&si=FqhjwJvMH7wAezMZ)