//
//  Network.swift
//  Capacitor
//
//  Created by 송재상 on 1/22/24.
//

import Foundation

class Network {
    static func requestData<T: Decodable>(url: String, token: String, jsonData: Data,type: T.Type, completionHandler: @escaping (Result<T?,NetworkError>) -> Void) {
        guard let url = URL(string: url) else {
            completionHandler(.failure(.invaliPath(url: url)))
            return
        }
                
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue(token, forHTTPHeaderField: "Authorization")
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        /*
        let encoder = JSONEncoder()
        do {
            let jsonData = try encoder.encode(parameters)
            let jsonString = String(data: jsonData, encoding: .utf8) ?? ""
            print(jsonString)
        } catch let error {
            completionHandler(.failure(.jsonEncodingError(error)))
        }
        */
        let config = URLSessionConfiguration.ephemeral
        let session = URLSession(configuration: config)
        session.dataTask(with: request) { data, response, error in
            let statusCode = (response as? HTTPURLResponse)?.statusCode ?? 200
            print("statusCode : \(statusCode)")

            if statusCode != 200 {
                completionHandler(.failure(.httpURLResponseFail(statusCode)))
                return
            }
            
            if error != nil {
                completionHandler(.failure(.requestError(error!)))
                return
            }
            
            do {
                if let responseData = data {
                    
                    let json = try JSONSerialization.jsonObject(with: responseData)
                    print("json : \(json)")

                    let decoder = JSONDecoder()
                    let typedObject = try decoder.decode(T.self, from: responseData)
                    
                    completionHandler(.success(typedObject))
                }
            } catch {
                completionHandler(.failure(.parseError(error)))
            }
            
        }.resume()
    }
}
