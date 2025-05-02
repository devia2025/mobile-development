import Foundation

class AppVersion {
    static func check(completion: @escaping (String?) -> Void) {
        guard let urlString = Constants.get(.LAST_VERSION_URL),
            let url = URL(string: urlString) else {
            completion(nil)
            return
        }
        
        let task = URLSession.shared.dataTask(with: url) {data, response, error in
            if error != nil {
                completion(nil)
                return
            }
            
            guard let data = data else {
                completion(nil)
                return
            }
            
            do {
                if let jsonArray = try JSONSerialization.jsonObject(with: data, options: []) as? [[String: String]],
                   let iosVersion = jsonArray.first?["Ios"] {
                    completion(iosVersion)
                } else {
                    completion(nil)
                }
            } catch {
                completion(nil)
            }
        }
        
        task.resume()
    }
}
