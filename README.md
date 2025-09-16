## 🛍️Android Shopping Application.
A mobile application for product search and purchase - built and designed for e-shops.

> ℹ️ This project is not open source and does not grant any usage rights.
> For usage terms and legal information, see [Code Ownership & Usage Terms](#-code-ownership--usage-terms).

## 📕Overview
UnipiPLIShopping is an Android application that that allows users to view and purchase products from an e-shop easily and simply, with just a few taps of the finger. It is also notifies users if they are within close proximity to a store that carries any of the application's products

## ⚡Features
 - 🔐User registration and authentication.
 - 🎨Theme color and font size selection.
 - 🌓Custom system theme in light or dark.
 - 🗣️Pre-set language depending on the system language, either Greek, English, or Italian.
 - 🔎Online store product browsing and product selection.
 - 🔔User notification if they are within close proximity to a store that contains a product.
 - 📄Product details view and product insertion in the shopping bag.
 - 🧺Shopping bag emptying.
 - 🛒Product purchase simulation.


## 🧠Technologies Used.
 - Android SDK.
 - Java for the Back-End.
 - Firebase for Database, Authentication and Registration.
 - XML for the UI and the application’s system settings.

🎯Purpose.
This application was created to provide a nice and easy-to-use experience to users who want to browse and purchase store products from an online platform, as well as be notified if they are within close proximity to a store that carries any of the application's products. This application is developed solely for academic and research purposes.

## 🧰Prerequisites.
Before building and running this application, ensure you have the following:
 - Android studio (Ladybug 2024.2.1 or newer).
 - Firebase for Database, Authentication and Registration.
 - OPTIONALLY an Android device (min SDK version 31 -> Android version 12).


## 📦 Installation

1. Clone the repository.

```bash
git clone https://github.com/theofanistzoumakas/Android_Shopping_Application.git
cd Android_Shopping_Application
```

2. Open the project on Android Studio.
3. Create a new Firebase project.
4. Enable and connect your Firebase project's authentication and database features.
5. In your Firebase project's database, create a collection with the following sub-collections:
   - App1Orders
   - App1Products
   - App1Users
   
   In field App1Products, for each product you wish to add, add a collection.
   For each product’s collection, add the following fields:
   - App1ProductCode -> number ( must be a counter - first product has code number one, second product has code number two)
   - App1ProductDescription -> string
   - App1ProductImage -> string
   - App1ProductPrice ->  number
   - App1ProductReleaseDate -> number
   - App1ProductStore -> string (this are coordinates)
   - App1ProductTitle -> string
6. Connect an android device (min SDK version 31 -> Android version 12) or open an Android Studio’s emulator to run the project.
7. Run the project on Android Studio.


## 🔒 Code Ownership & Usage Terms
This project was created and maintained by:

Theofanis Tzoumakas (@theofanistzoumakas)

Konstantinos Pavlis (@kpavlis)

Michael-Panagiotis Kapetanios (@KapetaniosMP)

🚫 Unauthorized use is strictly prohibited.
No part of this codebase may be copied, reproduced, modified, distributed, or used in any form without explicit written permission from the owners.

For licensing inquiries or collaboration requests, please contact via email: theftzoumi _at_ gmail _dot_ com .

© 2025  Theofanis Tzoumakas, Konstantinos Pavlis, Michael-Panagiotis Kapetanios. All rights reserved.
