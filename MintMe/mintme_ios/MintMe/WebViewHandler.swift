import UIKit
import WebKit

class WebViewHandler: NSObject, WKNavigationDelegate {
    var web: WKWebView
    var progressBar: UIActivityIndicatorView
    var isInitialLoad: Bool = true
    weak var viewController: ViewController?

    init(web: WKWebView, progressBar: UIActivityIndicatorView, viewController: ViewController, configuration: WKWebViewConfiguration) {
        self.web = web
        self.progressBar = progressBar
        self.viewController = viewController
        super.init()
        setupWebView()
    }
    
    private func setupWebView() {
        web.navigationDelegate = self
        web.translatesAutoresizingMaskIntoConstraints = false
        progressBar.isHidden = true
        progressBar.translatesAutoresizingMaskIntoConstraints = false

        if let parentView = web.superview {
            parentView.addSubview(progressBar)
            parentView.addSubview(web)

            NSLayoutConstraint.activate([
                web.leadingAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.leadingAnchor),
                web.trailingAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.trailingAnchor),
                web.topAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.topAnchor),
                web.bottomAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.bottomAnchor)
            ])
            
            NSLayoutConstraint.activate([
                progressBar.centerXAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.centerXAnchor),
                progressBar.centerYAnchor.constraint(equalTo: parentView.safeAreaLayoutGuide.centerYAnchor),
                progressBar.widthAnchor.constraint(equalToConstant: 50),
                progressBar.heightAnchor.constraint(equalToConstant: 50)
            ])
        }
    }

    func load(_ url: URL) {
        NetworkChecker.pingServer { [weak self] (isConnected: Bool) in
            DispatchQueue.main.async {
                if isConnected {
                    let request = URLRequest(url: url)
                    self?.web.load(request)
                } else {
                    self?.loadOfflinePage()
                }
            }
        }
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if let url = navigationAction.request.url,
          let backupUrl = Constants.get(.DOWNLOAD_2FA_URL), let alertTitle = Constants.get(.ALERT_SAVE_2FA),
          let alertDescription = Constants.get(.ALERT_DESCRIPTION), let blob = Constants.get(.DOWNLOAD_CONTENT_AS_BLOB) {
            if url.absoluteString == backupUrl {
                downloadFile(from: url)
                decisionHandler(.cancel)
                return
            }
            
            if url.scheme == blob {
                self.viewController?.showAlert(title: alertTitle, message: alertDescription)
                decisionHandler(.cancel)
                return
            }
        
            if navigationAction.navigationType == .linkActivated, let websiteURL = Constants.get(.WEBSITE_URL),
                !url.absoluteString.starts(with: websiteURL) {
                    UIApplication.shared.open(url)
                    decisionHandler(.cancel)
                    return
            }
        }
        decisionHandler(.allow)
    }
    
    func downloadFile(from url: URL) {
        guard let documentsPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first,
              let downloadFileName = Constants.get(.DOWNLOAD_2FA_FILENAME) else {
            DispatchQueue.main.async {
                if let title = Constants.get(.ALERT_ERROR), let message = Constants.get(.DOWNLOAD_ERROR){
                    self.viewController?.showAlert(title: title, message: message)
                }
            }
            return
        }

        let backupURL = documentsPath.appendingPathComponent(downloadFileName)
        let session = URLSession(configuration: .default)
        let downloadTask = session.downloadTask(with: url) { (tempURL, response, error) in
            if error != nil {
                DispatchQueue.main.async {
                    if let title = Constants.get(.ALERT_ERROR), let message = Constants.get(.DOWNLOAD_ERROR){
                        self.viewController?.showAlert(title: title, message: message)
                    }
                }
                return
            }

            guard let tempURL = tempURL else {
                DispatchQueue.main.async {
                    if let title = Constants.get(.ALERT_ERROR), let message = Constants.get(.DOWNLOAD_ERROR){
                        self.viewController?.showAlert(title: title, message: message)
                    }
                }
                return
            }

            do {
                try FileManager.default.moveItem(at: tempURL, to: backupURL)

                DispatchQueue.main.async {
                    self.viewController?.saveFile(from: backupURL)
                }
            } catch {
                DispatchQueue.main.async {
                    if let title = Constants.get(.ALERT_ERROR), let message = Constants.get(.DOWNLOAD_ERROR){
                        self.viewController?.showAlert(title: title, message: message)
                    }
                }
            }
        }

        downloadTask.resume()
    }
    
    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        NetworkChecker.pingServer { [weak self] isConnected in
            DispatchQueue.main.async {
                if let currentUrl = webView.url {
                    if !currentUrl.absoluteString.contains("login") && !currentUrl.absoluteString.contains("register"){
                        self?.loadOfflinePage()
                    }
                }
            }
        }
    }
    
    func webView(_ web: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        if isInitialLoad {
            progressBar.isHidden = false
            progressBar.startAnimating()
            web.isHidden = true
        }
    }

    func webView(_ web: WKWebView, didFinish navigation: WKNavigation!) {
        if isInitialLoad {
            progressBar.stopAnimating()
            progressBar.isHidden = true
            web.isHidden = false
            isInitialLoad = false
        }
        
        NetworkChecker.pingServer {isConnected in
            DispatchQueue.main.async {
                if isConnected {
                    if let currentURL = web.url?.absoluteString,
                        !currentURL.contains("offline") && !currentURL.contains("offline") {
                        RuntimeStorage.lastUrl = currentURL
                    }
                } else {
                    self.loadOfflinePage()
                }
            }}
    }
    
    func loadOfflinePage() {
        if let offlineURL = Bundle.main.url(forResource: "offline", withExtension: "html") {
            web.loadFileURL(offlineURL, allowingReadAccessTo: offlineURL)
            return
        }
    }

    func loadUpdatePage() {
        if let UpdateURL = Bundle.main.url(forResource: "update", withExtension: "html") {
            web.loadFileURL(UpdateURL, allowingReadAccessTo: UpdateURL)
            return
        }
    }
    
    func loadInitialURL() {
        if let lastUrl = RuntimeStorage.lastUrl, let url = URL(string: lastUrl) {
            load(url)
            return
        }
        
        if let websiteURLString = Constants.get(.WEBSITE_URL), let url = URL(string: websiteURLString) {
            load(url)
            return
        }
    }
}
