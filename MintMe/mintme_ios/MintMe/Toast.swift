import UIKit

func showToast(message: String, duration: TimeInterval = 3.0, on view: UIView) {
    let toastLabel = UILabel()
    toastLabel.text = message
    toastLabel.textColor = .white
    toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.8)
    toastLabel.font = UIFont.systemFont(ofSize: 12.0)
    toastLabel.textAlignment = .center
    toastLabel.numberOfLines = 0
    toastLabel.alpha = 0.0
    toastLabel.clipsToBounds = true
    toastLabel.layer.cornerRadius = 8
    
    let maxSize = CGSize(width: view.frame.size.width - 40, height: view.frame.size.height)
    var textSize = toastLabel.sizeThatFits(maxSize)
    textSize.width += 20
    textSize.height += 10
    toastLabel.frame = CGRect(x: (view.frame.size.width - textSize.width) / 2,
                              y: view.frame.size.height - 100,
                              width: textSize.width,
                              height: textSize.height)
    view.addSubview(toastLabel)

    UIView.animate(withDuration: 0.4, animations: {
            toastLabel.alpha = 1.0
        }) { _ in
            UIView.animate(withDuration: 0.4, delay: duration, options: .curveEaseOut, animations: {
                toastLabel.alpha = 0.0
            }) { _ in
                toastLabel.removeFromSuperview()
            }
        }
}
