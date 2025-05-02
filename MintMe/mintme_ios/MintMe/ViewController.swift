import UIKit
import WebKit

class ViewController: UIViewController, WKNavigationDelegate, WKScriptMessageHandler, UIDocumentPickerDelegate {
    @IBOutlet var progressbar: UIActivityIndicatorView!
    @IBOutlet var web: WKWebView!
    var loadWebview: WebViewHandler!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let config = WKWebViewConfiguration()
        web = WKWebView(frame: self.view.bounds, configuration: config)
        web.configuration.userContentController = config.userContentController
        web.navigationDelegate = self
        web.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        guard let loadLastURLName = Constants.get(.LOAD_LAST_URL),
           let loadLastVersion = Constants.get(.LOAD_LAST_VERSION) else {
            return
        }
        
        config.userContentController.add(self, name: loadLastURLName)
        config.userContentController.add(self, name: loadLastVersion)
        
        self.view.addSubview(web)
        loadWebview = WebViewHandler(web: web, progressBar: progressbar, viewController: self, configuration: config)
        versionChecker()
    }

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if let loadLastURLName = Constants.get(.LOAD_LAST_URL), message.name == loadLastURLName {
            if let urlString = RuntimeStorage.lastUrl, let url = URL(string: urlString) {
                let request = URLRequest(url: url)
                web.load(request)
            }
        }
        
        if let loadLastVerion = Constants.get(.LOAD_LAST_VERSION),
           message.name == loadLastVerion,
            let appID = Constants.get(.APP_ID),
            let appIDURL = URL(string: appID) {
            UIApplication.shared.open(appIDURL, options: [:], completionHandler: nil)
        }
    }

    func showAlert(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        present(alert, animated: true, completion: nil)
    }
    
    func saveFile(from url: URL) {
        let documentPicker = UIDocumentPickerViewController(forExporting: [url], asCopy: true)
        documentPicker.delegate = self
        documentPicker.modalPresentationStyle = .formSheet
        present(documentPicker, animated: true, completion: nil)
    }
    
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        if let cancelSave2FAMessage = Constants.get(.CANCEL_SAVE_2FA) {
            showToast(message: cancelSave2FAMessage, on: self.view)
        }
    }
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        if let saveURL = urls.first, let completedMessage = Constants.get(.DOWNLOADED_2FA) {
            showToast(message: "\(completedMessage) \(saveURL.path)", on: self.view)
        }
    }
    
    func versionChecker() {
        guard let currentVersion = Bundle.main.infoDictionary?["AppVersion"] as? String else {
            return
        }
        
        AppVersion.check{ [weak self] iosVersion in
            DispatchQueue.main.async {
                guard let self = self else {return}
                if iosVersion == currentVersion {
                    self.loadWebview.loadInitialURL()
                } else {
                    self.loadWebview.loadUpdatePage()
                }
            }
        }
    }
}
