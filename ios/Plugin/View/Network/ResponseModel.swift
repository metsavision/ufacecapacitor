//
//  ResponseModel.swift
//  Capacitor
//
//  Created by 송재상 on 1/22/24.
//

import Foundation

struct ResponseModel: Decodable {
    let code: String?
    let message: String?
    let data: Float?
}
