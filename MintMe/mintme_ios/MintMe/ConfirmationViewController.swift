//
//  ConfirmationViewController.swift
//  MintMe
//
//  Created by Ibrahim Amin on 21/10/2024.
//

import Foundation
import UIKit

class ConfirmationViewController: UIViewController {
    
    var receivedURL: URL?

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set the background color for visibility
        view.backgroundColor = .white
        
        // Optionally, display the received URL
        if let url = receivedURL {
            print("Received URL: \(url)")
            displayURL(url)
        }
    }

    private func displayURL(_ url: URL) {
        // Display the URL in a label, for example
        let urlLabel = UILabel()
        urlLabel.text = url.absoluteString
        urlLabel.textAlignment = .center
        urlLabel.frame = view.bounds // Make it fill the view
        urlLabel.numberOfLines = 0 // Allow multiple lines if needed
        view.addSubview(urlLabel)
    }
}
