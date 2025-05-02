import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?
    var loadWebview: WebViewHandler!
    
    func scene(_ scene: UIScene, continue userActivity: NSUserActivity) {
        if userActivity.activityType == NSUserActivityTypeBrowsingWeb, let url = userActivity.webpageURL {
            RuntimeStorage.lastUrl = url.absoluteString

            if url.absoluteString.contains("login/check-google"), let redirectMsg = Constants.get(.REDIRECT_USER) {
                if let rootVC = window?.rootViewController as? ViewController {
                    showToast(message: redirectMsg, on: rootVC.view)
                }
            }
            
            if let webhandler = window?.rootViewController as? ViewController {
                webhandler.loadWebview.loadInitialURL()
            }
        }
    }

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let _ = (scene as? UIWindowScene) else { return }

        if let urlContent = connectionOptions.userActivities.first(
            where: { $0.activityType == NSUserActivityTypeBrowsingWeb }), let url = urlContent.webpageURL {
            RuntimeStorage.lastUrl = url.absoluteString
        }
    }
}
