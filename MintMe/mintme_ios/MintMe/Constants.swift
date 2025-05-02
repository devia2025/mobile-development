import Foundation

enum ConfigKeys: String {
    case WEBSITE_URL
    case PING_SERVICES
    case LAST_URL
    case LAST_VERSION_URL
    case LOAD_LAST_URL
    case LOAD_LAST_VERSION
    case APP_ID
    case DOWNLOAD_2FA_URL
    case DOWNLOADED_2FA
    case CANCEL_SAVE_2FA
    case ALERT_SAVE_2FA
    case ALERT_ERROR
    case ALERT_DESCRIPTION
    case REDIRECT_USER
    case DOWNLOAD_CONTENT_AS_BLOB
    case DOWNLOAD_2FA_FILENAME
    case DOWNLOAD_ERROR
}

struct Constants {
    private static let configMap: [ConfigKeys:String] = [
        .WEBSITE_URL: "https://www.mintme.com/",
        .PING_SERVICES: "https://www.gstatic.com/generate_204",
        .LAST_URL: "https://www.mintme.com/",
        .LAST_VERSION_URL: "https://www.mintme.com/mobile-version.json",
        .LOAD_LAST_URL: "loadLastUrl",
        .LOAD_LAST_VERSION: "loadLastVersion",
        .APP_ID: "https://apps.apple.com/app/id6739541964",
        .DOWNLOAD_2FA_URL: "https://www.mintme.com/settings/2fa/backupcodes/download",
        .DOWNLOADED_2FA: "Download Completed: ",
        .CANCEL_SAVE_2FA: "User canceled saving the 2FA file",
        .ALERT_SAVE_2FA: "Alert!",
        .ALERT_ERROR: "Error!",
        .ALERT_DESCRIPTION: "Something went wrong. Please try again or perform this action via the website",
        .REDIRECT_USER: "Redirect, please wait..!",
        .DOWNLOAD_CONTENT_AS_BLOB: "blob",
        .DOWNLOAD_2FA_FILENAME: "backup-codes.txt",
        .DOWNLOAD_ERROR: "Failed to download the file",
    ]

    static func get(_ key: ConfigKeys) -> String? {
        return configMap[key]
    }
}
