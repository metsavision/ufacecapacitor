//
//  RequestModel.swift
//  Capacitor
//
//  Created by 송재상 on 1/22/24.
//

import Foundation

struct RequestModel: Codable {
    var service: String? = nil
    var apiName: String? = nil
    var param: Param? = nil
}

struct Param: Codable {
    var id: String? = nil
    var face: String? = nil
}
