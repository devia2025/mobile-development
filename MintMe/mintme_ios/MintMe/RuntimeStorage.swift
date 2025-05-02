import Foundation

struct RuntimeStorage {
    static var lastUrl: String? {
        get{
            return UserDefaults.standard.string(forKey: ConfigKeys.LAST_URL.rawValue)
        }
        set {
            UserDefaults.standard.setValue(newValue, forKey: ConfigKeys.LAST_URL.rawValue)
        }
    }
}
