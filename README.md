**Photo Album App**

Overview
The Photo Album App is an Android application that allows users to browse and view images efficiently. The app supports features like pagination, offline caching, search functionality, full-screen image preview with zoom, and swipe gestures to navigate images. It fetches images from an API and stores them in a Room database for offline access.

**Features**

•	Fetches images from an API and caches them in a local Room database

•	Allows searching images by ID or author name with autocomplete suggestions

•	Supports full-screen image preview with pinch-to-zoom functionality

•	Enables swipe gestures to navigate between images in full-screen mode

•	Implements pagination for seamless image loading

•	Loads images from local storage when offline

•	Handles network failures and automatically refreshes when the connection is restored

•	Displays placeholder images for missing or failed loads

•   Users can toggle favorite status for images

•   Saves images directly to the gallery in full resolution

• Automatically generates captions for images using Microsoft Azure Computer Vision API




    
**How the App Works**

Home Screen

•	Displays images in a grid format with pagination

Search Functionality

•	Users can search by ID or author name

•	Real-time filtering and dynamic results update

Full-Screen Image Preview

•	Clicking an image opens it in full-screen mode

•	Supports pinch-to-zoom and panning

•	Swipe left or right to view next and previous images

Favorite & Save Images

•    Users can mark/unmark images as favorites

•    Click Save Image to store images in the gallery

•    Improved full-resolution image saving

•    Fixed previous issues where only part of the image was saved

Offline Support

•	If there is no internet connection, images are loaded from the local Room database

•	Ensures a smooth user experience even in offline mode

Auto Image Captioning

• Captions are generated for each image using Microsoft Azure's Computer Vision API 

• Captions appear below each image once AI-based analysis completes  

• The caption describes the content of the image based on machine learning algorithms

Error Handling

•	Displays placeholder images when an image fails to load


**Installation**

Installation from APK

1.	Download the APK file from the provided source.

2.	Enable "Install from Unknown Sources" in device settings.

3.	Open the APK file and install the app.

4.	Launch the app and start browsing images.

**Installation from Source Code**

1.	Clone the repository: 
    Vaishnavi2306/PhotoAlbumApp

2.	Open the project in Android Studio.

3.	Sync dependencies by selecting "Sync Project with Gradle Files".

4.	Connect a physical device or use an emulator.

5.	Click "Run" to install and launch the app.

**Dependencies**

The project uses the following dependencies:

•	Retrofit for network requests

•	Room for local database storage

•	Glide for efficient image loading

•	Paging library for paginated data loading

•	PhotoView for pinch-to-zoom functionality in full-screen mode

