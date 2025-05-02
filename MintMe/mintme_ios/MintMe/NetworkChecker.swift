import Foundation

class NetworkChecker {
    static func pingServer(completion: @escaping (Bool) -> Void) {
        guard let pingURLString = Constants.get(.PING_SERVICES),
              let url = URL(string: pingURLString) else {
            completion(false)
            return
        }
        var request = URLRequest(url: url)
        request.timeoutInterval = 3.0
        
        let task = URLSession.shared.dataTask(with: request) { _, response, _ in
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 204 {
                completion(true)
            } else {
                completion(false)
            }
        }
        task.resume()
    }
}
