# MintMe Mobile Application

#### 
> Note: _This application includes setup instructions for both Android and iOS_.

Requirements:

- Android App: (Android Studio + JDK 23)
- iOS App: (Xcode 15)

## 1. Requirements

- Android App: [Android Studio](https://developer.android.com/studio/), [JDK23](https://www.oracle.com/java/technologies/downloads/#jdk23-windows)
- IOS App: [Xcode15](https://developer.apple.com/xcode/) (login to download)


## 2. Clone Reposotory
```sh
git clone ssh://git@gitlab.abchosting.org:2279/abc-hosting/cryptocurrencies/mintme/mintme-mobile-app.git
cd mintme-mobile-app
```

## 3. Setup

#### Android

> Set Java Environment:

* Open Start > Environment Variables
* Under System Variables, press `New` and set: Variable Name: `JAVA_HOME`, Variable Value: `C:\Program Files\Java\jdk-23`
* Add Java to Path: Select the Path key in System Variables > Edit > New, Add: `C:\Program Files\Java\jdk-23\bin`

> Enable Developer Mode on Device:
* Verify that developer mode is enabled on your device
* Inside Developer Options, enable USB Debugging to test the app via USB

#### iOS 

> Download Simulator (Recommended):

* When starting the project, you’ll be prompted to download a simulator

`OR`
* Connect your iPhone to test directly on a device.

> Enable Developer Mode on iPhone:

* Go to Settings > Privacy & Security > Developer Mode, then toggle Developer Mode On.

> Trust Developer App:

* If the app fails to install, you must verify the developer:

> - Go to Settings > General > VPN & Device Management
> - Select Developer App under your user and trust the app
> - Try running the app again

***
## Documentation Links
> - Android App: [Android Studio](https://developer.android.com/studio/)
> - IOS App: [Xcode15](https://developer.apple.com/xcode/) (login to download)
